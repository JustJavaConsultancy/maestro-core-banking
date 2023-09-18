import axios from 'axios';

interface BankType {
  name: string;
  value: string;
}

export interface Bank {
  bankName?: string;
  bankAccronym?: string;
  bankCode?: string;
  type?: string;
}
export interface SendMoneyFormErrors {
  accountNumberError: string;
  amountError: string;
  channelError: string;
  destBankCodeError: string;
  descriptionError: string;
}

export const bankTypes: BankType[] = [
  {
    name: 'Pay To Commercial Bank Account',
    value: 'wallettobank',
  },
  {
    name: 'Pay To Microfinance Bank',
    value: 'wallettobankmicro',
  },
  {
    name: 'Pay To  Wallet account',
    value: 'wallettowallet',
  },
];

export const getBanks = async (type: string) => {
  const result = await axios.get(`/api/banks/${type}`);
  // console.log(result.data);

  return result.data;
  // return {data: []}
};

export const verifyAccount = async (accountNumber: string, bankCode: string | undefined | any, accountType: string) => {
  const payload = {
    accountNumber,
    accountType,
    bankCode,
  };

  const result = await axios.post(`/api/verify-account`, payload);

  return result;
};

export const verifyWalletUser = async (walletNumber: string) => {
  const result = await axios.get(`/api/wallet-accounts/avs/${walletNumber}`);
  return result;
};

export const sendMoneyFormErrors = {
  accountNumberError: '',
  amountError: '',
  channelError: '',
  destBankCodeError: '',
  descriptionError: '',
};

export const sendMoneyFormValidationRules = (values: any, errors: SendMoneyFormErrors) => {
  let hasError = false;
  errors = { ...sendMoneyFormErrors };

  if (!values.accountNumber) {
    hasError = true;
    errors.accountNumberError = 'Account Number  is required';
  }
  if (!values.amount || values.amount === 0) {
    hasError = true;
    errors.amountError = 'Please enter a valid amount';
  }
  if (!values.channel) {
    hasError = true;
    errors.channelError = 'Channel is required';
  }
  if (values.bankIndex < 0 && !values.destBankCode && values.channel !== 'wallettowallet') {
    hasError = true;

    errors.destBankCodeError = 'Bank is required';
  }
  if (!values.shortComment) {
    hasError = true;
    errors.descriptionError = 'Description required';
  }

  return [errors, hasError];
};

export const generateReference = () => {
  const length = 4;
  let result = '';
  const characters = '0123456789';
  const charactersLength = characters.length;

  for (let i = 0; i < length; i++) {
    result += characters.charAt(Math.floor(Math.random() * charactersLength));
  }
  result = String(Number(Date.now())) + result;
  return result; // toUpperCase();
};

export const sendMoney = async (payload: any) => {
  const result = await axios.post(`/api/send-money`, payload);

  return result;
};

export const partnerDestBank = {
  mcPherson: {
    bankCode: '033',
    bankName: 'UBA PLC',
    accountNumber: '1016167691',
    bankAccronym: 'UBA',
  },
  paymasta: {
    bankCode: '101',
    bankName: 'Providus Bank',
    accountNumber: '5401161608',
    bankAccronym: 'PROVIDUS',
  },
  wynk: {
    bankCode: '050',
    bankName: 'ECOBANK NIGERIA PLC',
    accountNumber: '4530008933',
    bankAccronym: 'ECOBANK',
  },
  wragby: {
    bankCode: '058',
    bankName: 'GUARANTY TRUST BANK PLC',
    accountNumber: '0222686290',
    bankAccronym: 'GTB',
  },
};
