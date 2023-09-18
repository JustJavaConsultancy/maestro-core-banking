import React, { useEffect, useState } from 'react';
import Select from 'react-select';
import Axios from 'axios';
import Spinner from '../../../../component/spinner/Spinner';

const UpgradeKYC= ({ user }) => {
    const [selectedOption, setSelectedOption] = useState(null);
    const [kycData, setKycData] = useState([]);
    const [show, setShow] = useState(false);
    const [loading, setLoading] = useState(false);

    const { user: {firstName, lastName}, phoneNumber } = user;
    
    const getKYCLevels = async (): Promise<void>=> {
        try {
          const res = await Axios.get(`/api/kyclevels`);
          const data = res.data.map(item => {
            return {
              value: item.kyc,
              label: item.description
            }
          })
          setKycData(data)
        } catch (err) {
          console.error(err.response);
        }
      }

    const handleUpgrade = async ():Promise<void> => {
        try {
            setLoading(true)
            const res = await Axios.post(`/api/kyc-requests/${phoneNumber}/${selectedOption?.value}`)
            if(res.data.code === "00") alert(`User KYC successfully upgraded to ${selectedOption?.value}`)
         } catch(err) {
            console.error(err);
            alert('An error occurred while upgrading customer, try again');
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        getKYCLevels()
    }, [])

    return ( 
        <div className="container__reactivate">
            <Select
                defaultValue={selectedOption}
                onChange={setSelectedOption}
                options={kycData}
                placeholder="Choose level"
              />
            {!show && 
            <button 
                type="button" 
                className="kyc__btn" 
                onClick={(e: React.MouseEvent<HTMLButtonElement>) => setShow(true)}>Upgrade KYC Level
            </button>
            }
            {show && (
                !selectedOption ? 
                <div><p>Please kindly select a KYC Level to upgrade {firstName} {lastName} to</p></div>
                :
                <div>
                  <p>Are you sure you want to upgrade {firstName} {lastName} to Level {selectedOption?.value}</p>
                  <div>
                      <button onClick={() => setShow(false)}>Cancel</button>
                      <button onClick={handleUpgrade}>{loading ? <Spinner /> : 'Upgrade'}</button>
                  </div>
                </div>
            )}
        </div>
     );
}
 
export default UpgradeKYC;