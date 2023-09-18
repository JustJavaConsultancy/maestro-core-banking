import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './approval-workflow.reducer';
import { IApprovalWorkflow } from 'app/shared/model/approval-workflow.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IApprovalWorkflowDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ApprovalWorkflowDetail = (props: IApprovalWorkflowDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { approvalWorkflowEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="apiGatewayApp.approvalWorkflow.detail.title">ApprovalWorkflow</Translate> [
          <b>{approvalWorkflowEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">
              <Translate contentKey="apiGatewayApp.approvalWorkflow.name">Name</Translate>
            </span>
          </dt>
          <dd>{approvalWorkflowEntity.name}</dd>
          <dt>
            <Translate contentKey="apiGatewayApp.approvalWorkflow.initiator">Initiator</Translate>
          </dt>
          <dd>{approvalWorkflowEntity.initiatorName ? approvalWorkflowEntity.initiatorName : ''}</dd>
          <dt>
            <Translate contentKey="apiGatewayApp.approvalWorkflow.approver">Approver</Translate>
          </dt>
          <dd>{approvalWorkflowEntity.approverName ? approvalWorkflowEntity.approverName : ''}</dd>
          <dt>
            <Translate contentKey="apiGatewayApp.approvalWorkflow.transactionType">Transaction Type</Translate>
          </dt>
          <dd>{approvalWorkflowEntity.transactionTypeName ? approvalWorkflowEntity.transactionTypeName : ''}</dd>
        </dl>
        <Button tag={Link} to="/approval-workflow" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/approval-workflow/${approvalWorkflowEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ approvalWorkflow }: IRootState) => ({
  approvalWorkflowEntity: approvalWorkflow.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ApprovalWorkflowDetail);
