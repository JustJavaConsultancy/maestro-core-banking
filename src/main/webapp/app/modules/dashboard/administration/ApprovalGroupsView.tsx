import React,{useEffect, useState} from 'react';
import Layout from '../Layout';
import InnerLayout from '../components/Layout';
import {Table} from 'react-bootstrap';
import Axios from 'axios';
import { group } from 'console';

export default function(props) {
    const [groups, setGroups] = useState([]);
    const [loading, setLoading] = useState(false);

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

    const action = (a, selectedGroup) => {
        switch(a) {
            case 'edit' :
                props.history.push(`/approval/edit/${selectedGroup.id}`);
                break;
            case 'delete' : 
             if(confirm("are you sure you want to delete " + selectedGroup.name + "?")) {
                setLoading(true);
                Axios.delete(`/api/approval-groups/${selectedGroup.id}`)
                .then(res=> {
                    getGroups();
                    alert("Group deleted successfully!");
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

    return(
        <Layout>
            <InnerLayout dontfilter = {true} show = {true} title = 'Approval Groups' path = 'approval-groups'>
                <div className = "approval-group-header">
                    <input style = {{padding: "10px", fontSize: "12px", border: "1px solid #435FAA",  borderRadius: "10px"}} placeholder = "Search"/>
                    <button onClick = {()=>props.history.push("/create-approval-group")} className = "create-approval-button">Create Approval Group</button>
                </div>
                <div className = "table-div">
                    <Table className = "transaction-table"  hover responsive>
                        <thead>
                            <tr>
                                <th>S/N</th>
                                <th>Name</th>
                                <th>Number of Members</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {groups.map((theGroup, i)=> 
                                <tr key = {i}>
                                    <td>{i+1}</td>
                                    <td>{theGroup.name}</td>
                                    <td>{theGroup.phoneNumbers.length}</td>
                                    <td>
                                       <select value = "none" disabled = {loading} onChange = {e => action(e.target.value, theGroup) }>
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