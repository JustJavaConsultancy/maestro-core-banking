import React, { useEffect, useState, useRef } from 'react';
import Layout from '../../Layout';
import InnerLayout from '../../components/Layout';
import axios from 'axios';
import { useHistory, useLocation } from 'react-router-dom';
import MoneyFormat from 'react-number-format';
import moment from 'moment';
import { Table } from 'react-bootstrap';

interface LocationType {
  scheme: string;
}

export default function (props) {
  const location = useLocation<LocationType>();

  const [wallets, setWallets] = useState([]);
  const [total, setTotal] = useState(0);
  const [schemes, setSchemes] = useState([]);
  const [scheme, setScheme] = useState({ schemeID: '' });
  const toPrint = useRef();

  const getWallets = async (schemeId: string): Promise<void> => {
    try {
      const theWallets = await axios.get(`/api/wallet-accounts/customers/${schemeId}`);
      setTotal(theWallets.data.metadata.totalNumberOfRecords);
      //   setPage(paget);
      setWallets(theWallets.data.data);
      console.error(theWallets);
    } catch (e: any) {
      console.error(e);
    }
  };

  //   const getSchemes = async () => {
  //     try {
  //       const theSchemes = await axios.get(`api/schemes/admin/${props.userLog.login}`);
  //       console.error(theSchemes);
  //       setSchemes(theSchemes.data.data);
  //       setScheme(theSchemes.data.data[0]);
  //       getWallets(theSchemes.data.data[0].schemeID);
  //     } catch (e) {
  //       console.error(e);
  //     }
  //   };

  const handleschemeChange = s => {
    console.error(s.schemeID);

    setScheme(s);
    getWallets(s.schemeID);
  };

  useEffect(() => {
    getWallets(location.state.scheme ? location.state.scheme : '53797374656d73706563732077616c6c6574');
    // getSchemes();
    // getWallets(0);
  }, []);

  return (
    <Layout>
      <InnerLayout
        // schemes={schemes}
        // schemeChange={s => {
        //   handleschemeChange(JSON.parse(s));
        // }}
        toPrint={toPrint}
        toExport={wallets}
        show={true}
        title="Download Wallets"
        path="dowmload-wallets"
      >
        <div ref={toPrint}>
          <Table hover responsive className="transaction-table">
            <thead className="tableListData">
              <tr>
                <th>S/N</th>
                <th>Wallet Number</th>
                <th>Wallet Name</th>
                <th>Customer Name</th>
                <th>Customer PhoneNumber</th>
                <th>Book Balance (₦)</th>
                <th>Available Balance (₦)</th>
                <th>Date Created</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {wallets.length === 0 ? (
                <tr>
                  <td></td>
                </tr>
              ) : (
                wallets.map((wallet, i) => (
                  <tr key={i}>
                    <td>{i + 1}</td>
                    <td>{wallet.accountNumber}</td>
                    <td>{wallet.accountName}</td>
                    <td>{wallet.accountOwnerName}</td>
                    <td>{wallet.accountOwnerPhoneNumber}</td>
                    <td>
                      <MoneyFormat
                        value={wallet.currentBalance}
                        displayType="text"
                        thousandSeparator={true}
                        decimalScale={2}
                        fixedDecimalScale={true}
                        // prefix = "₦"
                      />
                    </td>
                    <td>
                      <MoneyFormat
                        value={wallet.actualBalance}
                        displayType="text"
                        thousandSeparator={true}
                        decimalScale={2}
                        fixedDecimalScale={true}
                        // prefix = "₦"
                      />
                    </td>
                    <td>{moment(wallet.dateOpened).format('ll')}</td>
                    <td>
                      <div
                        style={{
                          padding: '5px 10px',
                          borderRadius: '15px',
                          fontSize: '12px',
                          color: '#00A07E',
                          backgroundColor: 'rgba(0, 196, 140, 0.25)',
                        }}
                      >
                        {wallet.status}
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </Table>
        </div>
      </InnerLayout>
    </Layout>
  );
}
