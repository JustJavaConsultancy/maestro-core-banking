import './appManagement.scss';
import React, { useState, useEffect } from 'react';
import InnerLayout from '../components/Layout';
import Layout from '../Layout';
import Axios, { AxiosResponse } from 'axios';
import { isEmpty } from 'lodash';
import { Table } from 'reactstrap';

export type UpdateData =  {
    device: string,
    androidurl: string,
    iosurl: string,
    comments: string,
    androidversion: string,
    iosversion: string
}

const resetData: UpdateData = {
    device: '',
    androidurl: '',
    iosurl: '',
    comments: '',
    androidversion: '',
    iosversion: ''
}
export default function Schemes(props) {

    const [data, setData] = useState<UpdateData>({
        device: '',
        androidurl: '',
        iosurl: '',
        comments: '',
        androidversion: '',
        iosversion: ''
    });

    const [updates, setUpdates] = useState<Array<UpdateData>>([]);
    const [loading, setLoading] = useState<boolean>(false);

    const getVersions = async (): Promise<void> => {
        console.error("new version created");
        try {
            const version: AxiosResponse<any> = await Axios.get('api/app-updates');
            console.error(version);
            setUpdates(version?.data?.reverse())
        } catch (error) {
            console.error(error);
        }

    }

    const CreateNewVerstion = async () => {
        // if(data.device !== "ALL") {
        //     alert("You can only select 'ALL' at the moment")
        //     return
        // }
        console.error(data);
        setLoading(true);
        try {
            const saveUpdate = await Axios.post('api/app-updates', data);
            alert("Update saved successfully")
            console.error(saveUpdate);
            setData(resetData);
            getVersions()
        } catch (error) {
            console.error(error);
            alert("Unable to save update please Try again")

        }
        finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        getVersions();
    }, []);


    return (
        <>
            <Layout>
                <InnerLayout dontfilter={true} show={true} title='Application Version Management' path='Application Version Management'>
                    <form onSubmit={(e: any) => {
                        e.preventDefault();
                        CreateNewVerstion()
                    }}>
                        <label htmlFor='device' style={{ display: 'block', marginBottom: -10, fontSize: 12, fontFamily: 'Poppins-Regular', color: 'green' }}>Device</label>
                        <select name="device" onChange={(e: any) => setData({ ...data, device: e.target.value })} className='textinput marg'>
                            <option >Select target</option>
                            <option value="ALL">All</option>
                            <option value="IOS">IOS</option>
                            <option value="ANDROID">ANDROID</option>
                        </select>
                        {!isEmpty(data.device) &&
                            <>
                                {(data.device === "ALL" || data.device === "IOS") && <>
                                    <label htmlFor="iosversion" style={{ display: 'block', marginBottom: -10, fontSize: 12, fontFamily: 'Poppins-Regular', color: 'green' }}>IOS Version</label>
                                    <input name="iosversion" required={true} onChange={e => {
                                        setData({ ...data, iosversion: e.target.value })
                                    }} value={data.iosversion} type= 'number' className='textinput marg' placeholder="Ios version Number" />
                                    <label htmlFor="iosurl" style={{ display: 'block', fontSize: 12, marginBottom: -10, fontFamily: 'Poppins-Regular', color: 'green' }}>IOS Download URL</label>
                                    <input name="iosurl" required={true} onChange={e => {
                                        setData({ ...data, iosurl: e.target.value })
                                    }} value={data.iosurl} type="url" className='textinput marg' placeholder="IOS Update URL" />
                                </>}
                                {(data.device === "ALL" || data.device === "ANDROID") && <>
                                    <label htmlFor="androidversion" style={{ display: 'block', fontSize: 12, marginBottom: -10, fontFamily: 'Poppins-Regular', color: 'green' }}>Android Version</label>
                                    <input  name="androidversion" required={true} onChange={e => {
                                        setData({ ...data, androidversion: e.target.value })
                                    }} value={data.androidversion} type= 'number' className='textinput marg' placeholder="Android version Number" />
                                    <label htmlFor="androidurl" style={{ display: 'block', fontSize: 12, marginBottom: -10, fontFamily: 'Poppins-Regular', color: 'green' }}>Android Download URL</label>
                                    <input name="androidurl" required={true} onChange={e => {
                                        setData({ ...data, androidurl: e.target.value })
                                    }} value={data.androidurl} type="url" className='textinput marg' placeholder="Android Update URL" />
                                </>}
                                <label htmlFor="comments" style={{ display: 'block', marginBottom: -10, fontSize: 12, fontFamily: 'Poppins-Regular', color: 'green' }}>Release Note(Optional)</label>
                                <textarea name="comments" value={data.comments} onChange={(e: any) => setData({ ...data, comments: e.target.value })} className='textinput marg' placeholder="Release Note(Optional)" ></textarea>
                                <div style={{ display: 'flex', justifyContent: 'center', width: '70%' }}>
                                    <br /><button disabled={loading} style={{ justifySelf: 'center' }} type='submit' className='viewreport'>{!loading ? 'Update Application' : 'loading...'}</button>
                                </div>
                            </>}
                    </form>
                    <h5>Versions</h5>
                    <div style={{ marginRight: '10vw' }}>
                        <Table className='tableListData' striped>
                            <thead>
                                <th>S/N</th>
                                <th>IOS URL</th>
                                <th>IOS Version</th>
                                <th>Android URL</th>
                                <th>Android Version</th>
                            </thead>
                            <tbody>
                                {updates.map((s, i) =>
                                    <tr key={i}>
                                        <td>{i + 1}</td>
                                        <td>{s.iosurl}</td>
                                        <td>{s.iosversion}</td>
                                        <td>{s.androidurl}</td>
                                        <td>{s.androidversion}</td>
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