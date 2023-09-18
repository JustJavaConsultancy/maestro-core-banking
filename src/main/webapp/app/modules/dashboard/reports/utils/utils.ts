import Axios from 'axios';
import { isEmpty } from 'lodash';
import moment from 'moment';

export const getCustomerStatement = async (info, setStatement) => {
  try {
    const getCustomerReport = await Axios.get(
      `/api/account-statement/${info.accountNumber}/${!isEmpty(info.start) ? info.start : info.dateOpened}/${
        !isEmpty(info.end) ? info.end : info.dateOpened
      }`
    );
    let opening = 0;
    let closing = 0;

    if (getCustomerReport.data.length && getCustomerReport.data.length > 0) {
      const openingBalance =
        getCustomerReport.data[0]['creditDebit'] === 'debit'
          ? getCustomerReport.data[0]['currentBalance'] + getCustomerReport.data[0]['amount']
          : getCustomerReport.data[0]['currentBalance'] - getCustomerReport.data[0]['amount'];
      opening = openingBalance;
      closing = getCustomerReport.data[getCustomerReport.data.length - 1]['currentBalance'];
    }
    if (info.accountNumber === '') {
      setStatement(getCustomerReport.data.data);
      return getCustomerReport.data.data;
    } else {
      setStatement(getCustomerReport.data);
      return {
        res: getCustomerReport.data,
        opening,
        closing,
      };
    }
  } catch (e) {
    console.error(e);
  }
};

export const getGlobalStatement = async (info, setStatement) => {
  try {
    const getWalletAccount = await Axios.get(
      `/api/account-statement/scheme/${!isEmpty(info.start) ? info.start : info.dateOpened}/${
        !isEmpty(info.end) ? info.end : moment(Date()).format('YYYY-MM-DD')
      }/${info.schemeID}`
    );
    setStatement(getWalletAccount.data.data);
    return getWalletAccount.data.data;
  } catch (err) {
    console.error(err);
  }
};

export const getCalloverReport = async (info, setCalloverReport) => {
  try {
    let url;
    if (info.schemeID === '53797374656d73706563732077616c6c6574') {
      url = `/api/transaction/get-logs/${info.start}/${!isEmpty(info.end) ? info.end : moment(Date()).format('YYYY-MM-DD')}`;
    } else {
      url = `/api/transaction/logs/${info.schemeID}/${info.start}/${!isEmpty(info.end) ? info.end : moment(Date()).format('YYYY-MM-DD')}`;
    }
    const callOverReport = await Axios.get(url);
    setCalloverReport(callOverReport.data.data);
    return callOverReport.data.data;
  } catch (e) {
    console.error(e);
  }
};

export const getCustomerShemeBalance = async (info, setStatement): Promise<void> => {
  try {
    const theWallets = await Axios.get(`/api/wallet-accounts/customers/${info.schemeID}?size=100000`);
    setStatement(theWallets.data.data);
    return theWallets.data.data;
  } catch (e) {
    console.error(e);
  }
};
