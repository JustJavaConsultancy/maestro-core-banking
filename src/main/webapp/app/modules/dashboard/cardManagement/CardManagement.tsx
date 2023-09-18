import React, { useEffect, useState } from 'react';
import Layout from '../Layout';
import { useHistory } from 'react-router-dom';

import HeaderCards, { Card } from '../components/cardManagement/HeaderCards';
import { Table } from 'react-bootstrap';
import NumberFormat from 'react-number-format';
import { IRootState } from 'app/shared/reducers';
import { useSelector } from 'react-redux';
import Axios from 'axios';
import InnerLayout from '../components/Layout';

import rcard from '../../../../content/images/rcards.png';
import acard from '../../../../content/images/acards.png';
import expcard from '../../../../content/images/expcards.png';
import share from '../../../../content/images/share1.png';

import './cardManagement.scss';

type SchemeSummaryType = {
  active: number;
  inactive: number;
  name: string;
  total: number;
};

type CardRequestType = {
  active_cards: number;
  all_requests: number;
  expired_cards: number;
  scheme_summary: SchemeSummaryType[];
};

export function getSchemeName(arr: any[], name: string) {
  const theScheme = arr.filter(scheme => scheme.scheme === name);
  const schemeId = theScheme.length ? theScheme[0].schemeID : '';
  return schemeId;
}

function CardManagement() {
  const history = useHistory();

  const [schemes, setSchemes] = useState([]);
  const [cardRequests, setCardRequests] = useState<CardRequestType>();

  const adminNumber = useSelector((state: IRootState) => state.authentication.account.login);

  const getSchemes = async () => {
    try {
      const theSchemes = await Axios.get(`/api/schemes/admin/${adminNumber}`);
      setSchemes(theSchemes.data.data);
    } catch (e) {
      console.error(e);
    }
  };

  const cardRequestHandler = async () => {
    try {
      const res = await Axios.get('/api/card-management/summary');
      console.error(res);
      if (res.data.code === '00') {
        setCardRequests(res.data.data);
        return;
      }
    } catch (error) {
      if (error.response) {
        alert(error.response.data.message);
        return;
      }
      alert('Something went wrong!');
      console.error(error);
    }
  };

  useEffect(() => {
    getSchemes();
  }, []);

  useEffect(() => {
    if (schemes.length) {
      cardRequestHandler();
    }
  }, [schemes.length]);

  return (
    <Layout>
      <InnerLayout dontfilter={true} show={true} title="Card Management" path="Card Summary">
      <section id="main_card">
        <div id="main_card-section">
          <HeaderCards
            backgroundColor="#fe8c4c"
            header="Total Cards Requested"
            img={rcard}
            subHeader="Requested Cards"
            total={cardRequests?.all_requests ? cardRequests.all_requests : 0}
          />
          <HeaderCards
            backgroundColor="#53C4C0"
            header="Active Cards"
            img={acard}
            subHeader="Active Cards"
            total={cardRequests?.active_cards ? cardRequests.active_cards : 0}
          />
          <HeaderCards
            backgroundColor="#252525"
            header="Expired Cards"
            img={expcard}
            subHeader="Expired Cards"
            total={cardRequests?.expired_cards ? cardRequests.expired_cards : 0}
          />
        </div>

        <main>
          <div className="content-style">
            <h6 className="card_title">Total Cards per Scheme</h6>
          </div>
          <Card customStyle="content-style">
            <Table responsive hover className="transaction-table">
              <thead className="">
                <th>S/N</th>
                <th>Scheme</th>
                <th>Number of Cards</th>
                <th>Number of Active Cards</th>
                <th>Number of inactive Cards</th>
                <th></th>
              </thead>
              <tbody>
                {!cardRequests?.scheme_summary.length ? (
                  <tr>
                    <td colSpan={9}>
                      <div className="no_data-box">
                        <h2>No Data</h2>
                      </div>
                    </td>
                  </tr>
                ) : (
                  cardRequests?.scheme_summary.map((cardScheme, i) => (
                    <tr key={i}>
                      <td>{i + 1}</td>
                      <td>{cardScheme.name === 'Systemspecs wallet' ? 'Pouchii' : cardScheme.name}</td>
                      <td>
                        <NumberFormat
                          value={cardScheme.total}
                          displayType="text"
                          thousandSeparator={true}
                          // decimalScale={2}
                          fixedDecimalScale={true}
                          // prefix="-"
                          // className="report-debit"
                        />
                      </td>
                      <td>
                        <NumberFormat
                          value={cardScheme.active}
                          displayType="text"
                          thousandSeparator={true}
                          // decimalScale={2}
                          fixedDecimalScale={true}
                          // prefix="-"
                          // className="report-debit"
                        />
                      </td>
                      <td>
                        <NumberFormat
                          value={cardScheme.inactive}
                          displayType="text"
                          thousandSeparator={true}
                          // decimalScale={2}
                          fixedDecimalScale={true}
                          // prefix="-"
                          // className="report-debit"
                        />
                      </td>
                      <td>
                        <div
                          className="view_scheme-logo"
                          onClick={() => history.push('card-report', { scheme: getSchemeName(schemes, cardScheme.name) })}
                        >
                          <span className="view_scheme-img">
                            <img src={share} alt="view icon" />
                          </span>
                          <span>View Scheme</span>
                        </div>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </Table>
          </Card>
        </main>
      </section>
      </InnerLayout>
    </Layout>
  );
}

export default CardManagement;
