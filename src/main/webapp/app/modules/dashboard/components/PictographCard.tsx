import React from 'react';
import ThreeDot from './ThreeDots';
import MoneyFormat from 'react-number-format';

export default function (props) {
  return (
    <div style={{ width: '100%' }}>
      <div className="cardTitle">
        <div style={{ flex: 0.7, display: 'flex' }}>
          <p style={{ marginTop: '5px', fontWeight: 'bold' }}>Deposits & withdrawals</p>
        </div>
        <div style={{ flex: 0.3, display: 'flex', justifyContent: 'flex-end' }}>
          <ThreeDot />
        </div>
      </div>
      <div className="brow2">
        <div style={{ position: 'relative', display: 'flex', width: '250px', height: '280px' }}>
          <div className="circle-big">
            <p style={{ fontSize: '18px', fontWeight: 'bold', color: '#fff' }}>
              <MoneyFormat
                value={props.meta.totalDeposits}
                displayType="text"
                decimalScale={2}
                fixedDecimalScale={true}
                thousandSeparator={true}
                prefix="₦"
              />
            </p>
          </div>
          <div className="circle-small">
            <p style={{ fontSize: '14px', color: '#fff', fontWeight: 'bold' }}>
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
          <table className="table1">
            <tbody>
              <tr>
                <td>
                  <td>
                    <div style={{ backgroundColor: '#00C49E' }} className="dot"></div>
                  </td>
                  <td>
                    {' '}
                    <span style={{ fontSize: '11px', marginLeft: '5px' }}>Total Deposits</span>
                  </td>
                </td>
                <td>
                  <td>
                    <div style={{ marginLeft: '20px', backgroundColor: '#EE4E4E' }} className="dot"></div>
                  </td>
                  <td>
                    {' '}
                    <span style={{ fontSize: '11px', marginLeft: '5px' }}>Total withdrawals</span>
                  </td>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
