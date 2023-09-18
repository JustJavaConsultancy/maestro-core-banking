import React, { useState } from 'react';
import axios from 'axios';
import { v4 as uuidv4 } from 'uuid';
import generateUserPassword from 'app/modules/utility/utility';

import './changeUserPass.scss';

function ChangeUserPassword({ userPhone, userName }) {
  const { arr, generatePassword } = generateUserPassword;

  // const [newPassword, setNewPassword] = useState(uuidv4());
  const [newPassword, setNewPassword] = useState(generatePassword(arr));
  const [showPassword, setShowPassword] = useState(false);
  const [showMsg, setShowMsg] = useState('');
  const [showConfirm, setShowConfirm] = useState(false);

  const changePassword = async (phone: string): Promise<void> => {
    const payload = {
      phoneNumber: phone,
      newPassword,
    };
    try {
      const response = await axios.post(`/api/admin/password/change`, payload);
      // const response = await axios.post(`https://walletdemo.remita.net/api/admin/password/change`, payload);

      if (response.data.code === '00') {
        setShowMsg('');
        setShowConfirm(false);
        setShowPassword(true);
        return;
      }

      // eslint-disable-next-line no-console
      console.log(response);
    } catch (error) {
      setShowPassword(false);
      setShowConfirm(false);
      setShowMsg('Password change unsuccessful!!');
      console.error(error);
    }
  };

  return (
    <div>
      {!showConfirm && (
        <div>
          <button
            className="pass-btn"
            onClick={() => {
              // changePassword(userPhone);
              // setNewPassword('');
              setShowPassword(false);
              setShowConfirm(true);
            }}
          >
            Change Password
          </button>
        </div>
      )}

      {showConfirm && (
        <div>
          <div className="msg-confirm">
            <p>
              Are you sure you want to change <br /> {userName}&#39;s password?
            </p>
          </div>
          <button onClick={() => changePassword(userPhone)} className="pass-btn btn-continue">
            Continue
          </button>
          <button onClick={() => setShowConfirm(false)} className="pass-btn btn-cancel">
            Cancel
          </button>
        </div>
      )}
      {showPassword && (
        <div className="show-pass">
          <p className="mb-0">-
            New password: <strong>{newPassword}</strong>
          </p>
        </div>
      )}
      {showMsg && (
        <p className="mb-0" style={{ color: '#f00' }}>
          <strong>{showMsg}</strong>
        </p>
      )}
    </div>
  );
}

export default ChangeUserPassword;
