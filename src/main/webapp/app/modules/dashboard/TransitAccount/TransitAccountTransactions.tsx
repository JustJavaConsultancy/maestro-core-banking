import Axios from 'axios';
import React, {useEffect, useState, useRef} from 'react';
import InnerLayout from '../components/Layout';
import Layout from '../Layout';
import {Table, Modal} from 'reactstrap';
import MoneyFormat from 'react-number-format';
import moment from 'moment';
import TransactionModal from '../Utils/ViewTransactionModal';
import { stat } from 'fs';
export default function(props) {

    const [transactions, setransactions] = useState([]);
    const [total, setTotal] = useState(0);
    const [size, setSize] = useState(20);
    const [page, setPage] = useState(0);
    const [isvisible, setIsvisble] = useState(false);
    const [currentRef, setCurrentRef] = useState('');
    const [showModal, setShowModal] = useState(false);
    const [status, setStatus] = useState("INTRANSIT")
    const [selectedTransaction, setSelectedTransaction] = useState({
        memo: '',
        transactionDate: '',
        amount: '',
        transactionRef: '',
        transactionStatus: ''
    });
    const [pof, setPof] = useState('Bank')
    const [reverseStatus, setReverseStatus] = useState('complete')
    const [search, setSearch] = useState('');

    const toPrint = useRef();

    const getTransactions: any = async (paget, sta) => {
        try {
            const theTransactions = await Axios.get(`api/account-statements/1000000088/${sta}/1999-12-12/${moment(Date()).format('YYYY-MM-DD')}?page=${paget}&size=${size}`);
            setPage(paget);
            setTotal(theTransactions.data.metadata.totalNumberOfRecords);
            setransactions(theTransactions.data.data)
            console.error(theTransactions);
        } catch(e) {
            console.error(e);
        }
    }

    // const treatTransaction = async (id, type) => {
    //     try {
    //        const response = await Axios.post(`api/journal/${type}/${id}`);
    //        console.error(response);
    //        alert(`Transaction succesfully ${type === 'hold'? 'put on hold' : 'released'}`);
    //        setIsvisble(false);
    //        getTransactions(page)
    //     }catch(e) {
    //         console.error(e.response);
    //         if(e.response) {
    //             alert(e.response.data.message)
    //         }else{
    //             alert('An error occured!')
    //         }
    //     }
    // }
    const handleClose = () => setShowModal(!showModal);

    const reverseTransaction = async (transaction) => {
        try{
            await Axios.post(`api/reverse/transit/${transaction.transactionRef}`);
            getTransactions(page, status)
           // setIsvisble(false);
            alert("Transaction reversed successfully")
        }catch(e) {
            console.error(e.response);
            if(e.response) {
                alert(e.response.data.message)
            }else{
                alert('An error occured!')
            }
        }
    }

    const changeTrasactionStatus = async (transactionRef, sta) => {
        try{
            const ro = await Axios.post(`api/journal/${transactionRef}/${sta}`);
            console.error(ro);

            getTransactions(page, status)
           // setIsvisble(false);
            alert("Transaction reversed successfully")
        }catch(e) {
            console.error(e.response);
            if(e.response) {
                alert(e.response.data.message)
            }else{
                alert('An error occured!')
            }
        
        }
    }

    // const searchTransactions: any = async (s) => {
    //     try {
    //         const theTransactions = await Axios.get(`/api/account-statement/search?key=${s}&page=${0}&size=${size}`);
    //         console.error(theTransactions);
    //         setTotal(theTransactions.data.metadata.totalNumberOfRecords);            
    //         setPage(0);
    //         setransactions(theTransactions.data.data)
    //        // for(let x = 0; x <=)
    //     } catch(e) {
    //         console.error(e);
    //     }
    // }
    const  viewTransaction = (ref) => {
        setCurrentRef(ref);
        handleClose();
    }

    useEffect(() => {
        getTransactions(0, status);
    }, []);

    return(
        <>
        <Layout>
            <InnerLayout
            setSearch = {s => {
                 setSearch(s)
                // searchTransactions(s)
            }} 
            toExport = {transactions} dontfilter = {true}  toPrint = {toPrint} nextPage = {(pageto)=>getTransactions(pageto, status)} page = {total} size = {size} title = 'Transit Account Statement' path = 'Transit Account Statement'>
                <select value = {status} onChange = {e => {
                    setStatus(e.target.value);
                    getTransactions(0, e.target.value);
                }}>
                        <option value = "START">START</option>
                        <option value = "PROCESSING">PROCESSING</option>
                        <option value = "INCOMPLETE">INCOMPLETE</option>
                        <option value = "COMPLETED">COMPLETED</option>
                        <option value = "OK">OK</option>
                        <option value = "SUSPENDED">SUSPENDED</option>
                        <option value = "INTRANSIT">INTRANSIT</option>
                        <option value = "REVERSED">REVERSED</option>
                </select>
                <div  ref = {toPrint} style = {{minWidth:'300px', minHeight:'300px', marginTop:'20px'}}>
                    
                    <Table hover striped>
                        <thead className = 'tableListData'>
                            <th>S/N</th>
                            <th>Transaction Date</th>
                            <th>Transaction Reference</th>
                            <th>Amount</th>
                            <th>Narration</th>
                            <th>Status</th>
                            <th></th>
                        </thead>
                        <tbody>
                            {
                                transactions.map((transaction, index) => 
                                    <tr className = 'tableListData' key = {index}>
                                       <td onClick = {()=>viewTransaction(transaction.transactionRef)}>{(index + 1) +  (Number(page) * size)}</td>
                                        <td onClick = {()=>viewTransaction(transaction.transactionRef)}>{moment(transaction.transactionDate).format('ll')}</td>
                                        <td onClick = {()=>viewTransaction(transaction.transactionRef)}>{transaction.transactionRef}</td>
                                        <td onClick = {()=>viewTransaction(transaction.transactionRef)} style = {{color: transaction.creditDebit === 'debit'? 'red': 'green'}}>
                                            {transaction.creditDebit === 'debit'?'-':'+'}
                                            <MoneyFormat
                                                value = {transaction.amount}
                                                displayType = 'text'
                                                thousandSeparator = {true}
                                                decimalScale = {2}
                                                fixedDecimalScale = {true}
                                                prefix = "₦"
                                            />
                                        </td>
                                        <td onClick = {()=>viewTransaction(transaction.transactionRef)}>{transaction.memo}</td>
                                        <td>
                                            <div style = {{padding: '5px 10px', borderRadius:'15px', fontSize:'12px', color:'#00A07E', backgroundColor:'rgba(0, 196, 140, 0.25)'}}>
                                            {/* <select value = {transaction.transactionStatus} onChange = {e=>changeTrasactionStatus(transaction.transactionRef, e.target.value)} >
                                                    <option value = "START">START</option>
                                                    <option value = "PROCESSING">PROCESSING</option>
                                                    <option value = "INCOMPLETE">INCOMPLETE</option>
                                                    <option value = "COMPLETED">COMPLETED</option>
                                                    <option value = "OK">OK</option>
                                                    <option value = "SUSPENDED">SUSPENDED</option>
                                                    <option value = "INTRANSIT">INTRANSIT</option>
                                                    <option value = "REVERSED">REVERSED</option>
                                                </select> */}
                                                {transaction.transactionStatus}
                                            </div>
                                        </td>
                                        <td>
                                           {transaction.transactionStatus !== "REVERSED" && transaction.transactionStatus !==  "COMPLETED" && <button onClick = {()=>{
                                                const confirm =  window.confirm("Are you sure you want to reverse this transaction?");
                                                if(confirm) {
                                                    reverseTransaction(transaction)
                                                }
                                                // setIsvisble(true)
                                            }} style = {{borderWidth: 0, fontFamily: 'poppins', fontSize: '12px', backgroundColor: '#565682', color:'#fff', borderRadius:"10px"}}>Reverse</button>}
                                        </td>
                                    </tr>
                                )
                            }
                        </tbody>
                    </Table>
                </div>
            </InnerLayout>
        </Layout>
        <Modal isOpen={showModal} toggle={handleClose} >
            <TransactionModal reference = {currentRef}/>
        </Modal>
        </>
    )
}