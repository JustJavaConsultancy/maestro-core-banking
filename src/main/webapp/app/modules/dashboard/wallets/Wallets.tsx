import Axios from 'axios';
import React, { useEffect, useState, useRef, ChangeEvent } from 'react';
import InnerLayout from '../components/Layout';
import Layout from '../Layout';
import { Modal, Table } from 'react-bootstrap';
import { connect } from 'react-redux';
import { IRootState } from 'app/shared/reducers';
import { RouteComponentProps } from 'react-router-dom';
import { setWalletData } from 'app/shared/reducers/reports';
import { useHistory } from 'react-router-dom';
import MoneyFormat from 'react-number-format';
import moment from 'moment';
import { headers } from './walletData';
import { ThreeDots } from  'react-loader-spinner';

import { setGlobalScheme } from 'app/shared/reducers/scheme';
import { hasAnyAuthority } from 'app/shared/util/hasAuthorities';
import { AUTHORITIES } from 'app/config/constants';
import { FaCopy } from 'react-icons/fa';
import { FcCheckmark } from 'react-icons/fc';

import './wallets.scss';

export interface IWalletstProps extends StateProps, DispatchProps, RouteComponentProps<{}> {}

type ShowQRCodeType = {
  show: boolean;
  qrImg: string;
  customStyle?: string;
  backdropClassName?: string;
  onHide: () => void;
};

type SharePlatformType = {
  image: string;
  platform: string;
};

function Wallets(props: IWalletstProps) {
  const [wallets, setWallets] = useState([]);
  const [total, setTotal] = useState(0);
  const [size, setSize] = useState(20);
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState('');
  const [schemes, setSchemes] = useState([]);
  const [scheme, setScheme] = useState({ schemeID: '' });
  const toPrint = useRef();
  const [loading, setLoading] = useState(false);

  const [hidePagination, setHidePagination] = useState(false);

  const [changeHeaderName, setChangeHeaderName] = useState(false);
  const [isDisable, setIsDisable] = useState(true);

  const [walletType, setWalletType] = useState('customers');
  const [agentType, setAgentType] = useState('super');

  const history = useHistory();
  // const { schemeID } = scheme;

  const [showModal, setShowModal] = useState(false);
  const [qrCode, setQrCode] = useState('');
  const [searchLoading, setSearchLoading] = useState(false);

  const isAdmin = hasAnyAuthority(props.userLog.authorities, [AUTHORITIES.ADMIN, AUTHORITIES.ROLE_SUPER_ADMIN]);
  const isPolaris = hasAnyAuthority(props.userLog.authorities, [AUTHORITIES.POLARIS_ADMIN]);
  
  const isMcPhersonFinancial = hasAnyAuthority(props.userLog.authorities, [AUTHORITIES.MCPHERSON_FINANCIAL]);
  const isFuoyeFinancial = hasAnyAuthority(props.userLog.authorities, [AUTHORITIES.FUOYE_FINANCIAL]);
  const isSchFinancial = isMcPhersonFinancial || isFuoyeFinancial;

  const isPaymasta = hasAnyAuthority(props.userLog.authorities, [AUTHORITIES.PAYMASTA_ADMIN]);
  const isWragby = hasAnyAuthority(props.userLog.authorities, [AUTHORITIES.WRAGBY_ADMIN]);
  const isWynk = hasAnyAuthority(props.userLog.authorities, [AUTHORITIES.WYNK_ADMIN]);

  const getWallets = async (paget, s): Promise<void> => {
    setWallets([]);
    try {
      setLoading(true)
      const theWallets = await Axios.get(`/api/wallet-accounts/customers/${s}/search?key=${search}&page=${paget}&size=${size}`);
      // const theWallets = await Axios.get(`https://wallet.remita.net/api/wallet-accounts/customers/${s}/search?key=${search}&page=${paget}&size=${size}`);
      setTotal(theWallets.data.metadata.totalNumberOfRecords);
      setPage(paget);

      if (s === '53797374656d73706563732077616c6c6574' && (walletType === 'customers' || walletType === 'agents')) {
        const filteredWallets =
          walletType === 'customers'
            ? theWallets.data?.data.filter(data => data.walletAccountTypeId === 1)
            : theWallets.data?.data.filter(data => data.walletAccountTypeId > 1);
        setWallets(filteredWallets);
      } else {
        setWallets(theWallets.data.data);
      }
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false)
    }
  };

  const getPolarisAccounts = async () => {
    try {
      setLoading(true);
      // const polarisAccts = await Axios.get('https://wallet.remita.net/api/get-polaris-accounts');
      const polarisAccts = await Axios.get('/api/get-polaris-accounts');
      if(polarisAccts.data.code === '00') {
        setWallets(polarisAccts.data.data);
      }
    } catch(err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  }

  const searchWallets = async (s): Promise<void> => {
    try {
      setSearchLoading(true)
      const theWallets = await Axios.get(`/api/wallet-accounts/customers/${scheme?.schemeID}/search?key=${s}&page=${0}&size=${size}`);
      setTotal(theWallets.data.metadata.totalNumberOfRecords);
      setPage(0);
      setWallets(theWallets.data.data);
      console.error(theWallets);
    } catch (e) {
      console.error(e);
    } finally {
      setSearchLoading(false);
    }
  };

  const getSpecialWallet = async (schID): Promise<void> => {
    try {
        setLoading(true);
        const theWallets = await Axios.get(`/api/wallet-accounts/special/${schID}`);
        // const theWallets = await Axios.get(`https://wallet.remita.net/api/wallet-accounts/special/5061794d61737461`);
        setWallets(theWallets.data.data);
        // eslint-disable-next-line no-console
        console.log(theWallets.data.data)
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const changeAccountStatus = async (ref, status): Promise<void> => {
    try {
      const r = await Axios.post(`/api/wallet-account/status/${ref}/${status}`);
      console.error(r);
      // getWallets(page)
      alert('success');
    } catch (e) {
      console.error(e.response);
      if (e.response) {
        alert(e.response.data.message);
      } else {
        alert('An error occured!');
      }
    }
  };

  const getSchemes = async () => {
    try {
      if(isMcPhersonFinancial) { 
        setScheme({schemeID: '4d6350686572736f6e'});
      } else if(isPolaris) {
        setScheme({schemeID: '53797374656d73706563732077616c6c6574'});
      } else {
        const theSchemes = await Axios.get(`/api/schemes/admin/${props.userLog.login}`);
        setSchemes(theSchemes.data.data);
        setScheme(theSchemes.data.data[0]);
        // getWallets(0, theSchemes.data.data[0].schemeID);
      }
    } catch (e) {
      console.error(e);
    }
  };

  const handleschemeChange = s => {
    console.error(s?.schemeID);

    setScheme(s);
    getWallets(0, s?.schemeID);
  };

  const asyncExportMethod = async () => {
    try {
      const result = await Axios.get(`/api/wallet-accounts/customers/${scheme?.schemeID}/search?key=${search}&page=${0}&size=${1000}`);
      return result;
    } catch (error) {
      console.error(error);
      return [];
    }
  };

  const getQRCode = (name: string, phone: string, account: string) => {
    const adminToken = JSON.parse(sessionStorage.getItem('jhi-authenticationToken'));

    const payload = {
      merchantName: name,
      phoneNo: phone,
      accountNo: account,
    };

    fetch('/api/document/get-wallet-qr-code', {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${adminToken}`,
        Accept: 'application/json text/plain, */*',
        'Content-Type': 'application/json',
      },

      body: JSON.stringify(payload),
    })
      .then(response => {
        response
          .blob()
          .then(blobResponse => {
            const dataBlob = blobResponse;

            const reader = new FileReader();
            reader.readAsDataURL(dataBlob);
            reader.onloadend = () => {
              // eslint-disable-next-line no-console
              console.log(reader.result)
              setQrCode(reader.result as string);
              setShowModal(true);
            };
          })
          .catch(err => {
            alert('Error getting QR Code');
          });
      })
      .catch(err => console.error(err));
  };

  useEffect(() => {
    getSchemes();
  }, []);

  useEffect(() => {
    if (scheme?.schemeID) {
      setIsDisable(false);
    } else {
      setIsDisable(true);
    }
  }, [scheme.schemeID]);

  useEffect(() => {
    if (scheme.schemeID) {
      if (walletType === 'customers') {
        setChangeHeaderName(false);
        setHidePagination(false);
        if(isSchFinancial) {
          getSpecialWallet(scheme?.schemeID);
        } 
        else if(isPolaris) {
          setHidePagination(true);
          setChangeHeaderName(true);
          getPolarisAccounts();
        }
        else {
          getWallets(0, scheme?.schemeID);
        }
      } else if (walletType === 'agents') {
        getWallets(0, scheme?.schemeID);
      } else {
        setChangeHeaderName(true);
        setHidePagination(true);
        getSpecialWallet(scheme?.schemeID);
      }
    }
  }, [walletType, scheme.schemeID]);

  return (
    <>
      <Layout>
        <InnerLayout
          // phone= {props.userLog.login}
          schemes={schemes}
          schemeChange={s => {
            handleschemeChange(JSON.parse(s));
          }}
          toPrint={toPrint}
          toExport={wallets}
          setSearch={async (s: any) => {
            await searchWallets(s);
          }}
          nextPage={pageto => getWallets(pageto, scheme?.schemeID)}
          show={hidePagination}
          page={total}
          size={size}
          getData={asyncExportMethod}
          isDisable={isDisable}
          headers={headers}
          title="Wallets"
          path="Wallets"
          searchLoading={searchLoading}
          showDateRange={false}
          showSearch={isAdmin}
        >
          <div ref={toPrint} style={{ minWidth: '300px', minHeight: '300px', marginTop: '20px' }}>
            {(isAdmin || isPaymasta || isWragby || isWynk) && (
              <div className="wallets-download-box">
                {(isAdmin || isPaymasta || isWragby || isWynk) ? (
                  <select
                    onChange={e => {
                      setWalletType(e.target.value);
                    }}
                  >
                    <option value="customers">Customers Wallets</option>
                    {isAdmin && <option value="agents">Agents</option>}
                    <option value="special">Operational Wallets</option>
                  </select>
                ) : scheme?.schemeID === '4962696c65' ? (
                  <select
                    onChange={(e: ChangeEvent<HTMLSelectElement>) => {
                      const { value } = e.target;
                      setAgentType(value);
                    }}
                  >
                    <option value="super">SuperAgent Wallets</option>
                    <option value="agent">Agent Wallets</option>
                  </select>
              ) : null}
              </div>
            )}
            {loading && (
              <div className="loader__container">
                <ThreeDots 
                  height="100" 
                  width="100" 
                  color='#29307C' 
                  ariaLabel='loading'
                />
              </div>
            )
            }
            {!loading && ((wallets && wallets.length > 0) ? (
              <Table hover responsive className="transaction-table">
                <thead className="tableListData">
                  <th>S/N</th>
                  <th>Wallet Number</th>
                  {(isSchFinancial) ? ( <th>Wallet Name</th> )
                   : 
                  (
                    <>
                      <th> {changeHeaderName ? 'Wallet Owner' : 'Customer Name'}</th>
                      {!changeHeaderName && <th>Customer PhoneNumber</th>}
                    </>
                  )}
                  <th>Nuban Account</th>
                  <th>Book Balance (₦)</th>
                  <th>Available Balance (₦)</th>
                  <th>Date Created</th>
                  <th>Status</th>
                  {!isPolaris && <th>QR Code</th>}
                </thead>

                <tbody>
                  {wallets && wallets.map((wallet, index) => (
                    <tr className="tableListData click" key={index}>
                      <td
                        onClick={() => {
                          props.setWalletData(wallet);
                          history.push('/wallet-transaction');
                        }}
                      >
                        {index + 1 + Number(page) * size}
                      </td>
                      <td
                        onClick={() => {
                          props.setWalletData(wallet);
                          history.push('/wallet-transaction');
                        }}
                      >
                        {wallet.accountNumber}
                      </td>
                      {(isSchFinancial) ?
                        (<td
                          onClick={() => {
                            props.setWalletData(wallet);
                            history.push('/wallet-transaction');
                          }}
                          >
                        {wallet.accountName}
                        </td>) :
                      <>
                        <td
                          onClick={() => {
                            props.setWalletData(wallet);
                            history.push('/wallet-transaction');
                          }}
                        >
                          {changeHeaderName ? wallet.accountName : wallet.accountOwnerName}
                        </td>
                        {!changeHeaderName && <td>{wallet.accountOwnerPhoneNumber}</td>}
                      </>
                      }
                      <td>{wallet?.nubanAccountNo}</td>
                      <td>
                        <MoneyFormat
                          value={wallet.currentBalance}
                          displayType="text"
                          thousandSeparator={true}
                          decimalScale={2}
                          fixedDecimalScale={true}
                          // prefix = "₦"
                        />
                      </td>
                      <td>
                        <MoneyFormat
                          value={wallet.actualBalance}
                          displayType="text"
                          thousandSeparator={true}
                          decimalScale={2}
                          fixedDecimalScale={true}
                          // prefix = "₦"
                        />
                      </td>
                      <td>{moment(wallet.dateOpened).format('ll')}</td>
                      <td>
                        <div
                          style={{
                            padding: '5px 8px',
                            borderRadius: '15px',
                            fontSize: '12px',
                            color: '#fff',
                            backgroundColor: wallet.status === 'ACTIVE' ? '#8DE48B' : 'orange',
                          }}
                        >
                          {wallet.status}
                          {/* <select value = {wallet.status} onChange = {e=>changeAccountStatus(wallet.accountNumber, e.target.value)} >
                                                    <option value = "ACTIVE">ACTIVE</option>
                                                    <option value = "INACTIVE">INACTIVE</option>
                                                    <option value = "SUSPENDED">SUSPENDED</option>
                                                    <option value = "DEBIT_ON_HOLD">DEBIT_ON_HOLD</option>
                                                </select> */}
                        </div>
                      </td>
                      {!isPolaris && <td className="qr_box" style={{ padding: 0 }}>
                        <small onClick={() => getQRCode(wallet.accountOwnerName, wallet.accountOwnerPhoneNumber, wallet.accountNumber)}>
                          <QRIcon /> <span className="qr_style">View QR</span>
                        </small>
                      </td>}
                    </tr>
                  ))}
                </tbody>
              </Table>
            ) : (
              <div className="center_msg-box">
                <p>No data</p>
              </div>
            ))}
          </div>
        </InnerLayout>
      </Layout>

      <ShowQRCode show={showModal} onHide={() => setShowModal(false)} qrImg={qrCode} customStyle="qr_modal-contents" />
    </>
  );
}

const mapStateToProps = ({ 
  reports, 
  authentication: { isAuthenticated, account, sessionHasBeenFetched },
}: any) => ({
  data: reports.data,
  userLog: account,
});

const mapDispatchToProps = { setWalletData, setGlobalScheme };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Wallets);

export function QRIcon() {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      // width="13"
      // height="9"
      width="11"
      height="8"
      viewBox="0 0 13 9"
      fill="none"
    >
      <path
        d="M6.5 8.375C8.98379 8.375 11.2362 7.0161 12.8983 4.80888C13.0339 4.62805 13.0339 4.37541 12.8983 4.19458C11.2362 1.9847 8.98379 0.625798 6.5 0.625798C4.01621 0.625798 1.76378 1.9847 0.101719 4.19192C-0.033906 4.37275 -0.033906 4.62539 0.101719 4.80622C1.76378 7.0161 4.01621 8.375 6.5 8.375ZM6.32183 1.77196C7.97059 1.66824 9.33216 3.02715 9.22844 4.67857C9.14335 6.04013 8.03974 7.14374 6.67817 7.22884C5.02941 7.33255 3.66784 5.97365 3.77156 4.32223C3.85931 2.96332 4.96292 1.85971 6.32183 1.77196ZM6.40427 3.03246C7.29247 2.97662 8.02644 3.70793 7.96794 4.59613C7.92273 5.3301 7.32704 5.92312 6.59308 5.97099C5.70487 6.02684 4.9709 5.29553 5.02941 4.40732C5.07727 3.6707 5.67296 3.07767 6.40427 3.03246Z"
        fill="#285C8C"
      />
    </svg>
  );
}

export function ShowQRCode(props: ShowQRCodeType) {
  return (
    <Modal
      show={props.show}
      onHide={props.onHide}
      backdrop="static"
      centered
      contentClassName={props.customStyle}
      backdropClassName={props.backdropClassName}
    >
      <Modal.Body>
        <div className="QR_main-box">
          <div className="QR_close-box">
            <span className="QR_close" onClick={props.onHide}>
              <QRCloseBtn />
            </span>
          </div>

          <div className="QR_content-box">
            <div className="QR_img-box">
              <img src={props.qrImg} alt="qr code" width="100%" height="100%" />
            </div>

            <div className="QR_share">
              <h5>Copy QR Link</h5>
              <div className="platform_share">
                <SharePlatform link={props.qrImg}/>
              </div>
            </div>
          </div>
        </div>
      </Modal.Body>
    </Modal>
  );
}

export function QRCloseBtn() {
  return (
    <svg xmlns="http://www.w3.org/2000/svg" width="19" height="19" viewBox="0 0 19 19" fill="none">
      <path d="M16.8025 2L2.19727 17" stroke="#CACACA" strokeWidth="3" />
      <path d="M17 16.8025L2 2.19726" stroke="#CACACA" strokeWidth="3" />
    </svg>
  );
}

export function SharePlatform({ link }: any) {
  const [showMark, setShowMark] = useState(false);

  const handleClick = () => {
    navigator.clipboard.writeText(link);
    setShowMark(true);
    const timer = setTimeout(() => setShowMark(false), 1000)
    return () => clearTimeout(timer);
  }

  return (
    <div className="platform_share-box" onClick={handleClick}>
        <div className="platform_share-box-img">
          {showMark ? <FcCheckmark size="30px" color="#435faa" /> : <FaCopy size="30px"/>}
        </div>
        {showMark && <span style={{ color: 'green'}}>Copied!</span>}
    </div>
  );
}
