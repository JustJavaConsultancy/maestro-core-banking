import React, { ChangeEvent, useState } from 'react';
import axios from 'axios';

import '../changeUserPassword/changeUserPass.scss';
import './changePhoneNumber.scss';

type PhoneType = {
  phone: string;
  userName: string;
};

function ChangeUserPhoneNumber({ phone, userName }: PhoneType) {
  const [newPhone, setNewPhone] = useState('');
  const [showInput, setShowInput] = useState(false);
  const [confirm, setConfirm] = useState(false);
  const [msg, setMsg] = useState('');
  const [resMsg, setResMsg] = useState('');

  const handleProceed = () => {
    const currentNumber = newPhone.trim();

    if (currentNumber.length < 11) {
      setMsg('Invalid phone number');
      return;
    }
    const userPhone = '+234' + currentNumber.substring(currentNumber.length - 10);

    if (userPhone.length < 14) {
      setMsg('Invalid phone number');
      return;
    }

    setConfirm(true);
  };

  const changeUserPhone = async () => {
    setMsg('');
    const payload = {
      oldPhoneNumber: phone,
      newPhoneNumber: newPhone,
    };

    try {
      const response = await axios.post(`/api/admin/phonenumber/change`, payload);

      if (response.data.code === '00') {
        setResMsg("Successfully changed user's phone number");
        setConfirm(false);
        setNewPhone('');
      }
    } catch (error) {
      if (error.response) {
        setResMsg("User's phone number change failed");
        return;
      }
      alert('Network error');
    }
  };

  return (
    <div className="my-2">
      {!showInput && (
        <button
          className="pass-btn"
          onClick={() => {
            setShowInput(!showInput);
          }}
        >
          Change Phone number
        </button>
      )}

      {showInput && (
        <div className="my-2 admin_phone-change-box">
          <input
            value={newPhone}
            type="number"
            placeholder="new phone number"
            onChange={(e: ChangeEvent<HTMLInputElement>) => {
              setNewPhone(e.target.value);
              setMsg('');
            }}
          />
          <button onClick={handleProceed}>Proceed</button>
          {msg && (
            <div>
              <small style={{ color: '#f00' }}>
                <strong>{msg}</strong>
              </small>
            </div>
          )}
          {resMsg && (
            <div>
              <small style={{ color: resMsg.includes('Successfully') ? 'hsl(110, 100%, 25%)' : 'hsl(0, 100%, 35%)' }}>
                <strong>{resMsg}</strong>
              </small>
            </div>
          )}
        </div>
      )}

      {confirm && newPhone && (
        <div>
          <div className="msg-confirm">
            <p>
              Are you sure you want to change <br /> {userName}&#39;s phone number <span style={{ color: '#000' }}>({phone})</span> <br />{' '}
              to
              <span style={{ color: '#000' }}>{` ${newPhone}`}</span>
            </p>
          </div>
          <button onClick={changeUserPhone} className="pass-btn btn-continue">
            Continue
          </button>
          <button
            onClick={() => {
              setConfirm(false);
              setNewPhone('');
              setShowInput(false);
            }}
            className="pass-btn btn-cancel"
          >
            Cancel
          </button>
        </div>
      )}
    </div>
  );
}

export default ChangeUserPhoneNumber;
