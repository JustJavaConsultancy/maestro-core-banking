import { size } from 'lodash';
import React from 'react';
import Bar from './Bars';
import ThreeDot from './ThreeDots';
import MoneyFormat from 'react-number-format';
//  totalDeposits: 0,
//  totalWithdrawals: 0,
export default function (props) {
  return (
    <div style={{ width: '100%' }}>
      <div style={{ marginRight: '15px' }} className="cardTitle">
        <div style={{ flex: 0.7, display: 'flex' }}>
          <p style={{ marginTop: '5px', fontWeight: 'bold' }}>Transactions</p>
        </div>
        <div style={{ flex: 0.3, display: 'flex', justifyContent: 'flex-end' }}>
          <ThreeDot />
        </div>
      </div>
      <div className="brow1">
        <div className="cashmessage">
          <div style={{ display: 'flex' }}>
            <p style={{ paddingRight: '5px' }}>Inflow: </p>
            <p style={{ color: 'green' }} className="sum">
              {`+  `}
              <MoneyFormat
                value={props.meta.totalDeposits}
                displayType="text"
                thousandSeparator={true}
                decimalScale={2}
                fixedDecimalScale={true}
                prefix="₦"
              />
            </p>
            <p style={{ marginLeft: '20px', paddingRight: '5px' }}>outflow Sum: </p>
            <p style={{ color: 'red' }} className="sum">
              -{' '}
              <MoneyFormat
                value={props.meta.totalWithdrawals}
                displayType="text"
                decimalScale={2}
                fixedDecimalScale={true}
                thousandSeparator={true}
                prefix="₦"
              />
            </p>
          </div>
          <div>
            <p style={{ color: '#00C49E' }} className="sum">
              <MoneyFormat
                value={Number(props.meta.totalDeposits) - Number(props.meta.totalWithdrawals)}
                displayType="text"
                decimalScale={2}
                fixedDecimalScale={true}
                thousandSeparator={true}
                prefix="₦"
              />{' '}
              in Correspondent Account
            </p>
          </div>
        </div>
        <div className="chart">
          <Bar height="100px" inflow="20%" outflow="80%" x="Jan" />
          <Bar height="120px" inflow="20%" outflow="80%" x="Feb" />
          <Bar height="80px" inflow="50%" outflow="50%" x="Mar" />
          <Bar height="70px" inflow="40%" outflow="60%" x="Apr" />
          <Bar height="50px" inflow="60%" outflow="40%" x="May" />
          <Bar height="40px" inflow="50%" outflow="50%" x="Jun" />
          <Bar height="120px" inflow="40%" outflow="60%" x="Jul" />
          <Bar height="150px" inflow="30%" outflow="70%" x="Aug" />
          <Bar height="50px" inflow="60%" outflow="40%" x="Sep" />
          <Bar height="90px" inflow="40%" outflow="60%" x="Oct" />
          <Bar height="98px" inflow="60%" outflow="40%" x="Nov" />
          <Bar height="100px" inflow="40%" outflow="60%" x="Dec" />
        </div>
      </div>
    </div>
  );
}
