import Axios from 'axios';
import moment from 'moment';
import React, {useEffect, useState} from 'react';
import InnerLayout from '../components/Layout';
import Layout from '../Layout';
import MoneyFormat from 'react-number-format';

export default function(props) {
    const [service, setService] = useState([]);
    const [loading, setLoading] = useState(true);
    const [date, setDate] = useState({
        start:moment('1993-03-30').format('YYYY-MM-DD'),
        end: moment(Date()).format('YYYY-MM-DD')
    });
    const [revenue, setRevenue] = useState({
        creditToBank: 0.0,
        debitToBank: 0.0,
        creditToWallets: 0.0,
        debitToWallets: 0.0,
        creditToTransit: 0.0,
        debitToTransit: 0.0,
        walletChargesBalance: 0.0,
        remitaChargesBalance: 0.0
    });

   const data =  [
        {
            "service": "Startimes",
            "status": "UP"
        },
        {
            "service": "DSTV",
            "status": "UP"
        },
        {
            "service": "GOTV",
            "status": "UP"
        },
        {
            "service": "JEDC",
            "status": "UP"
        },
        {
            "service": "EKDC",
            "status": "UP"
        },
        {
            "service": "IBEDC",
            "status": "UP"
        },
        {
            "service": "EEDC",
            "status": "UP"
        },
        {
            "service": "AEDC",
            "status": "UP"
        },
        {
            "service": "IKEDC",
            "status": "UP"
        },
        {
            "service": "MTN",
            "status": "UP"
        },
        {
            "service": "GLO",
            "status": "UP"
        },
        {
            "service": "9 MOBILE",
            "status": "UP"
        },
        {
            "service": "AIRTEL",
            "status": "UP"
        },
        {
            "service": "SMILE",
            "status": "UP"
        }
    ]
    const getServiceUpTime = async (): Promise<void> => {
        
        try {
            const getServiceHealth = await Axios.get('/api/health/check-external');
            console.error(getServiceHealth.data);
            setService(getServiceHealth.data.data);
            setLoading(false);
        }catch(e) {
            console.error(e);
            
        }
    }

    const getRevenuePerservice = async () => {
        try {
            const getrevenue = await Axios.get(`api/end-of-day/${date.start}/${date.end}`);
            console.error(getrevenue, "this is the revenue");
            
            setRevenue(getrevenue.data.data);
        } catch(e) {
            console.error(e);   
        }
    }
    useEffect(() => {
        setService(data);
        getServiceUpTime();
        getRevenuePerservice();
    },[]);



    return(
        <Layout>
            <InnerLayout dontfilter={true}  title = {`System Health`} path = {` System Health`}>
                <div className = "service-up" >
                    <div>
                        <label>Start Date: 
                        <input onChange = {e => setDate({...date, start:moment(e.target.value).format('YYYY-MM-DD')})} type = 'date'/>
                        </label>
                        <label>End Date: 
                        <input onChange = {e => setDate({...date, end:moment(e.target.value).format('YYYY-MM-DD')})} type = 'date'/>
                        </label><br/>
                        <button style = {{border:0, width: '100px'}} onClick = {getRevenuePerservice}>Filter</button><br/><br/>
                        {Object.keys(revenue).map((v,i)=>
                            <div style = {{margin: '0 0 20px',backgroundColor: 'gray', padding: '10px'}}  key = {i}>
                                <span style = {{fontFamily: 'poppins-regular'}}>{v}: </span>
                                <MoneyFormat
                                    value = {Object.values(revenue)[i]}
                                    displayType = 'text'
                                    thousandSeparator = {true}
                                    decimalScale = {2}
                                    fixedDecimalScale = {true}
                                    prefix = "₦"
                                />
                                {/* <span style = {{color: '#fff'}}></span> */}
                            </div>
                        )}
                    </div>
                    <div style = {{display:'flex', flexDirection:'column', alignItems:'center'}}>
                    {service.map((d,i) => 
                        <div className = 'shealth'  key = {i} style = {{backgroundColor:loading? 'blue': d['status'] === 'UP' && !loading? '#00C49E' : '#EE4E4E', width:'400px'}}>
                           {d['service']} <b style = {{marginLeft:'15px'}}>{loading && ' Loading...'}</b>
                        </div>
                    )}
                </div>
                </div>
            </InnerLayout>
        </Layout>
    )
}