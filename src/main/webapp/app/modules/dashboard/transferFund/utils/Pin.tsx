import React, { ChangeEvent, FormEvent, useRef } from 'react';
import { useState } from 'react';
import { Modal, Card, Spinner } from 'react-bootstrap';
import './Modal.scss';
import MoneyFormat from 'react-number-format';

export interface OtpProps {
  amount: number;
  show: boolean;
  loading: boolean;
  onSubmit: any;
  onHide: any;
  acountType?: string;
  companyName?: string;
  useBonus?: boolean;
  bonusAmount?: string | number;
  resMsg?: boolean;
  isAgent?: boolean;
  showError?: string;
}

export default function Pin(props: OtpProps): JSX.Element {
  const [otp, setOtp] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const otpRef = useRef(null);

  const handleChange = (event: ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    event.persist();
    return setOtp(event.target.value);
  };

  const handleSubmit = () => {
    //  e.preventDefault();
    props.onSubmit(otp);
  };

  return (
    <Modal {...props} aria-labelledby="contained-modal-title-vcenter" centered backdrop="static">
      <Modal.Header closeButton></Modal.Header>
      <Modal.Body className="text-center">
        <h3 className="font-weight-bold">
          Please enter your <br />
          Pin to Continue
        </h3>
        <form>
          <Card>
            <div id="icon">
              <i className="fa fa-asterisk"></i>
            </div>

            <Card.Body>
              <input
                maxLength={4}
                name="otp"
                ref={otpRef}
                className="otp-input"
                type="password"
                max="6"
                placeholder="Enter Pin"
                onChange={handleChange}
                value={otp}
              />
              {error && <p className="text-danger">{error}</p>}
            </Card.Body>
          </Card>
          <div className="pin-more-info">
            {props.acountType && (
              <p className="">
                You are about to purchase a {props.acountType} Electricity tariff from {props.companyName} for{' '}
                <MoneyFormat
                  value={props.amount}
                  displayType="text"
                  thousandSeparator={true}
                  className="currency-style"
                  decimalScale={2}
                  fixedDecimalScale={true}
                  prefix="₦"
                />
              </p>
            )}

            {props.useBonus && (
              <p className="mb-1 bonus-charge">
                Applied Bonus:{' '}
                <MoneyFormat
                  value={props.bonusAmount}
                  displayType="text"
                  thousandSeparator={true}
                  className="currency-style"
                  decimalScale={2}
                  fixedDecimalScale={true}
                  prefix="₦"
                />
              </p>
            )}

            {!props.isAgent && (
              <p className="mb-1 bonus-charge">
                Charge:{' '}
                <MoneyFormat
                  value={50}
                  displayType="text"
                  thousandSeparator={true}
                  className="currency-style"
                  decimalScale={2}
                  fixedDecimalScale={true}
                  prefix="₦"
                />
              </p>
            )}

            {props.showError && <p style={{ color: '#f00', fontWeight: 'bold', marginBottom: '0' }}>{props.showError}</p>}
          </div>

          <div className="d-flex flex-column justify-content-center align-items-center py-3">
            {props.resMsg && <p style={{ color: '#f00', fontWeight: 'bold' }}>Transaction Failed</p>}
            <button type="button" onClick={handleSubmit} className="btn-default-action bg-gray" disabled={otp.length < 4 || loading}>
              {props.loading ? (
                <Spinner style={{ color: '#fff' }} animation="border" role="status">
                  <span className="sr-only">Loading...</span>
                </Spinner>
              ) : (
                <span>
                  Pay{' '}
                  <strong>
                    <MoneyFormat
                      value={props.amount + (!props.isAgent ? 50 : 0)}
                      displayType="text"
                      thousandSeparator={true}
                      className="currency-style"
                      decimalScale={2}
                      fixedDecimalScale={true}
                      prefix="₦"
                    />
                  </strong>
                </span>
              )}
            </button>
          </div>
        </form>
      </Modal.Body>
    </Modal>
  );
}
