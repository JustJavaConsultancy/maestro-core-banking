import React, {useEffect, useState} from 'react';
import InnerLayout from '../components/Layout';
import Layout from '../Layout';
import { Table } from 'reactstrap';
import Axios from 'axios';
import { codeTypes } from './TypeAndCodes';

export default function StatusOfRequest(props) {
    const [requests, setRequests] = useState([]);

    const getRequestsStatus = async () => {
        try {
            const getRequests = await Axios.get('api/approval-requests');
            console.error(getRequests);
            setRequests(getRequests.data.data);
        } catch (error) {
            console.error(error);
        }
    }

    useEffect(() => {
        getRequestsStatus();
    }, []);

    return(
        <>
            <Layout>
                <InnerLayout dontfilter={true}  title = "Request Status" path = "Request Status">
                    <Table className = "requests"  striped>
                        <thead>
                            <tr>
                                <th>SN</th>
                                <th>requestId</th>
                                <th>requestRef</th>
                                <th>request Type Code</th>
                                <th>comment</th>
                                <th>initiator Name</th>
                                <th>checker Name</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            {requests.map((req, i) => 
                                <tr key = {i}>
                                    <td>{i+1}</td>
                                    <td>{req.requestId}</td>
                                    <td>{req.requestRef}</td>
                                    <td>{codeTypes[req.requestTypeCode] || req.requestTypeCode}</td>
                                    <td>{req.comment}</td>
                                    <td>{req.initiatorName}</td>
                                    <td>{req.checkerName}</td> 
                                    <td>{req.status}</td>
                                </tr>
                            )}
                        </tbody>
                    </Table>
                </InnerLayout>
            </Layout>
        </>
    )
}