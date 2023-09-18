import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ApprovalWorkflow from './approval-workflow';
import ApprovalWorkflowDetail from './approval-workflow-detail';
import ApprovalWorkflowUpdate from './approval-workflow-update';
import ApprovalWorkflowDeleteDialog from './approval-workflow-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ApprovalWorkflowUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ApprovalWorkflowUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ApprovalWorkflowDetail} />
      <ErrorBoundaryRoute path={match.url} component={ApprovalWorkflow} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ApprovalWorkflowDeleteDialog} />
  </>
);

export default Routes;
