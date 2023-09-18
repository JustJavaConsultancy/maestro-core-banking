import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './approval-workflow.reducer';
import { IApprovalWorkflow } from 'app/shared/model/approval-workflow.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IApprovalWorkflowProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const ApprovalWorkflow = (props: IApprovalWorkflowProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { approvalWorkflowList, match, loading } = props;
  return (
    <div>
      <h2 id="approval-workflow-heading">
        <Translate contentKey="apiGatewayApp.approvalWorkflow.home.title">Approval Workflows</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="apiGatewayApp.approvalWorkflow.home.createLabel">Create new Approval Workflow</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {approvalWorkflowList && approvalWorkflowList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="apiGatewayApp.approvalWorkflow.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="apiGatewayApp.approvalWorkflow.initiator">Initiator</Translate>
                </th>
                <th>
                  <Translate contentKey="apiGatewayApp.approvalWorkflow.approver">Approver</Translate>
                </th>
                <th>
                  <Translate contentKey="apiGatewayApp.approvalWorkflow.transactionType">Transaction Type</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {approvalWorkflowList.map((approvalWorkflow, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${approvalWorkflow.id}`} color="link" size="sm">
                      {approvalWorkflow.id}
                    </Button>
                  </td>
                  <td>{approvalWorkflow.name}</td>
                  <td>
                    {approvalWorkflow.initiatorName ? (
                      <Link to={`approval-group/${approvalWorkflow.initiatorId}`}>{approvalWorkflow.initiatorName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {approvalWorkflow.approverName ? (
                      <Link to={`approval-group/${approvalWorkflow.approverId}`}>{approvalWorkflow.approverName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {approvalWorkflow.transactionTypeName ? (
                      <Link to={`right/${approvalWorkflow.transactionTypeId}`}>{approvalWorkflow.transactionTypeName}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${approvalWorkflow.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${approvalWorkflow.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${approvalWorkflow.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="apiGatewayApp.approvalWorkflow.home.notFound">No Approval Workflows found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ approvalWorkflow }: IRootState) => ({
  approvalWorkflowList: approvalWorkflow.entities,
  loading: approvalWorkflow.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ApprovalWorkflow);
