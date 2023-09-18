import React, { ChangeEvent, useEffect, useRef, useState } from 'react';
import Layout from '../../Layout';
import InnerLayout from '../../components/Layout';
import { Card } from '../../components/cardManagement/HeaderCards';
import { Table } from 'react-bootstrap';
import Axios from 'axios';
import moment from 'moment';
import { useHistory, useLocation } from 'react-router-dom';
import { CurrentPostType } from '../PostedRequest';
import ApprovalIcon from '../../../../../content/images/approval.svg';
import DeclineIcon from '../../../../../content/images/decline.svg';

import './currentPost.scss';
import PromptModal from '../../components/promptModal/PromptModal';
import DeductModal from '../deductMoney/DeductModal';
import SuccessModal from '../successModal/SuccessModal';
import countrolAccountMySuffixDeleteDialog from 'app/entities/countrol-account-my-suffix/countrol-account-my-suffix-delete-dialog';

function CurrentPost() {
  const token = JSON.parse(sessionStorage.getItem('jhi-authenticationToken'));
  const [approveShow, setApproveShow] = useState(false);
  const [declineShow, setDeclineShow] = useState(false);
  const [showDeductModal, setShowDeductModal] = useState(false);
  const [loading, setLoading] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);

  interface CurrentUserType extends CurrentPostType  {
    accountNumber?: number;
    user: {
      id: string | number;
      fullname: string;
    }
  }

  const location: any = useLocation();
  const history = useHistory();
  const post = location.state.post;

  const confirmPostHandler = async (status: string) => {
    const payload = {
      advertId: post.id,
      status,
      token,
    };

    try {
      setLoading(true)
      let res;
      if(location.state.path === 'Adverts') {
        // const res = await Axios.post(`https://walletdemo.remita.net/services/wallencyschools/api/admin-schools/approve-adverts`, payload);
        res = await Axios.post('/services/wallencyschools/api/admin-schools/approve-adverts', payload);
      } else {
        res = await Axios.post(`/services/wallencyschools/api/admin-schools/approve-feeds`, payload);
      }
      // eslint-disable-next-line no-console
      console.log(res);
      if (res.data.code === '00' && res.data.data.status === 'APPROVED') {
        setShowSuccessModal(!showSuccessModal)
      } else {
        alert('Request declined successfully!');
        history.push('/posted-requests');
      }
    } catch (error) {
      if (error.response) {
        alert(error.response.data.message);
        return;
      } else {
        alert('Network Error!, Try again later');
      }
    } finally {
      setLoading(false)
      setApproveShow(false)
      setDeclineShow(false)
    }
  };

  return (
    <Layout>
      <InnerLayout dontfilter={true} show={true} title="Current Post" path={` Post Request Detail`}>
        <Card customStyle="pad_style">
          <section className="post__container">
            <div className="post__header__box">
              <h5 className="post__header">
                Story Post Request <span className="post__header__modify">User ID {post.user.id}</span>
              </h5>
              <div className="post__header__btn">
                <button onClick={() => setApproveShow(true)}>
                  <img src={ApprovalIcon} alt="approve icon" />
                  <span>Approve Request</span>
                </button>
                <button onClick={() => setDeclineShow(true)}>
                  <img src={DeclineIcon} alt="decline icon" />
                  <span>Decline Request</span>
                </button>
              </div>
            </div>

            <section className="post__body__box">
              <article className="post__body__first">
                <div className="head__container">
                  <h6 className="guide__header">Post Header</h6>
                  <small>Title</small>
                  <p>{post.title}</p>
                </div>

                <div className="post__body__detail_box">
                  <div className="post__body__deatail">
                    <div>
                      <small>Category</small>
                      <p>{post.category.name || 'N/A'}</p>
                    </div>
                    <div>
                      <small>Audience</small>
                      <p>{post.audience.code}</p>
                    </div>
                  </div>

                  <figure className="cover__photo__box">
                    <figcaption>
                      <small>Cover Photo</small>
                    </figcaption>
                    <img src={post.thumbnail} alt="post cover" width="100%" />
                  </figure>
                </div>
              </article>

              <article className="post__body__second">
                <div>
                  <h6 className="guide__header">Post Content</h6>
                  <p>{post.content}</p>
                </div>
              </article>
            </section>
          </section>
        </Card>
        {(
          <PromptModal 
            show={approveShow} 
            setShow={()=> {
                confirmPostHandler('APPROVED')
              }
            }
            setCancel={() =>setApproveShow(!approveShow) }
            title="Approve" 
            requestLoading={loading}
          />
          )}
        {<PromptModal 
            show={declineShow} 
            setShow={() => confirmPostHandler('REJECTED')} 
            title="Decline" 
            setCancel={() =>setDeclineShow(!declineShow) }
            requestLoading={loading}
          />
        }
        {<SuccessModal 
          show={showSuccessModal} 
          setShow={() => {
            setShowSuccessModal(!showSuccessModal)
            history.push('/posted-requests');
          }}
          accountNumber={post.accountNumber} 
        />}
      </InnerLayout>
    </Layout>
  );
}

export default CurrentPost;
