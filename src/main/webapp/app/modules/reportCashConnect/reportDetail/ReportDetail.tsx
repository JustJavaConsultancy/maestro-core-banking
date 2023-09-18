import React, { useCallback, useRef, useState } from 'react';
import { Table } from 'react-bootstrap';

import { useLocation } from 'react-router';
import moment from 'moment';
import MoneyFormat from 'react-number-format';

import ReactToPrint from 'react-to-print';
import exportFromJSON from 'export-from-json';

import './reportDetail.scss';
import { SchemeType } from '../ReportCashConnect';

interface PolarisDataType {
  data: {
    provider_response: {
      account_id: string;
      account_number: string;
      available_balance: number;
      ledger_balance: number;
      minimum_balance: string;
    };
  };
}
interface LocationType {
  report: string;
  fromDate: string;
  toDate: string;
  jsonResponse: { Message?: any; statement_list?: any; account_number?: string; closing_balance?: string; opening_balance?: string };
  isPolaris: boolean;
  nuban: string;
  scheme: SchemeType;
  polarisData: PolarisDataType;
}

export default function () {
  const location = useLocation<LocationType>();
  const isPolaris = location.state.isPolaris;
  // const openingBalancePolaris = location.state.jsonResponse.opening_balance;
  // const availableBalance = location.state.polarisData.data.provider_response.available_balance;
  // const ledgerBalance = location.state.polarisData.data.provider_response.ledger_balance;

  const [format, setFormat] = useState('pdf');
  const [data, setData] = useState({
    startDate: location.state.fromDate,
    endDate: location.state.toDate,
    transactions: !isPolaris ? location.state.jsonResponse.Message : location.state.jsonResponse.statement_list,
  });

  const { endDate, startDate, transactions } = data;
  const toPrint = useRef(null);

  const bankStatement = useCallback(
    () =>
      transactions.map(transaction => ({
        ['Transaction Date']: isPolaris ? transaction.transaction_date : transaction.TransactionDate,
        ['Transaction Ref']: isPolaris ? transaction.transaction_reference : transaction.ReferenceID,
        ['Narration']: isPolaris ? transaction.description : transaction.Narration,
        ['Debit']: isPolaris ? (transaction.transaction_type === 'D' ? transaction.transaction_amount / 100 : '') : transaction.RecordType,
        ['Credit']: isPolaris ? (transaction.transaction_type === 'C' ? transaction.transaction_amount / 100 : '') : transaction.RecordType,
        ['Transaction Type']: isPolaris ? (transaction.transaction_type === 'D' ? 'debit' : 'credit') : transaction.RecordType,
        ['Balance']: isPolaris ? transaction.balance / 100 : transaction.Balance,
      })),
    [transactions]
  );

  const exportFile = () => {
    const reportStatement = bankStatement();
    enum toexport {
      csv = 'csv',
      xls = 'csv',
      txt = 'txt',
      json = 'json',
    }
    const dataToExport = reportStatement;
    const fileName = isPolaris ? 'Polaris-Pouchii Bank Statement' : 'CashConnet-Pouchii Bank Statement';
    exportFromJSON({ data: dataToExport, fileName, exportType: toexport[format] });
  };

  return (
    <section id="connect-details" ref={toPrint}>
      <div className="connect_report-header">
        <div>
          <h6 style={{ marginBottom: '1rem', fontWeight: 'bold' }}>Summary</h6>
          <div className="report-bg">
            <p>
              Account Owner Name:{' '}
              <strong>
                <span className="report_modify">{!isPolaris ? 'CashConnect MFB' : 'Polaris Bank'} Transaction Report</span>
              </strong>
            </p>
            {isPolaris && (
              <p>
                Scheme Type:{' '}
                <strong>
                  <span className="report_modify">
                    {location.state.scheme.scheme === 'Systemspecs wallet' ? 'Pouchii' : location.state.scheme.scheme} Scheme
                  </span>
                </strong>
              </p>
            )}
            <p className="date_filter">
              Date Range:
              <strong>
                <span className="report_modify">{moment(startDate).format('LL')}</span> - <span>{moment(endDate).format('LL')}</span>
              </strong>
            </p>
            {/* {isPolaris ? ( */}
            <>
              <p>
                Account Number:{' '}
                <strong>
                  <span className="report_modify">{isPolaris ? location.state.jsonResponse.account_number : location.state.nuban}</span>
                </strong>
              </p>
              <p>
                Available Balance:{' '}
                <strong>
                  <span className="report_modify">
                    <MoneyFormat
                      value={
                        (isPolaris ? location.state.polarisData.data.provider_response.available_balance : transactions[0].Balance) / 100
                      }
                      displayType="text"
                      thousandSeparator={true}
                      decimalScale={2}
                      fixedDecimalScale={true}
                      prefix="₦"
                      className="report_figure"
                    />
                  </span>
                </strong>
              </p>
              <p>
                Opening Balance:{' '}
                <strong>
                  <span className="report_modify">
                    <MoneyFormat
                      value={(isPolaris ? +location.state.jsonResponse.opening_balance : transactions[0].OpeningBalance) / 100}
                      displayType="text"
                      thousandSeparator={true}
                      decimalScale={2}
                      fixedDecimalScale={true}
                      prefix="₦"
                      className="report_figure"
                    />
                  </span>
                </strong>
              </p>
              <p>
                Closing Balance:{' '}
                <strong>
                  <span className="report_modify">
                    <MoneyFormat
                      value={(isPolaris ? +location.state.jsonResponse.closing_balance : transactions[0].Balance) / 100}
                      displayType="text"
                      thousandSeparator={true}
                      decimalScale={2}
                      fixedDecimalScale={true}
                      prefix="₦"
                      className="report_figure"
                    />
                  </span>
                </strong>
              </p>
            </>
            {/* ) : null} */}
          </div>
        </div>

        <div className="download_content-box">
          <select className="select_options-dd" onChange={e => setFormat(e.target.value)}>
            <option value="pdf">pdf</option>
            <option value="xls">xls</option>
            <option value="csv">csv</option>
            <option value="txt">text</option>
            <option value="json">json</option>
          </select>

          {format === 'pdf' ? (
            <ReactToPrint
              trigger={() => {
                return (
                  <div className="connect-btn-box">
                    <button style={{ backgroundColor: '#f2f5fb6d', color: '#000' }}>Download</button>
                  </div>
                );
              }}
              content={() => toPrint.current}
              copyStyles
              documentTitle="Polaris Bank Report"
            />
          ) : (
            <div className="connect-btn-box">
              <button style={{ backgroundColor: '#f2f5fb6d', color: '#000' }} onClick={exportFile}>
                Download
              </button>
            </div>
          )}
        </div>
      </div>
      {/* <div className="detail-header">
        <h3>{!isPolaris ? 'CashConnect MFB' : 'Polaris Bank'} Transaction Report</h3>
        <p className="date_filter">
          <span>{moment(startDate).format('LL')}</span> - <span>{moment(endDate).format('LL')}</span>
        </p>

        {isPolaris && (
          <>
            <p>
              <span>
                Available Balance:
                <MoneyFormat
                  value={availableBalance / 100}
                  displayType="text"
                  thousandSeparator={true}
                  decimalScale={2}
                  // fixedDecimalScale={true}
                  prefix="₦"
                  className="report_figure"
                />
              </span>
            </p>
            <p>
              <span>
                Ledger Balance:
                <MoneyFormat
                  value={ledgerBalance / 100}
                  displayType="text"
                  thousandSeparator={true}
                  decimalScale={2}
                  // fixedDecimalScale={true}
                  prefix="₦"
                  className="report_figure"
                />
              </span>
            </p>
          </>
        )}
      </div> */}

      <div>
        {transactions.length < 1 ? (
          <div style={{ textAlign: 'center' }}>
            <Table style={{ marginBottom: '10rem' }}>
              <thead>
                <tr>
                  <th>S/N</th>
                  <th>Transaction Date</th>
                  {/* <th>ID</th> */}
                  {/* <th>Amount</th> */}
                  <th>Credit (₦)</th>
                  <th>Debit (₦)</th>
                  <th>Opening Balance (₦)</th>
                  <th>Closing Balance (₦)</th>
                  {/* <th>InstrumentNo</th> */}
                  {/* <th>Record Type</th> */}
                  <th>Transaction Ref</th>
                  {/* <th>Service Code</th> */}
                  <th>Narration</th>
                  <th>Reversed</th>
                  {/* <th>Withdrawable Amount</th> */}
                </tr>
              </thead>
            </Table>
            <h1>No Data</h1>
          </div>
        ) : (
          <Table className="px-5">
            <thead>
              <tr>
                <th>S/N</th>
                <th>Transaction Date</th>
                {/* <th>ID</th> */}
                {/* <th>Amount</th> */}
                <th>Transaction Ref</th>
                <th>Narration</th>
                {isPolaris && (
                  <>
                    <th>Debit (₦)</th>
                    <th>Credit (₦)</th>
                  </>
                )}

                {!isPolaris && (
                  <>
                    <th>Credit (₦)</th>
                    <th>Debit (₦)</th>
                  </>
                )}
                {!isPolaris ? (
                  <>
                    <th>Opening Balance (₦)</th>
                    <th>Closing Balance (₦)</th>
                  </>
                ) : (
                  <th>Balance (₦)</th>
                )}
                {/* <th>InstrumentNo</th> */}
                {/* <th>Record Type</th> */}
                {/* <th>Reference ID</th> */}
                {/* <th>Service Code</th> */}
                {/* <th>Narration</th> */}
                {!isPolaris && <th>Reversed</th>}
                {/* <th>Withdrawable Amount</th> */}
              </tr>
            </thead>
            <tbody>
              {transactions.map((transaction, i) => (
                <tr key={i}>
                  <td>{i + 1}.</td>
                  <td>
                    <small>{moment(!isPolaris ? transaction.CurrentDate : transaction.transaction_date).format('LL')}</small>
                  </td>
                  <td>{!isPolaris ? transaction.ReferenceID : transaction.transaction_reference}</td>
                  <td>
                    <small>{!isPolaris ? transaction.Narration : transaction.description}</small>
                  </td>
                  {/* <td>{transaction.Id}</td> */}
                  {/* <td>{transaction.Amount}</td> */}
                  {isPolaris && (
                    <>
                      <td>
                        {transaction.transaction_type === 'D' ? (
                          <MoneyFormat
                            value={transaction.transaction_amount / 100}
                            displayType="text"
                            thousandSeparator={true}
                            decimalScale={2}
                            fixedDecimalScale={true}
                            prefix="-"
                            className="report-debit"
                          />
                        ) : (
                          ''
                        )}
                      </td>
                      <td>
                        {transaction.transaction_type === 'C' ? (
                          <MoneyFormat
                            value={transaction.transaction_amount / 100}
                            displayType="text"
                            thousandSeparator={true}
                            decimalScale={2}
                            fixedDecimalScale={true}
                            prefix="+"
                            className="report-credit"
                          />
                        ) : (
                          ''
                        )}
                      </td>
                    </>
                  )}
                  {!isPolaris && (
                    <>
                      <td className="connect-credit">{transaction.Credit}</td>
                      <td className="connect-debit">{transaction.Debit}</td>
                    </>
                  )}

                  {!isPolaris ? (
                    <>
                      <td>
                        <MoneyFormat
                          value={transaction.OpeningBalance / 100}
                          displayType="text"
                          thousandSeparator={true}
                          decimalScale={2}
                          // fixedDecimalScale={true}
                        />
                      </td>
                      <td className={i === 0 ? 'connect-current-total' : ''}>
                        <MoneyFormat
                          value={transaction.Balance / 100}
                          displayType="text"
                          thousandSeparator={true}
                          decimalScale={2}
                          // fixedDecimalScale={true}
                        />
                      </td>
                    </>
                  ) : (
                    <td>
                      <MoneyFormat
                        value={transaction.balance / 100}
                        displayType="text"
                        thousandSeparator={true}
                        decimalScale={2}
                        // fixedDecimalScale={true}
                      />
                    </td>
                  )}
                  {/* <td>{transaction.RecordType}</td> */}

                  {!isPolaris && (
                    <td>
                      <span className={`connect-reversed ${transaction.IsReversed ? 'connect-reversed-true' : 'connect-reversed-false'}`}>
                        {transaction.IsReversed ? 'true' : 'false'}
                      </span>
                    </td>
                  )}

                  {/* <td>{transaction.WithdrawableAmount}</td> */}
                </tr>
              ))}
            </tbody>
          </Table>
        )}
      </div>
    </section>
  );
}
