import React, { useState, useEffect } from 'react';
import Axios from 'axios';
import { Spinner } from 'react-bootstrap';

import Layout from '../Layout';
import InnerLayout from '../components/Layout';

import './IbileReceipts.scss';
 
const IbileReceipts = () => {
    const [refCode, setRefCode] = useState("");
    const [receiptCode, setReceiptCode] = useState("");
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const handleClick = async () => {
        if(!refCode) {
            setError("Kindly input a reference code");
            return;
        }

        try {
            setLoading(true)
            const res = await Axios.get(`https://wallet.remita.net/api/ibile/generate-receipt/${refCode}`);
            console.error(res)
            setReceiptCode(res.data.data.ReceiptNumber);
        } catch(err) {  
            setError("Transaction reference code not found!");
            console.error(err.statusCode);
        } finally {
            setLoading(false);
        }

    }

    const copyText = () => {
        navigator.clipboard.writeText(receiptCode);
    }

    return (
        <Layout>
            <InnerLayout dontfilter={true} show={true} title="Ibile" path={"Ibile Receipts"}>
                <div className="ibile-receipts-container">
                    <h2>Ibile Receipts</h2>
                    <div>
                        <input 
                            placeholder="Reference code" 
                            value={refCode}
                            onChange={e => {
                                setRefCode(e.target.value);
                                setError('');
                                setReceiptCode('');
                            }}
                        />
                        {error && <span>{error}</span>}
                        <button onClick={handleClick}>
                            {loading ? 
                                <Spinner 
                                    style={{ color: '#fff' }} 
                                    animation="border" 
                                    role="status" 
                                    size="sm"
                                /> : 
                                'Get Receipt Link'
                            }
                        </button>
                    </div>
                    {(receiptCode && refCode) && 
                        <div>
                            <h3>Receipt Link</h3>
                            <div>
                                <span>{receiptCode}</span>
                                <div>
                                    <button
                                        onClick={copyText}
                                    >Copy Link</button>
                                    <button
                                        onClick={() => window.open(receiptCode, '_blank')}
                                    >Visit Link</button>
                                </div>
                            </div>
                        </div>
                    }
                </div>
            </InnerLayout>
        </Layout>
     );
}
 
export default IbileReceipts;