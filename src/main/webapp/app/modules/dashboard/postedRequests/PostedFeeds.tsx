import React, { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import Axios from 'axios';

import PostedRequest from './PostedRequest';
import { CurrentPostType } from './PostedRequest';
import { hasAnyAuthority } from 'app/shared/util/hasAuthorities';
import { AUTHORITIES } from 'app/config/constants';
import { IRootState } from 'app/shared/reducers';
import { useSchemes } from '../messaging/helper/HelperUtils';

const PostedFeeds = () => {
    const [feeds, setFeeds] = useState([]);
    const [loading, setLoading] = useState(false);
    const [selectedSchool, setSelectedSchool] = useState({ id:0, scheme:'', name: 'All' });
    const [showDropdown, setShowDropdown] = useState(false);

    const account = useSelector((state: IRootState) => state.authentication.account);

    const isMcPherson = hasAnyAuthority(account.authorities, [AUTHORITIES.MCPHERSON, AUTHORITIES.MCPHERSON_FINANCIAL]);
    const isFuoye = hasAnyAuthority(account.authorities, [AUTHORITIES.FUOYE_FINANCIAL, AUTHORITIES.FUOYE_ADMIN]);
    const isSch = isMcPherson || isFuoye;
  
    const { isLoading, schemes } = useSchemes();

    const handleShowDropdown = () => {
      setShowDropdown(!showDropdown)
    }

    const handleSchoolSelect = (sch) => {
      setSelectedSchool(sch)
      handleShowDropdown()
    }

    const getFeeds = async () => {
      const { id, scheme } = selectedSchool
        try {
          setLoading(true)
          let res;
          if(id === 0) {
            res = await Axios.get(`/services/wallencyschools/api/admin-schools/get-feeds`);
            // res = await Axios.get(`https://wallet.remita.net/services/wallencyschools/api/get-feeds`);
          } else {
            res = await Axios.get(`/services/wallencyschools/api/get-feeds/${scheme}`);
            // res = await Axios.get(`https://wallet.remita.net/services/wallencyschools/api/get-feeds/${scheme}`);
          }

          const filterData = res.data.data.filter(item => item.status === 'NEW')
          const filterDatas = res.data.data.filter(item => item.status !== 'NEW')
          const finalData = [...filterData, ...filterDatas]
          if (res.data.code === '00') {
            setFeeds(finalData);
          }
        } catch (error) {
          if (error.response) {
            alert(error.response.data.message);
            return;
          }
          console.error(error)
          alert('Network Error!');
        } finally {
          setLoading(false)
        }
      };

    useEffect(()=> {
        getFeeds()
    }, [selectedSchool])

    useEffect(() => {
      if(!isLoading) {
        if (isSch) {
          setSelectedSchool({ id: 10, name: schemes[0].scheme, scheme: schemes[0].schemeID });
        }
      }
    }, [isLoading]);

    return ( 
        <>
            <PostedRequest 
              postData={feeds} 
              loading={loading} 
              path="Posts"
              handleSchoolSelect={handleSchoolSelect}
              selectedSchool={selectedSchool}
              showDropdown={showDropdown}
              handleShowDropdown={handleShowDropdown}
              getPosts={getFeeds}
            />
        </>
    );
}
 
export default PostedFeeds;