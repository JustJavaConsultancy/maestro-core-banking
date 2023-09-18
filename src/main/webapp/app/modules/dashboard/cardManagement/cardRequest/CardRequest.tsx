import React, { ChangeEvent, useEffect, useRef, useState } from 'react';
import { useSelector } from 'react-redux';
import Axios from 'axios';
import moment from 'moment';
import Layout from '../../Layout';
import InnerLayout from '../../components/Layout';

import ReactToPrint from 'react-to-print';
import exportFromJSON from 'export-from-json';

import { Card } from '../../components/cardManagement/HeaderCards';
import { Table } from 'react-bootstrap';
import FilterDate from './utils/utils';
import { IRootState } from 'app/shared/reducers';

import './cardRequest.scss';

export function getSchemeName(arr: any[], id: string) {
  const theScheme = arr.filter(scheme => scheme.schemeID === id);
  const schemeName = theScheme.length ? theScheme[0].scheme : '';

  return schemeName;
}

function CardRequest() {
  const [schemes, setSchemes] = useState([]);
  const [cardRequests, setCardRequests] = useState([]);
  const [format, setFormat] = useState('pdf');
  const [filterDate, setFilterDate] = useState({
    from: '',
    to: '',
  });

  const toPrint = useRef(null);

  const { from, to } = filterDate;

  const exportFile = () => {
    const reportStatement = cardRequests;
    enum toexport {
      csv = 'csv',
      xls = 'csv',
      txt = 'txt',
      json = 'json',
    }
    const dataToExport = reportStatement;
    const fileName = 'Card Requests';
    exportFromJSON({ data: dataToExport, fileName, exportType: toexport[format] });
  };

  const adminNumber = useSelector((state: IRootState) => state.authentication.account.login);

  const getSchemes = async () => {
    try {
      const theSchemes = await Axios.get(`/api/schemes/admin/${adminNumber}`);
      setSchemes(theSchemes.data.data);
    } catch (e) {
      console.error(e);
    }
  };

  const cardRequestHandler = async () => {
    try {
      const res = await Axios.get('/api/card-requests');
      // const res = await Axios.get('https://wallet.remita.net/api/card-requests');
      // console.error(res);
      if (res.data.code === '00') {
        setCardRequests(res.data.data);
        return;
      }
    } catch (error) {
      if (error.response) {
        alert(error.response.data.message);
        return;
      }
      alert('Something went wrong!');
      console.error(error);
    }
  };

  const cardRequestHandlerByDate = async () => {
    setCardRequests([]);
    const startDate = moment(from).format('YYYY-MM-DD');
    const endDate = moment(to).format('YYYY-MM-DD');

    try {
      const res = await Axios.get(`/api/card-requests/date/${startDate}/${endDate}`);
      // // eslint-disable-next-line no-console
      // console.log(res);
      if (res.data.code === '00') {
        setCardRequests(res.data.data);
        return;
      }
    } catch (error) {
      if (error.response) {
        alert(error.response.data.message);
        return;
      }
      alert('Something went wrong!');
      console.error(error);
    }
  };

  const handleClick = (e: ChangeEvent<HTMLFormElement>) => {
    e.preventDefault();
  };

  const filterChangeHandler = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    setFilterDate(prev => {
      return { ...prev, [name]: value };
    });
  };

  useEffect(() => {
    getSchemes();
  }, []);

  useEffect(() => {
    if (schemes.length) {
      cardRequestHandler();
    }
  }, [schemes.length]);

  useEffect(() => {
    if (from && to) {
      cardRequestHandlerByDate();
    }
  }, [from, to]);

  return (
    <Layout>
      <InnerLayout dontfilter={true} show={true} title="Card Management" path="Card Requests">
        <form className="msg_header-box" onSubmit={handleClick}>
          <div className="msg_input-box">
            <input type="text" placeholder="Search" />
          </div>

          <div className="request_filter">
            <FilterDate text="from" when="from" value={from} onChange={filterChangeHandler} />
            <FilterDate text="to" when="to" value={to} onChange={filterChangeHandler} />
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
                documentTitle="Card Requests"
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
                  <th>Account Number</th>
                  <th>First Name</th>
                  <th>Last Name</th>
                  {/* <th>Department</th> */}
                  <th>Scheme Type</th>
                  <th>Date Requested</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {!cardRequests.length && !schemes.length ? (
                  <tr>
                    <td colSpan={9}>
                      <div className="no_data-box">
                        <h2>No Data</h2>
                      </div>
                    </td>
                  </tr>
                ) : (
                  cardRequests.map((request, i) => (
                    <tr key={request.customerid}>
                      <td>{i + 1}</td>
                      <td>{request.walletId}</td>
                      <td>{request.cardNuban}</td>
                      <td>{request.owner?.user?.firstName}</td>
                      <td>{request.owner?.user?.lastName}</td>
                      {/* <td>{request.department ? request.department : ''}</td> */}
                      {/* <td>{request.scheme}</td> */}
                      <td>
                        {getSchemeName(schemes, request.scheme) === 'Systemspecs wallet'
                          ? 'Pouchii'
                          : getSchemeName(schemes, request.scheme)}
                      </td>
                      {/* <td>{request.createdDate ? request.createdDate : ''}</td> */}
                      <td>{moment(request.createdDate).format('ll')}</td>
                      <td>
                        <div className="request_status-box">
                          <span className="request_status">{request.status}</span>
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

export default CardRequest;
