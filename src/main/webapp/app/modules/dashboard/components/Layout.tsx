import './Layout.scss';
import React, { useState, useEffect, useRef } from 'react';
import { faHome, faArrowAltCircleLeft } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useHistory } from 'react-router-dom';
import ReactToPrint from 'react-to-print';
import exportFromJSON from 'export-from-json';
import { isEmpty } from 'lodash';
import Select from 'react-select';
import { CSVLink } from 'react-csv';
import Axios from 'axios';
import { connect } from 'react-redux';
import { Spinner } from 'react-bootstrap';

import { hasAnyAuthority } from 'app/shared/util/hasAuthorities';
import { AUTHORITIES } from 'app/config/constants';
import Union from '../../../../content/images/Union.svg'; 
import Search from '../../../../content/images/white-search.svg'; 
import { useSchemes } from '../messaging/helper/HelperUtils';

function Layout (props) {
  const [page, setPage] = useState([]);
  const [active, setActive] = useState(0);
  const history = useHistory();
  const [format, setFormat] = useState('pdf');
  const [data, setData] = useState([]);
  const [showKYC, setShowKYC] = useState(false)
  const csvInstance = useRef<any | null>(null);
  const [kycInfo, setKycInfo] = useState([]);
  const [vendorWallets, setVendorWallets] = useState([]);
  const [searchKey, setSearchKey] = useState('');
  const [showFilter, setShowFilter] = useState(true);
  const [allSchemes, setAllSchemes] = useState([{
    "id" : 1,
    "schemeID" : "53797374656d73706563732077616c6c6574",
    "scheme" : "Systemspecs wallet",
    "accountNumber" : "1790307208",
    "bankCode" : "076",
    "callBackUrl" : null
  }]
  );

  const { isLoading, schemes } = useSchemes();

  const isTrue = history.location.pathname === '/dowmload-wallets' ? true : false;
  
  const isAdmin = hasAnyAuthority(props.account.authorities, [AUTHORITIES.ADMIN, AUTHORITIES.ROLE_SUPER_ADMIN]);
  const isPolaris = hasAnyAuthority(props.account.authorities, [AUTHORITIES.POLARIS_ADMIN]);
  
  const isMcPherson = hasAnyAuthority(props.account.authorities, [AUTHORITIES.MCPHERSON, AUTHORITIES.MCPHERSON_FINANCIAL]);
  const isMcPhersonFinancial = hasAnyAuthority(props.account.authorities, [AUTHORITIES.MCPHERSON_FINANCIAL]);
  const isFuoyeFinancial = hasAnyAuthority(props.account.authorities, [AUTHORITIES.FUOYE_FINANCIAL]);
  const isSchFinancial = isMcPhersonFinancial || isFuoyeFinancial;
  
  const isPaymasta = hasAnyAuthority(props.account.authorities, [AUTHORITIES.PAYMASTA_ADMIN]);
  const isWragby = hasAnyAuthority(props.account.authorities, [AUTHORITIES.WRAGBY_ADMIN]);
  const isWynk = hasAnyAuthority(props.account.authorities, [AUTHORITIES.WYNK_ADMIN]);
  const isPartnerFinancial = isPaymasta || isWragby || isWynk

  
  const getKYCLevels = async (): Promise<void>=> {
    try {
      const res = await Axios.get(`/api/kyclevels`);
      const kycData = res.data.map(item => {
        return {
          value: item.kycID,
          label: item.description
        }
      })
      setKycInfo([{value:"all", label: "All"}, ...kycData])
    } catch (err) {
      console.error(err.response);
    }
  }

  const getPolarisAccounts = async () => {
    try {
      // const polarisAccts = await Axios.get('https://wallet.remita.net/api/get-polaris-accounts');
      const polarisAccts = await Axios.get('/api/get-polaris-accounts');
      if(polarisAccts.data.code === '00') {
        setVendorWallets(polarisAccts.data.data);
        props.changeWallet(polarisAccts.data.data[0].accountNumber)
      }
    } catch(err) {
      console.error(err);
    }
  }

  const getSpecialWallet = async (schID): Promise<void> => {
    try {
        const theWallets = await Axios.get(`/api/wallet-accounts/special/${schID}`);
        // const theWallets = await Axios.get(`https://wallet.remita.net/api/wallet-accounts/special/${schID}`);
        setVendorWallets(theWallets.data.data);
        props.changeWallet(theWallets.data.data[0].accountNumber)
        // eslint-disable-next-line no-console
        console.log(theWallets.data.data)
    } catch (e) {
      console.error(e);
    }
  };

  const getAllSchemes = async () => {
    try {
      const allSchemesRes = await Axios.get('/api/schemes'); 
      setAllSchemes(allSchemesRes.data)
    } catch(err) {
      console.error(err)
    }
  }


  useEffect(() => {
    const pages = Number(props.page) / Number(props.size);
    const p = [];
    for (let x = 0; x < pages; x++) {
      p[x] = x;
    }
    setPage(p);
  }, [props.page]);

  useEffect(() => {
    if(!props.dontfilter) {
      if(isAdmin){
        setShowKYC(props.kycFilter)
        getKYCLevels();
      }
      if(isPolaris) {
        getPolarisAccounts();
      }
    }
    if (data && csvInstance && csvInstance.current && csvInstance.current.link) {
      setTimeout(() => {
        csvInstance.current.link.click();
        setData([]);
      });
    }
  }, []);

  useEffect(() => {
    if(!isLoading) {
      if(!props.dontfilter && isPartnerFinancial) {
          getSpecialWallet(schemes[0].schemeID);
        }
    }
  }, [isLoading])

  useEffect(() => {
    if(!isAdmin) {
      setShowFilter(false)
    }
    getAllSchemes();
  }, [])

  const exportFile = () => {
    enum toexport {
      csv = 'csv',
      xls = 'csv',
      txt = 'txt',
      json = 'json',
    }
    const dataToExport = props.toExport;
    const fileName = isMcPherson ? 'McPherson Report' : 'Specswallet Report';
    exportFromJSON({ data: dataToExport, fileName, exportType: toexport[format] });
  };

  return (
    <section className="inner-layout-container">
      <div className="inner__layout__header">
        <div>
          <span>
            {props.title}
          </span>
        </div>
        <div>
          <img src={Union} alt="union symbol" />
          <span>{'>'}</span>
          <p>{props.path}</p>
        </div>
      </div>
      <div style={{ backgroundColor: '#fff', marginTop: '29px', borderRadius: '8px', padding: '12px', paddingTop: '35px' }}>
        {!props.dontfilter && (
          <div className="childToolBar">
            <div className="search-date">
              {props.showSearch &&
                  !isTrue && (
                    <div className="search-input">
                      <input onChange={e => setSearchKey(e.target.value)} placeholder="Search" value={searchKey}/>
                      <div>
                        <button onClick={() => props.setSearch(searchKey)}>
                          {props.searchLoading ? 
                            <Spinner style={{ color: '#fff' }} animation="border" role="status" size="sm"/> : 
                            <img src={Search} alt="search icon" />
                          }
                        </button>
                      </div>
                    </div>
                )
              }
              {props.showDateRange && (isPolaris || isSchFinancial || isPartnerFinancial) && (
                <div className="date__filter__input">
                  <div>
                    <label>Showing from: </label>
                    <input 
                      type="date"
                      value={props.dateRange.start}
                      onChange={(e) => props.handleDateChange({...props.dateRange, start: e.target.value})}
                    />
                  </div>
                  <div>
                    <label>to: </label>
                    <input 
                      type="date"
                      value={props.dateRange.end}
                      onChange={(e) => props.handleDateChange({...props.dateRange, end: e.target.value})}
                    />
                  </div>
                </div>
              )}
            </div>
            {(props.showWallets && (isPolaris || isPartnerFinancial)) && (
              <select onChange={e => props.changeWallet(e.target.value)} className="filter">
                {vendorWallets && vendorWallets.map(vw => (
                  <option key={vw.accountName} value={vw.accountNumber}>
                    {vw.accountName}
                  </option>
                ))}
              </select>
            )}
            <div style={{ flex: 0.5, display: 'flex', justifyContent: 'flex-end' }}>
              {showFilter && (
                !isTrue && (
                <select onChange={e => props.schemeChange(e.target.value)} className="filter">
                  {(allSchemes.length > 0) &&
                    allSchemes.map((s, i) => (
                      <option key={i} value={JSON.stringify(s)}>
                        {s.scheme}
                      </option>
                    ))}
                </select>
              )
              )}
              {showFilter && (
                showKYC && (
                  <div style={{marginRight: "1rem", width: '100px'}}>
                    <Select
                      defaultValue={props.selectedOption}
                      onChange={e => props.setSelectedOption(e)}
                      options={kycInfo}
                      placeholder="All"
                    />
                  </div>
                )
              )}
              <select onChange={e => setFormat(e.target.value)} style={{ height: '50px', marginRight: '10px' }} className="filter print">
                <option value="pdf">pdf</option>
                <option value="xls">xls</option>
                <option value="csv">csv</option>
                <option value="txt">text</option>
                <option value="json">json</option>
              </select>
              {props.headers && (
                <button
                  className="download-wallet-btn"
                  disabled={format === 'pdf'}
                  onClick={exportFile}
                >
                  Download
                </button>
              )}
              {(
                <ReactToPrint
                  trigger={() => {
                    return (
                      <button className="filter download-wallet-btn print" disabled={format !== 'pdf'}>{history.location.pathname === '/dowmload-wallets' ? 'Download' : 'Export'}</button>
                    );
                  }}
                  content={() => props.toPrint.current}
                />
              )}
              {data.length > 0 ? (
                <CSVLink data={data} headers={props.headers} filename={`Pouchii ${props.title}.csv`} ref={csvInstance} />
              ) : null}
            </div>
          </div>
        )}
        {props.children}

        {!props.show && (
          <div className="paginate">
            <div style={{ width: '250px', overflow: 'scroll' }}>
              <div style={{ display: 'flex' }}>
                {page.map((p, i) => (
                  <button
                    style={{ backgroundColor: active === i ? '#565682' : '#fff', color: active === i ? '#fff' : '#565682' }}
                    onClick={() => {
                      window.scrollTo({ top: 0, behavior: 'auto' });
                      setActive(i);
                      props.nextPage(i);
                    }}
                    key={i}
                  >
                    {i}
                  </button>
                ))}
              </div>
            </div>
          </div>
        )}
      </div>
    </section>
  );
}

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
});

export default connect(mapStateToProps)(Layout);
