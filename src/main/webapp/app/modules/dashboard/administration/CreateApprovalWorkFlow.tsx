import './approvalgroup.scss';
import React, {useEffect, useState} from 'react';
import Layout from '../Layout';
import InnerLayout from '../components/Layout';
import Axios from 'axios';
import { isEmpty } from 'lodash';

export default function CreateWorkFlow(props) {

    const [groups, setGroups] = useState([]);
    const [loading, setLoading] = useState(false);
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
    const [data, setData] = useState({
        name: "",
        initiatorId: "",
        approverId: "",
        transactionTypeCode: "HF"
    });

    const getGroups = async () => {
        try {
            const theGroup = await Axios.get("/api/approval-groups");
            console.error(theGroup);
            setGroups(theGroup.data.data);
            
        } catch (error) {
            console.error(error.response);
        }
    }

    useEffect(() => {
        getGroups();
    }, []);

    const createWorkFlow = async () => {
        if(isEmpty(data.name) || isEmpty(data.approverId) || isEmpty(data.initiatorId) || isEmpty(data.transactionTypeCode)) {
            alert("All fields are compulsary!");
            return
        }
        try {
            setLoading(true)
            const theWorkFlow = await Axios.post('/api/approval-workflows', data)
            console.error(theWorkFlow);
            alert("Workflow created successfully")
            props.history.push("/approval-workflow")
        } catch (error) {
            if(error.response) {
                alert(error.response.data.message)
            }else{ 
                alert("Error creating workflow")
            }
        }finally {
            setLoading(false);
        }
    }

    
    return(
        <Layout>
            <InnerLayout dontfilter = {true} show = {true} title = 'Create Approval Workflow' path = 'create-approval-workflow'>
                <div className = "approval-gorup-selector">
                <label style = {{width: "900px", display: "flex", alignItems: "center"}}>
                            <span style = {{flex: 0.2}} className = "sp">Transaction Type:</span>
                            <div style = {{flex: 0.8}} >
                                <input value = {data.name} onChange = {e => setData({...data, name: e.target.value})} placeholder = "Enter the workflow Name" className = "work-flow-select"/>
                            </div>
                    </label>
                    <hr/>
                    <label style = {{width: "900px", display: "flex", alignItems: "center"}}>
                            <span style = {{flex: 0.2}} className = "sp">Transaction Type:</span>
                            <div style = {{flex: 0.8}} >
                                <select value = {data.transactionTypeCode} onChange = {e=>setData({...data, transactionTypeCode: e.target.value})} className = "work-flow-select">
                                    {rights.map((r, i)=>
                                        <option key = {i} value = {r.code}>{r.name}</option>
                                    )}
                                </select>
                            </div>
                    </label>
                    <hr/>
                    <label style = {{width: "900px", display: "flex", alignItems: "center"}}>
                            <span style = {{flex: 0.2}} className = "sp">Transaction Initiator:</span>
                            <div  style = {{flex: 0.8}} >
                            <select value = {data.initiatorId} onChange = {e=>setData({...data, initiatorId: e.target.value})} className = "work-flow-select">
                                <option value = "">Select Transaction Initiator</option>
                                {groups.map((g, i) => 
                                    <option value = {g.id} key = {i}>{g.name}</option>
                                )}
                            </select>
                            </div>
                    </label>
                    <hr/>
                    <label style = {{width: "900px", display: "flex", alignItems: "center"}}>
                            <span style = {{flex: 0.2}} className = "sp">Transaction Approver:</span>
                            <div  style = {{flex: 0.8}} >
                            <select value = {data.approverId}  onChange = {e=>setData({...data, approverId: e.target.value})} className = "work-flow-select">
                            <option value = "">Select Transaction Approver</option>
                                {groups.map((g, i) => 
                                    <option value = {g.id} key = {i}>{g.name}</option>
                                )}
                            </select>
                            </div>
                    </label>
                    <br/>
                    <label style = {{width: "900px", display: "flex", alignItems: "center"}}>
                            <span style = {{flex: 0.2}} className = "sp">Authentication Type:</span>
                            <div style = {{flex: 0.8}} >
                                <select className = "work-flow-select">
                                    <option>One Time Password</option>
                                    <option>Dynamic PIN</option>
                                    <option>Password</option>
                                </select>
                            </div>
                    </label>
                    <br/>
                    <div style = {{display: "flex", justifyContent: "center", alignItems: "center"}}>
                            <button onClick = {createWorkFlow} disabled = {loading} className = 'viewreport'>{loading? "Loading..." : "Create Workflow"}</button>
                    </div>
                </div>
            </InnerLayout>
        </Layout>
    )
}