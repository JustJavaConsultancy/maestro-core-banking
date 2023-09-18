import './sendMoney.scss';
import React, { ChangeEvent, useEffect, useState, useRef } from 'react';
import InnerLayout from '../components/Layout';
import Layout from '../Layout';
import Axios, { AxiosResponse } from 'axios';
import { isEmpty } from 'lodash';
import XLSX from 'xlsx';

export default function SendMoney(props) {
  const [daName, setDAName] = useState('');
  const [saName, setSAName] = useState('');
  const [amount, setAmount] = useState(null);
  const [sourceAccountNumber, setSourceAccountNumber] = useState('');
  const [accountNumber, setAccountNumber] = useState('');
  const [loading, setLoading] = useState(false);

  const [narration, setNarration] = useState('');
  const [inputFile, setInputFile] = useState('');
  const [csvData, setCsvData] = useState([]);
  const [showFileInput, setShowFileInput] = useState(true);
  const [showErr, setShowErr] = useState(false);
  // const [userFile, setUserFile] = useState<File>();

  const imageRef = useRef(null);

  const clearData = () => {
    setAccountNumber('');
    setSourceAccountNumber('');
    setSAName('');
    setDAName('');
    setAmount('');
    setNarration('');
    setCsvData([]);
    setInputFile('');
  };

  const sendTheMoney = async event => {
    event.preventDefault();

    const isFalse = csvData.length
      ? false
      : saName === 'User not found!' || daName === 'User not found!' || Number(amount) < 1 || narration === '';
    if (isFalse) {
      alert('Please ensure you enter the correct details before submiting');
      return;
    }
    clearData();
    setLoading(true);
    const jsonData = JSON.stringify(csvData);
    const adminToken = JSON.parse(sessionStorage.getItem('jhi-authenticationToken'));

    // await Axios.post(`/api/send-money-correspondent/`, jsonData)

    try {
      const send =
        csvData.length > 0
          ? await Axios({
              url: `/api/send-money-correspondent`,
              data: jsonData,
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
                Accept: 'application/json',
                Authorization: `Bearer ${adminToken}`,
              },
            })
          : await Axios.post(`/api/send-money-correspondent/${sourceAccountNumber}/${accountNumber}/${amount}/${narration}`);
      setLoading(false);
      alert('Money Sent Successfully');

      // eslint-disable-next-line no-console
      console.log(send);
    } catch (e) {
      alert('Error sending money');
      setLoading(false);
      // eslint-disable-next-line no-console
      console.log(e);
    }
  };

  const verifyUser = async (accNumber, set) => {
    try {
      const theUser = await Axios.get(`/api/wallet-accounts/avs/${accNumber}`);
      set === 'd' ? setDAName(theUser.data) : setSAName(theUser.data);
    } catch (error) {
      set === 'd' ? setDAName('User not found!') : setSAName('User not found!');
    }
  };

  const handleFileInput = (e: ChangeEvent<HTMLInputElement>) => {
    e.stopPropagation();
    setShowErr(false);
    const file = e.target.files[0];
    setInputFile(e.target.files[0].name);

    if (file.size) {
      const fileReader = new FileReader();
      fileReader.readAsBinaryString(file);
      fileReader.onload = event => {
        const data = event.target.result;
        const workbook = XLSX.read(data, { type: 'binary' });

        workbook.SheetNames.forEach(sheet => {
          const rowObj: Record<string, string | number | boolean>[] = XLSX.utils.sheet_to_json(workbook.Sheets[sheet]);

          if (rowObj.length < 1) {
            setShowErr(true);
            return;
          }
          // eslint-disable-next-line no-console
          console.log(rowObj);
          setCsvData(rowObj);
        });
      };
    }
  };

  useEffect(() => {
    if (sourceAccountNumber || accountNumber || amount || narration) {
      setShowFileInput(false);
      return;
    }
    setShowFileInput(true);
  }, [sourceAccountNumber, accountNumber, amount, narration]);

  // useEffect(() => {
  //   // eslint-disable-next-line no-console
  //   console.log(csvData);
  // }, [csvData]);

  return (
    <>
      <Layout>
        <InnerLayout dontfilter={true} show={true} title="Transit Funds Transfer" path="Transit Funds Transfer">
          <form onSubmit={sendTheMoney} className="sendMoneyConnector">
            <input
              // required={true}
              onChange={e => {
                setSourceAccountNumber(e.target.value);
                setSAName('');
                if (e.target.value.length > 9) {
                  verifyUser(e.target.value, 's');
                }
              }}
              value={sourceAccountNumber}
              type="number"
              className="textinput marg"
              placeholder="Enter the source account number"
            />
            {!isEmpty(saName) && (
              <div
                className="msg-box"
                style={{ backgroundColor: saName !== 'User not found!' ? 'hsl(204, 90%, 49%)' : 'hsl(0, 100%, 45%)' }}
              >
                <p className="mb-0">
                  <span style={{ fontWeight: 'normal' }}>Account:</span> {saName}
                </p>
              </div>
            )}
            <input
              // required={true}
              onChange={e => {
                setAccountNumber(e.target.value);
                setDAName('');
                if (e.target.value.length > 9) {
                  verifyUser(e.target.value, 'd');
                }
              }}
              value={accountNumber}
              type="number"
              className="textinput marg"
              placeholder="Enter the Destination account number"
            />
            {!isEmpty(daName) && (
              <div
                className="msg-box"
                style={{ backgroundColor: daName !== 'User not found!' ? 'hsl(204, 90%, 49%)' : 'hsl(0, 100%, 45%)' }}
              >
                <p className="mb-0">
                  <span style={{ fontWeight: 'normal' }}>Name:</span> {daName}
                </p>
              </div>
            )}
            <input
              // required={true}
              onChange={e => setAmount(e.target.value)}
              value={amount}
              type="number"
              className="textinput marg"
              placeholder="Enter Amount"
              step="any"
            />
            {/* <br /> */}
            <input
              // required={true}
              onChange={e => setNarration(e.target.value)}
              value={narration}
              type="text"
              className="textinput marg"
              placeholder="Narration"
            />

            {showFileInput && (
              <>
                <small style={{ fontWeight: 'bold', marginTop: '1rem' }}>For bulk payments only!</small>
                <div
                  className="image-box"
                  onClick={e => {
                    e.stopPropagation();
                    // e.stopImmediatePropagation()
                    imageRef.current.click();
                  }}
                >
                  <label
                    htmlFor="file-upload"
                    className={`img-label mb-0 ${inputFile ? 'img-label-chosen' : ''}`}
                    onClick={e => e.stopPropagation()}
                  >
                    {inputFile ? 'File chosen' : 'Choose a file'}
                  </label>
                  <span>{inputFile}</span>
                  <input
                    required={true}
                    // onChange={e => setNarration(e.target.value)}
                    onChange={handleFileInput}
                    // value={inputFile}
                    type="file"
                    className="textinput marg my-file"
                    accept=".xls, .xlsx, .csv"
                    id="file-upload"
                    ref={imageRef}
                    // placeholder="Narration"
                  />
                </div>
              </>
            )}
            {showErr && (
              <div style={{ color: '#f00' }}>
                <small className="mb-0">Selected file is empty</small>
              </div>
            )}

            {/* {csvData.length > 0 && (
              <div>
                <pre>{JSON.stringify(csvData, null, 2)}</pre>
              </div>
            )} */}
            <br />
            <button disabled={loading} type="submit" className="send-money-btn">
              {!loading ? 'Send' : 'loading...'}
            </button>
          </form>
        </InnerLayout>
      </Layout>
    </>
  );
}
