import React, {useState, useEffect, useRef} from 'react';
import { Table } from 'reactstrap';
import Axios from 'axios';
import {connect} from 'react-redux'
import { IRootState } from 'app/shared/reducers';
import { RouteComponentProps, useLocation } from 'react-router-dom';
import { setWalletData } from 'app/shared/reducers/reports';
import { isEmpty } from 'lodash';
import exportFromJSON from 'export-from-json';
import { useReactToPrint } from 'react-to-print';
import MoneyFormat from 'react-number-format';
import moment from 'moment';

export interface IWalletstProps extends StateProps, DispatchProps, RouteComponentProps<{}> {}

function Wallets(props: any) {

    const [wallets, setWallet] = useState([]);
    const [balance, setBalance] = useState(0);
    const [downloadFormat, setDownloadFormat] = useState('pdf');
    const [data, setData] = useState({
      start: '',
      end: '',
      accountNumber: '',
      accountOwnerName: '',
      schemeID: '',
      error: false,
      scheme: '',
      format: 'customer',
      dateOpened: moment(Date()).format('YYYY-MM-DD')
    });
  
    const [schemeStatement, setSchemeStatement] = useState([]);
    const toPrint = useRef();
    const handlePrint = useReactToPrint({
        content: () => toPrint.current,
        // onAfterPrint: () => setShowPrint(false)
    }); 
    const location: any = useLocation();
  
    const handleDownload = (value: any, fileName: string) => {
        if(downloadFormat === 'pdf') {
            handlePrint();
            } else {
            if(downloadFormat === 'csv') {
                exportFromJSON({ data: value, fileName, exportType: 'csv' });
            } else {
                exportFromJSON({ data: value, fileName, exportType: 'xls' });
            }
        }
    }
    
    useEffect(() => {
      if(location.state) {
        setData(location.state.data)
        setSchemeStatement(location.state.customerStatement);
      } else {
        setData(props.customerData)
        setSchemeStatement(props.schemeStatement)
      }
    }, [])

    return(
        <>
        <section ref={toPrint}> 
            <span className = "titles">Scheme Customer Wallet Balance</span>
            <div className="scheme__report__container">
                <div style = {{marginLeft:'54px'}}>
                    <p className = 'titles2'>Summary</p>
                    <div className = 'dateContainer'>
                        <span className = "titles2" ><span style = {{color:'#000', fontWeight:'bold'}}>Report Generation Date/Time:</span> {Date().replace('GMT+0100 (West Africa Standard Time)','')}</span>
                        <br/>
                        <p>Scheme Name: <b>{data?.scheme}</b></p>
                    </div>
                </div>
                <div className="report__download__section">
                <div className="download-format">
                    <select
                    onChange={e => setDownloadFormat(e.target.value)}
                    >
                    <option value="pdf">PDF</option>
                    <option value="csv">CSV</option>
                    <option value="xls">XLS</option>
                    </select>
                </div>
                <div>
                    <button onClick={() => handleDownload(schemeStatement, 'Scheme Report')}>Download</button>
                </div>
                </div>
            </div>
            <div style = {{marginLeft:'54px', marginTop:'50px'}}>
                <div>
                    <p className = 'titles2'>REPORT DETAILS</p>
                </div>
        
                <Table striped hover>
                    <thead>
                        <th>S/N</th>
                        <th>Introducer</th>
                        <th>Registration Date</th>
                        <th>Wallet Number</th>
                        <th>Wallet Name</th>
                        <th>Book Balance (₦)</th>
                        <th>Available Balance (₦)</th>
                       
                        {/* <th></th> */}
                    </thead>
                    <tbody>
                        {(schemeStatement && schemeStatement.length > 0) && schemeStatement.map((v, i)=>
                            <tr className = 'click' key = {i}>
                                <td>{i+1}</td>
                                <td>---</td>
                                <td>{moment(v.dateOpened).format('ll')}</td>
                                <td>{v.accountNumber}</td>
                                <td>{v.accountName}</td>
                                <td>
                                <MoneyFormat
                                    value = {Number(v.currentBalance)}
                                    displayType = 'text'
                                    thousandSeparator = {true}
                                    decimalScale = {2}
                                    fixedDecimalScale = {true}
                                   // prefix = "₦"
                                />
                               </td>
                               <td>
                                <MoneyFormat
                                    value = {Number(v.actualBalance)}
                                    displayType = 'text'
                                    thousandSeparator = {true}
                                    decimalScale = {2}
                                    fixedDecimalScale = {true}
                                   // prefix = "₦"
                                />
                               </td>                               
                            </tr>
                        )}
                    </tbody>
                </Table>
            </div>
        </section>
        </>
    )
}

const mapStateToProps = ( {reports} : IRootState) => ({
    data: reports.data
});

const mapDispatchToProps = { setWalletData };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Wallets);