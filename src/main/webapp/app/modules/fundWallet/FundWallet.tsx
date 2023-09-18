import Axios from 'axios';
import React, {useEffect, useState} from 'react';
import RemitaPayment from "./inline";
import './Funwallet.scss';
function makeid() {
    const length = 2;
     let result           = '';
     const characters       = '0123456789';
     const charactersLength = characters.length;
     for ( let i = 0; i < length; i++ ) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
     }
     result = Date.now().toString().slice(-9) + result
     return result;// toUpperCase();
  }
export default function(props) {
    const [transId, setTransID] = useState("");
    const [paymentData, setpaymentData] = useState({
        key: "U1BFQ1NSVzF8NzA4MjA4NzUyOXxlMTZjNzJiNDc3Njk3OGM5NTlkYmRlZTc1MWQzOThmY2I1NDc4MmVmYTg2NmVhM2Q3NDBlMTZhOWZjZTJlOGE3ODgzOWQ4NzMyYTBlNWZhMzg0NDc2NTM2MmY5YTdiNzhmZThhNDVjOWNmZjE3YjZiNTA4ZGYzYTg5MjAzZjExOQ==", // enter your key here
        customerId: "",
        firstName: "",
        lastName: "",
        email: "",
        phoneNumber: "",
        amount: 100,
        narration: '',
        transactionId: transId
      });

    const [e, setE] = useState(false);
    const [funded, setFunded] = useState(false);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const t = makeid();
        setTransID(t);
       // console.error(t);
        const {name, email, phone, wallet, firstName, lastName, amount} = props.match.params;
        setpaymentData({
        ...paymentData,
        customerId: wallet,
        firstName,
        lastName,
        email,
        phoneNumber: phone,
        amount,
        narration: `Funding of Wallet ID: ${wallet} for ${firstName} ${lastName} via USSD initiated pay link`
        })
    }, []);

     const fundWallet = async () => {
         setLoading(true)
        try {
            const response = await Axios.post(`/api/fund-wallet/${transId}`);
            console.error(response);
            setFunded(true)
            setE(false)
            setLoading(false)
            const t = makeid();
            setTransID(t);
        } catch (error) {
            console.error(error);
            setE(true)
            setLoading(false);
            const t = makeid();
            setTransID(t);
        }
     }
     const onSuccess = (response) => {
        console.error("callback Successful Response", response);
        fundWallet()
      }
      const onError =  (response) => {
        // function callback when payment fails
        setE(true)
        setFunded(false)
        const t = makeid();
        setTransID(t);
        console.error("callback Error Response", response);
      }
      

      const onClose = () => {
        // function callback when payment modal is closed
        console.error("closed");
        // console.error("callback Successful Response", response);
        if(!loading)  fundWallet()
      }

      const [data, setData] = useState({
        ...paymentData,
        onSuccess,
        onError,
        onClose,
      });
      
    useEffect(() => {
        const tempPaymentData = paymentData;
        tempPaymentData.transactionId = transId
         setData({
            ...tempPaymentData,
            onSuccess,
            onError,
            onClose,
          });
    }, [transId])


    return(
        <div className  = "fund-container">
       {loading? <h4>Loading...</h4> : <>
        {!funded && !e && <>
         <p>You are about to fund your wallet with the sum of N50</p>
         <p>Please Click the button below to continue</p>
        <RemitaPayment
          remitaData={data}
          className='btn viewreport' // class to style the button
          text='Fund Wallet' // text to show on button
          // add a 'live' prop to use the live urls/keys
        />
         </>
        }
        {funded && <h1 style = {{fontFamily: 'Poppins-Regular'}}>Wallet Funded successfully</h1>}
        {e && <>
         <p>Error Funding wallet Please Try again!</p>
        <RemitaPayment
          remitaData={data}
          live = {true}
          className='btn viewreport' // class to style the button
          text='Fund Wallet' // text to show on button
          // add a 'live' prop to use the live urls/keys
        />
         </>
        }
        </>}
        </div>
    )
}