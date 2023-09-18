import React, { useEffect, useRef, useState } from 'react';
import Layout from '../Layout';
import InnerLayout from '../components/Layout';
import MoneyFormat from 'react-number-format';
import axios from 'axios';

import { Table } from 'react-bootstrap';

import '../showUsers/showUsers.scss';
import '../administration/kycRequest/kycRequest.scss';
import { UserDataInterface } from '../administration/kycRequest/KycRequest';

export default function (props) {
  const [data, setData] = useState([]);
  const [canFetch, setCanFetch] = useState(false);
  const [image, setImage] = useState('');
  const [userData, setUserData] = useState({
    kycLevel: 0,
    id: '',
    fullName: '',
    phoneNumber: '',
  });

  const getUsers = async () => {
    try {
      const result = await axios.get(`/api/profiles/53797374656d73706563732077616c6c6574/search?key=${''}&page=${0}&size=${1000}`);
      // eslint-disable-next-line no-console
      console.log(result);

      const newData = result.data.data.filter(user => user.kycLevel > 1);

      // eslint-disable-next-line no-console
      console.log(newData);

      setData(newData);
    } catch (error) {
      if (error.response) {
        console.error(error.response.data.message);
        setData([]);
      }
      setData([]);
    }
  };

  const fetchUserDocs = () => {
    const adminToken = JSON.parse(sessionStorage.getItem('jhi-authenticationToken'));

    if (userData.kycLevel && userData.phoneNumber) {
      const userLevel = userData.kycLevel === 2 ? 'kyc' : 'kyc3';

      // fetch(`https://wallet.remita.net/api/documents/kyc3/+2348038328349`, {
      fetch(`/api/documents/${userLevel}/${userData.phoneNumber}`, {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${adminToken}`,
        },
      })
        .then(response => {
          response
            .blob()
            .then(blobResponse => {
              const dataBlob = blobResponse;
              // eslint-disable-next-line no-console
              console.log(dataBlob);

              const reader = new FileReader();
              reader.readAsDataURL(dataBlob);
              reader.onloadend = () => {
                setImage(reader.result as string);
              };

              // const urlCreator = window.URL || window.webkitURL;
              // window.scroll({ behavior: 'smooth', top: 0 });
              // setImage(urlCreator.createObjectURL(dataBlob));
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

  useEffect(() => {
    getUsers();
  }, []);
  useEffect(() => {
    // eslint-disable-next-line no-console
    console.log(userData);
  }, [userData]);

  useEffect(() => {
    if (userData.kycLevel && userData.phoneNumber && canFetch) {
      // getUserDocs();
      fetchUserDocs();
    }
    // eslint-disable-next-line no-console
    console.log(userData);
  }, [canFetch]);

  return (
    <Layout>
      <InnerLayout show={true} dontfilter={true} title="KYC Documents" path="kyc-docs">
        <Table className="transaction-table" hover responsive>
          <thead>
            <th>S/N</th>
            <th>Full Name</th>
            <th>Phone Number</th>
            <th>Address</th>
            <th>KYC Level</th>
            <th>Uploaded Document</th>
            {/* <th>Wallet No</th> */}
            {/* <th>Wallet Balance</th> */}
          </thead>
          <tbody>
            {data.length === 0 ? (
              <div className="user-loading-box">
                <div>
                  <h3>No Data</h3>
                </div>
              </div>
            ) : (
              data.map((user, i) => (
                <tr key={i}>
                  <td>{i + 1}</td>
                  <td>{user.fullName}</td>
                  <td>{user.phoneNumber}</td>
                  <td>{user.fullAddress}</td>
                  <td>{user.kycLevel}</td>
                  <td
                    onClick={() => {
                      setCanFetch(true);
                      setUserData(user);
                      // getUserDocs();
                    }}
                    className="doc-upload-box"
                  >
                    Document Uploaded
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </Table>

        {image && (
          <div className="doc-modal-box">
            <div className="doc-inner-box">
              <div
                onClick={() => {
                  setCanFetch(false);
                  setImage('');
                }}
                className="doc-box-close"
                style={{ cursor: 'pointer' }}
              >
                <h4>X</h4>
              </div>
              <h4 className="doc-box-title">KYC UPGRADE REQUEST</h4>

              <div className="doc-user-profile">
                <ul>
                  <li>
                    Full name: <strong>{userData.fullName}</strong>
                  </li>
                  <li>
                    User ID: <strong>{'0' + userData.phoneNumber.substr(userData.phoneNumber.length - 10)}</strong>
                  </li>
                  <li>
                    BVN: <strong>{}</strong>
                  </li>
                  <li>
                    Current KYC Level: <strong>KYC Level {userData.kycLevel}</strong>
                  </li>
                  {/* <li>
                    Target KYC Level: <strong>KYC Level {userData. + 1}</strong>
                  </li> */}
                </ul>
              </div>

              <div className="doc-img-box">
                <h6>Uploaded Documents</h6>

                <div className="doc-img">
                  <img src={image} alt="image" width="100%" />
                </div>
              </div>

              {/* <div className="doc-btn-box">
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
                    handleConfirm(userData.id, 'REJECTED');
                  }}
                  className="doc-btn doc-failed"
                >
                  Decline
                </button>
              </div> */}
            </div>
          </div>
        )}
      </InnerLayout>
    </Layout>
  );
}
