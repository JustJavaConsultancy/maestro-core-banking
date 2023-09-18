import React, { ChangeEvent, useEffect, useRef, useState } from 'react';
import { useHistory } from 'react-router';
import Layout from '../../Layout';
import InnerLayout from '../../components/Layout';
import axios from 'axios';
import { Table } from 'react-bootstrap';
import moment from 'moment';
import { Modal } from 'reactstrap';

import { data as myData } from './data';

import './kycRequest.scss';

export interface UserDataInterface {
  currentLevel: number;
  profilePhoneNumber: string;
  id: number | string;
  profileFullName: string;
  documentId: number | string;
  nin: string;
  bvn: string;
  documentType: string;
}

export default function () {
  const history = useHistory();

  const [usersData, setUsersData] = useState([]);
  const [userData, setUserData] = useState<UserDataInterface>({
    currentLevel: 0,
    id: '',
    profileFullName: '',
    profilePhoneNumber: '',
    documentId: '',
    nin: '',
    bvn: '',
    documentType: '',
  });
  const [image, setImage] = useState('');
  const [canFetch, setCanFetch] = useState(false);
  const [showToast, setShowToast] = useState(false);
  const [currentStatus, setCurrentStatus] = useState('');
  const downloadRef = useRef<HTMLAnchorElement>();
  const [filteredReasons, setFilteredReasons] = useState<string[]>([]);
  const [showReasons, setShowReasons] = useState(false);

  const [reasons, setReasons] = useState({
    one: false,
    two: false,
    three: false,
    four: false,
  });

  const { one, two, three, four } = reasons;

  const getKycRequest = async () => {
    try {
      const kycs = await axios.get('/api/kyc-requests-awaiting-approval');
      // const kycs = await axios.get('https://walletdemo.remita.net/api/kyc-requests-awaiting-approval');
      // const kycs = await axios.get('https://wallet.remita.net/api/kyc-requests-awaiting-approval');

      // eslint-disable-next-line no-console
      console.log(kycs.data);
      setUsersData(kycs?.data);
      // setUsersData(myData);
    } catch (error) {
      console.error(error);
    }
  };

  const emptyState = () => {
    setFilteredReasons([]);
    setShowReasons(false);
    setReasons({ ...reasons, four: false, three: false, two: false, one: false });
  };

  const handleConfirm = async (kycId, status) => {
    const data = {
      kycRequestId: kycId,
      status,
      reason: status === 'APPROVED' ? [] : filteredReasons,
    };

    try {
      const response = await axios.post(`/api/kyc-requests/approve`, data);
      // const response = await axios.post(`https://wallet.remita.net/api/kyc-requests/approve`, data);
      // const response = await axios.post(`https://walletdemo.remita.net/api/kyc-requests/approve`, data);

      if (response.data.code === '00') {
        emptyState();
        setCurrentStatus(status);
        // eslint-disable-next-line no-console
        console.log(response.data.data);

        // eslint-disable-next-line no-console
        console.log(typeof status);
        // if (status === ('APPROVED' || 'REJECTED')) {
        window.scroll({ behavior: 'smooth', top: 0 });
        setShowToast(true);
        setTimeout(() => {
          setShowToast(false);
          getKycRequest();
        }, 1500);
        // }

        // alert('User upgraded!');
        // getKycRequest();
        // return;
      } else {
        alert('user already upgraded!!');
      }
    } catch (error) {
      console.error(error);
      alert('User upgrade unsuccessful!!');
      emptyState();
    } finally {
      setCanFetch(false);
      setImage('');
    }
  };

  // const getUserDocs = async () => {
  //   if (userData.currentLevel && userData.profilePhoneNumber) {
  //     const userLevel = userData.currentLevel === 1 ? 'kyc' : 'kyc3';

  //     try {
  //       // const response = await axios.get(`/api/documents/${userLevel}/${userData.profilePhoneNumber}`);
  //       const response = await axios.get(`https://wallet.remita.net/api/documents/kyc3/+2348132332011`);
  //       // eslint-disable-next-line no-console
  //       console.log(response);

  //       setImage(response.data);
  //     } catch (error) {
  //       console.error(error);
  //     }
  //   } else {
  //     alert('No User Chosen!');
  //   }
  // };

  const fetchUserDocs = () => {
    const adminToken = JSON.parse(sessionStorage.getItem('jhi-authenticationToken'));

    if (userData.currentLevel && userData.profilePhoneNumber) {
      const userLevel = userData.currentLevel === 1 ? 'kyc' : 'kyc3';

      // fetch(`https://wallet.remita.net/api/documents/kyc3/+2348038328349`, {
      fetch(`/api/documents/${userLevel}/${userData.profilePhoneNumber}`, {
        // fetch(`https://wallet.remita.net/api/documents/${userLevel}/${userData.profilePhoneNumber}`, {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${adminToken}`,
        },
      })
        .then(response => {
          response
            .blob()
            .then(blobResponse => {
              const data = blobResponse;
              // eslint-disable-next-line no-console
              console.log(data);

              if (data.size) {
                const reader = new FileReader();
                reader.readAsDataURL(data);
                reader.onloadend = () => {
                  setImage(reader.result as string);
                };
              } else {
                setImage('');
              }

              // const urlCreator = window.URL || window.webkitURL;
              // window.scroll({ behavior: 'smooth', top: 0 });
              // setImage(urlCreator.createObjectURL(data));
            })
            .catch(err => {
              alert('Error getting image');
            });
        })
        .catch(err => console.error(err));
    } else {
      alert('No User Chosen!');
    }
  };

  const hideModal = () => {
    setCanFetch(false);
    setImage('');
  };

  const handleCheck = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, checked, dataset } = e.target;
    setReasons({ ...reasons, [name]: checked });

    if (checked && !filteredReasons.includes(dataset[name])) {
      setFilteredReasons([...filteredReasons, dataset[name]]);
    } else {
      const newFilter = filteredReasons.filter(filterReason => filterReason !== dataset[name]);
      setFilteredReasons(newFilter);
    }
  };

  useEffect(() => {
    getKycRequest();
  }, []);

  useEffect(() => {
    if (userData.currentLevel && userData.profilePhoneNumber && canFetch) {
      // getUserDocs();
      fetchUserDocs();
    }
  }, [canFetch]);

  useEffect(() => {
    if (image && userData.documentType !== 'NIN') {
      const imageArray = image.split(',');
      downloadRef.current.href = `data:image/png;base64,${imageArray[1]}`;
      downloadRef.current.download = `${userData.profileFullName.toUpperCase()}.png`;
      downloadRef.current.click();
    }
  }, [image]);

  useEffect(() => {
    // eslint-disable-next-line no-console
    console.log(filteredReasons);
  }, [filteredReasons]);

  return (
    <>
      <Layout>
        <InnerLayout dontfilter={true} show={true} title="KYC Requests" path="show-users">
          <div>
            <div className="prev-request-btn-box">
              <button onClick={() => history.push('/previous-kyc-request')}>Previous Requests</button>
            </div>
            <Table className="transaction-table" hover responsive>
              <thead>
                <tr>
                  <th>S/N</th>
                  <th>Phone No.</th>
                  <th>Full Name</th>
                  {/* <th>Email</th> */}
                  <th>Current KYC level</th>
                  <th>Target KYC level</th>
                  <th>Uploaded Document</th>
                  <th>Request Date</th>
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
                      <td>{i + 1}</td>
                      <td>{user.profilePhoneNumber}</td>
                      <td>{user.profileFullName.toUpperCase()}</td>
                      {/* <td>{user.email ? user.email : ''}</td> */}
                      <td>{user.currentLevel ? 'KYC Level ' + user.currentLevel : ''}</td>
                      <td>{user.currentLevel ? 'KYC Level ' + (user.currentLevel + 1) : ''}</td>
                      <td
                        onClick={() => {
                          setCanFetch(true);
                          setUserData(user);
                          // getUserDocs();
                        }}
                        className="doc-upload-box"
                      >
                        <small>Document Uploaded</small>
                      </td>
                      <td>{moment(user.lastModifiedDate).format('lll') ? moment(user.lastModifiedDate).format('lll') : ''}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </Table>
            {/* <button onClick={() => handleConfirm(user.id, 'APPROVED')} className="kyc-confirm-btn">
            Approve
            </button>
          <button onClick={() => handleConfirm(user.id, 'REJECTED')} className="kyc-confirm-btn kyc-red">
            Reject
          </button> */}
          </div>

          <div
            className={`toast-box ${showToast ? 'toast-box-show' : ''}  ${
              currentStatus === 'APPROVED' ? 'toast-style-accept' : 'toast-style-reject'
            }`}
          >
            <p className="mb-0">{currentStatus === 'APPROVED' ? 'Request accepted successfully!' : 'Request rejected!!'}</p>
          </div>
        </InnerLayout>
      </Layout>

      <Modal isOpen={canFetch} toggle={hideModal} centered={true}>
        <div className="doc-modal-box">
          <div className="doc-inner-box">
            <div onClick={hideModal} className="doc-box-close" style={{ cursor: 'pointer' }}>
              <h4>X</h4>
            </div>
            <h4 className="doc-box-title">KYC UPGRADE REQUEST {userData.id}</h4>

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
                      <strong>KYC Level {userData.currentLevel}</strong>
                    </td>
                  </tr>
                  <tr>
                    <th>Target KYC Level</th>
                    <td>
                      <strong>KYC Level {userData.currentLevel + 1}</strong>
                    </td>
                  </tr>
                </tbody>
              </Table>
            </div>

            <div className="doc-img-box">
              <h6>Uploaded Documents</h6>

              <div className="doc-img">
                {userData.documentType === 'NIN' ? (
                  <p className="mb-0 err-img">NO UPLOADS</p>
                ) : image ? (
                  <div className="kyc-img">
                    <img src={image} alt="image" width="100%" height="100%" />
                  </div>
                ) : (
                  <p className="mb-0 err-img">NO DOCUMENTS FOUND</p>
                )}
                <div className="kyc-download">
                  <a ref={downloadRef}>download</a>
                </div>
              </div>
            </div>

            <div className="doc-btn-box">
              <button
                onClick={() => {
                  handleConfirm(userData.id, 'APPROVED');
                }}
                className="doc-btn doc-success"
              >
                Approve
              </button>
              <button
                onClick={() => {
                  setShowReasons(!showReasons);

                  // handleConfirm(userData.id, 'REJECTED');
                }}
                className="doc-btn doc-failed"
              >
                Decline
              </button>
            </div>

            {showReasons && (
              <div className="reason-container">
                <div className="reason-title">
                  <h6>You are about to decline this KYC Upgrade request. Please select your reason for declining to proceed.</h6>
                </div>

                <ul className="reason-box">
                  <li>
                    <div>
                      <label>
                        <input type="checkbox" name="one" checked={one} onChange={handleCheck} data-one="Poor image quality" />
                        Poor image quality
                      </label>
                    </div>
                  </li>
                  <li>
                    <div>
                      <label>
                        <input
                          type="checkbox"
                          name="two"
                          checked={two}
                          onChange={handleCheck}
                          data-two="Expired document (older than 3 months)"
                        />
                        Expired document (older than 3 months)
                      </label>
                    </div>
                  </li>
                  <li>
                    <div>
                      <label>
                        <input
                          type="checkbox"
                          name="three"
                          checked={three}
                          onChange={handleCheck}
                          data-three="No or wrong documentation attached"
                        />
                        No or wrong documentation attached
                      </label>
                    </div>
                  </li>
                  <li>
                    <div>
                      <label>
                        <input
                          type="checkbox"
                          name="four"
                          checked={four}
                          onChange={handleCheck}
                          data-four="User’s name did not match documentation"
                        />
                        User’s name did not match documentation
                      </label>
                    </div>
                  </li>
                </ul>

                <div className="reason-btn-box">
                  <button
                    className="reason-btn cancel"
                    onClick={() => {
                      emptyState();
                    }}
                  >
                    Cancel
                  </button>
                  <button
                    onClick={() => handleConfirm(userData.id, 'REJECTED')}
                    className={`reason-btn decline ${filteredReasons.length ? 'decline-enabled' : ''}`}
                    disabled={filteredReasons.length ? false : true}
                  >
                    OK
                  </button>
                </div>
              </div>
            )}
          </div>
        </div>
      </Modal>
    </>
  );
}
