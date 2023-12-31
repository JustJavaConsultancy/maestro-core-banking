import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './scheme-category-my-suffix.reducer';
import { ISchemeCategoryMySuffix } from 'app/shared/model/scheme-category-my-suffix.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISchemeCategoryMySuffixProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const SchemeCategoryMySuffix = (props: ISchemeCategoryMySuffixProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { schemeCategoryList, match, loading } = props;
  return (
    <div>
      <h2 id="scheme-category-my-suffix-heading">
        <Translate contentKey="apiGatewayApp.schemeCategory.home.title">Scheme Categories</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="apiGatewayApp.schemeCategory.home.createLabel">Create new Scheme Category</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {schemeCategoryList && schemeCategoryList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="apiGatewayApp.schemeCategory.schemecategoryID">Schemecategory ID</Translate>
                </th>
                <th>
                  <Translate contentKey="apiGatewayApp.schemeCategory.schemeCategory">Scheme Category</Translate>
                </th>
                <th>
                  <Translate contentKey="apiGatewayApp.schemeCategory.description">Description</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {schemeCategoryList.map((schemeCategory, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${schemeCategory.id}`} color="link" size="sm">
                      {schemeCategory.id}
                    </Button>
                  </td>
                  <td>{schemeCategory.schemecategoryID}</td>
                  <td>{schemeCategory.schemeCategory}</td>
                  <td>{schemeCategory.description}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${schemeCategory.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${schemeCategory.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${schemeCategory.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="apiGatewayApp.schemeCategory.home.notFound">No Scheme Categories found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ schemeCategory }: IRootState) => ({
  schemeCategoryList: schemeCategory.entities,
  loading: schemeCategory.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SchemeCategoryMySuffix);
