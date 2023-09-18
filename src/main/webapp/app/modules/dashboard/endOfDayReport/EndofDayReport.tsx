import React, { Fragment, useEffect, useState } from 'react';
import InnerLayout from '../components/Layout';
import Layout from '../Layout';
import { Table } from 'react-bootstrap';
import moment from 'moment';
import Axios from 'axios';
import { connect } from 'react-redux';
import { IRootState } from 'app/shared/reducers';
import { RouteComponentProps } from 'react-router-dom';

import './endOfDayReport.scss';

const Data = [
  {
    name: 'Cash Out',
    amount: 0,
    count: 0,
  },
  {
    name: 'Funds Transfer',
    amount: 0,
    count: 0,
  },
  {
    name: 'Airtime Purchase',
    amount: 0,
    count: 0,
  },
  {
    name: 'Data Purchase',
    amount: 0,
    count: 0,
  },
  {
    name: 'Cable TV',
    amount: 0,
    count: 0,
  },
  {
    name: 'Electricity',
    amount: 0,
    count: 0,
  },
  {
    name: 'Internet Subscription',
    amount: 0,
    count: 0,
  },
  {
    name: 'Cable TV',
    amount: 0,
    count: 0,
  },
];
const DataCredit = [
  {
    name: 'Cash In',
    amount: 0,
    count: 0,
  },
  {
    name: 'Request Money',
    amount: 0,
    count: 0,
  },
  {
    name: 'Wallet Topup',
    amount: 0,
    count: 0,
  },
];
export interface IUserstateProps extends StateProps, DispatchProps, RouteComponentProps<{}> {}

function EndOfDaYReport(props: any) {
  const [date, setDate] = useState({
    start: moment('1993-03-30').format('YYYY-MM-DD'),
    end: moment(Date()).format('YYYY-MM-DD'),
  });
  const [schemes, setSchemes] = useState([]);

  const getSchemes = async () => {
    // alert("gottem")
    try {
      const theSchemes = await Axios.get(`api/schemes/admin/${props.userLog.login}`);
      // console.error(theSchemes);
      setSchemes(theSchemes.data.data);
      // setScheme(theSchemes.data.data[0])
      // getUsers(0, theSchemes.data.data[0].schemeID);
    } catch (e) {
      console.error(e);
    }
  };

  useEffect(() => {
    getSchemes();
  }, []);

  return (
    <Fragment>
      <Layout>
        <InnerLayout schemes={schemes} title={`End of Day Report`} path={`End of Day Report`}>
          <div>
            <div className="end-day-filter-box">
              <div>
                {' '}
                <label className="mr-4">
                  Start Date:
                  <input
                    className="ml-2"
                    onChange={e => setDate({ ...date, start: moment(e.target.value).format('YYYY-MM-DD') })}
                    type="date"
                  />
                </label>
              </div>

              <div>
                <label className="mr-4">
                  End Date:
                  <input
                    className="ml-2"
                    onChange={e => setDate({ ...date, end: moment(e.target.value).format('YYYY-MM-DD') })}
                    type="date"
                  />
                </label>
              </div>
              {/* <label htmlFor="schemes">Select Scheme :</label>
            <select name="schemes" id="schemes">
            {schemes.map((s, i) => (
                <option key={i}>{s.scheme}</option>
                ))}
            </select> */}
              {/* <br /> */}
              <div>
                <button className="end-day-btn" onClick={() => {}}>
                  Filter
                </button>
              </div>
            </div>
            <Table bordered striped>
              <thead>
                <tr>
                  <th>Debit Transactions</th>
                  <th>Amount</th>
                  <th>Count</th>
                </tr>
              </thead>
              <tbody>
                {Data.map((data, index) => (
                  <tr key={index}>
                    <td>{data.name}</td>
                    <td>{data.amount}</td>
                    <td>{data.count}</td>
                  </tr>
                ))}
                <tr>
                  <th>Grand Total</th>
                  <th>0</th>
                  <th>0</th>
                </tr>
                <br />
              </tbody>
              <thead>
                <tr>
                  <th>Credit Transaction</th>
                  <th>Amount</th>
                  <th>Count</th>
                </tr>
              </thead>
              <tbody>
                {DataCredit.map((data, index) => (
                  <tr key={index}>
                    <td>{data.name}</td>
                    <td>{data.amount}</td>
                    <td>{data.count}</td>
                  </tr>
                ))}
                <tr>
                  <th>Grand Total</th>
                  <th>0</th>
                  <th>0</th>
                </tr>
              </tbody>
            </Table>
          </div>
        </InnerLayout>
      </Layout>
    </Fragment>
  );
}

const mapStateToProps = ({ reports, authentication: { isAuthenticated, account, sessionHasBeenFetched } }: IRootState) => ({
  user: reports.user,
  userLog: account,
});

const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(EndOfDaYReport);
