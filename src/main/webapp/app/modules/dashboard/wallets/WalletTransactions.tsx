import React, { useState, useEffect } from 'react';
import { Table, Modal } from 'reactstrap';
import { Spinner } from 'react-bootstrap';
import Axios from 'axios';
import { connect, useSelector } from 'react-redux';
import { IRootState } from 'app/shared/reducers';
import { useHistory, RouteComponentProps } from 'react-router-dom';
import moment from 'moment';
import { ThreeDots } from  'react-loader-spinner';
import { isEmpty } from 'lodash';
import MoneyFormat from 'react-number-format';

import { setWalletData } from 'app/shared/reducers/reports';
import InnerLayout from '../components/Layout';
import Layout from '../Layout';
import TransactionModal from '../Utils/ViewTransactionModal';
import './walletTransaction.scss';
import { hasAnyAuthority } from 'app/shared/util/hasAuthorities';
import { AUTHORITIES } from 'app/config/constants';

export interface IStatementProps extends StateProps, DispatchProps, RouteComponentProps<{}> {}
function Wallet(props: IStatementProps) {
  const [statement, setStatement] = useState([]);
  const [opening, setOpening] = useState('0');
  const [closing, setClosing] = useState('0');
  const [currentRef, setCurrentRef] = useState('');
  const [data, setData] = useState({
    accountOwnerName: '',
    dateOpened: Date(),
    accountNumber: '',
    schemeName: '',
    accountName: '',
    accountOwnerPhoneNumber: '',
  });
  const [isvisible, setIsvisble] = useState(false);
  const [selectedTransaction, setSelectedTransaction] = useState({
    memo: '',
    transactionDate: '',
    amount: '',
    transactionRef: '',
    transactionStatus: '',
  });
  const [range, setRange] = useState({
    from: Date(),
    to: Date(),
  });
  const [trange, settRange] = useState({
    from: Date(),
    to: Date(),
  });
  const [pof, setPof] = useState('Bank');
  const [createdDate, setCreatedDate] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [reverseStatus, setReverseStatus] = useState('complete');
  const [loading, setLoading] = useState(false);
  // const [dateTo, setDateTo] = useState((Date());
  const history = useHistory();
  const handleClose = () => setShowModal(!showModal);

  const isMcPhersonFinancial = hasAnyAuthority(props.userLog.authorities, [AUTHORITIES.MCPHERSON_FINANCIAL]);
  const isFuoyeFinancial = hasAnyAuthority(props.userLog.authorities, [AUTHORITIES.FUOYE_FINANCIAL]);
  const isSchFinancial = isFuoyeFinancial || isMcPhersonFinancial;

  const isPolaris = hasAnyAuthority(props.userLog.authorities, [AUTHORITIES.POLARIS_ADMIN]);

  const getStatement = async (info, dateTo, datefrom) => {
    setData(info);
    try {
      setLoading(true)
      const getWalletAccount = await Axios.get(
        `/api/account-statement/${info.accountNumber}/${datefrom}/${moment(dateTo).format('YYYY-MM-DD')}`
      );
      // const getWalletAccount = await Axios.get(
      //   `https://wallet.remita.net/api/account-statement/${info.accountNumber}/${datefrom}/${moment(dateTo).format('YYYY-MM-DD')}`
      // );
      if (getWalletAccount.data.length && getWalletAccount.data.length > 0) {
        const openingBalance =
          getWalletAccount.data[0]['creditDebit'] === 'debit'
            ? getWalletAccount.data[0]['currentBalance'] + getWalletAccount.data[0]['amount']
            : getWalletAccount.data[0]['currentBalance'] - getWalletAccount.data[0]['amount'];
        setOpening(String(openingBalance));
        //  setOpening(getWalletAccount.data[0]['currentBalance']);
        setClosing(getWalletAccount.data[getWalletAccount.data.length - 1]['currentBalance']);
      }
      const res = getWalletAccount.data.reverse();
      setStatement(res);
      // console.error(getWalletAccount.data);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const viewTransaction = ref => {
    setCurrentRef(ref);
    handleClose();
  };

  useEffect(() => {
    if (isEmpty(props.data)) {
      history.push('/dwallets');
      return;
    }
    const dateTo = moment().format('YYYY-MM-DD');
    const dateFrom = moment().subtract(60, 'd').format('YYYY-MM-DD');
    setRange({
      to: dateTo,
      from: dateFrom,
    });
    getStatement(props.data, dateTo, dateFrom);
    //  console.error(props.data,  'hello');

    // // eslint-disable-next-line no-console
    // console.log(dateTo, dateFrom);
  }, [props.data]);

  const treatTransaction = async (id, type) => {
    try {
      const response = await Axios.post(`api/journal/${type}/${id}`);
      console.error(response);
      alert(`Transaction succesfully ${type === 'hold' ? 'put on hold' : 'released'}`);
      setIsvisble(false);
    } catch (e) {
      console.error(e.response);
      if (e.response) {
        alert(e.response.data.message);
      } else {
        alert('An error occured!');
      }
      // alert(`Transaction succesfully ${type === 'hold'? 'put on hold' : 'released'}`);
    }
  };

  const reverseTransaction = async transaction => {
    const dataToSend = {
      transRef: transaction.transactionRef,
      status: reverseStatus,
      pointOfFailure: pof,
    };
    try {
      const response = await Axios.post('api/reverse', dataToSend);
      const dateTo = moment().format('YYYY-MM-DD');
      const dateFrom = moment().subtract(60, 'd').format('YYYY-MM-DD');
      getStatement(props.data, dateTo, dateFrom);
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
  const filter = () => {
    const dateData: any = { ...props.data };
    //  dateData.dateOpened = moment(range.from).format('YYYY-MM-DD')
    getStatement(dateData, trange.to, moment(trange.from).format('YYYY-MM-DD'));
    setRange(trange);
  };

  const search = async (keyword: string) => {
    if (isEmpty(keyword)) {
      filter();
      return;
    }
    try {
      const account = await Axios.get(`api/account-statement/wallet/${data.accountNumber}/search?key=${keyword}&page=0&size=300
           `);
      console.error(account);
      setStatement(account.data.data);
    } catch (error) {
      console.error(error.response);
    }
  };

  return (
    <>
      <Layout>
        <InnerLayout dontfilter={true} page={0} size={0} title="Wallet Transactions" path={`> wallet > ${data.accountNumber}`}>
          <div style={{ width: '70vw' }}>
            {/* <span className = "titles">Statement of Account</span> */}
            <div>
              <p style={{ marginTop: '20px' }} className="titles2">
                Summary
              </p>
              <div className="dateContainer">
                <table>
                  <thead></thead>
                  <tbody>
                    <tr className="drow">
                      <th>Wallet Name: </th>
                      <td>{data.accountName}</td>
                    </tr>
                    {!isPolaris && 
                      <tr className="drow">
                        <th>Wallet Owner: </th>
                        <td>{data.accountOwnerName}</td>
                      </tr>
                    }
                    <tr className="drow">
                      <th>Data Created: </th>
                      <td>{data.dateOpened}</td>
                    </tr>
                    <tr>
                      <th>Date Range: </th>
                      <td>
                        from <b>{moment(range.from).format('ll')}</b> to <b>{moment(range.to).format('ll')}</b>
                      </td>
                    </tr>
                    <tr className="drow">
                      <th>Wallet Number: </th>
                      <td>{data.accountNumber}</td>
                    </tr>
                    <tr className="drow">
                      <th>Scheme Name: </th>
                      <td>{data.schemeName}</td>
                    </tr>
                    <tr className="drow">
                      <th>Opening Balance: </th>
                      <td>
                        {loading ? 
                          <Spinner 
                            style={{ color: '#435faa' }} 
                            animation="border" 
                            role="status" 
                            size="sm"
                          /> :
                          <MoneyFormat
                            value={opening}
                            displayType="text"
                            thousandSeparator={true}
                            decimalScale={2}
                            fixedDecimalScale={true}
                            prefix="₦"
                          />
                      }
                      </td>
                    </tr>
                    <tr className="drow">
                      <th>Closing Balance: </th>
                      <td>
                      {loading ? 
                        <Spinner 
                          style={{ color: '#435faa' }} 
                          animation="border" 
                          role="status" 
                          size="sm"
                        /> :
                        <MoneyFormat
                          value={closing}
                          displayType="text"
                          thousandSeparator={true}
                          decimalScale={2}
                          fixedDecimalScale={true}
                          prefix="₦"
                        />
                        }
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <div style={{ marginTop: '50px', marginLeft: '20px', overflow: 'scroll' }}>
              <div className="wallet-transaction-filter-box">
                <div>
                  <h5 style={{ marginTop: '20px' }} className="titles2 filter-title">
                    Filter Date
                  </h5>
                  <table>
                    <tr>
                      <td>
                        <label className="mr-3">
                          from:
                          <input
                            className="ml-2"
                            value={trange.from}
                            onChange={e => settRange({ ...range, from: e.target.value })}
                            type="date"
                          />
                        </label>
                        <label className="mr-3">
                          to:
                          <input
                            className="ml-2"
                            value={trange.to}
                            onChange={e => settRange({ ...range, to: e.target.value })}
                            type="date"
                          />
                        </label>
                      </td>
                      <td>
                        <button onClick={filter} className="small-button btn-filter-search">
                          Filter
                        </button>
                      </td>
                    </tr>
                  </table>
                </div>

                <div className="transaction-search">
                  <input onChange={e => search(e.target.value)} placeholder="search" />
                </div>
              </div>

              <div style={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between' }}>
                <p className="titles2">REPORT DETAILS</p>
              </div>
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
              {!loading && <Table striped>
                <thead>
                  <th>S/N</th>
                  <th>Transaction Date</th>
                  <th>Reference Number</th>
                  <th>Narration</th>
                  <th>Debit (₦)</th>
                  <th>Credit (₦)</th>
                  <th>Balance (₦)</th>
                  <th></th>
                </thead>
                <tbody>
                  {statement.map((v, i) => (
                    <tr style={{ cursor: 'pointer' }} className="tdata" key={i}>
                      <td onClick={() => !isSchFinancial ? viewTransaction(v.transactionRef) : null}>{i + 1}</td>
                      <td onClick={() => !isSchFinancial ? viewTransaction(v.transactionRef) : null}>{moment(v.transactionDate).format('ll')}</td>
                      <td onClick={() => !isSchFinancial ? viewTransaction(v.transactionRef) : null}>
                        <span style={{ fontSize: v.transactionRef.length > 15 ? '7px' : '14px' }}>{v.transactionRef}</span>
                      </td>
                      <td onClick={() => !isSchFinancial ? viewTransaction(v.transactionRef) : null}>{v.memo}</td>
                      <td onClick={() => !isSchFinancial ? viewTransaction(v.transactionRef) : null}>
                        <MoneyFormat
                          value={Number(v.debit)}
                          displayType="text"
                          thousandSeparator={true}
                          decimalScale={2}
                          fixedDecimalScale={true}
                          prefix="₦"
                        />
                      </td>
                      <td onClick={() => !isSchFinancial ? viewTransaction(v.transactionRef) : null}>
                        <MoneyFormat
                          value={Number(v.credit)}
                          displayType="text"
                          thousandSeparator={true}
                          decimalScale={2}
                          fixedDecimalScale={true}
                          prefix="₦"
                        />
                      </td>
                      <td onClick={() => !isSchFinancial ? viewTransaction(v.transactionRef) : null}>
                        <MoneyFormat
                          value={Number(v.currentBalance)}
                          displayType="text"
                          thousandSeparator={true}
                          decimalScale={2}
                          fixedDecimalScale={true}
                          prefix="₦"
                        />
                      </td>
                      <td>
                        <button
                          onClick={() => {
                            setSelectedTransaction(v);
                            setIsvisble(true);
                          }}
                          style={{
                            borderWidth: 0,
                            fontFamily: 'poppins',
                            fontSize: '12px',
                            backgroundColor: '#565682',
                            color: '#fff',
                            borderRadius: '10px',
                          }}
                        >
                          Options
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>}
            </div>
          </div>
        </InnerLayout>
        {isvisible && (
          <div className="options-modal">
            <div className="modal-inner">
              <span onClick={() => setIsvisble(false)} className="click" style={{ position: 'absolute', color: 'red', right: 10, top: 10 }}>
                Close
              </span>
              <p>{selectedTransaction.memo}</p>
              <p>
                Transaction Date: <b>{selectedTransaction.transactionDate}</b>
              </p>
              <p>
                Transaction Reference: <b>{selectedTransaction.transactionRef}</b>
              </p>
              <p>
                Transaction Amount:{' '}x
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
                <button
                  onClick={() => treatTransaction(selectedTransaction.transactionRef, 'hold')}
                  style={{
                    borderWidth: 0,
                    fontFamily: 'poppins',
                    padding: '10px',
                    fontSize: '12px',
                    backgroundColor: '#565682',
                    color: '#fff',
                    borderRadius: '10px',
                  }}
                >
                  Hold this Transaction
                </button>
              )}
              {selectedTransaction.transactionStatus === 'SUSPENDED' && (
                <button
                  onClick={() => treatTransaction(selectedTransaction.transactionRef, 'unhold')}
                  style={{
                    borderWidth: 0,
                    fontFamily: 'poppins',
                    margin: '20px',
                    fontSize: '12px',
                    padding: '10px',
                    backgroundColor: 'green',
                    color: '#fff',
                    borderRadius: '10px',
                  }}
                >
                  Release this Transaction
                </button>
              )}
              <br />
              {selectedTransaction.transactionStatus !== 'REVERSED' && selectedTransaction.transactionStatus !== 'COMPLETED' && (
                <div>
                  <label>
                    status:
                    <select onChange={e => setReverseStatus(e.target.value)}>
                      <option value="incomplete">incomplete</option>
                      <option value="completed">completed</option>
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
                      borderWidth: 0,
                      fontFamily: 'poppins',
                      padding: '10px',
                      margin: '20px',
                      fontSize: '12px',
                      backgroundColor: 'red',
                      color: '#fff',
                      borderRadius: '10px',
                    }}
                  >
                    Reverse Transaction
                  </button>
                </div>
              )}
            </div>
          </div>
        )}
      </Layout>
      <Modal isOpen={showModal} toggle={handleClose}>
        <TransactionModal reference={currentRef} />
      </Modal>
    </>
  );
}

const mapStateToProps = ({ reports, authentication: { account } }: IRootState) => ({
  data: reports.data,
  userLog: account,
});

const mapDispatchToProps = { setWalletData };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Wallet);
