import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Row, Col, Alert } from 'reactstrap';
import { useHistory, Redirect } from 'react-router-dom';
//  import { IRootState } from 'app/shared/reducers';

import { hasAnyAuthority } from 'app/shared/util/hasAuthorities';
import { AUTHORITIES } from 'app/config/constants';

import './home.scss';

export type IHomeProp = StateProps;

export const Home = (props: IHomeProp) => {
  const isAdmin = hasAnyAuthority(props.account.authorities, [AUTHORITIES.ADMIN, AUTHORITIES.ROLE_SUPER_ADMIN]);
  const isMcPhersonFinancial = hasAnyAuthority(props.account.authorities, [AUTHORITIES.MCPHERSON_FINANCIAL]);
  const isPolaris = hasAnyAuthority(props.account.authorities, [AUTHORITIES.POLARIS_ADMIN]);
  const isMcPhersonAdmin = hasAnyAuthority(props.account.authorities, [AUTHORITIES.MCPHERSON]);
  const isWragby = hasAnyAuthority(props.account.authorities, [AUTHORITIES.WRAGBY_ADMIN]);

  const history: any = useHistory();
  const { isAuthenticated } = props;
  
  if (isAuthenticated) {
    if(isAdmin || isMcPhersonAdmin) {
      return <Redirect to="/dash" />
    } else if(isMcPhersonFinancial || isPolaris) {
      return <Redirect to="/dwallets" />
    } else {
      return <Redirect to="/customers" />
    }
  }

  return (
    <Row>
      <section className="home-bg">
        <h2 className="welcome-head">Welcome to Pouchii Admin Panel</h2>
        <p className="welcome-paragraph">Manage users, transactions, profiles, view metrics etc.</p>
        <button
          onClick={() => {
            history.push('/login');
          }}
          className="get-started-button"
        >
          Get started
        </button>
      </section>
    </Row>
  );
};

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
});

type StateProps = ReturnType<typeof mapStateToProps>;

export default connect(mapStateToProps)(Home);
