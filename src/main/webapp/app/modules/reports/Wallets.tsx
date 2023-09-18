import './wallet.scss';
import React, {useState, useEffect, useRef} from 'react';
import { Table } from 'reactstrap';
import Axios from 'axios';
import {connect} from 'react-redux'
import { IRootState } from 'app/shared/reducers';
import { RouteComponentProps } from 'react-router-dom';
import { setWalletData } from 'app/shared/reducers/reports';
import ReactToPrint from 'react-to-print';
import { isEmpty } from 'lodash';
import exportFromJSON from 'export-from-json';
import MoneyFormat from 'react-number-format';
import moment from 'moment';

export interface IWalletstProps extends StateProps, DispatchProps, RouteComponentProps<{}> {}

function Wallets(props: IWalletstProps) {

    const [wallets, setWallet] = useState([]);
    const [balance, setBalance] = useState(0);
    const [format, setFormat] = useState('pdf');
    const [data, setData] = useState({scheme:""});
    const toPrint = useRef();

    const exportFile = () => {
        enum toexport {
            csv = 'csv',
            xls = 'csv',
            txt = 'txt',
            json = 'json'
        }
        const dataToExport = wallets;
        const fileName = 'specswallet Report';
        exportFromJSON({data: dataToExport, fileName, exportType: toexport[format] });
    }
    const getAllWallets = async (tocheck) => {
        console.error(tocheck);
        
        try {
            const getWalletAccount = await Axios.get(`/api/wallet-accounts/customers/${tocheck.schemeID}`);
            console.error(getWalletAccount.data); 
            if(getWalletAccount.data.data.length){
                let b = 0;
                getWalletAccount.data.data.map(v=>{
                    b += v.currentBalance;
                });
                setBalance(b)
                setWallet(getWalletAccount.data.data);
            }
        } catch(e) {
            console.error(e); 
        }
    }
    const getSpecialWallet = async (): Promise<void> => {
        try {
            const theWallets = await Axios.get(`/api/wallet-accounts/special`);
            //  setTotal(0);
            let b = 0;
            theWallets.data.data.map(v=>{
                b += v.currentBalance;
            });
           
            setWallet(theWallets.data.data)
            setBalance(b)
        } catch(e) {
            console.error(e);
        }
    }
    const viewWallet = (v) => {
        props.setWalletData(v);
        props.history.push('mini-statement')
    }
    useEffect(()=>{
       // getAllWallets();
    }, []);

    useEffect(()=>{
        console.error(props.data); 
        setData({scheme:"",...props.data})
        const tocheck: any = {...props.data}
        getAllWallets(tocheck);
    }, [props.data])

    return(
        <>
        <div style = {{float:'right'}}>
            <select onChange = {(e)=> setFormat(e.target.value)} style = {{height:'50px', marginRight:'10px'}} className = 'formatprint'>
                <option value = 'pdf'>pdf</option>
                <option value = 'xls'>xls</option>
                <option value = 'csv'>csv</option>
                <option value = 'txt'>text</option>
                <option value = 'json'>json</option>
            </select>
            {format === 'pdf' && <ReactToPrint
            trigger={() => {
                return <button style ={{marginTop:'10px', marginRight:'40px', border:'1px solid #565682', fontSize:'12px'}}>Download or print</button>;
            }}
            content={() => toPrint.current}
            />}
            {format !== 'pdf' && <button onClick={exportFile} style ={{marginTop:'10px', marginRight:'40px', border:'1px solid #565682', fontSize:'12px'}}>Print {format}</button>}
        </div>
        <section ref = {toPrint}> 
            <span className = "titles">Global Trial Balance</span>
             <div style = {{marginLeft:'54px'}}>
                <p className = 'titles2'>Summary</p>
                <div className = 'dateContainer'>
                    <span className = "titles2" ><span style = {{color:'#000', fontWeight:'bold'}}>Report Generation Date/Time:</span> {Date().replace('GMT+0100 (West Africa Standard Time)','')}</span>
                    <br/>
                    <p>Cummulative Balance: <b>
                        <MoneyFormat
                            value = {balance}
                            displayType = 'text'
                            thousandSeparator = {true}
                            decimalScale = {2}
                            fixedDecimalScale = {true}
                            prefix = "₦"
                        />
                      </b></p>
                    <p>
                        Scheme Name:
                        <span>{data.scheme}</span>      
                    </p>
                     
                </div>
            </div>
            <div style = {{marginLeft:'54px', background: "#fff", padding: "10px", marginTop:'50px'}}>
                <div>
                    <p className = 'titles2'>REPORT DETAILS</p>
                </div>
                {/* <select  onChange = {(e)=>{
                        if(e.target.value === 'customers') {
                            const tocheck: any = {...props.data}
                            getAllWallets(tocheck);
                        }else{
                            getSpecialWallet();
                        }
                    }}>
                        <option value = 'customers'>Customers Wallets</option>
                        <option value = 'special'>Operational Wallet</option>
                    </select> */}
                <Table  className = "transaction-table" hover>
                    <thead>
                        <th>S/N</th>
                        <th>Wallet Number</th>
                        <th>Wallet Name</th>
                        <th>ID</th>
                        <th>Customer Name</th>
                        <th>Customer PhoneNumber</th>
                        <th>Book Balance (₦)</th>
                        <th>Available Balance (₦)</th>
                        <th>Date Created</th>
                        {/* <th></th> */}
                    </thead>
                    <tbody>
                        {wallets.map((v, i)=>
                            <tr className = 'click' onClick = {() => viewWallet(v)} key = {i}>
                                <td>{i+1}</td>
                                <td>{v.accountNumber}</td>
                                <td>{v.accountName}</td>
                                <td>{v.accountOwnerId}</td>
                                <td>{v.accountOwnerName}</td>
                                <td>{v.accountOwnerPhoneNumber}</td>
                                <td>
                                <MoneyFormat
                                        value = {v.currentBalance}
                                        displayType = 'text'
                                        thousandSeparator = {true}
                                        decimalScale = {2}
                                        fixedDecimalScale = {true}
                                        // prefix = "₦"
                                    />
                                </td>
                                <td>
                                <MoneyFormat
                                        value = {v.actualBalance}
                                        displayType = 'text'
                                        thousandSeparator = {true}
                                        decimalScale = {2}
                                        fixedDecimalScale = {true}
                                        // prefix = "₦"
                                    />
                                </td>
                                <td>{moment(v.dateOpened).format('ll')}</td>
                                {/* <td>
                                    <button onClick = {()=>viewWallet(v)} className = 'viewButton'>View</button>
                                </td> */}
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