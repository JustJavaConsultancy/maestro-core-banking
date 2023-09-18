import React, { ChangeEvent, useEffect, useRef, useState } from 'react';
import { Spinner } from 'react-bootstrap';
import Axios from 'axios';

import Layout from '../Layout';
import InnerLayout from '../components/Layout';

import './ChangePassword.scss'

function ChangePassword() {
    const [password, setPassword] = useState({
        currentPassword: '', 
        newPassword: '', 
        confirmPassword: '', 
    });
    const [errors, setErrors] = useState('');
    const [loading, setLoading] = useState(false);
//   const isMcPherson = hasAnyAuthority(account.authorities, [AUTHORITIES.MCPHERSON, AUTHORITIES.MCPHERSON_FINANCIAL]);

    const resetFields = () => {
        setPassword({
            currentPassword: '', 
            newPassword: '', 
            confirmPassword: '', 
        })
    }

    const pwdChange = async () => {
        const data = { currentPassword: password.currentPassword, newPassword: password.newPassword };
        try {
            setLoading(true);
            const res = await Axios.post('/api/account/changelostpassword', data);
            if(res.data.code === '00') {
                alert(res.data.message);
            }
        } catch (err) {
            alert(err)
            console.error(err)
        } finally {
            setErrors('');
            resetFields();
            setLoading(false);
        }
    }

    const handleSubmit= (e:any) => {
        e.preventDefault();
        const { currentPassword, newPassword, confirmPassword } = password;
        if(currentPassword === '' || newPassword === '' || confirmPassword === '') {
            setErrors('All fields are required')
        } else if(newPassword !== confirmPassword) {
            setErrors('Passwords do not match')
        } else if(currentPassword === newPassword) {
            setErrors('Please kindly choose a different password from the current one')
        } else {
            pwdChange()
        }
    }

  return (
    <Layout>
      <InnerLayout dontfilter={true} show={true} title="Change Password" path={"Change Password"}>
       <div className="change-password-form-container">
        <h2>Change Password</h2>
        <form onSubmit={handleSubmit}>
            <div>
                <label>Current Password</label>
                <input 
                    type="password" 
                    autoComplete="off" 
                    onChange={(e) => {
                        setErrors('')
                        setPassword({...password, currentPassword: e.target.value})
                    }} 
                    value={password.currentPassword}
                />
            </div>
            <div>
                <label>New Password</label>
                <input 
                    type="password" 
                    autoComplete="off"
                    onChange={(e) => {
                        setErrors('')
                        setPassword({...password, newPassword: e.target.value})
                    }} 
                    value={password.newPassword}
                />
            </div>
            <div>
                <label>Confirm Password</label>
                <input 
                    type="password" 
                    autoComplete="off"
                    onChange={(e) => {
                        setErrors('')
                        setPassword({...password, confirmPassword: e.target.value})
                    }} 
                    value={password.confirmPassword}
                />
            </div>
            {errors && <span>{errors}</span>}
            <div>
                <button type="submit">
                    {loading ? 
                        <Spinner style={{ color: '#fff' }} animation="border" role="status" size="sm"/> : 
                        'Submit'
                    }
                </button>
            </div>
        </form>
       </div>
      </InnerLayout>
    </Layout>
  );
}

export default ChangePassword;
