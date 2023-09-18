import React, { ChangeEvent, useEffect, useState } from 'react';
import { useHistory } from 'react-router';
import { Table } from 'react-bootstrap';
import moment from 'moment';
import { useSelector } from 'react-redux';
import Axios from 'axios';

import Layout from '../Layout';
import InnerLayout from '../components/Layout';
import { DropDown, MessageModal } from './helper/HelperUtils';
import { useSchemes } from './helper/HelperUtils';
import { IRootState } from 'app/shared/reducers';
import { hasAnyAuthority } from 'app/shared/util/hasAuthorities';
import { AUTHORITIES } from 'app/config/constants';

import './messaging.scss';

export default function () {
  const [messages, setMessages] = useState([]);
  const [isOpen, setIsOpen] = useState(false);
  const [showTypes, setShowTypes] = useState({ scheme: '', notificationType: '' });
  const [sendTypes, setSendTypes] = useState({
    scheme: '',
    content: '',
    title: '',
    display: true,
    notificationType: '',
    audience: 'Systemspecs wallet',
  });
  const account = useSelector((state: IRootState) => state.authentication.account);
  const { isLoading, schemes } = useSchemes();

  const isAdmin = hasAnyAuthority(account.authorities, [AUTHORITIES.ADMIN, AUTHORITIES.ROLE_SUPER_ADMIN]);

  const isMcPhersonAdmin = hasAnyAuthority(account.authorities, [AUTHORITIES.MCPHERSON, AUTHORITIES.MCPHERSON_FINANCIAL]);
  const isFuoye = hasAnyAuthority(account.authorities, [AUTHORITIES.FUOYE_FINANCIAL, AUTHORITIES.FUOYE_ADMIN]);
  const isSch = isMcPhersonAdmin || isFuoye;

  const isPaymasta = hasAnyAuthority(account.authorities, [AUTHORITIES.PAYMASTA_ADMIN]);
  const isWragby = hasAnyAuthority(account.authorities, [AUTHORITIES.WRAGBY_ADMIN]);
  const isWynk = hasAnyAuthority(account.authorities, [AUTHORITIES.WYNK_ADMIN]);

  const { notificationType, scheme } = showTypes;

  const handleDropDownChange = (e: ChangeEvent<HTMLSelectElement>, isMsg: boolean) => {
    const { value } = e.target;
    setShowTypes(prevData => {
      if (isMsg) {
        return { ...prevData, notificationType: value };
      }
      return { ...prevData, scheme: value };
    });
  };

  const handleInputChange = (e: ChangeEvent<HTMLSelectElement | HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setSendTypes(prevData => {
      if (name === 'scheme') {
        const parsedData = JSON.parse(value);
        return { ...prevData, [name]: parsedData?.schemeID, audience: parsedData.scheme };
      }
      return { ...prevData, [name]: value };
    });
  };

  const handleClick = (e: ChangeEvent<HTMLFormElement>) => {
    e.preventDefault();
  };

  const getMessages = async () => {
    let uri: string;

    setShowTypes(prevState => ({...prevState, scheme: schemes[0]?.schemeID}))

    // if(isMcPhersonAdmin) {
    //   setShowTypes(prevState => ({...prevState, scheme: '4d6350686572736f6e'}))
    // }
    // if(isPaymasta) {
    //   setShowTypes(prevState => ({...prevState, scheme: '5061794d61737461'}))
    // }
    // if(isWragby) {
    //   setShowTypes(prevState => ({...prevState, scheme: '577261676279'}))
    // }

    if (!notificationType && scheme) {
      uri = `/api/app-notifications/scheme/${scheme}`;
    }

    if (!notificationType && !scheme) {
      uri = '/api/app-notifications';
    }

    if (notificationType && !scheme) {
      uri = `/api/app-notifications/type/${notificationType}`;
    }

    if (notificationType && scheme) {
      uri = `/api/app-notifications/type-scheme/${notificationType}/${scheme}`;
    }

    try {
      const response = await Axios.get(uri);
      setMessages(response.data);
    } catch (error) {
      // eslint-disable-next-line no-console
      console.log(error);
    }
  };

  const sendMsg = async () => {
    sendTypes.audience = sendTypes.audience === 'Systemspecs wallet' ? 'POUCHII' : sendTypes.audience;

    try {
      const response = await Axios.post('/api/app-notifications/send', sendTypes);
      // eslint-disable-next-line no-console
      console.log(response);
      if (response.status === 200 || response.status === 201) {
        alert('Message Sent Successfully');
        setIsOpen(false);
        getMessages();
      }
    } catch (error) {
      // eslint-disable-next-line no-console
      console.log(error);
    }
  };

  const changeTypes = () => {
    setSendTypes(prevState => (
        {
          ...prevState, 
          scheme: schemes[0]?.schemeID,
          audience: schemes[0].scheme
        }
      )
    )
    setShowTypes(prevState => ({...prevState, scheme: schemes[0]?.schemeID}))
  }

  useEffect(() => {
    if(isSch || isPaymasta || isWragby || isWynk){
      if(notificationType || scheme) {
        getMessages();
      }
    } else {
      getMessages();
    }
  }, [notificationType, scheme])

  useEffect(() => {
    if(!isLoading) {
      changeTypes();
    }
  }, [isLoading])

  return (
    <>
      <Layout>
        <InnerLayout dontfilter={true} show={true} title="Messaging" path="Messaging">
          <form className="msg_header-box" onSubmit={handleClick}>
            <div className="msg_input-box">
              <input type="text" placeholder="Search for a message here" />
            </div>

            <DropDown handleChange={handleDropDownChange} />

            <div className="msg_btn-box">
              <button onClick={() => setIsOpen(true)}>Send New Message</button>
            </div>
          </form>

          <Table className="transaction-table" hover responsive>
            <thead>
              <tr>
                <th>Audience</th>
                <th colSpan={2}>Message</th>
                <th>Type</th>
                <th>Date</th>
              </tr>
            </thead>
            <tbody>
              {!messages.length ? (
                <tr>
                  <td colSpan={5}>
                    <div className="no_data-box">
                      <h2>No Data</h2>
                    </div>
                  </td>
                </tr>
              ) : (
                messages.map(message => (
                  <tr key={message.id}>
                    <td>{message.audience}</td>
                    <td colSpan={2}>{message.content}</td>
                    <td>{message.notificationType === 'IN_APP' ? 'In-App' : 'Push Notification'}</td>
                    <td>{moment(message.createdDate).format('lll')}</td>
                  </tr>
                ))
              )}
            </tbody>
          </Table>
        </InnerLayout>
      </Layout>

      <MessageModal
        isOpen={isOpen}
        content={sendTypes.content}
        title={sendTypes.title}
        handleToggle={() => setIsOpen(false)}
        handleInputChange={handleInputChange}
        sendMsg={sendMsg}
      />
    </>
  );
}
