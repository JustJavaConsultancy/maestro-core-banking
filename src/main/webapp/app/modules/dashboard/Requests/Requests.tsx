import './requests.scss'
import Axios from 'axios';
import React, {useState, useEffect} from 'react';
import InnerLayout from '../components/Layout';
import Layout from '../Layout';
import {connect} from 'react-redux';
import { Table } from 'reactstrap';
import { codeTypes } from './TypeAndCodes';
import Request from './RequestView';

 interface Requests {
    comment: string,
    phoneNumber: string,
    requestId: string,
    requestRef: string,
    requestTypeCode: string,
    status: string,
    supportingDoc: string
}
 interface ApprovePayload {
    requestId : string,
    additionalComment: string,
    action: string
}
type IRequests = StateProps;

function Requests(props: IRequests) {

    const [request, setRequests] = useState([]);
    const [view, setView] = useState('requests');
    const [selectedRequest, setSelectedRequest] = useState({additionalComment:'', status: '', requestId:''});

    const getRequest = async () => {
        try {
            let reqs: Array<Requests> = [];
            const theRequests = await Axios.get(`api/approval-requests/${props.account.login}`);
            reqs = theRequests.data.data;
            setRequests(reqs)
        } catch (error) {
            console.error(error);
        }
    }

    const approveOrRejectRequest = async (action, id) => {
        const payload: ApprovePayload = {
            requestId : id.trim(),
            additionalComment: selectedRequest.additionalComment,
            action
        }
        const perform = window.confirm(`Are you sure you want to ${action === 'APPROVED'? 'APPROVE' : action} this request?`);
        if(!perform) return
        try {
            const treat = await Axios.post('api/approval-requests/approve', payload);
            console.error(treat);
            alert('Request treated successfully!')
            getRequest();
            setView('requests')
        } catch (error) {
            console.error(error);
            alert("Error Treating request!")
        }
    }

    const ViewRequest = (req) => {
        setSelectedRequest(req)
        setView('request')
    }

    useEffect(() => {
        getRequest();
    }, []);

    return(
        <>
        <Layout>
            <InnerLayout goBack = {() => setView('requests')} dontfilter={true}  title = "Approve Request" path = "Approve Request">
                {view === "requests" && <Table hover className = "requests" striped>
                    <thead>
                        <tr>
                            <th>SN</th>
                            <th>Request ID</th>
                            <th>Request Reference</th>
                            <th>Date Registered</th>
                            <th>request Type</th>
                            <th>Initiator</th>
                            <th>comment</th>
                            <th>status</th>
                            {/* <th></th> */}
                        </tr>
                    </thead>
                    <tbody>
                        {request.map((req, i) => 
                            <tr style = {{cursor: 'pointer'}} onClick = {()=>ViewRequest(req)} key = {i}>
                                <td>{i + 1}</td>
                                <td>{req.requestId}</td>
                                <td>{req.requestRef}</td>
                                <td>---</td>
                                <td>{codeTypes[req.requestTypeCode] || req.requestTypeCode}</td>
                                <td>{req.initiatorName}</td>
                                <td>{req.comment}</td>
                                <td style = {{fontSize:'12px'}}>{req.status}</td>
                                {/* <td>
                                    {req.status === 'AWAITING_APPROVAL' && <>
                                    <button  style = {{padding: '5px', fontSize: '12px', marginBottom: '5px', width:'50px'}} onClick = {()=>{
                                        approveOrRejectRequest("APPROVED", req.requestId)
                                    }} className = 'approval-button'>Approve</button>
                                    <button  style = {{padding: '5px', fontSize: '12px', backgroundColor: 'red', width:'50px'}} onClick = {()=>{
                                        approveOrRejectRequest("REJECT", req.requestId)
                                    }} className = 'approval-button'>Reject</button>
                                    </>}
                                </td> */}
                            </tr>
                        )}
                    </tbody>
                </Table>}
                {view === "request" && 
                    <>
                      <Request approveOrRejectRequest = {(action)=>approveOrRejectRequest(action, selectedRequest.requestId)} comment = {(additionalComment)=>setSelectedRequest({...selectedRequest, additionalComment})} data = {selectedRequest} />
                    </>
                }
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
export default connect(mapStateToProps)(Requests);