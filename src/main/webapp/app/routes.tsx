import React from 'react';
import { Route, Switch } from 'react-router-dom';
import Loadable from 'react-loadable';
import Login from 'app/modules/login/login';
import Register from 'app/modules/account/register/register';
import Activate from 'app/modules/account/activate/activate';
import PasswordResetInit from 'app/modules/account/password-reset/init/password-reset-init';
import PasswordResetFinish from 'app/modules/account/password-reset/finish/password-reset-finish';
import Logout from 'app/modules/login/logout';
import Home from 'app/modules/home/home';
import Entities from 'app/entities';
import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import PageNotFound from 'app/shared/error/page-not-found';
import { AUTHORITIES } from 'app/config/constants';
import Journal from 'app/modules/Journal/Journal';
import Wallets from 'app/modules/reports/Wallets';
import StatementOfAccount from 'app/modules/reports/Statement/StatementOfAccount';
import StatementOfAccountfUll from 'app/modules/reports/Statement/StatementOfAccountAll';
import Dashboad from 'app/modules/dashboard/dashboard/Dashboard';
import Customers from 'app/modules/dashboard/customers/Customers';
import DashWallets from 'app/modules/dashboard/wallets/Wallets';
import Report from 'app/modules/dashboard/reports/Report';
import WalletTransactions from 'app/modules/dashboard/wallets/WalletTransactions';
import AllTransactions from 'app/modules/dashboard/transactions/Transactions';
import Details from 'app/modules/dashboard/customers/CustomerDetails';
import ServiceHealth from 'app/modules/dashboard/servicesUpTime';
import CallOverReport from 'app/modules/reports/CallOverReport/CallOverReport';
import SchemeCustomerAccountBalance from 'app/modules/reports/schemeCustomerAccount/SchemeCustomerAccountBalance';
import VerifyRRR from 'app/modules/dashboard/verifyRRR/VerifyRRR';
import SendMoney from 'app/modules/dashboard/SendMoney/SendMoney';
import AccessRights from 'app/modules/dashboard/Rights/Rights';
import Schemes from 'app/modules/dashboard/schemes/Scheme';
import TransitAccountTransactions from 'app/modules/dashboard/TransitAccount/TransitAccountTransactions';
import Rights from 'app/modules/dashboard/Rights/Rights';
import CreateRequest from 'app/modules/dashboard/CreateRequest/CreateRequest';
import Requests from 'app/modules/dashboard/Requests/Requests';
import RequestsStatus from 'app/modules/dashboard/Requests/RequestStatus';
import FundWallet from 'app/modules/fundWallet/FundWallet';
import CreateApprovalGroup from 'app/modules/dashboard/administration/ApprovalGroup';
import EditApprovalGroup from 'app/modules/dashboard/administration/EditApprovalGroup';
import ApprovalGroup from 'app/modules/dashboard/administration/ApprovalGroupsView';
import ApprovalWorkFlow from 'app/modules/dashboard/administration/ApprovalWorkFlow';
import CreateApprovalWorkFlow from 'app/modules/dashboard/administration/CreateApprovalWorkFlow';
import EditApprovalWorkFlow from 'app/modules/dashboard/administration/EditApprovalWorkFlow';
import EndOfDayReport from 'app/modules/dashboard/endOfDayReport/EndofDayReport';
import SuperAgents from 'app/modules/dashboard/dashboard/superAgents/SupeerAgents';
import AppManagement from 'app/modules/dashboard/AppManagement/AppManagement';
import ChangePassword from './modules/dashboard/changePassword/ChangPassword';

import ShowUsers from './modules/dashboard/showUsers/ShowUsers';
import KycRequest from './modules/dashboard/administration/kycRequest/KycRequest';
import PreviousKycRequest from './modules/dashboard/administration/kycRequest/previousRequest/PreviousRequest';

import DownloadWallets from 'app/modules/dashboard/wallets/downloadWallet/DownloadWallets';
import KycDocuments from './modules/dashboard/showUserDocs/ShowUserDocs';
import CashConnectReport from './modules/reportCashConnect/ReportCashConnect';
import CashConnectReportDetails from './modules/reportCashConnect/reportDetail/ReportDetail';
import ContactUs from './modules/dashboard/contactUs/ContactUs';
import Messaging from './modules/dashboard/messaging/Messaging';
import TransferFund from './modules/dashboard/transferFund/TransferFund';
import CardManagement from './modules/dashboard/cardManagement/CardManagement';
import CardRequest from './modules/dashboard/cardManagement/cardRequest/CardRequest';
import CardReport from './modules/dashboard/cardManagement/cardReport/CardReport';
import PostedAdverts from './modules/dashboard/postedRequests/PostedAdverts';
import PostedFeeds from './modules/dashboard/postedRequests/PostedFeeds';
import CurrentPost from './modules/dashboard/postedRequests/currentPost/CurrentPost';
import IbileReceipts from './modules/dashboard/ibileReceipts/IbileReceipts';

const Account = Loadable({
  loader: () => import(/* webpackChunkName: "account" */ 'app/modules/account'),
  loading: () => <div>loading ...</div>,
});

const Admin = Loadable({
  loader: () => import(/* webpackChunkName: "administration" */ 'app/modules/administration'),
  loading: () => <div>loading ...</div>,
});

const Routes = () => (
  <div className="view-routes">
    <Switch>
      <PrivateRoute path="/app-management" component={AppManagement} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <PrivateRoute path="/dash" component={Dashboad} hasAnyAuthorities={[...AUTHORITIES.ALL]} />
      <PrivateRoute
        path="/customers"
        component={Customers}
        hasAnyAuthorities={[
          ...AUTHORITIES.ALL,
          AUTHORITIES.MCPHERSON,
          AUTHORITIES.PAYMASTA_ADMIN,
          AUTHORITIES.WRAGBY_ADMIN,
          AUTHORITIES.WYNK_ADMIN,
          AUTHORITIES.FUOYE_ADMIN,
        ]}
      />
      <PrivateRoute path="/create-approval-group" component={CreateApprovalGroup} hasAnyAuthorities={[...AUTHORITIES.ALL]} />
      <PrivateRoute path="/approval-groups" component={ApprovalGroup} hasAnyAuthorities={[...AUTHORITIES.ALL]} />
      <PrivateRoute path="/approval/edit/:id" component={EditApprovalGroup} hasAnyAuthorities={[...AUTHORITIES.ALL]} />
      <PrivateRoute path="/approval-workflow" component={ApprovalWorkFlow} hasAnyAuthorities={[...AUTHORITIES.ALL]} />
      <PrivateRoute path="/create-approval-workflow" component={CreateApprovalWorkFlow} hasAnyAuthorities={[...AUTHORITIES.ALL]} />
      <PrivateRoute path="/workflow/edit/:id" component={EditApprovalWorkFlow} hasAnyAuthorities={[...AUTHORITIES.ALL]} />
      <PrivateRoute
        path="/customers-details"
        component={Details}
        hasAnyAuthorities={[
          ...AUTHORITIES.ALL,
          AUTHORITIES.MCPHERSON,
          AUTHORITIES.PAYMASTA_ADMIN,
          AUTHORITIES.WRAGBY_ADMIN,
          AUTHORITIES.WYNK_ADMIN,
          AUTHORITIES.FUOYE_ADMIN,
        ]}
      />
      <PrivateRoute
        path="/dwallets"
        component={DashWallets}
        hasAnyAuthorities={[
          ...AUTHORITIES.ALL,
          AUTHORITIES.MCPHERSON_FINANCIAL,
          AUTHORITIES.PAYMASTA_ADMIN,
          AUTHORITIES.POLARIS_ADMIN,
          AUTHORITIES.WRAGBY_ADMIN,
          AUTHORITIES.WYNK_ADMIN,
          AUTHORITIES.FUOYE_FINANCIAL,
        ]}
      />
      <PrivateRoute
        path="/reports"
        component={Report}
        hasAnyAuthorities={[
          ...AUTHORITIES.ALL,
          AUTHORITIES.MCPHERSON_FINANCIAL,
          AUTHORITIES.PAYMASTA_ADMIN,
          AUTHORITIES.POLARIS_ADMIN,
          AUTHORITIES.WRAGBY_ADMIN,
          AUTHORITIES.WYNK_ADMIN,
          AUTHORITIES.FUOYE_FINANCIAL,
        ]}
      />
      <PrivateRoute path="/health" component={ServiceHealth} hasAnyAuthorities={[...AUTHORITIES.ALL]} />
      <PrivateRoute
        path="/wallet-transaction"
        component={WalletTransactions}
        hasAnyAuthorities={[
          ...AUTHORITIES.ALL,
          AUTHORITIES.MCPHERSON_FINANCIAL,
          AUTHORITIES.PAYMASTA_ADMIN,
          AUTHORITIES.POLARIS_ADMIN,
          AUTHORITIES.WRAGBY_ADMIN,
          AUTHORITIES.WYNK_ADMIN,
          AUTHORITIES.FUOYE_FINANCIAL,
        ]}
      />
      <PrivateRoute
        path="/alltransactions"
        component={AllTransactions}
        hasAnyAuthorities={[
          ...AUTHORITIES.ALL,
          AUTHORITIES.MCPHERSON_FINANCIAL,
          AUTHORITIES.PAYMASTA_ADMIN,
          AUTHORITIES.POLARIS_ADMIN,
          AUTHORITIES.WRAGBY_ADMIN,
          AUTHORITIES.WYNK_ADMIN,
          AUTHORITIES.FUOYE_FINANCIAL,
        ]}
      />
      <PrivateRoute path="/journal" component={Journal} hasAnyAuthorities={[...AUTHORITIES.ADMIN]} />
      <PrivateRoute path="/wallets" component={Wallets} hasAnyAuthorities={[...AUTHORITIES.ALL]} />
      <PrivateRoute
        path="/mini-statement"
        component={StatementOfAccount}
        hasAnyAuthorities={[
          ...AUTHORITIES.ALL,
          AUTHORITIES.MCPHERSON_FINANCIAL,
          AUTHORITIES.PAYMASTA_ADMIN,
          AUTHORITIES.POLARIS_ADMIN,
          AUTHORITIES.WRAGBY_ADMIN,
          AUTHORITIES.WYNK_ADMIN,
          AUTHORITIES.FUOYE_FINANCIAL,
        ]}
      />
      <PrivateRoute
        path="/change-password"
        component={ChangePassword}
        hasAnyAuthorities={[AUTHORITIES.MCPHERSON, AUTHORITIES.MCPHERSON_FINANCIAL, AUTHORITIES.FUOYE_FINANCIAL, AUTHORITIES.FUOYE_ADMIN]}
      />
      <PrivateRoute
        path="/full-statement"
        component={StatementOfAccountfUll}
        hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.OPERATIONS]}
      />
      <PrivateRoute
        path="/scheme-report"
        component={SchemeCustomerAccountBalance}
        hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.OPERATIONS]}
      />
      <PrivateRoute path="/callover" component={CallOverReport} hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.OPERATIONS]} />
      <PrivateRoute path="/verifyRRR" component={VerifyRRR} hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.OPERATIONS]} />
      <PrivateRoute path="/sendmoney" component={SendMoney} hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.ROLE_SUPER_ADMIN]} />
      <PrivateRoute
        path="/transferfund"
        component={TransferFund}
        hasAnyAuthorities={[
          AUTHORITIES.ADMIN,
          AUTHORITIES.MCPHERSON_FINANCIAL,
          AUTHORITIES.PAYMASTA_ADMIN,
          AUTHORITIES.WRAGBY_ADMIN,
          AUTHORITIES.WYNK_ADMIN,
          AUTHORITIES.FUOYE_FINANCIAL,
        ]}
      />
      <PrivateRoute path="/end-reports" component={EndOfDayReport} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <PrivateRoute path="/rights" component={Rights} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <PrivateRoute
        path="/create-request"
        component={CreateRequest}
        hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.OPERATIONS, AUTHORITIES.SUPPORT]}
      />
      <PrivateRoute
        path="/requests"
        component={Requests}
        hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.OPERATIONS, AUTHORITIES.SUPPORT]}
      />
      <PrivateRoute path="/request-status" component={RequestsStatus} hasAnyAuthorities={[...AUTHORITIES.ALL]} />
      <PrivateRoute path="/schemes" component={Schemes} hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.OPERATIONS]} />
      <PrivateRoute
        path="/transit"
        component={TransitAccountTransactions}
        hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.OPERATIONS]}
      />

      <PrivateRoute path="/external-reports" component={CashConnectReport} hasAnyAuthorities={[...AUTHORITIES.ALL]} />
      <PrivateRoute path="/cashconnect-reports-details" component={CashConnectReportDetails} hasAnyAuthorities={[...AUTHORITIES.ALL]} />
      <PrivateRoute path="/show-nuban" component={ShowUsers} hasAnyAuthorities={[...AUTHORITIES.ALL]} />
      <PrivateRoute path="/kyc-request" component={KycRequest} hasAnyAuthorities={[...AUTHORITIES.ALL]} />
      <PrivateRoute path="/previous-kyc-request" component={PreviousKycRequest} hasAnyAuthorities={[...AUTHORITIES.ALL]} />
      <PrivateRoute
        path="/ibile-receipts"
        component={IbileReceipts}
        hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.OPERATIONS, AUTHORITIES.SUPPORT]}
      />

      <PrivateRoute path="/dowmload-wallets" component={DownloadWallets} hasAnyAuthorities={[...AUTHORITIES.ALL]} />
      <PrivateRoute path="/kyc-docs" component={KycDocuments} hasAnyAuthorities={[...AUTHORITIES.ALL]} />
      <PrivateRoute path="/contact-us/:key?" component={ContactUs} hasAnyAuthorities={[...AUTHORITIES.ALL]} />
      <PrivateRoute
        path="/messaging"
        component={Messaging}
        hasAnyAuthorities={[
          ...AUTHORITIES.ALL,
          AUTHORITIES.MCPHERSON,
          AUTHORITIES.PAYMASTA_ADMIN,
          AUTHORITIES.WRAGBY_ADMIN,
          AUTHORITIES.WYNK_ADMIN,
          AUTHORITIES.FUOYE_ADMIN,
        ]}
      />
      <PrivateRoute path="/card-summary" component={CardManagement} hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.OPERATIONS]} />
      <PrivateRoute path="/card-request" component={CardRequest} hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.OPERATIONS]} />
      <PrivateRoute path="/card-report" component={CardReport} hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.OPERATIONS]} />
      <PrivateRoute
        path="/posted-adverts"
        component={PostedAdverts}
        hasAnyAuthorities={[
          AUTHORITIES.ROLE_SUPER_ADMIN,
          AUTHORITIES.ADMIN,
          AUTHORITIES.OPERATIONS,
          AUTHORITIES.MCPHERSON,
          AUTHORITIES.MCPHERSON_FINANCIAL,
          AUTHORITIES.FUOYE_FINANCIAL,
          AUTHORITIES.FUOYE_ADMIN,
        ]}
      />
      <PrivateRoute
        path="/posted-feeds"
        component={PostedFeeds}
        hasAnyAuthorities={[
          AUTHORITIES.ROLE_SUPER_ADMIN,
          AUTHORITIES.ADMIN,
          AUTHORITIES.OPERATIONS,
          AUTHORITIES.MCPHERSON,
          AUTHORITIES.MCPHERSON_FINANCIAL,
          AUTHORITIES.FUOYE_FINANCIAL,
          AUTHORITIES.FUOYE_ADMIN,
        ]}
      />
      <PrivateRoute
        path="/posts/:key?"
        component={CurrentPost}
        hasAnyAuthorities={[
          AUTHORITIES.ADMIN,
          AUTHORITIES.OPERATIONS,
          AUTHORITIES.MCPHERSON,
          AUTHORITIES.MCPHERSON_FINANCIAL,
          AUTHORITIES.FUOYE_FINANCIAL,
          AUTHORITIES.FUOYE_ADMIN,
        ]}
      />

      <Route path="/fund/:phone/:email/:amount/:wallet/:firstName/:lastName" exact component={FundWallet} />
      <ErrorBoundaryRoute path="/login" component={Login} />
      <ErrorBoundaryRoute path="/logout" component={Logout} />
      <ErrorBoundaryRoute path="/account/register" component={Register} />
      <ErrorBoundaryRoute path="/account/activate/:key?" component={Activate} />
      <ErrorBoundaryRoute path="/account/reset/request" component={PasswordResetInit} />
      <ErrorBoundaryRoute path="/account/reset/finish/:key?" component={PasswordResetFinish} />
      <PrivateRoute path="/admin" component={Admin} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <PrivateRoute path="/super-agents" component={SuperAgents} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <PrivateRoute path="/account" component={Account} hasAnyAuthorities={[...AUTHORITIES.ALL, AUTHORITIES.USER]} />
      <ErrorBoundaryRoute path="/" exact component={Home} />
      <PrivateRoute path="/" component={Entities} hasAnyAuthorities={[AUTHORITIES.USER]} />
      <ErrorBoundaryRoute component={PageNotFound} />
    </Switch>
  </div>
);

export default Routes;
