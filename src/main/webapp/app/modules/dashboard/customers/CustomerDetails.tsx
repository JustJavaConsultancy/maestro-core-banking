import './customer.scss';
import React, { useEffect, useState } from 'react';
import Select from 'react-select';
import InnerLayout from '../components/Layout';
import Layout from '../Layout';
import { Table } from 'reactstrap';
import { setUser } from 'app/shared/reducers/reports';
import { RouteComponentProps } from 'react-router-dom';
import { IRootState } from 'app/shared/reducers';
import { connect } from 'react-redux';
import { useHistory } from 'react-router-dom';
import Axios from 'axios';
import MoneyFormat from 'react-number-format';
import { setWalletData } from 'app/shared/reducers/reports';
import moment from 'moment';
import ChangeUserPassword from './changeUserPassword/ChangeUserPassword';
import ShowUserImg from './showUserImage/ShowUserImg';
import ChangeUserPhoneNumber from './changePhoneNumber/ChangePhoneNumber';
import ReactivateUser from './reactivateUser/ReactivateUser';
import UpgradeKYC from './upgradeKyc/UpgradeKyc';
import { hasAnyAuthority } from 'app/shared/util/hasAuthorities';
import { AUTHORITIES } from 'app/config/constants';

export interface IUserstateProps extends StateProps, DispatchProps, RouteComponentProps<{}> {}
const List = props => (
  <p>
    <span>{props.title}</span>
    <span style={{ color: '#212145', fontWeight: 'bold', marginLeft: '10px' }}>{props.value}</span>
  </p>
);

function User(props) {
  const [user, setdUser] = useState({
    user: {
      firstName: '',
      lastName: '',
      email: '',
      imageUrl: '',
      activated: false
    },
    phoneNumber: '',
    fullAddress: '',
    dateOfBirth: '',
  });

  const [showImg, setShowImg] = useState(false);
  const [wallets, setWallets] = useState([]);
  const [showProfileModal, setShowProfileModal] = useState(false)
  const history = useHistory();

  const isAdmin = hasAnyAuthority(props.userLog.authorities, [AUTHORITIES.ADMIN, AUTHORITIES.ROLE_SUPER_ADMIN]);
  const isMcPherson = hasAnyAuthority(props.userLog.authorities, [AUTHORITIES.MCPHERSON]);

  const getCustomerWallet = async (phone): Promise<void> => {
    try {
      const customerWallet = await Axios.get(`/api/wallet/customer/${phone}`);
      setWallets(customerWallet.data);
      console.error(customerWallet.data);
    } catch (e) {
      console.error(e);
    }
  };


  const changeAccountStatus = async (ref, status): Promise<void> => {
    try {
      const r = await Axios.post(`/api/wallet-account/status/${ref}/${status}`);
      console.error(r);
      getCustomerWallet(props.user.phoneNumber);
      alert('success');
    } catch (e) {
      console.error(e.response);
      if (e.response) {
        alert(e.response.data.message);
      } else {
        alert('An error occured!');
      }
    }
  };
  
  useEffect(() => {
    if (!props.user.user) {
      history.push('customers');
      return;
    }
    getCustomerWallet(props.user.phoneNumber);
    setdUser(props.user);
  }, [props.user]);


  return (
    <Layout>
      <InnerLayout dontfilter={true} 
        title={isMcPherson ? `${props.user.lastName} ${props.user.firstName}` : `${user.user.lastName} ${user.user.firstName}`} 
        path={isMcPherson ? `Student > ${props.user.firstName}` : `Customer > ${user.user.firstName}`}
      >
        <div>
          <div style={{ marginBottom: '25px' }}>
            <img style={{ marginRight: '10px' }} src="content/images/bdicon.png" />
            <span className="bd">Basic Details</span>
          </div>
          <div className="customerDetails">
            <div style={{ flex: 1 }}>
              {!isMcPherson ?
              (
                <>
                  <List title="First Name:  " value={user.user.firstName} />
                  <List title="Surname:  " value={user.user.lastName} />
                  <List title="Phone:  " value={user.phoneNumber} />
                  <List title="Address:  " value={user.fullAddress} />
                  <List title="Email:  " value={user.user.email} />
                  <List title="D.O.B:  " value={user.dateOfBirth} />
                </>
              ) : (
                <>
                  <List title="First Name:  " value={props.user.firstName} />
                  <List title="Surname:  " value={props.user.lastName} />
                  <List title="Phone:  " value={props.user.phoneNumber} />
                  <List title="Matric No:  " value={props.user.matricNumber} />
                  <List title="Email:  " value={props.user.email} />
                  <List title="Department:  " value={props.user.depatment} />
                </>
              )
              }
            </div>
            {isAdmin &&
              <div>
                <UpgradeKYC user={user} />
                {/* <button onClick={() => setShowProfileModal(true)}>Update User Info</button> */}
                <ChangeUserPassword userPhone={user.phoneNumber} userName={`${user.user.firstName} ${user.user.lastName}`} />
                <ChangeUserPhoneNumber phone={user.phoneNumber} userName={user.user.firstName} />
                <div>
                  <ReactivateUser phone={user.phoneNumber} activated={user.user.activated}/>
                </div>
                <div>
                  <button style={{ backgroundColor: 'hsl(110, 100%, 25%)' }} onClick={() => setShowImg(!showImg)} className="pass-btn">
                    {!showImg ? 'View Image' : 'Hide Image'}
                  </button>
                  {/* {showImg && <ShowUserImg imageUrl={'photo1607426654283.jpg'} />} */}
                  {showImg && <ShowUserImg imageUrl={user.user.imageUrl} />}
                </div>
              </div>
              }
          </div>
          {!isMcPherson &&
          <>
          <div style={{ margin: '30px 0 30px' }}>
            <img style={{ marginRight: '10px' }} src="content/images/walleticon.png" />
            <span className="bd">Wallets</span>
          </div>
          <div>
            <Table hover striped>
              <thead className="tableListData">
                <th>S/N</th>
                <th>Wallet Number</th>
                <th>Wallet Name</th>
                <th>Owner Name</th>
                <th>Owner PhoneNumber</th>
                <th>Balance (₦)</th>
                <th>Actual Balance (₦)</th>
                <th>Date Created</th>
                <th>Status</th>
              </thead>
              <tbody>
                {wallets.map((wallet, index) => (
                  <tr className="tableListData click" key={index}>
                    <td
                      onClick={() => {
                        props.setWalletData(wallet);
                        history.push('/wallet-transaction');
                      }}
                    >
                      {index + 1}
                    </td>
                    <td
                      onClick={() => {
                        props.setWalletData(wallet);
                        history.push('/wallet-transaction');
                      }}
                    >
                      {wallet.accountNumber}
                    </td>
                    <td
                      onClick={() => {
                        props.setWalletData(wallet);
                        history.push('/wallet-transaction');
                      }}
                    >
                      {wallet.accountName}
                    </td>
                    <td
                      onClick={() => {
                        props.setWalletData(wallet);
                        history.push('/wallet-transaction');
                      }}
                    >
                      {wallet.accountOwnerName}
                    </td>
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
                        <select value={wallet.status} onChange={e => changeAccountStatus(wallet.accountNumber, e.target.value)}>
                          <option value="ACTIVE">ACTIVE</option>
                          <option value="INACTIVE">INACTIVE</option>
                          <option value="SUSPENDED">SUSPENDED</option>
                          <option value="DEBIT_ON_HOLD">DEBIT_ON_HOLD</option>
                        </select>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </div>
          </>}
        </div>
      </InnerLayout>
    </Layout>
  );
}

const mapStateToProps = ({ reports, authentication: { account } }: IRootState) => ({
  user: reports.user,
  userLog: account,
});

const mapDispatchToProps = { setUser, setWalletData };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(User);
