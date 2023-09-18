import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IApprovalGroup } from 'app/shared/model/approval-group.model';
import { getEntities as getApprovalGroups } from 'app/entities/approval-group/approval-group.reducer';

import { getEntity, updateEntity, createEntity, reset } from './approval-workflow.reducer';
import { IApprovalWorkflow } from 'app/shared/model/approval-workflow.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IApprovalWorkflowUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ApprovalWorkflowUpdate = (props: IApprovalWorkflowUpdateProps) => {
  const [initiatorId, setInitiatorId] = useState('0');
  const [approverId, setApproverId] = useState('0');
  const [transactionTypeId, setTransactionTypeId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { approvalWorkflowEntity, approvalGroups, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/approval-workflow');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getApprovalGroups();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...approvalWorkflowEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="apiGatewayApp.approvalWorkflow.home.createOrEditLabel">
            <Translate contentKey="apiGatewayApp.approvalWorkflow.home.createOrEditLabel">Create or edit a ApprovalWorkflow</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : approvalWorkflowEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="approval-workflow-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="approval-workflow-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="approval-workflow-name">
                  <Translate contentKey="apiGatewayApp.approvalWorkflow.name">Name</Translate>
                </Label>
                <AvField
                  id="approval-workflow-name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="approval-workflow-initiator">
                  <Translate contentKey="apiGatewayApp.approvalWorkflow.initiator">Initiator</Translate>
                </Label>
                <AvInput id="approval-workflow-initiator" type="select" className="form-control" name="initiatorId" required>
                  {approvalGroups
                    ? approvalGroups.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.name}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <AvGroup>
                <Label for="approval-workflow-approver">
                  <Translate contentKey="apiGatewayApp.approvalWorkflow.approver">Approver</Translate>
                </Label>
                <AvInput id="approval-workflow-approver" type="select" className="form-control" name="approverId" required>
                  {approvalGroups
                    ? approvalGroups.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.name}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <AvGroup>
                <Label for="approval-workflow-transactionType">
                  <Translate contentKey="apiGatewayApp.approvalWorkflow.transactionType">Transaction Type</Translate>
                </Label>
                <AvInput id="approval-workflow-transactionType" type="select" className="form-control" name="transactionTypeId">
                  <option value="" key="0" />
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/approval-workflow" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  approvalGroups: storeState.approvalGroup.entities,
  approvalWorkflowEntity: storeState.approvalWorkflow.entity,
  loading: storeState.approvalWorkflow.loading,
  updating: storeState.approvalWorkflow.updating,
  updateSuccess: storeState.approvalWorkflow.updateSuccess,
});

const mapDispatchToProps = {
  getApprovalGroups,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ApprovalWorkflowUpdate);
