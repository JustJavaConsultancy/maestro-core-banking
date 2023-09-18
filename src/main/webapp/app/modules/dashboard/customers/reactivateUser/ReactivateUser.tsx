/* eslint-disable no-console */
import React, { useState, useEffect } from 'react';
import Axios from 'axios';

import './ReactivateUser.scss';

const ReactivatePhone = ({ phone, activated }): JSX.Element => {
  const [show, setShow] = useState<boolean>(false);

  const handleReactivation = async status => {
    try {
      const res = await Axios.post(`/api/manage-user-statues/${phone}/${status}`);
      if (res.data.code === '00') {
        alert(`Customer was ${status === 'ACTIVATE' ? 'reactivated' : 'deactivated'} successfully`);
        setShow(false);
      }
    } catch (error) {
      console.error(error);
      alert(`An error occurred while ${status === 'ACTIVATE' ? 'reactivating' : 'deactivating'} account, try again`);
    }
  };

  return (
    <div className="container__reactivate">
      <div>
        {!show && (
          <button onClick={(e: React.MouseEvent<HTMLButtonElement>) => setShow(true)}>
            {activated ? 'Deactivate' : 'Reactivate'} Account
          </button>
        )}
      </div>
      {show && (
        <div>
          <p>Are you sure you want to {activated ? 'Deactivate' : 'Reactivate'} this account</p>
          <div>
            <button onClick={() => setShow(false)}>Cancel</button>
            <button onClick={() => handleReactivation(activated ? 'DEACTIVATE' : 'ACTIVATE')}>
              {activated ? 'Deactivate' : 'Reactivate'}
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default ReactivatePhone;
