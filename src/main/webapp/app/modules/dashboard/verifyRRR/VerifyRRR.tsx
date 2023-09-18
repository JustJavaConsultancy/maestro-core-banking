import React, {useState, useEffect} from 'react';
import Layout from '../Layout';
import InnerLayout from '../components/Layout';
import Axios from 'axios';
import { isEmpty } from 'lodash';

export default function(props) {
    const [rrr, setRRR] = useState('');
    const [valid, setValid] = useState('');
    const [verifying, setVerifying] = useState(false);
    const [details, setDetails] = useState({});
    const verifyRRR = async (r=rrr) => {
        try {
            const getRRR = await Axios.get(`api/rrr/details/${r}`);
            setVerifying(false)
            
            if(getRRR.data.responseMsg.toLowerCase() === "RRR details found".toLocaleLowerCase()) {
                if(!isEmpty(getRRR.data.responseData[0].rrr)){
                    setValid(`${r} is valid with payment status: Pending`)
                    setDetails(getRRR.data.responseData[0])
                }else {
                    setValid(`${r} is valid with payment status: Completed`)
                }
            }else {
                setValid(`${r} ${getRRR.data.responseMsg}`)
            }
            console.error(getRRR);
        } catch(e) {
            console.error(e);   
        }finally{
            setVerifying(false)
        }
    }

    return(
        <Layout>
            <InnerLayout dontfilter={true} show = {true}  title = 'Verify RRR' path = 'Verify RRR'>
                <div style = {{height: '200px'}}>
                    <input disabled = {verifying} value = {rrr} onChange = {e=>{
                        if(e.target.value.length > 11) {
                            setVerifying(true)
                            verifyRRR(e.target.value);
                        }
                        setValid(null);
                        setRRR(e.target.value)}} type = 'number' placeholder = 'Enter RRR'/> {verifying && <span>verifying ...</span>}
                    {valid && <p style = {{marginTop:'40px'}}>
                            {valid}
                        </p>}
                </div>
            </InnerLayout>
        </Layout>
    )
}