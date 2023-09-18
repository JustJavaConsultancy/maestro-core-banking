import React, { ChangeEvent, useState, useRef, useEffect } from 'react';
import { useHistory } from 'react-router';
import { useSelector } from 'react-redux';
import Layout from '../dashboard/Layout';
import InnerLayout from '../dashboard/components/Layout';
import axios from 'axios';
import moment from 'moment';

import './reportCashConnect.scss';

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

interface PHoneType {
  authentication: { account: { login: string } };
}

export interface SchemeType {
  accountNumber: string;
  bankCode: string;
  id: string;
  scheme: string;
  schemeID: string;
}

export default function () {
  const phoneNumber = useSelector((state: PHoneType) => state.authentication.account.login);
  const history = useHistory();

  const tableRef = useRef<HTMLDivElement>(null);
  const [data, setData] = useState({
    nuban: '',
    startDate: '',
    endDate: '',
    report: '',
  });
  const [err, setErr] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [polarisData, setPolarisData] = useState<PolarisDataType>();
  const [showBalance, setShowBalance] = useState(false);
  const [showFilter, setShowFilter] = useState(false);
  const [schemes, setSchemes] = useState<SchemeType[]>([]);
  const [scheme, setScheme] = useState<SchemeType>({
    accountNumber: '',
    bankCode: '',
    id: '',
    scheme: '',
    schemeID: '',
  });

  console.error(scheme);

  const { endDate, nuban, startDate, report } = data;

  const handleChange = (e: ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setErr('');
    const { name, value } = e.target;

    if (name === 'report' && value === 'polaris') {
      setData({ ...data, [name]: value, nuban: '1790307208' });
      return;
    }

    if (name === 'scheme') {
      const newValue = JSON.parse(value);
      setScheme(newValue);
      return;
    }
    setData({ ...data, [name]: value });
  };

  const handleSubmit = async (e: ChangeEvent<HTMLFormElement>) => {
    const fromDate = startDate ? moment().format(startDate) : moment().format('YYYY-MM-DD');
    const toDate = endDate ? moment().format(endDate) : moment().format('YYYY-MM-DD');

    e.preventDefault();
    setIsLoading(true);

    if (report === 'polaris') {
      const num = moment(toDate).diff(moment(fromDate), 'days');
      if (num > 30) {
        setErr('Filtered date must not be greater than 30 days.');
        setIsLoading(false);
        return;
      }
    }

    // `https://wallet.remita.net/api/vulte/statement/1790307208/${fromDate}/${toDate}`

    const url =
      report === 'polaris'
        ? `/api/vulte/statement/${scheme.accountNumber}/${fromDate}/${toDate}/${scheme.schemeID}`
        : // `https://wallet.remita.net/api/vulte/statement/1790307208/${fromDate}/${toDate}`
          `/api/cash-connect/account/statement?accountNumber=${nuban}&fromDate=${fromDate}&toDate=${toDate}`;
    // `https://wallet.remita.net/api/cash-connect/account/statement?accountNumber=${nuban}&fromDate=${fromDate}&toDate=${toDate}`;

    try {
      // const response = await axios.get(`/api/cash-connect/account/statement?accountNumber=${nuban}&fromDate=${fromDate}&toDate=${toDate}`);
      const response = await axios.get(url);

      if (response.data.code === '00') {
        const jsonResponse = report === 'polaris' ? response.data.data : JSON.parse(response.data.data);
        history.push({
          pathname: 'cashconnect-reports-details',
          state: { jsonResponse, fromDate, toDate, report, polarisData, isPolaris: report === 'polaris' ? true : false, nuban, scheme },
        });
      }
    } catch (error) { 
      setErr('Invalid nuban wallet!');
      setIsLoading(false);
    }
  };

  const totalBalance = async () => {
    try {
      // const response = await axios.get('/api/vulte/1790307208');
      // const response = await axios.get('https://wallet.remita.net/api/vulte/1790307208');
      const response = await axios.get(`/api/vulte/${scheme.accountNumber}/${scheme.schemeID}`);

      if (response.data.code === '00') {
        setPolarisData(response.data.data);
        setShowBalance(true);
        setShowFilter(true);
      }
    } catch (error) {
      console.error(error);
      alert('Could not load report balance. Please try again.');
      setData({ ...data, nuban: '' });
    }
  };

  const getSchemes = async () => {
    try {
      const theSchemes = await axios.get(`/api/schemes/admin/${phoneNumber}`);
      console.error(theSchemes);
      setSchemes(theSchemes.data.data);
      // setScheme(theSchemes.data.data[0]);
      // getUsers(0, theSchemes.data.data[0].schemeID);
    } catch (e: any) {
      if (e.response) {
        alert(e.response.data.message);
        return;
      }
      alert('Network Error!');
    }
  };

  useEffect(() => {
    getSchemes();
  }, []);

  useEffect(() => {
    if (report === 'cashConnect' && nuban.length >= 10) {
      setShowFilter(true);
      return;
    }
    setShowFilter(false);
  }, [report, nuban]);

  useEffect(() => {
    if (report === 'polaris' && scheme.schemeID) {
      totalBalance();
    }
  }, [report, scheme.schemeID]);

  return (
    <Layout>
      <InnerLayout dontfilter={true} show={true} title="External Reports" path="External-reports">
        <>
          <div className="connect-box">
            <div className="connect-box-header">
              <h5>Please fill the form below to view reports</h5>
            </div>

            <form className="connect-form" onSubmit={handleSubmit}>
              <div className="connect-wallet">
                <select name="report" className="connect-input" onChange={handleChange}>
                  <option value="">Select Report</option>
                  <option value="cashConnect">CashConnect Reports</option>
                  <option value="polaris">Polaris Reports</option>
                </select>
              </div>

              {report === 'polaris' && (
                <div className="connect-wallet">
                  <select name="scheme" className="connect-input" onChange={handleChange}>
                    <option value="">Select Scheme</option>

                    {schemes.map(userScheme => (
                      <option key={userScheme.id} value={JSON.stringify(userScheme)}>
                        {userScheme.scheme}
                      </option>
                    ))}
                  </select>
                </div>
              )}

              {report === 'cashConnect' && (
                <div className="connect-wallet">
                  <label className="connect-label" htmlFor="wallet">
                    Nuban wallet:
                  </label>
                  <input
                    value={nuban}
                    className="connect-input"
                    name="nuban"
                    id="wallet"
                    type="number"
                    placeholder="nuban account"
                    // disabled={report === 'polaris' ? true : false}
                    onChange={handleChange}
                  />
                </div>
              )}

              {showFilter && (
                <div className="connect-filter-box">
                  <h6>Filter report date</h6>

                  <div className="connect-filter-date">
                    <div className="date-box">
                      <label className="connect-label" htmlFor="from">
                        From:
                      </label>

                      <input value={startDate} className="connect-input" name="startDate" id="from" type="date" onChange={handleChange} />
                    </div>

                    <div>
                      <label className="connect-label" htmlFor="to">
                        To:
                      </label>
                      <input value={endDate} className="connect-input" name="endDate" id="to" type="date" onChange={handleChange} />
                    </div>
                  </div>
                </div>
              )}

              {!showFilter && nuban.length >= 10 && scheme.schemeID && (
                <div className="load-box">
                  <span className="load-content">Filter Report...</span>
                </div>
              )}

              {err && (
                <div className="connect-err">
                  <p>{err}</p>
                </div>
              )}

              {showFilter && (
                <div className="connect-btn-box">
                  <button>{!isLoading ? 'View Report' : <span className="connect-spin"></span>}</button>
                </div>
              )}
            </form>
          </div>
        </>
      </InnerLayout>
    </Layout>
  );
}
