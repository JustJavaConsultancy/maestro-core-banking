import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IApprovalWorkflow, defaultValue } from 'app/shared/model/approval-workflow.model';

export const ACTION_TYPES = {
  FETCH_APPROVALWORKFLOW_LIST: 'approvalWorkflow/FETCH_APPROVALWORKFLOW_LIST',
  FETCH_APPROVALWORKFLOW: 'approvalWorkflow/FETCH_APPROVALWORKFLOW',
  CREATE_APPROVALWORKFLOW: 'approvalWorkflow/CREATE_APPROVALWORKFLOW',
  UPDATE_APPROVALWORKFLOW: 'approvalWorkflow/UPDATE_APPROVALWORKFLOW',
  DELETE_APPROVALWORKFLOW: 'approvalWorkflow/DELETE_APPROVALWORKFLOW',
  RESET: 'approvalWorkflow/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IApprovalWorkflow>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type ApprovalWorkflowState = Readonly<typeof initialState>;

// Reducer

export default (state: ApprovalWorkflowState = initialState, action): ApprovalWorkflowState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_APPROVALWORKFLOW_LIST):
    case REQUEST(ACTION_TYPES.FETCH_APPROVALWORKFLOW):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_APPROVALWORKFLOW):
    case REQUEST(ACTION_TYPES.UPDATE_APPROVALWORKFLOW):
    case REQUEST(ACTION_TYPES.DELETE_APPROVALWORKFLOW):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_APPROVALWORKFLOW_LIST):
    case FAILURE(ACTION_TYPES.FETCH_APPROVALWORKFLOW):
    case FAILURE(ACTION_TYPES.CREATE_APPROVALWORKFLOW):
    case FAILURE(ACTION_TYPES.UPDATE_APPROVALWORKFLOW):
    case FAILURE(ACTION_TYPES.DELETE_APPROVALWORKFLOW):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_APPROVALWORKFLOW_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_APPROVALWORKFLOW):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_APPROVALWORKFLOW):
    case SUCCESS(ACTION_TYPES.UPDATE_APPROVALWORKFLOW):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_APPROVALWORKFLOW):
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

const apiUrl = 'api/approval-workflows';

// Actions

export const getEntities: ICrudGetAllAction<IApprovalWorkflow> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_APPROVALWORKFLOW_LIST,
  payload: axios.get<IApprovalWorkflow>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IApprovalWorkflow> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_APPROVALWORKFLOW,
    payload: axios.get<IApprovalWorkflow>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IApprovalWorkflow> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_APPROVALWORKFLOW,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IApprovalWorkflow> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_APPROVALWORKFLOW,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IApprovalWorkflow> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_APPROVALWORKFLOW,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
