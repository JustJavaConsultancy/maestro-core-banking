import React, { useEffect, useRef, useState } from 'react';
import Layout from '../../../Layout';
import InnerLayout from '../../../components/Layout';
import axios from 'axios';
import { Table } from 'react-bootstrap';
import moment from 'moment';
import { Modal } from 'reactstrap';

import './previousRequest.scss';
import { UserDataInterface } from '../KycRequest';

interface NewUserDataInterface extends UserDataInterface {
  status: string;
}

export default function () {
  const [requests, setRequests] = useState([]);
  const [userData, setUserData] = useState<NewUserDataInterface>({
    currentLevel: 0,
    id: '',
    profileFullName: '',
    profilePhoneNumber: '',
    documentId: '',
    nin: '',
    bvn: '',
    documentType: '',
    status: '',
  });
  const [canFetch, setCanFetch] = useState(false);

  const getPrevKycRequest = async () => {
    try {
      const prevKycs = await axios.get('/api/kyc-requests/approved-and-rejected');
      // const prevKycs = await axios.get('https://walletdemo.remita.net/api/kyc-requests/approved-and-rejected');
      // const prevKycs = await axios.get('https://wallet.remita.net/api/kyc-requests/approved-and-rejected');

      // eslint-disable-next-line no-console
      console.log(prevKycs.data);
      setRequests(prevKycs?.data);
    } catch (error) {
      console.error(error);
    }
  };

  const hideModal = () => {
    setCanFetch(false);
    // setImage('');
  };

  useEffect(() => {
    getPrevKycRequest();
  }, []);

  return (
    <>
      <Layout>
        <InnerLayout dontfilter={true} show={true} title="Previous KYC Requests" path="previous-kyc-request">
          <Table className="transaction-table" hover responsive>
            <thead>
              <tr>
                <th>S/N</th>
                <th>Phone No.</th>
                <th>Full Name</th>
                {/* <th>Email</th> */}
                <th>Current KYC level</th>
                {/* <th>Target KYC level</th> */}
                {/* <th>Uploaded Document</th> */}
                <th>Handled Date</th>
                <th>Status</th>
              </tr>
            </thead>

            <tbody>
              {requests.map((request, i) => (
                <tr
                  key={i}
                  className={`prev-user ${request.status === 'APPROVED' ? 'approve-user' : 'reject-user'}`}
                  onClick={() => {
                    setCanFetch(true);
                    setUserData(request);
                    // getUserDocs();
                  }}
                >
                  <td>{i + 1}</td>
                  <td>{request.profilePhoneNumber}</td>
                  <td>{request.profileFullName.toUpperCase()}</td>
                  <td>
                    <strong>KYC Level {request.status === 'APPROVED' ? request.currentLevel + 1 : request.currentLevel}</strong>
                  </td>
                  <td>{moment(request.lastModifiedDate).format('lll')}</td>
                  <td>{request.status}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        </InnerLayout>
      </Layout>

      <Modal isOpen={canFetch} toggle={hideModal} centered={true}>
        <div className="doc-modal-box">
          <div className="doc-inner-box">
            <div onClick={hideModal} className="doc-box-close" style={{ cursor: 'pointer' }}>
              <h4>X</h4>
            </div>
            <h4 className="doc-box-title">
              KYC UPGRADE REQUEST {userData.id}{' '}
              <small>
                <strong className={userData.status === 'APPROVED' ? 'upgrade-approved' : 'upgrade-rejected'}>({userData.status})</strong>
              </small>
            </h4>

            <div className="doc-user-profile">
              <Table>
                <tbody>
                  <tr>
                    <th>Full name</th>
                    <td>
                      <strong>{userData.profileFullName.toUpperCase()}</strong>
                    </td>
                  </tr>

                  <tr>
                    <th>User ID</th>
                    <td>
                      <strong>{'0' + userData.profilePhoneNumber.substr(userData.profilePhoneNumber.length - 10)}</strong>
                    </td>
                  </tr>

                  {userData.bvn && (
                    <tr>
                      <th> BVN </th>
                      <td>
                        <strong>{userData.bvn}</strong>
                      </td>
                    </tr>
                  )}

                  {userData.documentType && (
                    <tr>
                      <th>Document Type</th>
                      <td>
                        <strong>
                          {userData.documentType === 'NIN'
                            ? 'NIN'
                            : userData.documentType === 'DRIVERS_LICENSE'
                            ? "Driver's License"
                            : userData.documentType === 'VOTERS_CARD'
                            ? "Voter's Card"
                            : userData.documentType === 'WATER_BILL'
                            ? 'Water Bill'
                            : userData.documentType === 'WASTE_BILL'
                            ? 'Waste Bill'
                            : userData.documentType === 'ELECTRICITY_BILL'
                            ? 'Electricity Bill'
                            : 'Letter from public authority'}
                        </strong>
                      </td>
                    </tr>
                  )}

                  {userData.nin && (
                    <tr>
                      <th>NIN</th>
                      <td>
                        <strong>{userData.nin}</strong>
                      </td>
                    </tr>
                  )}
                  {userData.documentId && (
                    <tr>
                      <th>Document Number</th>
                      <td>
                        <strong>{userData.documentId}</strong>
                      </td>
                    </tr>
                  )}

                  <tr>
                    <th>Current KYC Level</th>
                    <td>
                      <strong>KYC Level {userData.status === 'APPROVED' ? userData.currentLevel + 1 : userData.currentLevel}</strong>
                    </td>
                  </tr>
                  <tr>
                    <th>Targeted KYC Level</th>
                    <td>
                      <strong>KYC Level {userData.currentLevel + 1}</strong>
                    </td>
                  </tr>
                </tbody>
              </Table>
            </div>
          </div>
        </div>
      </Modal>
    </>
  );
}
