import React, { useEffect, useState, useRef } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import Axios from 'axios';
import exportFromJSON from 'export-from-json';
import { useReactToPrint } from 'react-to-print';
import moment from 'moment';
import { isEmpty } from 'lodash';
import { useHistory } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { Spinner } from 'react-bootstrap';


import { IRootState } from 'app/shared/reducers';
import Layout from '../Layout';
import InnerLayout from '../components/Layout';
import { setWalletData } from 'app/shared/reducers/reports';
import { getCustomerStatement, getGlobalStatement, getCalloverReport, getCustomerShemeBalance } from './utils/utils';
import CustomerReport from '../../reports/Statement/StatementOfAccount';
import GlobalStatement from '../../reports/Statement/StatementOfAccountAll';
import CallOverReport from '../../reports/CallOverReport/CallOverReport';
import SchemeCustomerAccountBalance from '../../reports/schemeCustomerAccount/SchemeCustomerAccountBalance';
import './report.scss';
import { hasAnyAuthority } from 'app/shared/util/hasAuthorities';
import { AUTHORITIES } from 'app/config/constants';

export interface IWalletstProps extends StateProps, DispatchProps, RouteComponentProps<{}> {}

function report(props) {
  const [data, setData] = useState({
    start: '',
    end: '',
    accountNumber: '',
    accountOwnerName: '',
    schemeID: '',
    error: false,
    scheme: '',
    format: 'customer',
    dateOpened: moment(Date()).format('YYYY-MM-DD')
  });
  const [schemes, setSchemes] = useState([]);
  const history = useHistory();
  const [format, setFormat] = useState('customer');
  const [showPrint, setShowPrint] = useState(false);
  const toPrint = useRef();
  const handlePrint = useReactToPrint({
    content: () => toPrint.current,
    // onAfterPrint: () => setShowPrint(false)
  }); 
  const [customerStatement, setCustomerStatement] = useState([]);
  const [globalStatement, setGlobalStatement] = useState([]);
  const [calloverReport, setCalloverReport] = useState([]);
  const [schemeStatement, setSchemeStatement] = useState([]);
  // const [opening, setOpening] = useState('');
  // const [closing, setClosing] = useState('');
  const [loading, setLoading] = useState(false);
  const [downloadFormat, setDownloadFormat] = useState('pdf');
  const [vendorWallets, setVendorWallets] = useState([]);

  const user = useSelector((state: IRootState) => state);

  // school admins
  const isMcPhersonFinancial = hasAnyAuthority(user.authentication.account.authorities, [AUTHORITIES.MCPHERSON_FINANCIAL]);
  const isFuoyeFinancial = hasAnyAuthority(user.authentication.account.authorities, [AUTHORITIES.FUOYE_FINANCIAL]);
  const isSchFinancial = isMcPhersonFinancial || isFuoyeFinancial;
  
  // financial partners admin
  const isPaymasta = hasAnyAuthority(user.authentication.account.authorities, [AUTHORITIES.PAYMASTA_ADMIN]);
  const isWragby = hasAnyAuthority(user.authentication.account.authorities, [AUTHORITIES.WRAGBY_ADMIN]);
  const isWynk = hasAnyAuthority(user.authentication.account.authorities, [AUTHORITIES.WYNK_ADMIN]);
  const isFinancialPartner = isPaymasta || isWragby || isWynk;
  
  // systemspecs admin
  const isAdminOperations = hasAnyAuthority(user.authentication.account.authorities, [AUTHORITIES.ADMIN, AUTHORITIES.OPERATIONS, AUTHORITIES.SUPPORT]);
  const isAdmin = hasAnyAuthority(user.authentication.account.authorities, [AUTHORITIES.ADMIN, AUTHORITIES.ROLE_SUPER_ADMIN]);
  const isPolaris = hasAnyAuthority(props.userLog.authorities, [AUTHORITIES.POLARIS_ADMIN]);
  const isOperations = hasAnyAuthority(user.authentication.account.authorities, [AUTHORITIES.OPERATIONS, AUTHORITIES.SUPPORT]);

  const handleDownload = (value: any, fileName: string) => {
    // eslint-disable-next-line no-console
    console.log(value)
    if(downloadFormat === 'pdf') {
      handlePrint();
    } else {
      if(downloadFormat === 'csv') {
        exportFromJSON({ data: value, fileName, exportType: 'csv' });
      } else {
        exportFromJSON({ data: value, fileName, exportType: 'xls' });
      }
    }
	}

  const getPolarisAccounts = async () => {
    try {
      // const polarisAccts = await Axios.get('https://wallet.remita.net/api/get-polaris-accounts');
      const polarisAccts = await Axios.get('/api/get-polaris-accounts');
      if(polarisAccts.data.code === '00') {
        setVendorWallets(polarisAccts.data.data);
        setData({
          ...data, 
          accountNumber: polarisAccts.data.data[0].accountNumber,
        })
      }
    } catch(err) {
      console.error(err);
    }
  }

  const getSpecialWallet = async (schID): Promise<void> => {
    try {
      if(schID) {
        const theWallets = await Axios.get(`/api/wallet-accounts/special/${schID}`);
        // const theWallets = await Axios.get(`https://wallet.remita.net/api/wallet-accounts/special/5061794d61737461`);
        setVendorWallets(theWallets.data.data);
        setData({...data, accountNumber: theWallets.data.data[0].accountNumber})
      }
    } catch (e) {
      console.error(e);
    }
  };

  const transformFn = (res: any, type: any) => {
    if(type === 'cst') {
      return res.map((csData) => {
        return {
          ID: csData.id,
          Debit: csData.debit,
          Credit: csData.credit,
          Date: csData.transactionDate,
          Reference: csData.transactionRef,
          Narration: csData.memo,
          Balance: csData.currentBalance
        }
      })
    }
  }

  const handlePdf = async (type) => {
    try {
      setLoading(true)
      if(format === 'customer') {
        const {res, opening, closing} = await getCustomerStatement(data, setCustomerStatement);
        const location = { pathname: '/mini-statement', state: { data, opening, closing, customerStatement: res }}
        type === 'download' ? handleDownload(transformFn(res, 'cst'), 'Customers Report') :  history.push(location);
      }
      if(format === 'statement') {
        const res = await getGlobalStatement(data, setGlobalStatement);
        const location = { pathname: '/full-statement', state: { data, customerStatement: res }}
        type === 'download' ? handleDownload(transformFn(res, 'cst'), 'Global Report') :  history.push(location);
      }
      if(format === 'call') {
        const res = await getCalloverReport(data, setCalloverReport);
        const location = { pathname: '/callover', state: { data, customerStatement: res }}
        type === 'download' ? handleDownload(res, 'Callover Report') :  history.push(location);
      }
      if(format === 'scheme') {
        const res = await getCustomerShemeBalance(data, setSchemeStatement);
        const location = { pathname: '/scheme-report', state: { data, customerStatement: res }}
        type === 'download' ? handleDownload(res, 'Scheme Report') :  history.push(location);
      }
    } catch (e) {
      console.error(e)
    } finally {
      setLoading(false)
    }
  };


  const verifyUser = async accountNumber => {
    try {
      if(accountNumber) {
        // const theUser = await Axios.get(`https://wallet.remita.net/api/wallet-accounts/avs/${accountNumber}`);
        const theUser = await Axios.get(`/api/wallet-accounts/avs/${accountNumber}`);
        console.error(theUser);
        if(theUser.data.code !== 'error') {
          setData(prevState => {
            return { 
              ...prevState, 
              accountOwnerName: theUser.data, 
              error: false 
            }
          });
        }
      }
    } catch (error) {
      setData({ ...data, error: true });
    }
  };

  const getSchemes = async () => {
    try {
      if(isPolaris){
        setData({
          ...data,
          schemeID: '53797374656d73706563732077616c6c6574',
          scheme: 'Systemspecs wallet'
        });
      } else {
        const theSchemes = await Axios.get(`api/schemes/admin/${props.userLog.login}`);
        setData({
          ...data,
          schemeID: theSchemes.data.data.length > 0 ? theSchemes.data.data[0].schemeID : '',
          scheme: theSchemes.data.data[0].scheme,
        });
        setSchemes(theSchemes.data.data);
      }
    } catch (e) {
      console.error(e);
    }
  };

  useEffect(() => {
    getSchemes();
    if(isPolaris) {
      getPolarisAccounts();
    }
    if(isMcPhersonFinancial) {
      setData({
        ...data,
        accountNumber: '1000000080'
      })
    }
  }, []);

  useEffect(() => {
    if(isMcPhersonFinancial) {
      verifyUser('1000000080');
    } else if(isFinancialPartner || isPolaris || isFuoyeFinancial) {
      verifyUser(data.accountNumber);
    }
  }, [data.accountNumber]);

  useEffect(() => {
    if(data.schemeID) {
      if(isFinancialPartner || isFuoyeFinancial) {
        getSpecialWallet(data.schemeID);
      }
    }
  }, [data.schemeID])

  return (
    <Layout>
      <InnerLayout dontfilter={true} show={true} title="Reports" path="Reports">
        <div style={{ minHeight: '60vh', display: 'flex', alignItems: 'center', flexDirection: 'column' }}>
          {(isAdmin || isOperations) && <select 
            onChange={e => {
              setFormat(e.target.value)
              setData({...data, format: e.target.value})
            }} 
            placeholder="Select Report" 
            className="select"
          >
              <option value="customer">Customer Wallet Statement</option>
              <option value="statement">Global Wallet Statement</option>
              <option value="trial">Global Trial Balance</option>
              {isAdmin && (
                <>
                  <option value="log">Activity Log Report</option>
                  <option value="audit">Audit Trail</option>
                  <option value="call">Call Over Report</option>
                  <option value="scheme">Scheme Customer Wallet Balance</option>
                </>
              )}
          </select>
              }
          <div className="filtersection">
            <div>
              <p className="additional">Additional Search Required</p>
            </div>
            {(isOperations || isAdmin) &&
              <div style={{ flexDirection: 'column' }} className="dateRange">
                <select
                  onChange={e => {
                    // const toSave = e.target.value
                    // console.error(toSave);
                    setData({ ...data, schemeID: schemes[e.target.value].schemeID, scheme: schemes[e.target.value].scheme });
                  }}
                  placeholder="Enter The SchemeID *"
                  className="textinput"
                >
                  {schemes.map((s, i) => (
                    <option key={i} value={i}>
                      {s.scheme}
                    </option>
                  ))}
                </select>
              </div>
            }
            {(isPolaris || isFinancialPartner || isFuoyeFinancial) && (
              <select 
                onChange={e => setData({...data, accountNumber: e.target.value})} 
                className="dateRange filter"
                style={{marginBottom: '20px'}}
              >
                {vendorWallets && vendorWallets.map(vw => (
                  <option key={vw.accountName} value={vw.accountNumber}>
                    {vw.accountName}
                  </option>
                ))}
              </select>
            )}
            <div className="download-format">
              <select
                onChange={e => setDownloadFormat(e.target.value)}
              >
                <option value="pdf">PDF</option>
                <option value="csv">CSV</option>
                <option value="xls">XLS</option>
              </select>
            </div>
            {format !== 'scheme' && format !== 'trial' && (
              <div className="dateRange dt">
                <label className="dateLabel">
                  Start Date:
                  <input onChange={e => setData({ ...data, start: moment(e.target.value).format('YYYY-MM-DD') })} type="date" />
                </label>
                <label className="dateLabel">
                  End Date:
                  <input onChange={e => setData({ ...data, end: moment(e.target.value).format('YYYY-MM-DD') })} type="date" />
                </label>
              </div>
            )}
            {format === 'customer' && (
              <div style={{ flexDirection: 'column' }} className="dateRange">
                <input
                  value={data.accountNumber}
                  onChange={e => {
                    setData({ ...data, accountOwnerName: '', accountNumber: e.target.value });
                    if(isAdmin || isOperations) {
                      if (e.target.value.length > 9) {
                        verifyUser(e.target.value);
                        return;
                      }
                    }
                  }}
                  placeholder="Enter Wallet Number *"
                  className="textinput"
                  type="text"
                  disabled={isSchFinancial || isFinancialPartner || isPolaris}
                />
                {((data.accountOwnerName && !data.error) && data.accountNumber)  && (
                  <p 
                    style={{ 
                      backgroundColor: '#7f7f9e', 
                      marginRight: '40px', 
                      fontSize: '12px', 
                      marginTop: '14px', 
                      padding: '10px', 
                      color: '#fff',
                      borderRadius: '5px', 
                    }}
                  >
                    {data.accountOwnerName}
                  </p>
                )}
                {data.error && (
                  <p
                    style={{
                      backgroundColor: 'red',
                      color: '#fff',
                      marginRight: '40px',
                      fontSize: '12px',
                      marginTop: '8px',
                      padding: '7px',
                      borderRadius: '3px',
                    }}
                  >
                    Invalid wallet Number
                  </p>
                )}
              </div>
            )}
            <div className="filter-action">
                <button 
                  onClick={() => handlePdf('view')}
                  className="viewreport"
                >
                  {loading ? 
                    <Spinner style={{ color: '#fff' }} animation="border" role="status" size="sm"/>: 
                    "View Report"
                  }
                </button>
                <button 
                  onClick={() => handlePdf('download')}
                  className="viewreport"
                >
                  {loading ? 
                    <Spinner style={{ color: '#fff' }} animation="border" role="status" size="sm"/>: 
                    "Download Report"
                  }
                </button>
            </div>
              <section ref={toPrint} className="print-source">
                {format === "customer" && (
                  <>
                    <CustomerReport 
                      customerStatement={customerStatement} 
                      customerData={data}
                    />
                  </>
                )}
                {format === "statement" && (
                  <GlobalStatement 
                    globalStatement={globalStatement}
                    customerData={data}
                  />
                )}
                {format === "call" && (
                  <CallOverReport
                    calloverReport={calloverReport}
                    customerData={data}
                  />
                )}
                {format === 'scheme' && (
                  <SchemeCustomerAccountBalance
                    schemeStatement={schemeStatement}
                    customerData={data}
                  />
                )}
              </section>
          </div>
        </div>
      </InnerLayout>
    </Layout>
  );
}

const mapStateToProps = ({ reports, authentication: { isAuthenticated, account, sessionHasBeenFetched } }: IRootState) => ({
  data: reports.data,
  user: reports.user,
  auth: account.authorities,
  userLog: account,
});

const mapDispatchToProps = { setWalletData };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(report);
