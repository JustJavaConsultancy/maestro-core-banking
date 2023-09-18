import './createRequest.scss';
import React, {useState, useEffect} from 'react';
import InnerLayout from '../components/Layout';
import Layout from '../Layout';
import axios from 'axios';
import { isEmpty } from 'lodash';
import { validatePhone as phoneValidator } from 'app/shared/Services/Verification';
import {connect} from 'react-redux';
type ICreateRequest = StateProps;

function CreateRequest(props: ICreateRequest) {
    const [inputField, setInputField] = useState(
        {
            label: 'Wallet Number:',
            placeholder: 'Enter The Wallet Number',
            type: 'wallet',
            code: 'PD'
        }
    );
    const [requestRef, setRequestRef] = useState('');
    const [reason, setReason] = useState({reason: '', comment: ''});
    const [name, setName] = useState({name:'', empty:true});
    const [loading, setLoading] = useState(false);
    const [transactionDetails, setTransactionDetails] = useState({journal:{
        externalRef:'',
        memo: '',
        transDate: '',
        transactionStatus: ''
    }});
    const [pof, setPof] = useState('wallet');
    const [file, setFile] = useState(null);
    const [filetype, setFileType] = useState("");
    const typeSelected = (type) => {
        setName({
            name: '',
            empty: true
        });
        setFile(null);
        setRequestRef('');
        setTransactionDetails({journal:{
            externalRef:'',
            memo: '',
            transDate: '',
            transactionStatus: ''
        }});
        switch(type) {
            case "PD" :
            case "BA":
                setInputField({
                    label: 'Wallet Number:',
                    placeholder: 'Enter The Wallet Number',
                    type: 'wallet',
                    code: type
                });
            break;
            case "DC":
            case "FC":
            case "UC":
                setInputField({
                    label: 'Phone Number:',
                    placeholder: 'Enter The Customers Phone Number',
                    type: 'customer',
                    code: type
                });
            break;
            case "RT":
            case "HF" :
                setInputField({
                    label: 'Transaction Reference:',
                    placeholder: 'Enter Transaction Reference',
                    type: 'transaction',
                    code: type
                });
            break;
            default :
                setInputField({
                    label: 'Transaction Reference:',
                    placeholder: 'Enter Transaction Reference',
                    type: 'transaction',
                    code: type
                });
        }
    }
    const [rList, setRList] = useState([]);

    const Clear = () => {
        setName({
            name: '',
            empty: true
        });
        setRequestRef('');
        setReason({reason:'', comment: ''});
        setName({name: '', empty: true});
        setFile(null);
        setTransactionDetails({journal:{
            externalRef:'',
            memo: '',
            transDate: '',
            transactionStatus: ''
        }});
    }
    const createRequest = async () => {
        if(!requestRef && name.empty) {
            alert("Invalid request!");
            return
        }
        // file.split(',')[file.split(',').length-1]
        const data = {
            requestRef,
            requestTypeCode : inputField.code,
            comment : reason.comment,
            supportingDoc : file? file : null,
            pointOfFailure: pof,
            docType: file ? (filetype.toLowerCase() === "pdf" ? "PDF" : "JPEG") : null
        }
        console.error(data);
        try {
            setLoading(true);
            const sendRequest = await axios.post('api/approval-requests', data);
            console.error(sendRequest);
            alert('request sent successfully for approval!');
            Clear();
            setLoading(false);
        } catch (error) {
            setLoading(false);
           // console.error(error);
            if(error.response){
                alert(error.response.data.message); 
                return
            }
            alert('Error creating request please try again!'); 
        }
    }


    const getTransaction = async (reference) => {
        try {
            const journal = await axios.get(`/api/daybook/${reference}`);
           return journal.data
        }catch(e) {
            console.error(e);
            return false
        }
    }

    const getAccessRightForCurrentUser = async () => {
        try {
            // const rights = await axios.get(`/api/access-rights/items/${props.account.login}`);
            const rights = await axios.get(`/api/rights/${props.account.login}`);
            setRList(rights.data.data);
            typeSelected(rights.data.data[0].code)
        } catch (error) {
            console.error(error);
        }
    }
    const verifyAccount = async (account) => {
        switch(inputField.type) {
            case 'wallet' : 
                try{
                  const theUser = await axios.get(`api/wallet-accounts/avs/${account}`);
                  setName({name: theUser.data, empty:false})
                 // setAdetails({accountName: theUser.data, e:true});
                }catch(error) {
                    console.error(error);
                    setName({name: `User not found`, empty:true})
                }
              break;
            case "customer" :
                try {
                    const theUser = await phoneValidator(account)
                    if(theUser)
                    setName({name: theUser, empty:false})
                    else
                    setName({name: `User not found`, empty:true})
                } catch (error) {
                    console.error(error);
                }
              break;
            case "transaction": 
               try {
                const getTransactionDetails = await getTransaction(account);
                setTransactionDetails(getTransactionDetails);
               } catch (error) {
                   console.error(error); 
               }
               break;
             default : 
             setName({name: `User not found`, empty:true});
             break;
        }
    }

    const toBase64 = theFile => new Promise((resolve, reject) => {
       
       // console.error(theFile, "i am the type", theFile.type.split('/')[1]);
        
        const reader = new FileReader();
        reader.readAsDataURL(theFile);
        reader.onload = () => {
           // console.error(reader.result);
            setFileType(theFile.type.split('/')[1])
            let result = String(reader.result);
            result = result.split(',')[1]
           // console.error(result);
            resolve(result)};
        reader.onerror = error =>{
            console.error(error);
             reject(error)};
    });

    useEffect(() => {
        getAccessRightForCurrentUser()
    },[]);

    return (
        <>
        <Layout>
            <InnerLayout dontfilter={true}  title = "Create Request" path = "Create Request">
                <div className = 'right-container'>
                    <table className = "approval-entry">
                        <tbody>
                            <tr>
                                <td className = "left-label">
                                    <label htmlFor = "Select-request-type">Request Type: </label>
                                </td>
                                <td>
                                    <select onChange = {(e) => typeSelected(e.target.value)} className = "regular-input"  name = "Select-request-type">
                                        {rList.map((r,i)=>
                                            <option key = {i} value = {r.code}>{r.name}</option>
                                        )}
                                        {/* <option value = "PD">Post No Debit</option>
                                        <option value = "DC">Deactivate Customer</option>
                                        <option value = "FC">Flag Customer</option>
                                        <option value = "UC">Update Customer Information</option>
                                        <option value = "BA">Block/Unblock Account</option>
                                        <option value = "HF">Hold Funds</option>
                                        <option value = "RT">Reverse Transaction</option> */}
                                    </select>
                                </td>
                            </tr>
                            <tr className = "left-label">
                                <td>{inputField.label} </td>
                                <td>
                                    <input value = {requestRef} onChange = {e => {
                                        setRequestRef(e.target.value);
                                        if(e.target.value.length > 9) {
                                            verifyAccount(e.target.value)
                                        }
                                    }} className = "regular-input" placeholder = {inputField.placeholder}/>
                                </td>
                            </tr>
                           {!isEmpty(name.name) && <tr>
                                <td className = "left-label">Details: </td>
                                <td>
                                    <h3 style = {{fontFamily:'poppins-regular', color: name.empty ? 'red' : '#435faa'}}>{name.name}</h3>
                                </td>
                            </tr>}
                            {!isEmpty(transactionDetails.journal.memo) && 
                            <>
                            <tr>
                                <td className = "left-label">Details: </td>
                                <td>
                                    <span style = {{fontFamily:'poppins-regular', color: '#435faa'}}>{transactionDetails.journal.memo}</span>
                                </td>
                              </tr>
                              <tr>
                                <td className = "left-label">RRR: </td>
                                <td>
                                    <span style = {{fontFamily:'poppins-regular', color: '#435faa'}}>{transactionDetails.journal.externalRef}</span>
                                </td>
                              </tr>
                              <tr>
                                <td className = "left-label">Transaction Date: </td>
                                <td>
                                    <span style = {{fontFamily:'poppins-regular', color: '#435faa'}}>{transactionDetails.journal.transDate}</span>
                                </td>
                              </tr>
                              <tr>
                                <td className = "left-label">Transaction Status: </td>
                                <td>
                                    <span style = {{fontFamily:'poppins-regular', color: '#435faa'}}>{transactionDetails.journal.transactionStatus}</span>
                                </td>
                              </tr>
                            {inputField.code === "RT" &&<tr>
                            <td className = "left-label">Point Of Failure: </td>
                            <td>
                                <label style = {{marginRight: '20px'}}>
                                    Bank
                                    <input style = {{margin:'10px'}} name = "pof" onClick = {e=> setPof('bank')} type = 'radio'/>
                                </label>
                                <label>
                                    Wallet
                                    <input style = {{margin:'10px'}}  name = "pof" onClick = {e=> setPof('wallet')}  type = 'radio'/>
                                </label>
                            </td>
                            </tr>}
                            </>
                            }
                            <tr>
                                <td className = "left-label">
                                    <label htmlFor = "Select-request-reason">Reason: </label>
                                </td>
                                <td>
                                   <select onChange = {e => setReason({...reason,reason:e.target.value})} className = "regular-input"  name = "Select-request-reason">
                                        <option value = "Fraud">Fraud</option>
                                        <option value = "Invalid customer">Invalid customer</option>
                                        <option value = "Inactive">Inactive</option>
                                        <option value = "Instruction from management">Instruction from management</option>
                                        <option value = "Suspect">Suspect</option>
                                        <option value = "Denial of wallet service">Denial of wallet service</option>
                                        <option value = "Others">Others</option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td className = "left-label">
                                    <label htmlFor = "Additional-Comments">Additional Comments: </label>
                                </td>
                                <td>
                                    <input value = {reason.comment} onChange = {e => setReason({...reason,comment: e.target.value})}  className = "regular-input multi"  name = "Additional-Comments" type = "textarea" placeholder = "Add Comments" />
                                </td>
                            </tr>
                            <tr>
                                <td className = "left-label">
                                    <label htmlFor = "docs">Supporting Document: </label>
                                </td>
                                <td>
                                    <input accept = '.pdf,.jpg' onChange = {async e => {
                                        const thefile = await toBase64(e.target.files[0])
                                        setFile(thefile)
                                        }} name = "docs" type = "file" placeholder = "supporting Document" />
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <div className = 'button-div-send'>
                            <button disabled = {loading} onClick = {createRequest} className = 'approval-button'>{loading? "Loading..." : "Send for Approval"}</button>
                    </div>
                </div>
            </InnerLayout>
        </Layout>
        </>
    )
}

const mapStateToProps = storeState => ({
    account: storeState.authentication.account,
    isAuthenticated: storeState.authentication.isAuthenticated,
  });
type StateProps = ReturnType<typeof mapStateToProps>;
export default connect(mapStateToProps)(CreateRequest);