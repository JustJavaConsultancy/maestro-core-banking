import '../wallet.scss';
import React, { useState, useEffect, useRef } from 'react';
import { Table } from 'reactstrap';
import Axios from 'axios';
import { connect } from 'react-redux';
import { IRootState } from 'app/shared/reducers';
import { RouteComponentProps, useLocation } from 'react-router-dom';
import moment from 'moment';
import { setWalletData } from 'app/shared/reducers/reports';
import ReactToPrint from 'react-to-print';
import { isEmpty } from 'lodash';
import exportFromJSON from 'export-from-json';
import { useReactToPrint } from 'react-to-print';
import MoneyFormat from 'react-number-format';

import './callOverReport.scss';

interface LocationType {
  start: string;
  end: string;
}

export interface IStatementProps extends StateProps, DispatchProps, RouteComponentProps<{}> {}
function CallOverReport(props: any) {
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
  const [downloadFormat, setDownloadFormat] = useState('pdf');
  const [calloverReport, setCallOverReport] = useState([]);

  const toPrint = useRef();
  const handlePrint = useReactToPrint({
    content: () => toPrint.current,
    // onAfterPrint: () => setShowPrint(false)
  }); 

  const location: any = useLocation();
  
  const handleDownload = (value: any, fileName: string) => {
    if(calloverReport) {
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
	}

  useEffect(() => {
    if(location.state) {
      setData(location.state.data)
      setCallOverReport(location.state.customerStatement);
    } else {
      setData(props.customerData)
      setCallOverReport(props.calloverReport)
    }
  }, [])

  // const data = props.customerData;

  return (
    <>
      <div ref={toPrint}>
        <span className="titles">Call Over Report</span>
        <div style={{ marginLeft: '54px' }}>
          <p className="titles2">Summary</p>
          <div className="dateContainer">
            <table>
              <thead></thead>
              <tbody>
                {/* <tr className = 'drow'>
                               <th>Wallet Owner Name: </th>
                               <td>{data.accountOwnerName}</td>
                           </tr> */}
                {/* <tr className = 'drow'>
                               <th>Data Created: </th>
                               <td>{data.dateOpened}</td>
                           </tr> */}
                <tr>
                  <th>Date Range: </th>
                  <td className="filtered-date-box">
                    <span>{!isEmpty(data.start) ? data.start : data.dateOpened}</span>
                    <span>→</span>
                    <span>{!isEmpty(data.end) ? data.end : moment(Date()).format('YYYY-MM-DD')}</span>
                  </td>
                </tr>
                {!isEmpty(data.scheme) && (
                  <tr className="drow">
                    <th>Scheme Name: </th>
                    <td>{data?.scheme}</td>
                  </tr>
                )}
                {/* <tr className = 'drow'>
                               <th>Opening Balance: </th>
                               <td>₦{opening}</td>
                           </tr>
                           <tr className = 'drow'>
                               <th>Closing Balance: </th>
                               <td>₦{closing}</td>
                           </tr> */}
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
                <button onClick={() => handleDownload(calloverReport, 'Customers Report')}>Download</button>
              </div>
            </div>
          </div>
        </div>
        <div style={{ marginLeft: '54px', marginTop: '50px' }}>
          <div>
            <p className="titles2">REPORT DETAILS</p>
          </div>
          <Table striped>
            <thead>
              <th>S/N</th>
              <th>Transaction Date</th>
              <th>Payment Type</th>
              <th>Description</th>
              <th>Referrence Number</th>
              <th>Source Account Name</th>
              {/* <th>Source Account Amount (₦)</th> */}
              <th>Destination Account Name</th>
              <th>Amount (₦)</th>
              <th>Status</th>
            </thead>
            <tbody>
              {(calloverReport && calloverReport.length > 0) && calloverReport.map((v, i) => (
                <tr className="tdata" key={i}>
                  <td>{i + 1}</td>
                  <td>{moment(v.createdDate).format('ll')}</td>
                  <td style={{ color: v.transactionDescription === 'Invalid transaction' ? 'hsl(0, 100%, 50%)' : 'initial' }}>
                    {v.transactionDescription}
                  </td>
                  <td>{v.narration}</td>
                  <td>{v.transRef}</td>
                  <td>{!isEmpty(v.sourceAccountName) ? v.sourceAccountName : '---'}</td>
                  {/* <td>
                                <MoneyFormat
                                    value = {Number(v.amount)}
                                    displayType = 'text'
                                    thousandSeparator = {true}
                                    decimalScale = {2}
                                    fixedDecimalScale = {true}
                                   // prefix = "₦"
                                />
                                </td> */}
                  <td>{!isEmpty(v.beneficiaryName) && v.beneficiaryName !== 'null' ? v.beneficiaryName : '---'}</td>
                  <td>
                    <MoneyFormat
                      value={Number(v.amount)}
                      displayType="text"
                      thousandSeparator={true}
                      decimalScale={2}
                      fixedDecimalScale={true}
                      // prefix = "₦"
                    />
                  </td>
                  <td>
                    <span
                      className="call-status-box"
                      style={{
                        backgroundColor:
                          v.status === 'COMPLETED'
                            ? 'hsl(147, 100%, 33%)'
                            : v.status === 'REVERSED'
                            ? 'hsla(219, 92%, 46%, 0.987)'
                            : 'hsl(0, 100%, 33%)',
                      }}
                    >
                      {v.status}
                    </span>
                  </td>
                </tr>
              ))}
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

export default connect(mapStateToProps, mapDispatchToProps)(CallOverReport);
