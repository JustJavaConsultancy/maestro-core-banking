import './scheme.scss';
import React, { useState, useEffect, useRef, useCallback } from 'react';
import InnerLayout from '../components/Layout';
import Layout from '../Layout';
import Axios from 'axios';
import { isEmpty } from 'lodash';
import { Table } from 'reactstrap';
import ReactToPrint from 'react-to-print';
import exportFromJSON from 'export-from-json';

export interface BanksInterface {
  bankAccronym: string;
  bankCode: string;
  bankName: string;
  type: string;
}
export default function Schemes(props) {
  const [data, setData] = useState({
    scheme: '',
    accountNumber: '',
    bankCode: '044',
  });

  const [accountName, setAccountName] = useState('');
  const [banks, setBanks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [schemes, setSchemes] = useState([]);
  const [schemeLength, setSchemeLength] = useState(schemes.length);
  const [format, setFormat] = useState('pdf');

  const toPrint = useRef(null);

  const getListOfBanks = async () => {
    setLoading(true);
    try {
      const getBanks = await Axios.get(`api/rits-banks`);
      setLoading(false);
      if (getBanks.data.data) {
        setBanks(getBanks.data.data.banks);
      }
    } catch (error) {
      setLoading(false);
      console.error(error);
    }
  };

  const verifyAccount = async account => {
    setLoading(true);
    try {
      const details = {
        accountNumber: account,
        accountType: 'bank',
        bankCode: data.bankCode,
      };
      const accDetails = await Axios.post(`api/verify-account`, details);
      console.error(accDetails);
      setAccountName(accDetails.data.accountName);
      setLoading(false);
    } catch (e) {
      setLoading(false);
      alert('An error occured!');
      console.error(e);
    }
  };
  const clearData = () => {
    setData({ accountNumber: '', bankCode: '044', scheme: '' });
    setAccountName('');
  };

  const getSchemes = async () => {
    try {
      const theSchemes = await Axios.get('api/schemes');
      setSchemes(theSchemes.data);
      setSchemeLength(theSchemes.data.length);
    } catch (e) {
      console.error(e);
    }
  };

  const CreateScheme = async event => {
    event.preventDefault();
    try {
      setLoading(true);
      if (isEmpty(accountName) || accountName === 'Account Not Found') {
        data.accountNumber = '';
        data.bankCode = '';
      }
      const newScheme = await Axios.post('api/schemes', data);
      getSchemes();
      console.error(newScheme);
      clearData();
      alert(' Scheme created successfully!');
      setLoading(false);
    } catch (e) {
      console.error(e);
      setLoading(false);
    }
  };

  const formattedScheme = useCallback(
    () =>
      schemes.map(scheme => ({
        ['Scheme Name']: scheme.scheme,
        ['Scheme ID']: scheme.schemeID,
        ['Pool Account Number']: scheme.accountNumber,
      })),
    [schemeLength]
  );

  const exportFile = () => {
    const reportStatement = formattedScheme();
    enum toexport {
      csv = 'csv',
      xls = 'csv',
      txt = 'txt',
      json = 'json',
    }
    const dataToExport = reportStatement;
    const fileName = 'Schemes';
    exportFromJSON({ data: dataToExport, fileName, exportType: toexport[format] });
  };

  useEffect(() => {
    getListOfBanks();
    getSchemes();
  }, []);

  return (
    <>
      <Layout>
        <InnerLayout dontfilter={true} show={true} title="Scheme Management" path="Scheme management">
          <form onSubmit={CreateScheme} style={{}} className="sendMoneyConnector">
            <input
              required={true}
              onChange={e => {
                setData({ ...data, scheme: e.target.value });
              }}
              value={data.scheme}
              type="text"
              className="textinput marg"
              placeholder="Enter the scheme name"
            />
            <br />
            {/* !isEmpty(saName) && <p style= {{backgroundColor:'#fff',  marginRight:'40px', fontSize: '12px', marginTop: '14px', padding: '10px', }}>{saName}</p> */}

            <select
              onChange={e => setData({ ...data, bankCode: e.target.value, accountNumber: '' })}
              value={data.bankCode}
              placeholder="select Bank"
              className="textinput marg"
            >
              {banks.map((b, i) => (
                <option key={i} value={b.bankCode}>
                  {b.bankName}
                </option>
              ))}
            </select>
            <input
              onChange={e => {
                setData({ ...data, accountNumber: e.target.value });
                setAccountName('');
                if (e.target.value.length > 9) {
                  verifyAccount(e.target.value);
                }
              }}
              value={data.accountNumber}
              type="number"
              className="textinput marg"
              placeholder="Enter the Destination account number"
            />
            {!isEmpty(accountName) && (
              <p style={{ backgroundColor: '#fff', marginRight: '40px', fontSize: '12px', marginTop: '14px', padding: '10px' }}>
                {accountName}
              </p>
            )}
            <div style={{ display: 'flex', justifyContent: 'center', width: '70%' }}>
              <br />
              <button
                disabled={loading}
                style={{ backgroundColor: '#f2f5fb6d', color: '#000', fontWeight: 'bold' }}
                type="submit"
                className="viewreport"
              >
                {!loading ? 'Create Scheme' : 'loading...'}
              </button>
            </div>
          </form>

          <hr className="scheme_hr" />

          <div>
            <div className="scheme_display">
              <h6>Schemes</h6>
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
          </div>

          <div ref={toPrint}>
            <div>{/* <h5>{new Date().getTime()}</h5> */}</div>
            <Table className="tableListData" striped>
              <thead>
                <th>S/N</th>
                <th>Scheme Name</th>
                <th>Scheme ID</th>
                <th>Pool Account Number</th>
                {/* <th>Bank Code</th> */}
              </thead>
              <tbody>
                {schemes.map((s, i) => (
                  <tr key={i}>
                    <td>{i + 1}</td>
                    <td>{s.scheme}</td>
                    <td>{s.schemeID}</td>
                    <td>{s.accountNumber}</td>
                    {/* <td>{s.bankCode}</td> */}
                  </tr>
                ))}
              </tbody>
            </Table>
          </div>
        </InnerLayout>
      </Layout>
    </>
  );
}
