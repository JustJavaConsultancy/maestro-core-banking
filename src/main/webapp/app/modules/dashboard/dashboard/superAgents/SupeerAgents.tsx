import Axios from 'axios';
import React, { useEffect, useState, useRef } from 'react';
import InnerLayout from '../../components/Layout';
import Layout from '../../Layout';
import { Modal } from 'reactstrap';
import { Table } from 'react-bootstrap';
import MoneyFormat from 'react-number-format';
import moment from 'moment';
import { connect } from 'react-redux';
import { IRootState } from 'app/shared/reducers';
import { RouteComponentProps } from 'react-router-dom';
import agentService from 'app/shared/Services/agent';

function SuperAgents(props) {
    const [superAgents, setSuperAgents] = useState([]);
    const [status, setStatus] = useState("UNAPPROVED");
    const [loading, setLoading] = useState(false);

    const getSuperAgents = (theStatus:string) => {
        agentService.getAgentByStatus(theStatus)
        .then((superAgent:any)=>{
            setSuperAgents(superAgent.data.data);
            console.error(superAgent.data.data);  
        })
    }

    const changeSuperAgentStatus = (theStatus:string, phone:string) => {
        setLoading(true)
        agentService.changeAgentByStatus(theStatus, phone)
        .then((superAgent:any)=>{
            getSuperAgents(status)
           // setSuperAgents(superAgent.data.data);
           // console.error(superAgent.data.data); 
           alert(`Super agent status ${theStatus} successfully`); 
        }).catch(e=>{
            if(e.response){
                alert(e.response.data.message)
            }else{
                alert("Network Error!")
            }
        }).finally(()=>{
            setLoading(false);
        })
    }

    useEffect(()=>{
        getSuperAgents("UNAPPROVED");
    },[]);


    return (
        <>
            <Layout>
                <InnerLayout dontfilter={true} show={true} title='Super Agents' path='Super Agents'>
                    <select onChange = {(e:any)=>{
                        setStatus(e.target.value)
                        getSuperAgents(e.target.value)}}>
                        {/* <option value = "">all</option> */}
                        <option value = "UNAPPROVED">Pending Approval</option>
                        <option value = "APPROVED">Approved</option>
                        <option value = "REJECTED">Rejected</option>
                    </select>
                    <div style = {{minWidth:'300px', minHeight:'300px', marginTop:'20px'}}>
                    <Table responsive hover className = "transaction-table">
                        <thead className = 'tableListData'>
                            <th>S/N</th>
                            <th>business Name</th>
                            <th>Full Name</th>
                            <th>Phone Number</th>
                            <th>email</th>
                            <th>Status</th>
                            <th>action</th>
                        </thead>
                        <tbody>
                            {superAgents.map((s, i) => 
                                <tr className = 'tableListData' key = {i}>
                                    <td>{i+1}</td>
                                    <td>{s?.agent?.businessName}</td>
                                    <td>{s?.agent?.profile?.fullName}</td>
                                    <td>{s?.agent?.profile?.phoneNumber}</td>
                                    <td>{s?.agent?.profile?.user?.email}</td>
                                    <td>{status}</td>
                                    <td>
                                        <select disabled = {loading} onChange = {(e:any)=>changeSuperAgentStatus(e.target.value, s?.agent?.profile?.phoneNumber)}>
                                            <option>none</option>
                                           {status==="UNAPPROVED" && <option value = "APPROVED">approve</option>}
                                           {status==="UNAPPROVED" && <option value = "REJECTED">Reject</option>}
                                        </select>
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </Table>
                </div>
                </InnerLayout>
            </Layout>
        </>
    )
}

const mapStateToProps = ({ reports, authentication: { isAuthenticated, account, sessionHasBeenFetched } }: IRootState) => ({
    user: reports.user,
    userLog: account
});

const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SuperAgents);