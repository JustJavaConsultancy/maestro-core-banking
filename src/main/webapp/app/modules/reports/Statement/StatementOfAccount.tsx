import '../wallet.scss';
import React, { useState, useEffect, useRef } from 'react';
import { useLocation } from 'react-router-dom';
import { Table } from 'reactstrap';
import Axios from 'axios';
import { connect } from 'react-redux';
import { IRootState } from 'app/shared/reducers';
import { RouteComponentProps } from 'react-router-dom';
import moment from 'moment';
import { setWalletData } from 'app/shared/reducers/reports';
import { isEmpty } from 'lodash';
import exportFromJSON from 'export-from-json';
import { useReactToPrint } from 'react-to-print';
import MoneyFormat from 'react-number-format';
import processData, { xcelData } from './ProcessDataToExport';

export interface IStatementProps extends StateProps, DispatchProps, RouteComponentProps<{}> {}
function Wallet(props: any) {
  const [opening, setOpening] = useState('');
  const [closing, setClosing] = useState('');
  const [downloadFormat, setDownloadFormat] = useState('pdf');
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
  const [customerStatement, setCustomerStatement] = useState([]);
  const toPrint = useRef();
  const handlePrint = useReactToPrint({
    content: () => toPrint.current,
    // onAfterPrint: () => setShowPrint(false)
  }); 

  const location: any = useLocation();
  
  const handleDownload = (value: any, fileName: string) => {
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

  useEffect(() => {
    if(location.state) {
      setData(location.state.data)
      setOpening(location.state.opening)
      setClosing(location.state.closing)
      setCustomerStatement(location.state.customerStatement);
    } else {
      setData(props.customerData)
      setOpening(props.opening)
      setClosing(props.closing)
      setCustomerStatement(props.customerStatement)
    }
  }, [])

  return (
    <>
      <div ref={toPrint}>
        <span className="titles">Customer Wallet Statement</span>
        <div style={{ marginLeft: '54px' }}>
          <p className="titles2">Summary</p>
          <div className="dateContainer">
            <table>
              <thead></thead>
              <tbody>
                <tr className="drow">
                  <th>Wallet Owner Name: </th>
                  <td>{data?.accountOwnerName}</td>
                </tr>
                {/* <tr className = 'drow'>
                               <th>Data Created: </th>
                               <td>{data.dateOpened}</td>
                           </tr> */}
                <tr>
                  <th>Date Range: </th>
                  <td>
                    <b>{!isEmpty(data?.start) ? moment(data?.start).format('ll') : moment(data?.dateOpened).format('ll')}</b> -{' '}
                    <b>{!isEmpty(data?.end) ? moment(data?.end).format('ll') : moment(Date()).format('ll')}</b>
                  </td>
                </tr>
                <tr className="drow">
                  <th>Wallet Number: </th>
                  <td>{data?.accountNumber}</td>
                </tr>
                {(
                  <tr className="drow">
                    <th>Scheme Name: </th>
                    <td>{data?.scheme}</td>
                  </tr>
                )}
                <tr className="drow">
                  <th>Opening Balance: </th>
                  <td>
                    <MoneyFormat
                      value={opening}
                      displayType="text"
                      thousandSeparator={true}
                      decimalScale={2}
                      fixedDecimalScale={true}
                      prefix="₦"
                    />
                  </td>
                </tr>
                <tr className="drow">
                  <th>Closing Balance: </th>
                  <td>
                    <MoneyFormat
                      value={closing}
                      displayType="text"
                      thousandSeparator={true}
                      decimalScale={2}
                      fixedDecimalScale={true}
                      prefix="₦"
                    />
                  </td>
                </tr>
              </tbody>
            </table>
            <div className="report__download__section">
              <div className="download-format">
                <select
                  onChange={e => setDownloadFormat(e.target.value)}
                >
                  <option value="pdf">PDF</option>
                  <option value="csv">CSV</option>
                  <option value="xls">XLS</option>
                </select>
              </div>
              <div>
                <button onClick={() => handleDownload(transformFn(customerStatement, 'cst'), 'Customers Report')}>Download</button>
              </div>
            </div>
          </div>
        </div>
        <div style={{ marginLeft: '54px', marginTop: '50px' }}>
          <div>
            <p className="titles2">REPORT DETAILS</p>
          </div>
          <Table hover className="transaction-table">
            <thead>
              <th>S/N</th>
              <th>Transaction Date</th>
              <th>Reference Number</th>
              <th>External Reference</th>
              <th>Narration</th>
              <th>Debit (₦)</th>
              <th>Credit (₦)</th>
              <th>Balance (₦)</th>
            </thead>
            <tbody>
              {(customerStatement && customerStatement?.length > 0) && 
                <tr>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td></td>
                  <td style={{ fontSize: '14px' }}>Opening Balance</td>
                  <td></td>
                  <td></td>
                  <td style={{ fontSize: '13px' }}>
                    {
                      <MoneyFormat
                        value={opening}
                        displayType="text"
                        thousandSeparator={true}
                        decimalScale={2}
                        fixedDecimalScale={true}
                        //   prefix = "₦"
                      />
                    }
                  </td>
                </tr>
              }
              {(customerStatement && customerStatement?.length > 0) ? customerStatement?.map((v, i) => (
                <tr className="tdata" key={i}>
                  <td>{i + 1}</td>
                  <td>{moment(v.transactionDate).format('ll')}</td>
                  <td>{v.transactionRef}</td>
                  <td>{v.externalRef !== 'null' ? v.externalRef : ''}</td>
                  <td>{v.memo}</td>
                  <td>
                    <MoneyFormat
                      value={Number(v.debit)}
                      displayType="text"
                      thousandSeparator={true}
                      decimalScale={2}
                      fixedDecimalScale={true}
                      // prefix = "₦"
                    />
                  </td>
                  <td>
                    <MoneyFormat
                      value={Number(v.credit)}
                      displayType="text"
                      thousandSeparator={true}
                      decimalScale={2}
                      fixedDecimalScale={true}
                      // prefix = "₦"
                    />
                  </td>
                  <td>
                    <MoneyFormat
                      value={Number(v.currentBalance)}
                      displayType="text"
                      thousandSeparator={true}
                      decimalScale={2}
                      fixedDecimalScale={true}
                      // prefix = "₦"
                    />
                  </td>
                </tr>
              )) : 
                <span>No Data Available</span>
              }
              {(customerStatement && customerStatement?.length) > 0 && <tr>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td style={{ fontSize: '14px' }}>Closing Balance</td>
                <td></td>
                <td></td>
                <td style={{ fontSize: '13px' }}>
                  {
                    <MoneyFormat
                      value={closing}
                      displayType="text"
                      thousandSeparator={true}
                      decimalScale={2}
                      fixedDecimalScale={true}
                      //   prefix = "₦"
                    />
                  }
                </td>
              </tr>}
            </tbody>
          </Table>
        </div>
      </div>
    </>
  );
}

const mapStateToProps = ({ reports }: IRootState) => ({
  data: reports.data,
});

const mapDispatchToProps = { setWalletData };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Wallet);
