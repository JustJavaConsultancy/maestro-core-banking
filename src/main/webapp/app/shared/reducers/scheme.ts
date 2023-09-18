import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

const SET_SCHEME = 'dashboard/SET_SCHEME';

export interface SchemeInterface {
  id: number;
  schemeID: string;
  scheme: string;
  accountNumber: string;
  bankCode: string;
  callBackUrl: any;
}

const initialState: Readonly<SchemeInterface> = {
  id: 1,
  schemeID: '53797374656d73706563732077616c6c6574',
  scheme: 'Systemspecs wallet',
  accountNumber: '1790307208',
  bankCode: '076',
  callBackUrl: null,
};

const schemeReducer = (state = initialState, action): SchemeInterface => {
  switch (action.type) {
    case SUCCESS(SET_SCHEME):
      return {
        ...state,
        ...action.payload,
      };
    default:
      return state;
  }
};

export const setGlobalScheme = payload => dispatch => {
  dispatch({
    type: SET_SCHEME,
    payload,
  });
};

export default schemeReducer;
