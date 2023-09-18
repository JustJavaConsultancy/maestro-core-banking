import React, { ChangeEvent, useEffect, useState, useRef } from 'react';
import { Redirect, useHistory } from 'react-router';

import Layout from '../../dashboard/Layout';
import InnerLayout from '../../dashboard/components/Layout';
import { useQuery } from '../customHooks/customHooks';
import { Modal } from 'reactstrap';

import './contactUs.scss';

export default function () {
  const query = useQuery();

  const [docId, setDocId] = useState(query.get('docId'));
  const [image, setImage] = useState('');
  const [isOpen, setIsOpen] = useState(false);

  if (!docId) {
    return <Redirect to="/dash" />;
  }
  const fetchUserDocs = documentId => {
    const adminToken = JSON.parse(sessionStorage.getItem('jhi-authenticationToken'));

    // fetch(`https://wallet.remita.net/api/documents/kyc3/+2348038328349`, {
    fetch(`/api/contact-form/support/${documentId}`, {
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

            if (data.size) {
              // setIsOpen(true);
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
  };

  const hideModal = () => setIsOpen(!isOpen);

  useEffect(() => {
    if (docId) {
      fetchUserDocs(docId);
    }
  }, [docId]);

  return (
    <>
      <Layout>
        <InnerLayout dontfilter={true} show={true} title="Contact Us" path="contact-us">
          <div className="contact-box">
            <h6>Please click the button to get user documents.</h6>
            <button
              onClick={() => {
                setIsOpen(true);
              }}
            >
              See Docs
            </button>
          </div>
        </InnerLayout>
      </Layout>

      <Modal isOpen={isOpen} toggle={hideModal} centered={true}>
        <div className="main-contact-box">
          <div className="contact-img-box">
            <img src={image} alt="document image" width="100%" />
          </div>
        </div>
      </Modal>
    </>
  );
}
