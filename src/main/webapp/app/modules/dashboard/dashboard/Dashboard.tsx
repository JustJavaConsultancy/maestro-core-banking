import './Dashboard.scss';
import React, { useEffect, useState } from 'react';
//  import { Link } from 'react-router-dom';
//  import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
// import { Row, Col, Alert } from 'reactstrap';
import { Redirect, useHistory } from 'react-router-dom';
import Layout from 'app/modules/dashboard/Layout';
import { Card1 } from '../components/Card1';
import { Card2 } from '../components/Card2';
import { height } from '@fortawesome/free-solid-svg-icons/faSort';
import GraphCard from '../components/GraphCard';
import PictographCard from '../components/PictographCard';
import Axios from 'axios';
import { hasAnyAuthority } from 'app/shared/util/hasAuthorities';
import { AUTHORITIES } from 'app/config/constants';
import { Container, Row, Col } from 'react-bootstrap';
//  import phone from 'src/main/webapp/content/images/phone.png';
//  import wallet from 'src/main/webapp/content/images/wallet.png';
//  import { IRootState } from 'app/shared/reducers';

export type IDashboard = StateProps;

export const Dashboard = (props: IDashboard) => {
  // const { account } = props;
  // const history:any = useHistory();
  //  const component:object;
  const [meta, setMeta] = useState({
    customerCount: 0,
    agentTransactionCount: 0,
    transactionCount: 0,
    totalDeposits: 0,
    totalWithdrawals: 0,
    walletCount: 0,
  });
  const history = useHistory();

  const getMeta = async () => {
    try {
      const theMeta = await Axios.get('/api/summary');
      setMeta(theMeta.data.data);
      // console.log(theMeta);
    } catch (e) {
      console.error(e);
    }
  };

  useEffect(() => {
    getMeta();
  }, []);

  // const isAdmin = hasAnyAuthority(props.account.authorities, [AUTHORITIES.ADMIN, AUTHORITIES.ROLE_SUPER_ADMIN]);
  // const isMcPhersonFinancial = hasAnyAuthority(props.account.authorities, [AUTHORITIES.MCPHERSON_FINANCIAL]);
  // const isPolaris = hasAnyAuthority(props.account.authorities, [AUTHORITIES.POLARIS_ADMIN]);
  // const isMcPhersonAdmin = hasAnyAuthority(props.account.authorities, [AUTHORITIES.MCPHERSON]);

  return (
    <>
        <Layout>
          <Container style={{marginTop: '40px'}}>
            <Row>
              <Col sm={12} md={6} lg={4}>
                <Card1
                  click={() => {
                    history.push('/dwallets');
                  }}
                  headtitle="Wallets"
                  bg=" #FE8C4C"
                  button="View All Wallets"
                  src="content/images/wallet.png"
                  title={meta.walletCount}
                  caption="wallets Created"
                  bcolor="#FE8C4C"
                  shadowColor="0px 36px 64px -22px #FE8C4C"
                />
                <br />
              </Col>
              <Col sm={12} md={6} lg={4}>
                <Card1
                  click={() => {
                    history.push('/alltransactions');
                  }}
                  headtitle="Transactions"
                  bg="#53C4C0"
                  src="content/images/phone.png"
                  button="View All Transactions"
                  title={meta.transactionCount}
                  caption="Transactions"
                  bcolor="#53C4C0"
                  shadowColor="0px 36px 64px -22px #53C4C0"
                />
                <br />
              </Col>
              <Col sm={12} md={6} lg={4}>
                <Card2 headtitle="Customer count" count={meta.customerCount} />
                <br />
              </Col>
            </Row>
            <br />
            <br />
            <Row style={{ display: 'flex', paddingBottom: '200px' }}>
              <Col sm={12} lg={6}>
                <GraphCard meta={meta} />
                <br />
              </Col>
              <Col sm={12} lg={6}>
                <PictographCard meta={meta} />
                <br />
              </Col>
            </Row>
          </Container>
        </Layout>
    </>
  );
};

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  authentication: storeState.authentication,
  isAuthenticated: storeState.authentication.isAuthenticated,
});
type StateProps = ReturnType<typeof mapStateToProps>;

export default connect(mapStateToProps)(Dashboard);
