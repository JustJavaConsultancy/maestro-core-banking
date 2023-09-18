import 'react-toastify/dist/ReactToastify.css';
import "react-loader-spinner/dist/loader/css/react-spinner-loader.css";
import './app.scss';

import React, { useEffect } from 'react';
import { connect, useSelector } from 'react-redux';
import { Card } from 'reactstrap';
import { BrowserRouter as Router } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import { hot } from 'react-hot-loader';

import { IRootState } from 'app/shared/reducers';
import { getSession, logout } from 'app/shared/reducers/authentication';
import { getProfile } from 'app/shared/reducers/application-profile';
import { setLocale } from 'app/shared/reducers/locale';
import Header from 'app/shared/layout/header/header';
import Footer from 'app/shared/layout/footer/footer';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import ErrorBoundary from 'app/shared/error/error-boundary';
import { AUTHORITIES } from 'app/config/constants';
import AppRoutes from 'app/routes';
import { useIdleTimer } from 'react-idle-timer';

const baseHref = document.querySelector('base').getAttribute('href').replace(/\/$/, '');

export interface IAppProps extends StateProps, DispatchProps {}

export const App = (props: IAppProps) => {
  const handleOnIdle = event => {
    if (props.isAuthenticated) {
      //  console.error('last active');
      //  history.push("/logout");
      alert('your session has expired you will be logged out');
      props.logout();
      window.location.href = '/logout';
    }
  };

  const handleOnActive = event => {
    // console.error('user is active', event)
    // console.error('time remaining')
  };

  const handleOnAction = e => {
    // console.error('user did something', e)
  };

  const { getRemainingTime, getLastActiveTime } = useIdleTimer({
    timeout: 1000 * 60 * 5,
    onIdle: handleOnIdle,
    onActive: handleOnActive,
    onAction: handleOnAction,
    debounce: 500,
  });

  useEffect(() => {
    props.getSession();
    props.getProfile();
  }, []);

  const paddingTop = '60px';
  return (
    <Router basename={baseHref}>
      <div className="app-container" style={{ paddingTop }}>
        <ToastContainer position={toast.POSITION.TOP_LEFT} className="toastify-container" toastClassName="toastify-toast" />
        <ErrorBoundary>
          {!props.isAuthenticated && (
            <Header
              isAuthenticated={props.isAuthenticated}
              isAdmin={props.isAdmin}
              currentLocale={props.currentLocale}
              onLocaleChange={props.setLocale}
              ribbonEnv={props.ribbonEnv}
              isInProduction={true} //  {props.isInProduction}
              isSwaggerEnabled={props.isSwaggerEnabled}
            />
          )}
        </ErrorBoundary>

        <ErrorBoundary>
          <AppRoutes />
        </ErrorBoundary>
        {/* </Card> */}
        {/* <Footer />  */}
      </div>
    </Router>
  );
};

const mapStateToProps = ({ authentication, applicationProfile, locale }: IRootState) => ({
  currentLocale: locale.currentLocale,
  isAuthenticated: authentication.isAuthenticated,
  isAdmin: hasAnyAuthority(authentication.account.authorities, [AUTHORITIES.ADMIN]),
  ribbonEnv: applicationProfile.ribbonEnv,
  isInProduction: applicationProfile.inProduction,
  isSwaggerEnabled: applicationProfile.isSwaggerEnabled,
});

const mapDispatchToProps = { setLocale, getSession, getProfile, logout };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(hot(module)(App));
