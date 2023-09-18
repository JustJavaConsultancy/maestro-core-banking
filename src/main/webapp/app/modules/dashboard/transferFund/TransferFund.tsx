import React, { ChangeEvent, FormEvent, useEffect, useState } from 'react';
import Select from 'react-select';
import { Spinner } from 'react-bootstrap';
import { useSelector } from 'react-redux';
import Axios from 'axios';
import { useHistory } from 'react-router-dom';

import InnerLayout from '../components/Layout';
import Layout from '../Layout';
import {
  bankTypes,
  Bank,
  getBanks,
  verifyAccount,
  verifyWalletUser,
  sendMoneyFormValidationRules,
  generateReference,
  sendMoney,
} from './utils/bankUtils';
import Pin from './utils/Pin';
import { hasAnyAuthority } from 'app/shared/util/hasAuthorities';
import { AUTHORITIES } from 'app/config/constants';
import { IRootState } from 'app/shared/reducers';
import { useSchemes } from '../messaging/helper/HelperUtils';

import './transferFund.scss';

function OptionTypeBase(bankIndex: any): any {
  return bankIndex;
}

interface BankInterface {
  [type: string]: string;
}

const defaultFormState = {
  accountNumber: '',
  amount: 0,
  channel: 'wallettobank',
  transRef: '',
  nameBank: '',
  destBankCode: '',
  shortComment: '',
  toBeSaved: false,
  pin: '0207',
  narration: '',
  sourceAccountNumber: '',
  sourceBankCode: 'ADB',
  beneficiaryName: '',
  bankIndex: -1,
  actual: '',
  bankName: '',
  formErrors: {
    accountNumberError: '',
    amountError: '',
    channelError: '',
    destBankCodeError: '',
    descriptionError: '',
  },
};

export default function TransferFund() {
  const [values, setValues] = useState(defaultFormState);
  const [accountName, setAccountName] = useState('');
  const [banks, setBanks] = useState<Bank[]>([]);
  const [showbanks, setShowBanks] = useState(true);
  const [options, setOptions] = useState([{ label: 'any', value: 0 }]);
  const [nameLoading, setNameLoading] = useState(false);
  const [tip, showTip] = useState(false);
  const [errorFetchingName, setErrorFetchingName] = useState(false);
  const [tipErrorMessage, showTipErrorMessage] = useState('');
  const [tipError, setTipError] = useState(false);
  const [isBank, setIsBank] = useState(false);
  const [active, setActive] = useState(false);
  const [showOtpModal, setShowOtpModal] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [show, setShow] = useState(false);
  const [error, setError] = useState(false);
  const [walletName, setWalletName] = useState('');
  const [isDisable, setIsDisable] = useState(true);
  const [loading, setLoading] = useState(false);
  const account = useSelector((state: IRootState) => state.authentication.account);
  const history = useHistory();
  const [vendorWallets, setVendorWallets] = useState([]);
  const [selectedWallet, setSelectedWallet] = useState('');

  const { isLoading, schemes } = useSchemes();

  const { channel, destBankCode, accountNumber, amount, formErrors, shortComment, bankIndex } = values;

  const isMcPhersonFinancial = hasAnyAuthority(account.authorities, [AUTHORITIES.MCPHERSON_FINANCIAL]);
  const isFuoyeFinancial = hasAnyAuthority(account.authorities, [AUTHORITIES.FUOYE_FINANCIAL]);
  const isSchFinancial = isMcPhersonFinancial || isFuoyeFinancial;

  const isPaymasta = hasAnyAuthority(account.authorities, [AUTHORITIES.PAYMASTA_ADMIN]);
  const isWragby = hasAnyAuthority(account.authorities, [AUTHORITIES.WRAGBY_ADMIN]);
  const isWynk = hasAnyAuthority(account.authorities, [AUTHORITIES.WYNK_ADMIN]);
  const isPartnerFinancial = isPaymasta || isWragby || isWynk;

  const isFinancial = isSchFinancial || isPartnerFinancial;

  const getPartnerBank = (data, code) => {
    const theBank = data.filter((bank: BankInterface) => bank.bankCode === code);
    setOptions([
      {
        label: theBank[0].bankName,
        value: 0,
      },
    ]);
    setBanks(theBank);
  }

  const loadBanks = (type: string) => {
    let theType = type;
    if (type === 'wallettowallet') {
      setShowBanks(false);
      return;
    }
    if (type === 'wallettobank') {
      theType = 'commercial';
      setShowBanks(true);
    }
    if (type === 'wallettobankmicro') {
      setShowBanks(true);
      theType = 'microfinance';
    }
    getBanks(theType).then(resp => {
      const { data } = resp;
      // eslint-disable-next-line no-console
      // console.log(mcuBank)

      const holdbank = [];
      for (let i = 0; i < data.length; i++) {
        holdbank.push({
          label: data[i].bankName,
          value: i,
        });
      }
      if (isMcPhersonFinancial) {
        getPartnerBank(data, '033')
      } else if (isPaymasta) {
        getPartnerBank(data, '101')
      } else if (isWynk) {
        getPartnerBank(data, '050')
      } else if (isWragby) {
        getPartnerBank(data, '058')
      }
      else {
        setOptions(holdbank);
        setBanks(data);
      }
    });
  };

  const getAccountName = (input: string, bankCode = (banks && banks[bankIndex] && banks[bankIndex].bankCode) || '033') => {
    setNameLoading(true);
    verifyAccount(input, bankCode, 'bank')
      .then(res => {
        const fetchData = res.data;
        setAccountName(fetchData.accountName);
        showTip(true);
        setNameLoading(false);
        setErrorFetchingName(false);
      })
      .catch(_error => {
        setNameLoading(false);
        setErrorFetchingName(true);
      });
  };

  const verifyAccountNumber = (input: string, code) => {
    if (channel === 'wallettobankmicro' || channel === 'wallettobank') {
      if (input.length < 10) setAccountName('');
      if (bankIndex > -1 && input.length >= 10) {
        showTipErrorMessage('');
        if(isFinancial) {
          getAccountName(input, code);
        } else {
          getAccountName(input);
        }
      } else if (bankIndex === -1 && input.length >= 10) {
        showTipErrorMessage('Please select your bank');
        setTipError(true);
      } else {
        showTip(false);
        showTipErrorMessage('');
        setTipError(false);
      }
    } else {
      if (input.length >= 10) {
        setNameLoading(true);
        verifyWalletUser(input)
          .then(response => {
            setAccountName(response.data);
            showTip(true);
            setNameLoading(false);
            setErrorFetchingName(false);
          })
          .catch(_e => {
            setNameLoading(false);
            setErrorFetchingName(true);
          });
      }
    }
  };

  const handleChange = (event: ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const formErr = { ...formErrors };
    const { name, value }: any = event.target;
    switch (name) {
      case 'channel':
        loadBanks(event.target.value);
        setAccountName('');
        setValues({ ...values, accountNumber: '' });
        formErr.channelError = !value.length ? 'Channel is required' : '';
        break;
      case 'accountNumber':
        formErr.accountNumberError = !value.length ? 'AccountNumber is required' : '';
        break;
      case 'bankIndex':
        setAccountName('');
        setValues({ ...values, accountNumber: '' });
        formErr.destBankCodeError = value < 0 ? 'Bank is required' : '';
        break;
      case 'shortComment':
        formErr.descriptionError = !value.length ? 'Description required' : '';
        break;
      case 'amount':
        formErr.amountError = !value.length || value <= '0' ? 'Please enter a valid amount' : '';
        break;
      default:
        break;
    }

    setValues({
      ...values,
      formErrors: formErr,
      [event.target.name]: event.target.value,
    });
  };

  const handleIsDisable = () => {
    if (isFinancial && walletName && accountName && amount) {
      setIsDisable(false);
    } else if (!(walletName && values.bankIndex && accountName && amount)) {
      setIsDisable(true);
      return;
    } else {
      setIsDisable(false);
    }
  };

  const validateForm = () => {
    const [errors, hasError]: any = sendMoneyFormValidationRules(values, formErrors);
    setValues({ ...values, formErrors: errors });
    return hasError;
  };

  const verifyUser = async accNumber => {
    try {
      const theUser = await Axios.get(`/api/wallet-accounts/avs/${accNumber}`);
      // eslint-disable-next-line no-console
      console.log(theUser);
      setWalletName(theUser.data);
    } catch (_error) {
      alert('Error validating user wallet');
    }
  };

  const clearFields = () => {
    if (isFinancial) {
      setValues({
          ...values,
          amount: 0,
          shortComment: '',
        });
    } else {
      setValues(prevData => {
        return { ...prevData, amount: 0, bankIndex: -1, sourceAccountNumber: '', actual: '', accountNumber: '', shortComment: '' };
      });
      setWalletName('');
      setAccountName('');
    }
  };

  const handleSchSubmit = async () => {
    if (values.shortComment && values.amount) {
      const data = {
        specificChannel: 'sendMoney',
        accountNumber: values.accountNumber,
        amount: Number(amount),
        channel: 'wallettobank',
        sourceBankCode: values.sourceBankCode,
        sourceAccountNumber: values.sourceAccountNumber,
        destBankCode: values.destBankCode,
        narration:
          `transfer of NGN${amount} from ${values.sourceAccountNumber}(${walletName}'s wallet)
          to ${accountName} ${banks[0]?.bankName}`
      };
      // eslint-disable-next-line no-console
      console.log(data);
      try {
        const res = await Axios.post(`/api/send-money-bank-correspondent`, data);
        alert(res.data.message);
      } catch (err) {
        alert(err.message)
        console.error(err);
      } finally {
        setValues({
          ...values,
          shortComment: '',
          amount: 0,
        });
      }
    } else {
      setErrorMessage('Kindly fill all fields');
    }
  };

  const handleSubmit = (pin: string) => {
    // event.preventDefault();
    const isFormValid = validateForm();

    if (!isFormValid) {
      setLoading(true);
      setShow(false);
      setError(false);

      const ref = generateReference();
      const data = {
        ...values,
        amount: Number(amount),
        channel: channel === 'wallettobankmicro' ? 'wallettobank' : channel,
        destBankCode: (channel === 'wallettobankmicro' || channel === 'wallettobank') && bankIndex ? banks[bankIndex].bankCode : 'ABC',
        // sourceAccountNumber: walletDetails.accountNumber,
        transRef: ref,
        pin,
        beneficiaryName: accountName,
        narration:
          `transfer of ₦${amount} from ${values.sourceAccountNumber}(${walletName}'s wallet)
          to ${accountName}` +
          ((channel === 'wallettobankmicro' || channel === 'wallettobank') && bankIndex
            ? `, bank name: ${banks[bankIndex].bankName}`
            : 'Systemspecs Wallet'),
      };

      // eslint-disable-next-line no-console
      console.log(data);
      sendMoney(data)
        .then(res => {
          // eslint-disable-next-line no-console
          console.log('aaaaaaaaaaaaaaaaaaaaaaaa', res);

          setLoading(false);
          //   fetchBeneficiaries();
          setShowOtpModal(false);
          clearFields();
          alert(res.data.message);
          //   setSuccessModal(true);
          //   setShow(false);
          //   setError(false);
          //   setTimeout(() => {
          //     getWalletAccount().then(resp => {
          //       // setWalletDetails(resp[0]);
          //       // console.log(resp);

          //       getAccounts();
          //     });
          //   }, 5000);
        })
        .catch(err => {
          console.error(err, 'Error: pin invalid');
          setLoading(false);
          setShowOtpModal(false);
          clearFields();
          //   const errorResponse = err.response;
          //   if (errorResponse.status === 500) {
          //     setErrorMessage('A problem was encountered while processing your request');
          //   } else {
          //     setErrorMessage(errorResponse.data.message);
          //   }
          //   setShow(true);
          //   setError(true);
          //   setLoading(false);
        });
    } else {
      console.error('invalid form');
      return;
    }
  };

  const getSpecialWallet = async (schID): Promise<void> => {
    try {
      const theWallets = await Axios.get(`/api/wallet-accounts/special/${schID}`);
      // const theWallets = await Axios.get(`https://wallet.remita.net/api/wallet-accounts/special/${schID}`);
      setVendorWallets(theWallets.data.data);
      setSelectedWallet(theWallets.data.data[0].accountNumber);
      // eslint-disable-next-line no-console
      console.log(theWallets.data.data);
    } catch (e) {
      console.error(e);
    }
  };

  useEffect(() => {
    if (!isLoading && isFinancial) {
      getSpecialWallet(schemes[0].schemeID);
    }
  }, [isLoading]);

  useEffect(() => {
    loadBanks('commercial');
    if (selectedWallet) {
      verifyUser(selectedWallet);
      if (isMcPhersonFinancial) {
        setValues({ 
          ...values, 
          accountNumber: '1016167691' 
        });
      } else if(isPaymasta) {
        setValues({ 
          ...values,
          accountNumber: '5401161608' 
        });
      } else if(isWynk) {
        setValues({ 
          ...values,
          accountNumber: '4530008933' 
        });
      } else if(isWragby) {
        setValues({ 
          ...values,
          accountNumber: '0222686290' 
        });
      }
      setValues(prevState => ({ 
        ...prevState,
        bankIndex: 0, 
        sourceAccountNumber: selectedWallet,
        bankName: banks?.[0]?.bankName, 
        destBankCode: banks?.[0]?.bankCode 
      }));
    }
  }, [selectedWallet]);


  useEffect(() => {
    handleIsDisable();
  }, [walletName, values.bankIndex, accountName, amount]);

  useEffect(() => {
    if (isMcPhersonFinancial) {
      verifyAccountNumber('1016167691', '033');
    } else if(isPaymasta) {
      verifyAccountNumber('5401161608', '101');
    } else if(isWynk) {
      verifyAccountNumber('4530008933', '050');
    } else if(isWragby) {
      verifyAccountNumber('0222686290', '058');
    }
  }, [bankIndex]);
  // eslint-disable-next-line no-console
  console.log(values)

  return (
    <>
      <Layout>
        <InnerLayout dontfilter={true} show={true} title="Transfer Funds (external)" path="transferfund">
          <div className="transfer_box">
            <form>
              {isFinancial && (
                <div>
                  <select
                    onChange={e => setSelectedWallet(e.target.value)}
                    style={{width: '100%', marginBottom: '25px'}}
                    placeholder='Select Wallet'
                  >
                    {vendorWallets &&
                      vendorWallets.map(vw => (
                        <option key={vw.accountName} value={vw.accountNumber}>
                          {vw.accountName}
                        </option>
                      ))}
                  </select>
                </div>
              )}
              <div className="form-group  mb-4">
                <input
                  name="sourceAccountNumber"
                  type="number"
                  onChange={e => {
                    handleChange(e);
                    setActive(true);
                    if (e.target.value.length > 9) {
                      verifyUser(e.target.value);
                    }
                  }}
                  className={`form-control  ${active ? '' : 'grey'} ${formErrors.amountError ? 'is-error' : ''}`}
                  placeholder="Source account number"
                  value={values.sourceAccountNumber.length <= 0 ? ''  : values.sourceAccountNumber}
                  disabled={isFinancial}
                />
              </div>
              {walletName ? <p className="name-hint">Name on Record: {walletName}</p> : null}

              {!isFinancial && (
                <div className="form-group">
                  <select
                    className={`form-control  ${formErrors.channelError ? 'red' : ''}`}
                    name="channel"
                    onChange={handleChange}
                    value={values.channel}
                  >
                    {bankTypes.map((type, index: number) => (
                      <option key={index} value={type.value}>
                        {type.name}
                      </option>
                    ))}
                  </select>
                </div>
              )}
              {!isFinancial && showbanks && (
                <div className="form-group">
                  <Select
                    onChange={(value, e) => {
                      setAccountName('');
                      setValues({
                        ...values,
                        accountNumber: '',
                        bankIndex: value.value,
                        actual: value,
                        nameBank: value.label,
                      });
                    }}
                    placeholder={isBank ? localStorage.getItem('currentBank') : 'Select bank'}
                    options={options}
                    value={OptionTypeBase(values.actual)}
                    name="bankIndex"
                  />
                  <p className=" text-danger">
                    <small>{formErrors.destBankCodeError}</small>
                  </p>
                </div>
              )}

              <div className="form-group mb-4">
                <input
                  name="accountNumber"
                  onChange={e => {
                    setAccountName('');
                    handleChange(e);
                    verifyAccountNumber(e.target.value, '');
                  }}
                  className={`form-control ${accountNumber.length ? 'visited' : 'grey'} ${formErrors.accountNumberError ? 'is-error' : ''}`}
                  placeholder={showbanks ? 'Beneficiary Account Number' : 'Wallet Number'}
                  value={
                    values.accountNumber.length <= 0
                      ? ''
                      : isFinancial
                      ? `${values.accountNumber} (${options[0].label})`
                      : values.accountNumber
                  }
                  disabled={isFinancial}
                />
                <p className=" text-danger">
                  <small>{formErrors.accountNumberError}</small>
                </p>
                {tipErrorMessage ? (
                  <p className=" text-danger">
                    <small>{tipErrorMessage}</small>
                  </p>
                ) : null}
              </div>

              {nameLoading ? (
                <div className="name-hint text-center mb-3">
                  <Spinner style={{ color: '#fff' }} animation="border" role="status" size="sm">
                    <span className="sr-only">Loading...</span>
                  </Spinner>
                </div>
              ) : null}
              {errorFetchingName && !nameLoading && (
                <p style={{ background: 'red' }} className="name-hint">
                  Error fetching name
                </p>
              )}

              {!nameLoading && accountName && tip ? <p className="name-hint">Name on Record: {accountName}</p> : null}

              <div className="form-group d-flex">
                {/* <select
                className={`form-control naira-dropdown drop-d col-4 col-lg-2 mr-1" ${active ? 'touched' : ''}`}
                name="currency"
                onChange={handleChange}
              >
                <option>NGN:</option>
              </select> */}

                <input
                  name="amount"
                  type="number"
                  onChange={e => {
                    handleChange(e);
                    setActive(true);
                  }}
                  className={`form-control  ${active ? '' : 'grey'} ${formErrors.amountError ? 'is-error' : ''}`}
                  placeholder="Amount to Pay*"
                  value={values.amount <= 0 ? '' : values.amount}
                />
              </div>

              <div className="form-group mt-1">
                <textarea
                  name="shortComment"
                  onChange={handleChange}
                  className={`form-control text-area ${shortComment.length ? 'visited' : 'grey'} ${
                    formErrors.descriptionError ? 'is-error' : ''
                  }`}
                  placeholder="Description of Payment"
                  value={values.shortComment}
                ></textarea>
                <p className=" text-danger">
                  <small>{formErrors.descriptionError}</small>
                </p>
              </div>

              <div className="d-flex justify-content-center align-items-center mt-4">
                <button
                  disabled={isDisable}
                  type="button"
                  onClick={() => (isFinancial ? handleSchSubmit() : setShowOtpModal(true))}
                  className="btn btn-default-action bg-gray"
                >
                  {loading ? (
                    <Spinner style={{ color: '#fff' }} animation="border" role="status">
                      <span className="sr-only">Loading...</span>
                    </Spinner>
                  ) : (
                    'Transfer'
                  )}
                </button>
              </div>
            </form>
          </div>
        </InnerLayout>
      </Layout>
      <Pin
        show={showOtpModal}
        amount={Number(amount)}
        onHide={() => setShowOtpModal(false)}
        onSubmit={(pin: string) => handleSubmit(pin)}
        loading={loading}
        isAgent={true}
      />
    </>
  );
}
