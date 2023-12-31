import { hasAnyAuthority } from 'app/shared/util/hasAuthorities';

const config = {
  VERSION: process.env.VERSION,
};

export default config;

export const SERVER_API_URL = process.env.SERVER_API_URL;

export const AUTHORITIES = {
  ADMIN: 'ROLE_ADMIN',
  USER: 'ROLE_USER',
  OPERATIONS: 'ROLE_OPERATIONS',
  AUDIT: 'ROLE_AUDIT',
  ANALYST: 'ROLE_ANALYST',
  SUPPORT: 'ROLE_SUPPORT',
  ROLE_SUPER_ADMIN: 'ROLE_SUPER_ADMIN',
  MCPHERSON: 'ROLE_MCPHERSON_ADMIN',
  MCPHERSON_FINANCIAL: 'ROLE_MCPHERSON_FINANCIAL_ADMIN',
  FUOYE_ADMIN: 'ROLE_FUOYE_ADMIN',
  FUOYE_FINANCIAL: 'ROLE_FUOYE_FINANCIAL_ADMIN',
  SUPER_MCPHERSON: ['ROLE_MCPHERSON_FINANCIAL_ADMIN', 'ROLE_MCPHERSON_ADMIN'],
  PAYMASTA_ADMIN: 'ROLE_PAYMASTA_ADMIN',
  POLARIS_ADMIN: 'ROLE_POLARIS_ADMIN',
  WRAGBY_ADMIN: 'ROLE_WRAGBY_ADMIN',
  WYNK_ADMIN: 'ROLE_WYNK_ADMIN',
  ALL: ['ROLE_ADMIN', 'ROLE_OPERATIONS', 'ROLE_AUDIT', 'ROLE_ANALYST', 'ROLE_SUPPORT', 'ROLE_SUPER_ADMIN'],
};

export const messages = {
  DATA_ERROR_ALERT: 'Internal Error',
};

export const APP_DATE_FORMAT = 'DD/MM/YY HH:mm';
export const APP_TIMESTAMP_FORMAT = 'DD/MM/YY HH:mm:ss';
export const APP_LOCAL_DATE_FORMAT = 'DD/MM/YYYY';
export const APP_LOCAL_DATETIME_FORMAT = 'YYYY-MM-DDTHH:mm';
export const APP_LOCAL_DATETIME_FORMAT_Z = 'YYYY-MM-DDTHH:mm Z';
export const APP_WHOLE_NUMBER_FORMAT = '0,0';
export const APP_TWO_DIGITS_AFTER_POINT_NUMBER_FORMAT = '0,0.[00]';
