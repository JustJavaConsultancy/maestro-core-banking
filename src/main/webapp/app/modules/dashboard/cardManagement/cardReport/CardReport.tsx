import React, { ChangeEvent, useEffect, useRef, useState } from 'react';
import Layout from '../../Layout';
import InnerLayout from '../../components/Layout';
import ReactToPrint from 'react-to-print';
import exportFromJSON from 'export-from-json';
import { useLocation } from 'react-router-dom';
import Axios from 'axios';
import { Card } from '../../components/cardManagement/HeaderCards';
import { Table } from 'react-bootstrap';

function formatDate(date: string) {
  const newDate = date.split('/');
  const formatedDate = newDate[0] + '/' + newDate[1].substring(2);
  return formatedDate;
}

type LocationType = {
  scheme: string;
};

function CardReport() {
  const {
    state: { scheme },
  } = useLocation<LocationType>();

  const [cardReports, setCardReports] = useState([]);
  const [format, setFormat] = useState('pdf');

  const toPrint = useRef(null);

  const exportFile = () => {
    const reportStatement = cardReports;
    enum toexport {
      csv = 'csv',
      xls = 'csv',
      txt = 'txt',
      json = 'json',
    }
    const dataToExport = reportStatement;
    const fileName = 'Card Reports';
    exportFromJSON({ data: dataToExport, fileName, exportType: toexport[format] });
  };

  const cardRequestHandler = async () => {
    try {
      const res = await Axios.get(`/api/card-management/report/${scheme}`);
      // const res = await Axios.get(`https://walletdemo.remita.net/api/card-management/report/${scheme}`);
      console.error(res);
      if (res.data.code === '00') {
        setCardReports(res.data.data);
        return;
      }
    } catch (error) {
      if (error.response) {
        alert(error.response.data.message);
        return;
      }
      alert('Network Error!');
      console.error(error);
    }
  };

  const handleClick = (e: ChangeEvent<HTMLFormElement>) => {
    e.preventDefault();
  };

  useEffect(() => {
    cardRequestHandler();
  }, []);

  return (
    <Layout>
      <InnerLayout dontfilter={true} show={true} title="Card Management" path="Card Report">
        <form className="msg_header-box" onSubmit={handleClick}>
          <div className="msg_input-box">
            <input type="text" placeholder="Search" />
          </div>

          <div className="request_filter">
            <select className="request_select" name="" id="" onChange={e => setFormat(e.target.value)}>
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
                    <div className="request_btn-box">
                      <button className="request_btn">Download</button>
                    </div>
                  );
                }}
                content={() => toPrint.current}
                copyStyles
                documentTitle="Card Reports"
              />
            ) : (
              <div className="request_btn-box">
                <button className="request_btn" onClick={exportFile}>
                  Download
                </button>
              </div>
            )}
          </div>
        </form>

        <div ref={toPrint}>
          <Card customStyle="pad_style">
            <Table responsive hover className="transaction-table">
              <thead className="">
                <tr>
                  <th>S/N</th>
                  <th>Wallet ID</th>
                  <th>Name</th>
                  <th>Nuban</th>
                  <th>Balance</th>
                  <th>Pan Number</th>
                  <th>Expiry Date</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {!cardReports.length ? (
                  <tr>
                    <td colSpan={8}>
                      <div className="no_data-box">
                        <h2>No Data</h2>
                      </div>
                    </td>
                  </tr>
                ) : (
                  cardReports.map((report, i) => (
                    <tr key={report.id}>
                      <td>{i + 1}</td>
                      <td>{report.id}</td>
                      <td>{report?.owner?.fullName}</td>
                      <td>{report.accountNumber}</td>
                      <td>{''}</td>
                      <td>{report.bin && report.pan ? `${report.bin}******${report.last4}` : ''}</td>
                      {/* <td>{formatDate(report.expiryDate)}</td> */}
                      <td>{report.expiryDate}</td>
                      <td>
                        <div className="request_status-box">
                          <span className="request_status">{report?.status}</span>
                        </div>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </Table>
          </Card>
        </div>
      </InnerLayout>
    </Layout>
  );
}

export default CardReport;
