import React,{useEffect, useState} from 'react';
import Layout from '../Layout';
import InnerLayout from '../components/Layout';
import {Table} from 'react-bootstrap';
import Axios from 'axios';
import { group } from 'console';
import { codeTypes } from '../Requests/TypeAndCodes';

export default function(props) {

    const [workFlow, setWorkFlow] = useState([]);
    const [loading, setLoading] = useState(false);

    const getGroups = async () => {
        try {
            const theGroup = await Axios.get("/api/approval-workflows");
            console.error(theGroup);
            setWorkFlow(theGroup.data.data);
        } catch (error) {
            console.error(error.response);
        }
    }

    const action = (a, selectedGroup) => {
        switch(a) {
            case 'edit' :
                props.history.push(`/workflow/edit/${selectedGroup.id}`);
                break;
            case 'delete' : 
             if(confirm("are you sure you want to delete " + selectedGroup.name + "?")) {
                setLoading(true);
                Axios.delete(`/api/approval-workflows/${selectedGroup.id}`)
                .then(res=> {
                    getGroups();
                    alert("Workflow deleted successfully!");
                }).catch(error=>{
                    if(error.response) {
                        alert(error.response.data.message)
                    }else {
                        alert("network error")
                    }
                }).finally(()=>{
                    setLoading(false)
                })
             }
             break;
            default :
            break
        }
    }

    useEffect(() => {
        getGroups();
    }, []);

    return(
        <Layout>
            <InnerLayout dontfilter = {true} show = {true} title = 'Approval Workflow' path = 'approval-workflow'>
                <div className = "approval-group-header">
                    <input style = {{padding: "10px", fontSize: "12px", border: "1px solid #435FAA",  borderRadius: "10px"}} placeholder = "Search"/>
                    <button onClick = {()=>props.history.push("/create-approval-workflow")} className = "create-approval-button">Create Approval Workflow</button>
                </div>
                <div className = "table-div">
                    <Table className = "transaction-table"  hover responsive>
                        <thead>
                            <tr>
                                <th>S/N</th>
                                <th>Workflow Name</th>
                                <th>Transaction Type</th>
                                <th>Initiator</th>
                                <th>Approver</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {workFlow.map((theWorkFlow, i)=> 
                                <tr key = {i}>
                                    <td>{i+1}</td>
                                    <td>{theWorkFlow.name}</td>
                                    <td>{theWorkFlow.transactionTypeName}</td>
                                    <td>{theWorkFlow.initiatorName}</td>
                                    <td>{theWorkFlow.approverName}</td>
                                    <td>
                                       <select onChange = {e => action(e.target.value, theWorkFlow)} value = "none">
                                            <option value = "none">None</option>
                                            <option value = "edit">Edit</option>
                                            <option value = "delete">Delete</option>
                                       </select>
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </Table>
                </div>
            </InnerLayout>
        </Layout>
    )
}