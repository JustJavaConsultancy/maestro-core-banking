import axios from 'axios';

import { SUCCESS } from 'app/shared/reducers/action-type.util';

export interface ActionInterface {
  SET_WALLET: string;
  SET_USER: string;
  GET_SCHEMES: string;
}

export interface InitialStateInterface {
  data: object;
  user: object;
  schemes: object;
}

export const ACTION_TYPES: ActionInterface = {
  SET_WALLET: 'applicationREPORT/GET_REPORT',
  SET_USER: 'applicationREPORT/SET_USER',
  GET_SCHEMES: 'applicationREPORT/GET_SCHEMES',
};

const initialState: InitialStateInterface = {
  data: {},
  user: {},
  schemes: {},
};

export type ApplicationReportState = Readonly<typeof initialState>;
export default (state: ApplicationReportState = initialState, action): ApplicationReportState => {
  switch (action.type) {
    case ACTION_TYPES.SET_WALLET:
      //  alert('hello')
      return {
        ...state,
        data: action.payload,
      };
    case ACTION_TYPES.SET_USER:
      return {
        ...state,
        user: action.payload,
      };
    case ACTION_TYPES.GET_SCHEMES:
      return {
        ...state,
        schemes: action.payload,
      };
    default:
      return {
        ...state,
      };
  }
};

export const setWalletData = data => ({
  type: ACTION_TYPES.SET_WALLET,
  payload: data,
});

export const setUser = user => ({
  type: ACTION_TYPES.SET_USER,
  payload: user,
});

export const setUserSchemes = (schemes: any) => ({
  type: ACTION_TYPES.GET_SCHEMES,
  payload: schemes,
});
