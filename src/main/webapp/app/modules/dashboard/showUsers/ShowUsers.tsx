import React, { useEffect, useRef, useState } from 'react';
import Layout from '../Layout';
import InnerLayout from '../components/Layout';
import MoneyFormat from 'react-number-format';
import axios from 'axios';

import { useSelector } from 'react-redux';
import { IRootState } from 'app/shared/reducers';

import { data as myData } from './data';

import './showUsers.scss';
import { Modal } from 'reactstrap';
import { Table } from 'react-bootstrap';
import { ActionSvg } from '../transactions/Transactions';

interface UserInfoType {
  accountFullName: string;
}

export default function () {
  const [usersData, setUsersData] = useState([]);
  const [userInfo, setUserInfo] = useState({ accountFullName: '', nubanAccountNo: '', schemeId: 0, accountOwnerPhoneNumber: '' });
  const [total, setTotal] = useState(0);
  const [size, setSize] = useState(1000);
  const [page, setPage] = useState(0);
  const toPrint = useRef();
  const [search, setSearch] = useState('');
  const [nuban, setNuban] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [schemes, setSchemes] = useState([]);
  const [userSchemeID, setUserSchemeID] = useState('');
  // const [scheme, setScheme] = useState({ id: '' });
  const [scheme, setScheme] = useState({ schemeID: '', id: 0, scheme: '' });

  const { schemeID, id } = scheme;

  const userState = useSelector((state: IRootState) => state);
  // // eslint-disable-next-line no-console
  // console.log(userState.authentication.account.login);

  const getUsers = async () => {
    try {
      const users = await axios.get('/api/wallet-accounts/nuban');
      // eslint-disable-next-line no-console
      console.log(users.data?.data);

      const data = users.data?.data.filter(userData => userData.scheme.schemeID === schemeID);

      if (search && data.length > 0) {
        const filteredData = data.filter(user => user.accountFullName.toLowerCase().includes(search.toLowerCase()));
        setUsersData(filteredData);
      } else {
        setUsersData(data);
      }
    } catch (error) {
      console.error(error.response);
    }
  };

  // const getAllUsers = async (schemeId: string, pageTo: number) => {
  const getAllUsers = async () => {
    setUsersData([]);

    try {
      // https://walletdemo.remita.net/api/wallet-accounts/customers/primary/53797374656d73706563732077616c6c6574/search?key=njoku&size=900
      // const users = await axios.get(`/api/wallet-accounts/customers/primary/${schemeId}/search?key=${search}&page=${pageTo}&size=${size}`);
      // const users = await axios.get(`/api/wallet-accounts/nuban/balances`);
      const users = await axios.get(`/api/wallet-accounts/nuban/balances`);
      // // eslint-disable-next-line no-console
      // console.log(users);

      if (users.data?.code === '00') {
        // setTotal(users.data?.metadata.totalNumberOfRecords);
        setPage(0);
        setUsersData(users.data?.data);
      } else {
        setUsersData([]);
      }

      // const data = users.data?.data.filter(userData => userData.schemeId === id);
      // // const data = users.data?.data.filter(userData => userData.schemeId === id);

      // // eslint-disable-next-line no-console
      // console.log(data);

      // if (search && data.length > 0) {
      //   const filteredData = data.filter(user => user.accountFullName.toLowerCase().includes(search.toLowerCase()));
      //   setUsersData(filteredData);

      //   // eslint-disable-next-line no-console
      //   console.log(filteredData);
      // } else {
      //   setUsersData(data);
      // }
    } catch (error) {
      console.error(error.response);
      setUsersData([]);
    }
  };

  // const getSchemes = async () => {
  //   try {
  //     const theSchemes = await axios.get(`/api/schemes`);
  //     // eslint-disable-next-line no-console
  //     console.log(theSchemes);

  //     setSchemes(theSchemes.data);
  //     setScheme(theSchemes.data[0].schemeID);
  //     // const data = theSchemes.data;
  //     // const firstData = data.shift();

  //     // setSchemes([...data, firstData]);
  //     // setScheme([...data, firstData][0]);
  //     // getWallets(0, theSchemes.data.data[0].schemeID);
  //   } catch (e) {
  //     console.error(e);
  //   }
  // };

  const getSchemes = async () => {
    try {
      const theSchemes = await axios.get(`/api/schemes/admin/${userState.authentication.account.login}`);
      // console.error(theSchemes);
      // setSchemes(myData);
      // setScheme(myData[0]);
      setSchemes(theSchemes.data.data);
      setScheme(theSchemes.data.data[0]);
      // getAllUsers(theSchemes.data.data[0].schemeID, 0);
      // getWallets(0, theSchemes.data.data[0].schemeID);
    } catch (e) {
      console.error(e);
    }
  };

  const searchUsers = async (schemeId: string, s: string) => {
    try {
      const theUsers = await axios.get(`/api/wallet-accounts/customers/primary/${schemeId}/search?key=${s}&page=${0}&size=${size}`);
      // const theUsers = await axios.get(
      //   `https://wallet.remita.net/api/api/wallet-accounts/customers/primary/${schemeId}/search?key=${s}&page=${0}&size=${size}`
      // );
      // console.error(theUsers);
      setTotal(theUsers.data.metadata.totalNumberOfRecords);
      setPage(0);
      setUsersData(theUsers.data.data);
      // for(let x = 0; x <=)
    } catch (e) {
      console.error(e);
    }
  };

  const handleschemeChange = s => {
    // console.error(s.schemeID);

    // // eslint-disable-next-line no-console
    // console.log(scheme);

    setScheme(s);
    // getAllUsers(s.schemeID, 0);
    // getWallets(0, s.schemeID);
  };

  const getNubanAcct = async (phone: string) => {
    try {
      const data = await axios.post(`/api/nuban/retrieve/${phone}`);
      // eslint-disable-next-line no-console
      console.log(data);
      if (data?.data?.code === '00') {
        // getAllUsers(schemeID, 0);
        getAllUsers();
      }
    } catch (e) {
      console.error(e);
    }
  };

  const changeNuban = async (phone: string, userScheme: string) => {
    try {
      const response = await axios.post(`/api/polaris/nuban/retrieve/${phone}/${userScheme}`);
      // eslint-disable-next-line no-console
      console.log(response);
      if (response.data.code === '00') {
        alert('Nuban Account changed successfully!!');
        setShowModal(false);
        getAllUsers();
      }
    } catch (error: any) {
      if (error.response) {
        alert(error.response.data.message);
        return;
      }
      alert('Network Error!');
    }
  };

  useEffect(() => {
    getSchemes();
  }, []);

  useEffect(() => {
    if (schemeID) {
      getAllUsers();
    }
  }, [schemeID]);

  useEffect(() => {
    if (userInfo.schemeId) {
      schemes.filter(myScheme => {
        if (myScheme.id === userInfo.schemeId) {
          setUserSchemeID(myScheme.schemeID);
        }
      });
    }
  }, [userInfo.schemeId]);

  return (
    <>
      <Layout>
        <InnerLayout
          schemes={schemes}
          schemeChange={s => {
            handleschemeChange(JSON.parse(s));
          }}
          // show={true}
          toPrint={toPrint}
          toExport={usersData}
          setSearch={s => {
            setSearch(s);
            searchUsers(schemeID, s);
            // searchWallets(s)
          }}
          // nextPage={pageto => getAllUsers(schemeID, pageto)}
          nextPage={pageto => getAllUsers()}
          page={total}
          size={size}
          title="Nuban Accounts"
          path="show-nuban"
          showSearch
        >
          <div ref={toPrint}>
            <Table className="transaction-table" hover responsive>
              <thead>
                <tr>
                  <th>S/N</th>
                  <th>Account Name</th>
                  <th>Phone Number</th>
                  {/* <th>Wallet No</th> */}
                  <th>Wallet Balance</th>
                  <th>Nuban Account</th>
                  <th>Bank Name</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                {usersData.length === 0 ? (
                  <div className="user-loading-box">
                    <div>
                      <h3>No Data</h3>
                    </div>
                  </div>
                ) : (
                  usersData.map((user, i) => (
                    <tr key={i}>
                      <td>{i + 1 + Number(page) * size}</td>
                      <td>{user.accountFullName}</td>
                      <td>{user.accountOwnerPhoneNumber}</td>
                      {/* <td>{user.accountNumber}</td> */}
                      <td>
                        <MoneyFormat
                          value={user.actualBalance}
                          displayType="text"
                          thousandSeparator={true}
                          decimalScale={2}
                          fixedDecimalScale={true}
                          prefix="₦"
                        />
                      </td>
                      <td>{user.trackingRef && user.nubanAccountNo}</td>
                      <td>{user.trackingRef && user.trackingRef.length > 20 ? 'Polaris' : 'CashConnect'}</td>
                      <td className="p-0">
                        <div
                          className="nuban_action"
                          onClick={() => {
                            if (user.trackingRef && user.trackingRef.length < 20) {
                              setUserInfo(user);
                              setShowModal(true);
                            }
                          }}
                        >
                          <ActionSvg />
                        </div>
                        {/* {user.nubanAccountNo ? (
                        <span>Active</span>
                      ) : (
                        <button
                          className="get-nuban-btn"
                          onClick={() => {
                            getNubanAcct(user.accountOwnerPhoneNumber);

                            //   const result = await getNubanAcct(user.accountOwnerPhoneNumber);
                            // user.nubanAccountNo = result;
                            // alert(`${user.accountFullName}’s Nuban no: ${result}`);
                            // // eslint-disable-next-line no-console
                            // console.log(result);

                            // const result = await getNubanAcct(user.accountOwnerPhoneNumber);
                            // user.nubanAccountNo = result;
                            // alert(`${user.accountFullName}’s Nuban no: ${result}`);
                            // // eslint-disable-next-line no-console
                            // console.log(result);
                          }}
                        >
                          Get Nuban Acct
                        </button>
                      )} */}
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </Table>
          </div>
        </InnerLayout>
      </Layout>
      <Modal
        isOpen={showModal}
        toggle={() => {
          setUserInfo({ accountFullName: '', accountOwnerPhoneNumber: '', nubanAccountNo: '', schemeId: 0 });
          setUserSchemeID('');
          setShowModal(false);
        }}
        centered={true}
        // backdrop="static"
      >
        <div className="nuban_action-box">
          <h5>Change {`${userInfo.accountFullName}'s`} Nuban Account Details</h5>
          <div>
            <label className="nuban_label" htmlFor="name">
              Name: <input type="text" id="name" value={userInfo.accountFullName} readOnly />
            </label>
            <label className="nuban_label" htmlFor="nuban">
              Nuban: <input type="text" id="nuban" value={userInfo.nubanAccountNo} readOnly />
            </label>

            {userSchemeID && (
              <>
                <div className="nuban_scheme">
                  <h6>Scheme Type:</h6>
                  <select name="userScheme">
                    {schemes
                      .filter(userScheme => userScheme.id === userInfo.schemeId)
                      .map(userScheme => {
                        return (
                          <option selected key={userScheme.id} value={userScheme.schemeID}>
                            {userScheme.scheme}
                          </option>
                        );
                      })}
                  </select>
                </div>

                <div className="nuban_btn">
                  <button onClick={() => changeNuban(userInfo.accountOwnerPhoneNumber, userSchemeID)}>Change Nuban</button>
                </div>
              </>
            )}
          </div>
        </div>
      </Modal>
    </>
  );
}

// export default ShowUsers;
