import React, { ChangeEvent, useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import Axios from 'axios';
import { Modal, ModalHeader } from 'reactstrap';
import { connect } from 'react-redux';
import { hasAnyAuthority } from 'app/shared/util/hasAuthorities';
import { AUTHORITIES } from 'app/config/constants';

import './helperUtils.scss';
import { IRootState } from 'app/shared/reducers';

interface DropDownType {
  handleChange: (e: ChangeEvent<HTMLSelectElement>, isMsg: boolean) => void;
}
interface MessageModalType {
  isOpen: boolean;
  content: string;
  title: string;
  handleToggle: () => void;
  handleInputChange: (e: ChangeEvent<HTMLSelectElement | HTMLInputElement | HTMLTextAreaElement>) => void;
  sendMsg: () => void;
}

export const useSchemes = () => {
  const adminNumber = useSelector((state: IRootState) => state.authentication.account.login);
  const [schemes, setSchemes] = useState([]);
  const [isLoading, setIsloading] = useState(true);

  const getSchemes = async () => {
    try {
      const theSchemes = await Axios.get(`/api/schemes/admin/${adminNumber}`);
      setSchemes(theSchemes.data.data);
      setIsloading(false);
    } catch (e) {
      console.error(e);
    }
  };

  useEffect(() => {
    getSchemes();
  }, []);

  return { isLoading, schemes };
};

export function DropDown({ handleChange }: DropDownType) {
  const account = useSelector((state: IRootState) => state.authentication.account);
  const { isLoading, schemes } = useSchemes();

  const isAdmin = hasAnyAuthority(account.authorities, [AUTHORITIES.ADMIN, AUTHORITIES.ROLE_SUPER_ADMIN]);

  return (
    <div className="msg_select-box">
      <span>show</span>

      <select
        className="first-select"
        onChange={e => {
          handleChange(e, true);
        }}
      >
        <option value="">All message types</option>
        <option value="IN_APP">In-App</option>
        <option value="PUSH_NOTIFICATION">Push Notification</option>
      </select>
    {isAdmin &&
      <select
        className="second-select"
        onChange={e => {
          handleChange(e, false);
        }}
      >
        <option value="">All audience types</option>
        {!isLoading &&
          schemes.length &&
          schemes.map(scheme => (
            <option value={scheme.schemeID} key={scheme.id}>
              {scheme.scheme}
            </option>
          ))}
      </select>
    }
    </div>
  );
}

export function MessageModal({ isOpen, content, title, handleToggle, handleInputChange, sendMsg }: MessageModalType) {
  const { isLoading, schemes } = useSchemes();
  const [isDisabled, setIsDisabled] = useState(true);
  const [schemeName, setSchemeName] = useState('Systemspecs wallet');
  const account = useSelector((state: IRootState) => state.authentication.account);

  const isAdmin = hasAnyAuthority(account.authorities, [AUTHORITIES.ADMIN, AUTHORITIES.ROLE_SUPER_ADMIN]);

  useEffect(() => {
    if (content && title) {
      setIsDisabled(false);
      return;
    }
    setIsDisabled(true);
  }, [content, title]);

  return (
    <Modal isOpen={isOpen} toggle={handleToggle} centered={true}>
      <ModalHeader toggle={handleToggle}></ModalHeader>
      <div className="msg_modal-box">
        <h4>Send Message</h4>

        <select className="form-control msg_select" name="notificationType" id="" onChange={handleInputChange}>
          <option value="">All message types</option>
          <option value="IN_APP">In-App</option>
          <option value="PUSH_NOTIFICATION">Push Notification</option>
        </select>
      {isAdmin &&
        <select className="form-control msg_select" name="scheme" id="" onChange={handleInputChange}>
          <option value="">All audience types</option>
          {!isLoading &&
            schemes.length &&
            schemes.map(scheme => (
              <option value={JSON.stringify(scheme)} key={scheme.id}>
                {scheme.scheme}
              </option>
            ))}
        </select>
      }
        <div className="form-control msg_input">
          <input name="title" type="text" placeholder="Enter Message Title" value={title} onChange={handleInputChange} />
        </div>

        <textarea
          className="msg_text"
          name="content"
          id=""
          cols={20}
          rows={4}
          placeholder="Enter Message Body"
          value={content}
          onChange={handleInputChange}
        />

        <div className="msg_btn-box" id="modal_msg-btn">
          <button onClick={sendMsg} disabled={isDisabled} className={`${isDisabled ? 'button_fade' : ''}`}>
            Send New Message
          </button>
        </div>
      </div>
    </Modal>
  );
}
