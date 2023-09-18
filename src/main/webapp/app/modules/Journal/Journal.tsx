import React,  {useState, useRef} from 'react';
import axios from 'axios';
import {Table} from 'reactstrap';
import MoneyFormat from 'react-number-format';
import Moment from 'moment';
import ReactToPrint from 'react-to-print';
import exportFromJSON from 'export-from-json'

export default function() {
   // let journal:any;
    //  let setJournals:any;
   const [journal, setJournals] = useState([]);
   const [format, setFormat] = useState('pdf');
   const toPrint = useRef();

    const getJournal = async () => {
        try {
            const journals = await axios.get('/api/daybook');
         //   console.log('====================================');
            console.error(journals);
            setJournals(journals.data);
           //   alert("hello")
          //  console.log('====================================');
        } catch (error) {
            alert("error");
           // console.log('====================================');
            console.error(error);
            //  console.log('====================================');
        }
    }
    React.useEffect(()=>{
        getJournal();
    },[])

    const exportFile = () => {
        enum toexport {
            csv = 'csv',
            xls = 'csv',
            txt = 'txt',
            json = 'json'
        }
        const dataToExport = journal;
        const fileName = 'specswallet Report';
        exportFromJSON({data: dataToExport, fileName, exportType: toexport[format] });
    }

    return(
        <>
        <h3 style = {{margin:"20px"}}>Cash Flow</h3>
        <div style = {{margin:"20px"}}>
            <select onChange = {(e)=> setFormat(e.target.value)} style = {{height:'50px', marginRight:'10px'}} className = 'formatprint'>
                <option value = 'pdf'>pdf</option>
                {/* <option value = 'xls'>xls</option>
                <option value = 'csv'>csv</option>
                <option value = 'txt'>text</option>
                <option value = 'json'>json</option> */}
            </select>
            {format === 'pdf' && <ReactToPrint
            trigger={() => {
                return <button style ={{marginTop:'10px', marginRight:'40px', border:'1px solid #565682', fontSize:'12px'}}>Download or print</button>;
            }}
            content={() => toPrint.current}
            />}
            {format !== 'pdf' && <button onClick={exportFile} style ={{marginTop:'10px', marginRight:'40px', border:'1px solid #565682', fontSize:'12px'}}>Print {format}</button>}
        </div>
        <div ref = {toPrint} style = {{
            margin: "20px"
        }}>
            {journal.map((j,i)=>{
                let totalCredit = 0;
                let totalDebit = 0;
                return(
                    <div key = {i}>
                    <p style = {{
                        padding: "10px",
                        backgroundColor: '#DB5328',
                        color: '#fff',
                        fontWeight: 'bold'
                    }} key = {i}> {Moment(j.journal.transDate).format('LLL')} {j.journal.memo}</p>
                    <Table>
                        <thead>
                            <th>Account Name</th>
                            <th>Credit</th>
                            <th>Debit</th>
                        </thead>
                        <tbody>
                            {j.journalLines.map((jl, k)=>{
                                totalCredit+=jl.credit;
                                totalDebit+=jl.debit
                                return(
                                    <tr key = {k}>
                                    <td style = {{width:"50%"}}> {jl.accountName} </td>
                                    <td>
                                       <MoneyFormat
                                        value = {jl.credit}
                                        displayType = 'text'
                                        thousandSeparator = {true}
                                        prefix = "₦"
                                        />
                                    </td>
                                    <td>
                                      <MoneyFormat
                                        value = {jl.debit}
                                        displayType = 'text'
                                        thousandSeparator = {true}
                                        prefix = "₦"
                                        />
                                    </td>
                                    </tr>
                                )
                            })}
                            <tr>
                                <th>Total</th>
                                <th>
                                    <MoneyFormat
                                    value = {totalCredit}
                                    displayType = 'text'
                                    thousandSeparator = {true}
                                    prefix = "₦"
                                    />
                                </th>
                                <th>
                                  <MoneyFormat
                                    value = {totalDebit}
                                    displayType = 'text'
                                    thousandSeparator = {true}
                                    prefix = "₦"
                                    />
                                </th>
                            </tr>
                        </tbody>
                    </Table>
                </div>
                )
            }

            )
            
            }
           
        </div>
        </>
    )
}