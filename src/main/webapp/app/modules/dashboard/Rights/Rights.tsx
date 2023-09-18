import './rights.scss';
import React, {useEffect, useState} from 'react';
import InnerLayout from '../components/Layout';
import Layout from '../Layout';
import axios from 'axios';
import { isEmpty } from 'lodash';
import {Table, Modal} from 'reactstrap';
import { validatePhone as phoneValidator } from 'app/shared/Services/Verification';

export default function Rights(props) {
    const [rights, setRights] = useState([
        {
            code: "DC",
            name: "Deactivate Customer",
            rightCode : "DC",
            maker : false

        },
        {
            code: "FC",
            name: "Flag Customer",
            rightCode : "FC",
            maker : false
        },
        {
            code: "UC",
            name: "Update Customer",
            rightCode : "UC",
            maker : false
        },
        {
            code: "BA",
            name: "Block/Unblock Account",
            rightCode : "BA",
            maker : false
        },
        {
            code: "HF",
            name: "Hold Funds",
            rightCode : "HF",
            maker : false
        },
        {
            code: "PD",
            name: "Post No Debit",
            rightCode : "PD",
            maker : false
        },
        {
            code: "RT",
            name: "Reverse Transaction",
            rightCode : "RT",
            maker : false
        }
    ]);
    const [accessRights, setAccessRights] = useState([]);
    const [user, setUser] = useState({phone:'', details: '', accessRightName: ''});
    const [loading, setLoading] = useState(false);
    const [showModal, setShowModel] = useState(false);
    const [selected, setSelected] = useState({
        accessRightName: '',
        accessItems: [],
        profileName: ''
    });
    
    const validatePhone = async (phone) => {
            const theUser = await phoneValidator(phone)
            if(theUser)
            setUser({...user, phone, details: theUser});
            else
            setUser({...user, phone, details:'User not found!'});
     }

    const getAccessRight  = async () => {
        try {
            const access = await axios.get('api/access-rights');
            setAccessRights(access.data.data);
        } catch (error) {
            console.error(error);
        }
    }

    const createAccess = async () => {
        if(isEmpty(user.details)) {
            alert("profile not found!");
            return
        }
        const data = {
            accessRightName : user.accessRightName,
            profilePhoneNumber : '+234'+user.phone.slice(user.phone.length - 10),
            accessItem : rights
        }
        console.error(data);
        try {
            setLoading(true);
            const create = await axios.post('api/access-rights', 
                data
            );
            setLoading(false);
            console.error(create);
            getAccessRight();
            alert(`Access Right for ${user.details}(${user.phone}) created successfully`)
            
        } catch (error) {
            console.error(error);
            setLoading(false);
            alert("Error submitting request!")
        }
    }

    const handleClose = () => {
        setShowModel(!showModal);
    }

    useEffect(() => {
        getAccessRight();
    }, []);

    return(
        <>
        <Layout>
            <InnerLayout dontfilter={true}  title = "Create Access Right" path = "Create Access Right">
                <div className = 'right-container'>
                    <div className = 'input-container'>
                        <div className = 'flx1'>Phone Number</div>
                        <div className = 'flx2'>
                            <input onChange = {e=> {
                                setUser({...user, phone: e.target.value, details: ''});
                                if(e.target.value.length > 10) {
                                    validatePhone(e.target.value);
                                }
                            }} value = {user.phone} className = 'phone-input' placeholder = 'Enter phonenumber' />
                        </div>
                    </div><br/>
                    <div>
                        {!isEmpty(user.details) && <p>Profile Name: <b>{user.details}</b></p>}
                    </div><br/>
                    <div className = 'input-container'>
                        <div className = 'flx1'>Access Right Name</div>
                        <div className = 'flx2'>
                            <input onChange = {e=> {setUser({...user, accessRightName: e.target.value})}} value = {user.accessRightName} className = 'phone-input' placeholder = 'Enter The Access Right Name' />
                        </div>
                    </div>
                    <br/>
                    
                    <div>
                        <table className = 'rights-table'>
                            <thead>
                                <tr>
                                    <th  className = 'makerchekerLable'></th>
                                    <th className = 'makerchekerLable'>Maker</th>
                                    <th  className = 'makerchekerLable'>Checker</th>
                                </tr>
                            </thead>
                            <tbody>
                                {rights.map((r, i) => 
                                    <tr key = {i}>
                                        <td className = 'rights-name-label'>{r.name}</td>
                                        <td className = 'radio-left-margin'>
                                            <label>
                                                <input onClick = {e=> {
                                                    const tempRight = rights;
                                                    tempRight[i].maker = true
                                                    setRights(tempRight)
                                                    console.error(tempRight);
                                                }} name = {r.code} type = 'radio'/>
                                            </label>
                                        </td>
                                        <td className = 'radio-left-margin'>
                                            <label>
                                                <input onClick = {e=> {
                                                    const tempRight = rights;
                                                    tempRight[i].maker = false
                                                    setRights(tempRight)
                                                    console.error(tempRight);
                                                }} name = {r.code} type = 'radio'/>
                                            </label>
                                        </td>
                                    </tr>
                                )}
                            </tbody>
                        </table>
                        <div className = 'button-div-send'>
                            <button disabled = {loading} onClick = {createAccess} className = 'approval-button'>{loading? 'Loading...' : 'Create Access Right'}</button>
                        </div>
                    </div><br/>
                    <div>
                        <h3>Access Rights</h3>
                        <div>
                          <Table striped>
                            <thead>
                                <tr>
                                    <th>SN</th>
                                    <th>Access Name</th>
                                    <th>Profile Name</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                {accessRights.map((acess,i)=>
                                    <tr key = {i}>
                                        <td>{i+1}</td>
                                        <td>{acess.accessRightName}</td>
                                        <td>{acess.profileName}</td>
                                        <td>
                                            <button disabled = {loading} style = {{padding: '5px', fontSize: '12px'}} onClick = {()=>{
                                                setSelected(acess)
                                                handleClose()
                                                }} className = 'approval-button'>{loading? 'Loading...' : 'View'}</button>
                                        </td>
                                    </tr>
                                )}
                            </tbody>
                           </Table>
                        </div>
                    </div>
                </div>
            </InnerLayout>
        </Layout>
        <Modal isOpen={showModal} toggle={handleClose} >
           <div style = {{padding: '10px'}}>
               <h4>{selected.accessRightName}</h4>
               <p>Profile Name: {selected.profileName}</p>
                <table className = 'rights-table'>
                    <thead>
                        <tr>
                            <th  className = 'makerchekerLable'></th>
                            <th  className = 'makerchekerLable'>Maker</th>
                            <th  className = 'makerchekerLable'>Checker</th>
                        </tr>
                    </thead>
                    <tbody>
                        {selected.accessItems.map((r, i) => 
                            <tr key = {i}>
                                <td className = 'rights-name-label'>{r.rightName}</td>
                                <td className = 'radio-left-margin'>
                                    <label>
                                        <input onClick = {e=> {
                                            const tempRight = rights;
                                            tempRight[i].maker = true
                                            setRights(tempRight)
                                            console.error(tempRight);
                                        }} checked = {r.maker} name = {r.code} type = 'radio'/>
                                    </label>
                                </td>
                                <td className = 'radio-left-margin'>
                                    <label>
                                        <input checked = {!r.maker} name = {r.code} type = 'radio'/>
                                    </label>
                                </td>
                            </tr>
                        )}
                    </tbody>
                </table>
           </div>
        </Modal>
        </>
    )
}