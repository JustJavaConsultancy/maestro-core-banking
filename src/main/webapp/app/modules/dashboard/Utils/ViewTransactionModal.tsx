import React, { useEffect, useState, useRef } from 'react';
import Axios from 'axios';
import moment from 'moment';
import { Table } from 'reactstrap';
import MoneyFormat from 'react-number-format';
import { useReactToPrint } from 'react-to-print';

import logo from '../../../../content/images/pouchii.svg';

export default function (props) {
  const [state, setState] = useState({
    journal: {
      transDate: '',
      memo: '',
      amount: 0,
      reference: '',
      displayMemo: '',
    },
    journalLines: [],
  });
  const componentRef = useRef(null);

  const handlePrint = useReactToPrint({
    content: () => componentRef.current,
  });

  const getTransaction = async () => {
    try {
      const journal = await Axios.get(`/api/daybook/${props.reference}`);
      console.error(journal);
      setState(journal.data);
    } catch (e) {
      console.error(e);
    }
  };

  const getName = () => {
    const name = state.journal?.memo.split(' ');
    return name.slice(4, 6).join(' ');
  }

  useEffect(() => {
    getTransaction();
  }, []);

  let totalCredit = 0;
  let totalDebit = 0;
  return (
    <>
      <div>
        <div className="modal-trans-header">
          <div className="modal-trans-date">
            <p>{moment(state.journal.transDate).format('LLL')}</p>
            {state.journal.memo && <button onClick={handlePrint}>Print Receipt</button>}
          </div>
          <p className="pb-0">{state.journal.memo}</p>
        </div>

        <Table>
          <thead>
            <th>Account Name</th>
            <th>Credit</th>
            <th>Debit</th>
          </thead>
          <tbody>
            {state.journalLines.map((jl, k) => {
              totalCredit += jl.credit;
              totalDebit += jl.debit;
              return (
                <tr key={k}>
                  <td style={{ width: '50%' }}>
                    {jl.accountName} <small className="modal-trans-acct">({jl.accountNumber})</small>
                  </td>
                  <td>
                    <MoneyFormat value={jl.credit} displayType="text" thousandSeparator={true} prefix="₦" />
                  </td>
                  <td>
                    <MoneyFormat value={jl.debit} displayType="text" thousandSeparator={true} prefix="₦" />
                  </td>
                </tr>
              );
            })}
            <tr>
              <th>Total</th>
              <th>
                <MoneyFormat value={totalCredit} displayType="text" thousandSeparator={true} prefix="₦" />
              </th>
              <th>
                <MoneyFormat value={totalDebit} displayType="text" thousandSeparator={true} prefix="₦" />
              </th>
            </tr>
          </tbody>
        </Table>
      </div>
      <div ref={componentRef} className="print-source transaction-receipt">
            <div>
              <img src ={logo} alt="Site logo" />
              <h3>Transaction Receipt</h3>
              <h4>{moment(state.journal.transDate).format('LLL')}</h4>
            </div>
            <hr />
            <div>
              <div>
                <p>Payer Name</p>
                <p className="second-para">{getName()}</p>
              </div>
              <div>
                <div>
                  <p>Payment date</p>
                  <p className="second-para">{moment(state.journal.transDate).format('LLL')}</p>
                </div>
                <div>
                  <p>Reference Number</p>
                  <p className="second-para">{state.journal.reference}</p>
                </div>
              </div>
            </div>
            <hr />
            <div>
              <p>Transaction details</p>
              <p className="second-para">{state.journal.displayMemo}</p>
            </div>
            <hr />
            <div>
              <p>Total Amount</p>
              <p>
                <MoneyFormat 
                  value={state.journal.amount} 
                  displayType="text" 
                  thousandSeparator={true} 
                  prefix="₦" 
                />
              </p>
            </div>
            <span className="half-circle"></span>
            <span className="half-circle"></span>
      </div>
    </>
  );
}
