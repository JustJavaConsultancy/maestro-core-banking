import React, { ChangeEvent, useEffect, useRef, useState } from 'react';
import { useSelector } from 'react-redux';
import Layout from '../Layout';
import InnerLayout from '../components/Layout';
import { Card } from '../components/cardManagement/HeaderCards';
import { Table } from 'react-bootstrap';
import Axios from 'axios';
import moment from 'moment';
import { useHistory } from 'react-router-dom';
import { ThreeDots } from  'react-loader-spinner';

import './postedRequest.scss';
import PostedRequestItem from './PostedRequestItem';
import ApprovalIcon from '../../../../content/images/approval.svg';
import DeclineIcon from '../../../../content/images/decline.svg';
import Refresh from '../../../../content/images/refresh.svg';
import ArrowSide from '../../../../content/images/arrow-side.png';
import Approval from '../../../../content/images/approval_green.svg';
import PromptModal from '../components/promptModal/PromptModal';
import SuccessModal from './successModal/SuccessModal';
import { hasAnyAuthority } from 'app/shared/util/hasAuthorities';
import { AUTHORITIES } from 'app/config/constants';
import { IRootState } from 'app/shared/reducers';

export interface CurrentPostType {
  id: string | number;
  user: { id: string | number };
  title: string;
  category: { id?: number, name: string};
  audience: { code: string };
  content: string;
  thumbnail: string;
  datePosted: string;
  school: { name: string };
  status: string
}

export interface CheckedValueType {
  id: string | number;
  checked: boolean;
}

function PostedRequest({postData, loading, path, selectedSchool, handleSchoolSelect, showDropdown, handleShowDropdown, getPosts}) {
  const history = useHistory();
  const token = JSON.parse(sessionStorage.getItem('jhi-authenticationToken'));
  const [checkedValues, setCheckedValues] = useState([]);
  const [selectAll, setSelectAll] = useState(false);
  const [schoolList, setSchoolList] = useState([]);
  const [approveShow, setApproveShow] = useState(false);
  const [declineShow, setDeclineShow] = useState(false);
  const [requestLoading, setRequestLoading] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [resetChecked, setResetChecked] = useState(false);

  const account = useSelector((state: IRootState) => state.authentication.account);

  const isMcPherson = hasAnyAuthority(account.authorities, [AUTHORITIES.MCPHERSON, AUTHORITIES.MCPHERSON_FINANCIAL]);
  const isFuoye = hasAnyAuthority(account.authorities, [AUTHORITIES.FUOYE_FINANCIAL, AUTHORITIES.FUOYE_ADMIN]);
  const isSch = isMcPherson || isFuoye;

  const handleChange = () => {
    if(!selectAll){
      const newPosts = postData.filter(item => item.status === 'NEW')
      .map(post => ({id: post.id, checked: true}))
      setCheckedValues(newPosts)
    } else {
      setCheckedValues([])
    }
    setSelectAll(!selectAll)
  }

  const getAllSchools = async () => {
    // const schools = await Axios.get('https://walletdemo.remita.net/services/wallencyschools/api/get-schools');
    const schools = await Axios.get('/services/wallencyschools/api/get-schools');
    const data = [{ id: 0, scheme:'', name: 'All' }, ...schools.data]
    setSchoolList(data);
  }

  const addValue = (data: CheckedValueType) => {
    const otherValues = checkedValues.filter(item => item.id !== data.id)
    // eslint-disable-next-line no-console
    console.log(otherValues)
    if(otherValues.length < 1) setSelectAll(false)
    data.checked ? setCheckedValues([...otherValues, data]) : setCheckedValues([...otherValues])
  }

  const currentPostHandler = (post: CurrentPostType) => {
    history.push({ pathname: `posts/${post.user.id}`, state: { post, path }});
  };

  const transformPostData = (type) => {
    return checkedValues.map(item => {
      if(type === 'approve'){
        return {
          advertId: item.id,
          status: 'APPROVED'
        }
      } else {
        return {
          advertId: item.id,
          status: 'REJECTED'
        }
      }
    })
  }

  const insertSingle = (post) => {
    setCheckedValues([{
      id: post.id
    }])
  }

  const handleRequest = async (type) => {
    const adverts = type === 'approve' ? transformPostData('approve') : transformPostData('reject')
    const data = {
      token,
      adverts,
    }

    try {
      setRequestLoading(true)
      let res;
      if(path === 'Adverts'){
        res = await Axios.post('/services/wallencyschools/api/admin-schools/approve-adverts', data);
        // res = await Axios.post('https://walletdemo.remita.net/services/wallencyschools/api/admin-schools/approve-adverts', data);
      } else {
        res = await Axios.post('/services/wallencyschools/api/admin-schools/approve-feeds', data);
      }
      if (res.data.code === '00' && res.data.data.status === 'APPROVED') {
        setShowSuccessModal(!showSuccessModal)
      } else {
        alert('Request declined successfully!');
      }
    } catch(err) {
      console.error(err)
      if (err.response) {
        alert(err.response.data.message);
        return;
      } else {
        alert('Network Error!, Try again later');
      }
    } finally {
      setRequestLoading(false)
      setApproveShow(false)
      setDeclineShow(false)
    }
  } 

  useEffect(() => {
    getAllSchools()
  }, [])

  return (
    <Layout>
      <InnerLayout dontfilter={true} show={true} title="Posted Requests" path={path}>
        <Card customStyle="pad_style">
          <div className="post__header">
            {!(!postData || postData.length < 1) &&
            <div className="post__header__box__start">
              <label className="check__container">
                <input 
                  type="checkbox" 
                  onChange={handleChange} 
                  checked={selectAll}
                />
                <span className="checkmark"></span>
              </label>
              <div 
                onClick={() => getPosts()}
              >
                <img 
                  src={Refresh} 
                  alt="refresh icon" 
                  className={loading ? "refresh__btn" : ''}
                />
              </div>
              {(selectAll || checkedValues.length > 0) && 
                  <div className="post__header__btn">
                      <button onClick={() => setApproveShow(!approveShow)}>
                        <img src={ApprovalIcon} alt="approve icon" />
                        <span>Approve Request</span>
                      </button>
                      <button onClick={() => setDeclineShow(!declineShow)}>
                        <img src={DeclineIcon} alt="decline icon" />
                        <span>Decline Request</span>
                      </button>
                  </div>
                }
            </div>
            }
            {!(isSch) &&
            <div className="post__header__box__end">
              <div> 
                <span>Showing results from </span>
                <div className="schools__dropdown" 
                  onClick={handleShowDropdown}
                >
                  <span>{selectedSchool.name}</span>
                  <div>
                    <img src={ArrowSide} alt='arrow' />
                  </div>
                </div>
                { showDropdown && 
                  <div className="schools__dropdown__list">
                    {
                    schoolList && schoolList.map(item => (
                      <div
                        onClick={() => handleSchoolSelect(item)}
                        key={item.id}
                      >
                        <p>
                          {item.name}
                        </p>
                        {selectedSchool.id === item.id && <img src={Approval} alt="" />}
                      </div>
                    ))
                  }
                  </div>
                }
              </div>
            </div>
            }
          </div>
          <Table responsive hover className="transaction-table">
            <thead className="">
              <tr>
                <th></th>
                <th>Sender ID</th>
                <th>Audience</th>
                <th>School</th>
                <th colSpan={3}>Message</th>
                <th>Status</th>
                <th>Date</th>
              </tr>
            </thead>
            <tbody>
            {loading && (
              <div className="loader__container" style={{marginLeft: '400px'}}>
                <ThreeDots 
                  height="100" 
                  width="100" 
                  color='#29307C' 
                  ariaLabel='loading'
                />
              </div>
            )
          }
              {(!loading && !postData.length) ? (
                <tr>
                  <td colSpan={8}>
                    <div className="no_data-box">
                      <h2>No Data</h2>
                    </div>
                  </td>
                </tr>
              ) : (
                postData.map((post, i) => (
                  <PostedRequestItem 
                    key={post.id} 
                    post={post} 
                    postHandler={currentPostHandler}
                    getData={addValue}
                    selected={selectAll}
                    resetChecked={resetChecked}
                    insertSingle={insertSingle}
                    setApproveShow={setApproveShow}
                  />
                ))
              )}
            </tbody>
          </Table>
        </Card>
        {
          <PromptModal 
            show={approveShow} 
            setShow={()=> {
                handleRequest('approve')
              }
            }
            setCancel={() =>setApproveShow(!approveShow) }
            title="Approve" 
            requestLoading={requestLoading}
          />
          }

        {
          <PromptModal 
            show={declineShow} 
            setShow={() =>  handleRequest('reject')} 
            title="Decline" 
            setCancel={() =>setDeclineShow(!declineShow) }
            requestLoading={requestLoading}
          />
        }
        {<SuccessModal 
          show={showSuccessModal} 
          setShow={() => {
            setShowSuccessModal(!showSuccessModal)
            setResetChecked(true)
            setCheckedValues([])
            history.push('/posted-adverts');
          }}
        />}
      </InnerLayout>
    </Layout>
  );
}

export default PostedRequest;
