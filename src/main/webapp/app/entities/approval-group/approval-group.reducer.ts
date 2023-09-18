import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IApprovalGroup, defaultValue } from 'app/shared/model/approval-group.model';

export const ACTION_TYPES = {
  FETCH_APPROVALGROUP_LIST: 'approvalGroup/FETCH_APPROVALGROUP_LIST',
  FETCH_APPROVALGROUP: 'approvalGroup/FETCH_APPROVALGROUP',
  CREATE_APPROVALGROUP: 'approvalGroup/CREATE_APPROVALGROUP',
  UPDATE_APPROVALGROUP: 'approvalGroup/UPDATE_APPROVALGROUP',
  DELETE_APPROVALGROUP: 'approvalGroup/DELETE_APPROVALGROUP',
  RESET: 'approvalGroup/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IApprovalGroup>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type ApprovalGroupState = Readonly<typeof initialState>;

// Reducer

export default (state: ApprovalGroupState = initialState, action): ApprovalGroupState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_APPROVALGROUP_LIST):
    case REQUEST(ACTION_TYPES.FETCH_APPROVALGROUP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_APPROVALGROUP):
    case REQUEST(ACTION_TYPES.UPDATE_APPROVALGROUP):
    case REQUEST(ACTION_TYPES.DELETE_APPROVALGROUP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_APPROVALGROUP_LIST):
    case FAILURE(ACTION_TYPES.FETCH_APPROVALGROUP):
    case FAILURE(ACTION_TYPES.CREATE_APPROVALGROUP):
    case FAILURE(ACTION_TYPES.UPDATE_APPROVALGROUP):
    case FAILURE(ACTION_TYPES.DELETE_APPROVALGROUP):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_APPROVALGROUP_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_APPROVALGROUP):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_APPROVALGROUP):
    case SUCCESS(ACTION_TYPES.UPDATE_APPROVALGROUP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_APPROVALGROUP):
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

const apiUrl = 'api/approval-groups';

// Actions

export const getEntities: ICrudGetAllAction<IApprovalGroup> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_APPROVALGROUP_LIST,
  payload: axios.get<IApprovalGroup>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IApprovalGroup> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_APPROVALGROUP,
    payload: axios.get<IApprovalGroup>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IApprovalGroup> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_APPROVALGROUP,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IApprovalGroup> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_APPROVALGROUP,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IApprovalGroup> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_APPROVALGROUP,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
