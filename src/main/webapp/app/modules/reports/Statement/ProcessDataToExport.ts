export type xcelData = {
  SN: number;
  Date: string;
  Reference: string;
  Narration: string;
  Debit: number;
  Credit: number;
  Balance: number;
};

const processData = dataToProcess => {
  const processedData: xcelData[] = [];
  const filterDataToDisplay: Promise<xcelData[]> = new Promise((resolve, reject) => {
    try {
      dataToProcess.forEach((element, i) => {
        const holder: xcelData = {
          SN: i + 1,
          Date: element.transactionDate,
          Reference: element.transactionRef,
          Narration: element.memo,
          Debit: element.debit,
          Credit: element.credit,
          Balance: element.currentBalance,
        };
        processedData.push(holder);
      });
      resolve(processedData);
    } catch (e) {
      reject(e);
    }
  });
  return filterDataToDisplay;
};

export default processData;
