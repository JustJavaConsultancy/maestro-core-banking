import moment from 'moment';
import React from 'react';
import {Table} from 'reactstrap';
import { codeTypes } from './TypeAndCodes';

export default function Request(props) {
    const {requestTypeCode, comment, dateCreated, dateLastModified, checkerName, supportingDoc, status, initiatorName, requestRef} = props.data
    return(
        <div className = 'right-container'>
            <table className = "approval-entry">
                <tbody>
                    <tr>
                        <td className = "left-label">
                            <label htmlFor = "Select-request-type">Request Type: </label>
                        </td>
                        <td>
                            <select defaultValue = {requestTypeCode} disabled className = "regular-input"  name = "Select-request-type">
                                <option value = "PD">Post No Debit</option>
                                <option value = "DC">Deactivate Customer</option>
                                <option value = "FC">Flag Customer</option>
                                <option value = "UC">Update Customer Information</option>
                                <option value = "BA">Block/Unblock Account</option>
                                <option value = "HF">Hold Funds</option>
                                <option value = "RT">Reverse Transaction</option> 
                            </select>
                        </td>
                    </tr>
                    <tr className = "left-label">
                        <td>requestRef</td>
                        <td>
                            <input disabled value = {requestRef} className = "regular-input"/>
                        </td>
                    </tr>
                    <tr className = "left-label">
                        <td>Initiator Name</td>
                        <td>
                            <input disabled value = {initiatorName} className = "regular-input"/>
                        </td>
                    </tr>
                    <tr className = "left-label">
                        <td>comment</td>
                        <td>
                            <input type = 'textarea' disabled value = {comment} className = "regular-input multi"/>
                        </td>
                    </tr>
                    {/* <tr className = "left-label">
                        <td>Status</td>
                        <td>
                            <input disabled value = {status} className = "regular-input"/>
                        </td>
                    </tr> */}
                    <tr className = "left-label">
                        <td>Supporting Document:</td>
                        <td>
                            <small>Click to preview</small>
                        </td>
                    </tr>
                    <tr className = "left-label">
                        <td>Additional Comments:</td>
                        <td>
                            <input disabled = {status !== 'AWAITING_APPROVAL'} onChange = {(e => props.comment(e.target.value))} type = 'textarea' placeholder = "Additional comment" className = "regular-input multi"/>
                        </td>
                    </tr>
                </tbody>
            </table>
            {status === 'AWAITING_APPROVAL' && <div className = "action-Button-div">
                    <button onClick = {()=>{
                        props.approveOrRejectRequest("REJECT")
                        }}  className = "transparentButton">Reject</button>
                    <button  onClick = {()=>{
                        props.approveOrRejectRequest("APPROVED")
                        }}className = "transparentButton RegularButton">Approve</button>
                </div>}
            <br/>
            <div style = {{background: "#fff", width: '100%', padding: '50px'}}>
                <h4>Audit Trail</h4><br/>
                <Table className = "requests" striped>
                    <thead>
                        <tr>
                            <th>Operation</th>
                            <th>Function</th>
                            <th>User</th>
                            <th>Time</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>CREATE</td>
                            <td>{codeTypes[requestTypeCode]}</td>
                            <td>{initiatorName}</td>
                            <td>{moment(dateCreated).format('ll')}</td>
                        </tr>
                        {status !== "AWAITING_APPROVAL" && <tr>
                            <td>{status}</td>
                            <td>{codeTypes[requestTypeCode]}</td>
                            <td>{checkerName}</td>
                            <td>{moment(dateLastModified).isSame(dateCreated)? '---' : moment(dateLastModified).format('ll')}</td>
                        </tr>}
                    </tbody>
                </Table>
            </div>
        </div>
    )
}