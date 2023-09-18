import './approvalgroup.scss';
import React, { useState, useEffect } from 'react';
import Layout from '../Layout';
import InnerLayout from '../components/Layout';
import Axios from 'axios';
import {Table} from 'react-bootstrap';
import { isEmpty } from 'lodash';

export default function ApprovalGroup(props:any) {
    const [selectedUser, setSelectedUser] = useState([]);
    const [searchResult, setSearchResult] = useState([]);
    const [users, setUsers] = useState([]);
    const [total, setTotal] = useState(0);
    const [size, setSize] = useState(15);
    const [page, setPage] = useState(0);
    const [search, setSearch] = useState('');
    const [renderPage, setRenderPage] = useState([]);
    const [active, setActive] = useState(0);
    const [groupName, setGroupName] = useState("");
    const [loading, setLoading] = useState(false);

    const getUsers: any = async (paget) => {
        try {
            const theUsers = await Axios.get(`/api/profiles/search?key=${search}&page=${paget}&size=${size}`);
            console.error(theUsers);
            setTotal(theUsers.data.metadata.totalNumberOfRecords);
            setPage(paget);
            setUsers(theUsers.data.data)
           // for(let x = 0; x <=)
        } catch(e) {
            console.error(e);
        }
    }

    const searchUsers: any = async (s: string) => {
        try {
            const theUsers = await Axios.get(`/api/profiles/search?key=${s}&page=${0}&size=${size}`);
            console.error(theUsers);
            setTotal(theUsers.data.metadata.totalNumberOfRecords);
            setPage(0);
            setUsers(theUsers.data.data)
           // for(let x = 0; x <=)
        } catch(e) {
            console.error(e);
        }
    }

    const sendForApproval = async () => {
        if(isEmpty(groupName)) {
            alert("You must enter a group name");
            return
        }
        if(selectedUser.length < 1) {
            alert("You must Select a user for this group");
            return
        }
        // const numbers = selectedUser;
        // numbers.map((n,i)=> {
        //    // let k= "hello"
        //     numbers[i] = n.substring(1);
        // })
        const data = {
            id: "",
            name: groupName,
            phoneNumbers: selectedUser
        }
        console.error(data);
        
        try {
            setLoading(true)
            const submit = await Axios.post('/api/approval-groups', data);
            console.error(submit);
            alert("Approval Group Created Successfully");
            props.history.push("/dash");
        } catch (error) {
            if(error.response) {
                alert(error.response.data.message)
            }else {
                alert("Network error!")
            }
        }finally {
            setLoading(false)
        }
        
    }

    useEffect(() => {
        getUsers(0);
    }, []);

    const  select = (phone) => {
        console.error(selectedUser);
        
            const holder = selectedUser;
            let userSelected = false;
            const holdUsers = users;
            for(let i=0; i<holder.length; i++) {
                if(phone === holder[i]) {
                    userSelected = true;
                    holder.splice(i,1);
                    break;
                }
            }
            if(!userSelected) {
                holder.push(phone)
                setSelectedUser(holder);
               
            }

            setUsers([users[0]]);
            setTimeout(() => {
                setUsers(holdUsers);
            }, 10)
           
    }


    useEffect(()=>{
        const pages = Number(total) / Number(size);
                const p = [];
                for(let x = 0; x < pages; x++) {
                    p[x] =  x;
                }
                console.error(p);  
                setRenderPage(p);
    },[page, total]);



    return(
        <Layout>
            <InnerLayout dontfilter = {true} show = {true} title = 'Create Approval Group' path = 'creat-approval-group'>
                <div className = "approval-gorup-selector">
                    <label style = {{width: "900px"}}>
                        Create Group
                        <input onChange = {e => setGroupName(e.target.value)} className = "custom-input1" placeholder = "Enter Group Name" />
                    </label>
                </div>
                <br/>
                <div>
                    <input onChange = {e => searchUsers(e.target.value)} className = "custom-input1 short" placeholder = "Search Users"/>
                </div>
                <div>
                <Table className = "transaction-table" hover>
                    <thead>
                        <tr>
                            <th><input type = "checkbox"/></th>
                            <td>S/N</td>
                            <th>Name</th>
                            <th>Phone</th>
                            <th>Email Address</th>
                            <th>Role Name</th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.map((user,i)=> 
                            <tr key = {i}>
                                <td>
                                    <input checked = {selectedUser.includes(user.user.login) === true} onClick = {()=>select(user.user.login)}  type = "checkbox"/>
                                </td>
                                <td>{(i + 1) +  (Number(page) * size)}</td>
                                <td>
                                    {user.user.firstName +" " + user.user.lastName}
                                </td>
                                <td>
                                    {user.user.login}
                                </td>
                                <td>
                                    {user.user.email}
                                </td>
                                <td>
                                    
                                </td>
                            </tr>
                        )}
                    </tbody>
                </Table>
                </div>
                <div className = 'paginate'>
                {renderPage.map((p,i)=>
                    <button style = {{backgroundColor: active === i? '#565682' : '#fff',  color: active === i? '#fff' : '#565682'}} onClick = {()=>{
                        setActive(i)
                        getUsers(i)
                    }} key = {i} >{i}</button>
                )}
                </div>
                <br/>
                <div style = {{display: "flex", flexDirection: "column",  justifyContent: "center"}} className = "approval-gorup-selector">
                    <div  style = {{display: "flex",  alignItems: "center"}}>
                        <label htmlFor = "comments">
                                Additional Comment:
                        </label>
                        <textarea name = "comments"  className = "custom-input1" placeholder = "Add comment" ></textarea>
                    </div>
                    <br/>
                    <div style = {{display: "flex", justifyContent: "center", alignItems: "center"}}>
                        <button onClick = {sendForApproval} disabled = {loading} className = 'viewreport'>{loading? "Loading..." : "Create Group"}</button>
                    </div>
                </div>
            </InnerLayout>
        </Layout>
    )
}