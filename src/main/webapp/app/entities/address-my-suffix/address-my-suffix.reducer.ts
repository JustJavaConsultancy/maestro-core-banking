import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAddressMySuffix, defaultValue } from 'app/shared/model/address-my-suffix.model';

export const ACTION_TYPES = {
  FETCH_ADDRESS_LIST: 'address/FETCH_ADDRESS_LIST',
  FETCH_ADDRESS: 'address/FETCH_ADDRESS',
  CREATE_ADDRESS: 'address/CREATE_ADDRESS',
  UPDATE_ADDRESS: 'address/UPDATE_ADDRESS',
  DELETE_ADDRESS: 'address/DELETE_ADDRESS',
  RESET: 'address/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAddressMySuffix>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type AddressMySuffixState = Readonly<typeof initialState>;

// Reducer

export default (state: AddressMySuffixState = initialState, action): AddressMySuffixState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ADDRESS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ADDRESS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ADDRESS):
    case REQUEST(ACTION_TYPES.UPDATE_ADDRESS):
    case REQUEST(ACTION_TYPES.DELETE_ADDRESS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_ADDRESS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ADDRESS):
    case FAILURE(ACTION_TYPES.CREATE_ADDRESS):
    case FAILURE(ACTION_TYPES.UPDATE_ADDRESS):
    case FAILURE(ACTION_TYPES.DELETE_ADDRESS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ADDRESS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ADDRESS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ADDRESS):
    case SUCCESS(ACTION_TYPES.UPDATE_ADDRESS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ADDRESS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/addresses';

// Actions

export const getEntities: ICrudGetAllAction<IAddressMySuffix> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ADDRESS_LIST,
  payload: axios.get<IAddressMySuffix>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IAddressMySuffix> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ADDRESS,
    payload: axios.get<IAddressMySuffix>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IAddressMySuffix> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ADDRESS,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAddressMySuffix> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ADDRESS,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAddressMySuffix> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ADDRESS,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
