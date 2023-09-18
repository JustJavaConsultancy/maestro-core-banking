import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ApprovalGroup from './approval-group';
import ApprovalGroupDetail from './approval-group-detail';
import ApprovalGroupUpdate from './approval-group-update';
import ApprovalGroupDeleteDialog from './approval-group-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ApprovalGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ApprovalGroupUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ApprovalGroupDetail} />
      <ErrorBoundaryRoute path={match.url} component={ApprovalGroup} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ApprovalGroupDeleteDialog} />
  </>
);

export default Routes;
