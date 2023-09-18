import './transactions.scss';
import Axios from 'axios';
import React, { useEffect, useState, useRef } from 'react';
import InnerLayout from '../components/Layout';
import { Modal } from 'reactstrap';
import { Table } from 'react-bootstrap';
import MoneyFormat from 'react-number-format';
import moment from 'moment';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { isEmpty } from 'lodash';
import { ThreeDots } from  'react-loader-spinner';

import TransactionModal from '../Utils/ViewTransactionModal';
import Layout from '../Layout';
import { IRootState } from 'app/shared/reducers';
import { headers } from './transactionData/transactionData';
import { hasAnyAuthority } from 'app/shared/util/hasAuthorities';
import { AUTHORITIES } from 'app/config/constants';

import { getCustomerStatement } from '../reports/utils/utils';

function Transactions(props) {
  const [transactions, setransactions] = useState([]);
  const [total, setTotal] = useState(0);
  const [size, setSize] = useState(200);
  const [page, setPage] = useState(0);
  const [isvisible, setIsvisble] = useState(false);
  const [currentRef, setCurrentRef] = useState('');
  const [selectedTransaction, setSelectedTransaction] = useState({
    memo: '',
    transactionDate: '',
    amount: '',
    transactionRef: '',
    transactionStatus: '',
  });
  const [pof, setPof] = useState('Bank');
  const [reverseStatus, setReverseStatus] = useState('completed');
  const [search, setSearch] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [schemes, setSchemes] = useState([]);
  const [scheme, setScheme] = useState({ schemeID: '' });
  const [isDisable, setIsDisable] = useState(true);
  const [loading, setLoading] = useState(false);
  const [filterInfo, setFilterInfo] = useState({
    start: '',
    end: '',
  });
  const [selectedWallet, setSelectedWallet] = useState('')
  const [searchLoading, setSearchLoading] = useState(false);

  const { schemeID } = scheme;
  const toPrint = useRef();

  const isAdmin = hasAnyAuthority(props.userLog.authorities, [AUTHORITIES.ADMIN, AUTHORITIES.ROLE_SUPER_ADMIN]);
  const isPolaris = hasAnyAuthority(props.userLog.authorities, [AUTHORITIES.POLARIS_ADMIN]);
  
  const isMcPhersonFinancial = hasAnyAuthority(props.userLog.authorities, [AUTHORITIES.MCPHERSON_FINANCIAL]);
  const isFuoyeFinancial = hasAnyAuthority(props.userLog.authorities, [AUTHORITIES.FUOYE_FINANCIAL]);
  const isSchFinancial = isMcPhersonFinancial || isFuoyeFinancial;

  const isPaymasta = hasAnyAuthority(props.userLog.authorities, [AUTHORITIES.PAYMASTA_ADMIN]);
  const isWragby = hasAnyAuthority(props.userLog.authorities, [AUTHORITIES.WRAGBY_ADMIN]);
  const isWynk = hasAnyAuthority(props.userLog.authorities, [AUTHORITIES.WYNK_ADMIN]);
  const isPartnerFinancial = isPaymasta || isWragby || isWynk;

  const getTransactions: any = async (paget, sch) => {
    try {
      setLoading(true)
      const theTransactions = await Axios.get(`/api/account-statement/${sch}/search?key=${search}&page=${paget}&size=${size}`);
      // const theTransactions = await Axios.get(
      //   `https://wallet.remita.net/api/account-statement/${sch}/search?key=&page=${paget}&size=${size}`
      // );
      setPage(paget);
      setTotal(theTransactions.data.metadata.totalNumberOfRecords);
      setransactions(theTransactions.data.data);
      console.error(theTransactions);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false)
    }
  };

  const getWalletTransactions = async (acct) => {
    try {
        setLoading(true)
        if(isMcPhersonFinancial || isFuoyeFinancial) {
          const res = await  Axios.get(`/api/transaction/logs/account/${acct}/${!isEmpty(filterInfo.start) ? filterInfo.start : moment().subtract(360, 'd').format('YYYY-MM-DD')}/${
            !isEmpty(filterInfo.end) ? filterInfo.end : moment(Date()).format('YYYY-MM-DD')
          }`)
          // res = await  Axios.get(`https://wallet.remita.net/api/transaction/logs/account/${acct}/${!isEmpty(filterInfo.start) ? filterInfo.start : moment().subtract(60, 'd').format('YYYY-MM-DD')}/${
          //   !isEmpty(filterInfo.end) ? filterInfo.end : moment(Date()).format('YYYY-MM-DD')
          // }`)
          setransactions(res.data.data);
        } else {
          const res = await Axios.get(
            `/api/account-statement/${acct}/${!isEmpty(filterInfo.start) ? filterInfo.start : moment().subtract(3, 'd').format('YYYY-MM-DD')}/${
            !isEmpty(filterInfo.end) ? filterInfo.end : moment(Date()).format('YYYY-MM-DD')}`
            );
          // const res = await Axios.get(
          //   `https://wallet.remita.net/api/account-statement/${acct}/${!isEmpty(filterInfo.start) ? filterInfo.start : moment().subtract(3, 'd').format('YYYY-MM-DD')}/${
          //     !isEmpty(filterInfo.end) ? filterInfo.end : moment(Date()).format('YYYY-MM-DD')}`
          //     );
          setransactions(res.data.reverse());
        }
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false)
    }
  }

  const getSchemes = async () => {
    try {
      if(isMcPhersonFinancial) {
        setScheme({schemeID: '4d6350686572736f6e'});
        getWalletTransactions('1000000080');
      }
      else if (isPolaris || isPartnerFinancial || isFuoyeFinancial) {
        if(isPolaris){
          setScheme({schemeID: '53797374656d73706563732077616c6c6574'});
        }
        if(selectedWallet) {
          getWalletTransactions(selectedWallet);
        }
      }
      else {
        const theSchemes = await Axios.get(`/api/schemes/admin/${props.userLog.login}`);
        setSchemes(theSchemes.data.data);
        setScheme(theSchemes.data.data[0]);
        getTransactions(0, theSchemes.data.data[0].schemeID);
      }
    } catch (e) {
      console.error(e);
    }
  };

  const handleClose = () => setShowModal(!showModal);
  const handleIsVisible = () => setIsvisble(!isvisible);

  const treatTransaction = async (id, type) => {
    try {
      const response = await Axios.post(`api/journal/${type}/${id}`);
      console.error(response);
      alert(`Transaction succesfully ${type === 'hold' ? 'put on hold' : 'released'}`);
      setIsvisble(false);
      getTransactions(page, scheme);
    } catch (e) {
      console.error(e.response);
      if (e.response) {
        alert(e.response.data.message);
      } else {
        alert('An error occured!');
      }
    }
  };

  const reverseTransaction = async transaction => {
    const data = {
      transRef: transaction.transactionRef,
      status: reverseStatus,
      pointOfFailure: reverseStatus !== 'complete' ? pof : '',
    };
    try {
      const response = await Axios.post('api/reverse', data);
      getTransactions(page, scheme);
      setIsvisble(false);
      alert('Transaction reversed successfully');
    } catch (e) {
      console.error(e.response);
      if (e.response) {
        alert(e.response.data.message);
      } else {
        alert('An error occured!');
      }
    }
  };

  const searchTransactions: any = async s => {
    try {
      setSearchLoading(true)
      const theTransactions = await Axios.get(`/api/account-statement/search?key=${s}&page=${0}&size=${size}`);
      console.error(theTransactions);
      setTotal(theTransactions.data.metadata.totalNumberOfRecords);
      setPage(0);
      setransactions(theTransactions.data.data);
    } catch (e) {
      console.error(e);
    } finally {
      setSearchLoading(false)
    }
  };
  
  const viewTransaction = (ref) => {
    setCurrentRef(ref);
    handleClose();
  };

  const asyncExportMethod = async () => {
    try {
      const result = await Axios.get(`/api/account-statement/${scheme.schemeID}/search?key=${search}&page=${0}&size=${1000}`);
      return result;
    } catch (error) {
      console.error(error);
      return [];
    }
  };

  const handleDateChange = (date) => {
    setFilterInfo(date)
  }

  useEffect(() => {
    getSchemes();
  }, [filterInfo.start, filterInfo.end, selectedWallet]);
  
  useEffect(() => {
    if (scheme.schemeID) {
      setIsDisable(false);
    } else {
      setIsDisable(true);
    }
  }, [schemeID]);


  const handleschemeChange = s => {
    console.error(s.schemeID);
    setScheme(s);
    getTransactions(0, s.schemeID);
  };

  const changeWallet = (sw) => {
    setSelectedWallet(sw)
  }

  return (
    <>
      <Layout>
        <InnerLayout
          schemes={schemes}
          schemeChange={s => {
            handleschemeChange(JSON.parse(s));
          }}
          setSearch={s => {
            setSearch(s);
            searchTransactions(s);
          }}
          getData={asyncExportMethod}
          isDisable={isDisable}
          headers={headers}
          toExport={transactions}
          toPrint={toPrint}
          nextPage={pageto => getTransactions(pageto, schemeID)}
          page={total}
          size={size}
          title="Transactions"
          path="Transactions"
          isSch={true}
          dateRange={filterInfo}
          handleDateChange={handleDateChange}
          changeWallet={changeWallet}
          showWallets
          searchLoading={searchLoading}
          showDateRange
          showSearch
        >
          <div ref={toPrint} style={{ minWidth: '300px', minHeight: '300px', marginTop: '20px' }} className="special-table">
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
           {!loading && ((transactions && transactions.length > 0) ? (
            <Table responsive hover className="transaction-table">
              <thead className="tableListData">
                <th>S/N</th>
                <th>Transaction Date</th>
                <th>Transaction Reference</th>
                <th>Amount</th>
                <th>Narration</th>
                {!(isSchFinancial) && <th>External Reference</th>}
                <th>Status</th>
                {isAdmin && <th></th>}
              </thead>
              <tbody>
                {(transactions && transactions.length > 0) && (
                transactions.map((transaction, index) => (
                  <tr style={{ cursor: 'pointer' }} className="tableListData" key={transaction.id}>
                    <td onClick={() => !(isSchFinancial) ? viewTransaction(transaction.transactionRef) : null}>
                      <small className="trans-size">{index + 1 + Number(page) * size}</small>
                    </td>
                    <td onClick={() => !(isSchFinancial) ? viewTransaction(transaction.transactionRef) : null}>
                      <small className="trans-size">
                        {(isSchFinancial) ? 
                        moment(transaction.createdDate).format('ll')
                        : moment(transaction.transactionDate).format('ll')
                        }
                      </small>  
                    </td>
                    <td onClick={() => !(isSchFinancial) ? viewTransaction(transaction.transactionRef) : null}>
                      <small className="trans-size">
                        {(isSchFinancial) ? transaction.transRef : transaction.transactionRef}
                      </small>
                    </td>
                    <td
                      onClick={() => !(isSchFinancial) ? viewTransaction(transaction.transactionRef) : null}
                      style={{ color: transaction.creditDebit === 'debit' ? 'red' : 'green' }}
                    >
                      {/* {transaction.creditDebit === 'debit' ? '-' : '+'} */}
                      <MoneyFormat
                        value={transaction.amount}
                        displayType="text"
                        thousandSeparator={true}
                        decimalScale={2}
                        fixedDecimalScale={true}
                        prefix="₦"
                      />
                    </td>
                    <td onClick={() => !(isSchFinancial) ? viewTransaction(transaction.transactionRef) : null} className="trans-external">
                      <small className="trans-size">
                        {isSchFinancial ? transaction.narration : transaction.memo}
                      </small>
                    </td>
                    {!(isSchFinancial) && 
                      <td onClick={() => !(isSchFinancial) ? viewTransaction(transaction.transactionRef) : null} className="trans-external">
                        <small className="trans-size">
                          {transaction.externalRef !== 'null' ? transaction.externalRef : ''}
                        </small>
                      </td>
                    }
                    <td onClick={() => !(isSchFinancial) ? viewTransaction(transaction.transactionRef) : null}>
                      <small
                        className={`trans-status ${
                          transaction.transactionStatus === 'COMPLETED'
                            ? 'trans-status'
                            : transaction.transactionStatus === 'INCOMPLETE'
                            ? 'trans-status-incomplete'
                            : 'trans-status-reverse'
                        }`}
                        style={
                          {
                            // fontSize: 8,
                            // padding: '5px 10px',
                            // borderRadius: '15px',
                            // fontSize: '12px',
                            // color: '#00A07E',
                            // backgroundColor: 'rgba(0, 196, 140, 0.25)',
                          }
                        }
                      >
                        {isSchFinancial ? transaction.status : transaction.transactionStatus}
                      </small>
                    </td>
                    {isAdmin && 
                      <td>
                        <button
                          className="trans-table-action"
                          onClick={() => {
                            setSelectedTransaction(transaction);
                            setIsvisble(true);
                          }}
                        >
                          <ActionSvg />
                        </button>
                      </td>
                    }
                  </tr>
                )))}
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
      <Modal isOpen={isvisible} toggle={handleIsVisible} centered={true}>
        <div className="options-modal">
          <div className="modal-inner">
            <div onClick={() => setIsvisble(false)} className="click">
              <span>x</span>
            </div>

            <div>
              <p>
                Transaction Narration: <strong>{selectedTransaction.memo}</strong>
              </p>
              <p>
                Transaction Date: <b>{selectedTransaction.transactionDate}</b>
              </p>
              <p>
                Transaction Reference: <b>{selectedTransaction.transactionRef}</b>
              </p>
              <p>
                Transaction Amount:{' '}
                <b>
                  <MoneyFormat
                    value={selectedTransaction.amount}
                    displayType="text"
                    thousandSeparator={true}
                    decimalScale={2}
                    fixedDecimalScale={true}
                    prefix="₦"
                  />
                </b>
              </p>
              {selectedTransaction.transactionStatus === 'COMPLETED' && (
                <>
                  <button
                    onClick={() => treatTransaction(selectedTransaction.transactionRef, 'hold')}
                    style={{
                      // borderWidth: 0,
                      // fontFamily: 'poppins',
                      // padding: '10px',
                      // fontSize: '12px',
                      backgroundColor: '#565682',
                      // color: '#fff',
                      // borderRadius: '10px',
                    }}
                    className="trans-btn"
                  >
                    Hold Transaction
                  </button>
                  <button
                    onClick={() => reverseTransaction(selectedTransaction)}
                    style={{
                      // borderWidth: 0,
                      // fontFamily: 'poppins',
                      // padding: '10px',
                      // margin: '20px',
                      // fontSize: '12px',
                      backgroundColor: 'red',
                      // color: '#fff',
                      // borderRadius: '10px',
                    }}
                    className="trans-btn"
                  >
                    Reverse Transaction
                  </button>
                </>
              )}
              {selectedTransaction.transactionStatus === 'SUSPENDED' && (
                <button
                  onClick={() => treatTransaction(selectedTransaction.transactionRef, 'unhold')}
                  style={{
                    // borderWidth: 0,
                    // fontFamily: 'poppins',
                    // margin: '20px',
                    // fontSize: '12px',
                    // padding: '10px',
                    backgroundColor: 'green',
                    // color: '#fff',
                    // borderRadius: '10px',
                  }}
                  className="trans-btn"
                >
                  Release Transaction
                </button>
              )}
              <br />
              {selectedTransaction.transactionStatus !== 'REVERSED' && selectedTransaction.transactionStatus !== 'COMPLETED' && (
                <div>
                  <label>
                    status:
                    <select onChange={e => setReverseStatus(e.target.value)}>
                      <option value="completed">completed</option>
                      <option value="incomplete">incomplete</option>
                    </select>
                  </label>
                  <label style={{ marginLeft: '10px' }}>
                    Point Of Failure:
                    <select onChange={e => setPof(e.target.value)}>
                      <option value="Bank">Bank</option>
                      <option value="Wallet">Wallet</option>
                    </select>
                  </label>
                  <button
                    onClick={() => reverseTransaction(selectedTransaction)}
                    style={{
                      // borderWidth: 0,
                      // fontFamily: 'poppins',
                      // padding: '10px',
                      // margin: '20px',
                      // fontSize: '12px',
                      backgroundColor: 'red',
                      // color: '#fff',
                      // borderRadius: '10px',
                    }}
                    className="trans-btn"
                  >
                    Reverse Transaction
                  </button>
                </div>
              )}
            </div>
          </div>
        </div>
      </Modal>
      <Modal isOpen={showModal} toggle={handleClose}>
        <TransactionModal reference={currentRef} />
      </Modal>
    </>
  );
}

const mapStateToProps = ({ reports, authentication: { isAuthenticated, account, sessionHasBeenFetched } }: IRootState) => ({
  user: reports.user,
  userLog: account,
});

const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Transactions);

export function ActionSvg(): JSX.Element {
  return (
    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="13" viewBox="0 0 14 13" fill="none">
      <path
        d="M8.14893 6.5C8.14893 7.3975 7.42142 8.125 6.52393 8.125C5.62643 8.125 4.89893 7.3975 4.89893 6.5C4.89893 5.6025 5.62643 4.875 6.52393 4.875C7.42142 4.875 8.14893 5.6025 8.14893 6.5Z"
        fill="#000"
      />
      <path
        d="M8.14893 1.625C8.14893 2.5225 7.42142 3.25 6.52393 3.25C5.62643 3.25 4.89893 2.5225 4.89893 1.625C4.89893 0.727501 5.62643 0 6.52393 0C7.42142 0 8.14893 0.727501 8.14893 1.625Z"
        fill="#000"
      />
      <path
        d="M8.14893 11.375C8.14893 12.2725 7.42142 13 6.52393 13C5.62643 13 4.89893 12.2725 4.89893 11.375C4.89893 10.4775 5.62643 9.75 6.52393 9.75C7.42142 9.75 8.14893 10.4775 8.14893 11.375Z"
        fill="#000"
      />
    </svg>
  );
}
