import Axios from 'axios';
import React, { useEffect, useState, useRef } from 'react';
import InnerLayout from '../components/Layout';
import Layout from '../Layout';
import { Table } from 'react-bootstrap';
import { setUser } from 'app/shared/reducers/reports';
import { RouteComponentProps } from 'react-router-dom';
import { IRootState } from 'app/shared/reducers';
import { connect } from 'react-redux';
import { useHistory } from 'react-router-dom';
import moment from 'moment';
import { isEmpty } from 'lodash';
import { ThreeDots } from  'react-loader-spinner';
import { hasAnyAuthority } from 'app/shared/util/hasAuthorities';
import { AUTHORITIES } from 'app/config/constants';

import { headers } from './data/customerData';

export interface IUserstateProps extends StateProps, DispatchProps, RouteComponentProps<{}> {}
function Users(props) {
  const [users, setUsers] = useState([]);
  const [total, setTotal] = useState(0);
  const [size, setSize] = useState(20);
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState('');
  const history = useHistory();
  const toPrint = useRef();
  const [schemes, setSchemes] = useState([]);
  const [scheme, setScheme] = useState({ schemeID: '' });
  const [loading, setLoading] = useState(false);
  const [selectedOption, setSelectedOption] = useState(null)
  const [isDisable, setIsDisable] = useState(true);
  const [searchLoading, setSearchLoading] = useState(false);

  const { schemeID } = scheme;

  const isMcPherson = hasAnyAuthority(props.userLog.authorities, [AUTHORITIES.MCPHERSON]);
  const isFuoyeAdmin = hasAnyAuthority(props.userLog.authorities, [AUTHORITIES.FUOYE_ADMIN]);

  const getUsers: any = async (paget, schem) => {
    try {
      setLoading(true)
      if(isMcPherson || isFuoyeAdmin) {
        // const theUsers = await Axios.get(`https://wallet.remita.net/services/wallencyschools/api/admin-schools/get-students/search/${schem}?key=${search}`)
        const theUsers = await Axios.get(`/services/wallencyschools/api/admin-schools/get-students/search/${schem}?key=${search}`)
        console.error(theUsers)
        setUsers(theUsers.data.data);
        setTotal(theUsers.data.metadata.totalNumberOfRecords);
      } else {
       const theUsers = await Axios.get(`/api/profiles/${schem}/search?key=${search}&page=${paget}&size=${size}`);
        // const theUsers = await Axios.get(`https://walletdemo.remita.net/api/profiles/${schem}/search?key=${search}&page=${paget}&size=${size}`);
        setUsers(theUsers.data.data);
        setTotal(theUsers.data.metadata.totalNumberOfRecords);
      }
      setPage(paget);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const filterUsers = async (e):Promise<void> => {
    setSelectedOption(e)
    try {
      setLoading(true)
      const theUsers = await Axios.get(`/api/profiles/${schemeID}/search?key=${search}&page=${0}&size=${size}`);
      if(e?.value !== "all") {
        const filteredData = theUsers.data.data.filter(user=> user.kycLevel === e?.value)
        setUsers(filteredData)
      } else {
        setUsers(theUsers.data.data)
      }
    } catch (err) {
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  const searchUsers: any = async (s, schem) => {
    try {
      setSearchLoading(true)
      if(isMcPherson || isFuoyeAdmin){
        const theUsers = await Axios.get(`/services/wallencyschools/api/admin-schools/get-students/search/${schem}?key=${s}`);
        setUsers(theUsers.data.data);
        setTotal(theUsers.data.metadata.totalNumberOfRecords);
        setPage(0);
      } else {
        const theUsers = await Axios.get(`/api/profiles/${schem}/search?key=${s}&page=${0}&size=${size}`);
        setTotal(theUsers.data.metadata.totalNumberOfRecords);
        setPage(0);
        setUsers(theUsers.data.data);
      }
    } catch (e) {
      console.error(e);
    } finally {
      setSearchLoading(false)
    }
  };

  const getSchemes = async () => {
    try {
      const theSchemes = await Axios.get(`api/schemes/admin/${props.userLog.login}`);
      if(isMcPherson) {
        setScheme({schemeID: '4d6350686572736f6e'});
        getUsers(0, '4d6350686572736f6e');
      } else {
        setSchemes(theSchemes.data.data);
        setScheme(theSchemes.data.data[0]);
        getUsers(0, theSchemes.data.data[0].schemeID);
      }
    } catch (e) {
      console.error(e);
    }
  };

  const handleschemeChange = s => {
    console.error(s.schemeID);

    setScheme(s);
    getUsers(0, s.schemeID);
  };

  const asyncExportMethod = async () => {
    try {
      const result = await Axios.get(`/api/profiles/${scheme.schemeID}/search?key=${search}&page=${0}&size=${1000}`);
      // const result = await Axios.get(`https://walletdemo.remita.net/api/profiles/${scheme.schemeID}/search?key=${search}&page=${0}&size=${1000}`);
      return result;
    } catch (error) {
      console.error(error);
      return [];
    }
  };

  useEffect(() => {
    getSchemes();
  }, []);

  useEffect(() => {
    if (scheme.schemeID) {
      setIsDisable(false);
    } else {
      setIsDisable(true);
    }
  }, [schemeID]);

  return (
    <Layout>
      <InnerLayout
        toPrint={toPrint}
        toExport={users}
        schemes={schemes}
        schemeChange={s => {
          handleschemeChange(JSON.parse(s));
        }}
        setSearch={s => {
          setSearch(s);
          searchUsers(s, scheme.schemeID);
        }}
        nextPage={pageto => getUsers(pageto, scheme.schemeID)}
        page={total}
        size={size}
        getData={asyncExportMethod}
        isDisable={isDisable}
        headers={headers}
        title={(isMcPherson || isFuoyeAdmin) ? "Student Details" : "Customer Details"}
        path={(isMcPherson || isFuoyeAdmin) ? "Students" : "Customers"}
        kycFilter={true}
        setSelectedOption={filterUsers}
        selectedOption={selectedOption}
        searchLoading={searchLoading}
        showSearch
      >
        <div ref={toPrint} style={{ minWidth: '300px', minHeight: '300px', marginTop: '20px' }}>
        {loading && (
              <div className="loader__container">
                <ThreeDots 
                  height="100" 
                  width="100" 
                  color='#29307C' 
                  ariaLabel='loading'
                />
              </div>
            )
          }
         {!loading && (
         <Table hover responsive className="transaction-table">
            <thead className="tableListData">
              <th>S/N</th>
              <th>{(isMcPherson || isFuoyeAdmin) ? 'Matric Number' : 'Customer ID'}</th>
              <th>Email</th>
              <th>First Name</th>
              <th>Surname</th>
              <th>{(isMcPherson || isFuoyeAdmin) ? 'Department' : 'KYC Level'}</th>
              {(isMcPherson || isFuoyeAdmin) && <th>Level</th>}
              {(!isMcPherson || !isFuoyeAdmin) && 
                <>
                  <th>Address</th>
                  <th>Date Registered</th>
                  <th>Status</th>
                </>
              }
            </thead>
            <tbody>
              {(users && users.length > 0) ? users.map(
                (user, index) =>
                  !isEmpty(user) && (
                    <tr
                      onClick={() => {
                        props.setUser(user);
                        history.push('/customers-details');
                      }}
                      className="tableListData click"
                      key={index}
                    >
                      <td>{index + 1 + Number(page) * size}</td>
                      <td>{(isMcPherson || isFuoyeAdmin) ? user.matricNumber : user.phoneNumber}</td>
                      <td>{(isMcPherson || isFuoyeAdmin) ? user.email : user.user.email}</td>
                      <td>{(isMcPherson || isFuoyeAdmin) ? user.firstName : user.user.firstName}</td>
                      <td>{(isMcPherson || isFuoyeAdmin) ? user.lastName : user.user.lastName}</td>
                      {(isMcPherson || isFuoyeAdmin) && <td>{user.depatment}</td>}
                      {(isMcPherson || isFuoyeAdmin) && <td>{user.level}</td>}
                      {(!isMcPherson || !isFuoyeAdmin) && 
                        <>
                          <td>{user.kycLevel ? user.kycLevel : 1}</td>
                          <td>{user?.addresses?.length > 0 ? user.addresses[0].address : ''}</td>
                          <td>{moment(user.dateCreated).format('ll')}</td>
                          <td>
                            <div
                              style={{
                                padding: '5px 10px',
                                borderRadius: '15px',
                                textAlign: 'center',
                                fontSize: '12px',
                                color: '#00A07E',
                                backgroundColor: 'rgba(0, 196, 140, 0.27)',
                              }}
                            >
                              {user.user.activated ? 'Active' : 'InActive'}
                            </div>
                          </td>
                        </>
                      }
                    </tr>
                  )
              ) : (
                <div className="center_msg-box" style={{marginLeft: '250%'}}>
                  <p>No data</p>
                </div>
              )}
            </tbody>
          </Table>
          )}
        </div>
      </InnerLayout>
    </Layout>
  );
}

const mapStateToProps = ({ reports, authentication: { isAuthenticated, account, sessionHasBeenFetched } }: IRootState) => ({
  user: reports.user,
  userLog: account,
});

const mapDispatchToProps = { setUser };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Users);
