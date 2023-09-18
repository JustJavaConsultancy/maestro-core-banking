import React, { ReactNode } from 'react';
import NumberFormat from 'react-number-format';
import leftLogo from '../../../../../content/images/card-left.png';
import rightLogo from '../../../../../content/images/card-right.png';
import rcard from '../../../../../content/images/rcards.png';
import './headerCards.scss';

type HeaderCardType = {
  header: string;
  subHeader: string;
  total: number;
  backgroundColor: string;
  img: string;
};

type CardType = {
  children: ReactNode;
  customStyle?: string;
};

const HeaderCards = (props: HeaderCardType) => {
  return (
    <div id="card-box">
      <h6 className="card_title" style={{ marginLeft: '1rem' }}>
        {props.header}
      </h6>
      <div
        className="card_inner-box"
        style={{ backgroundColor: props.backgroundColor, boxShadow: `${props.backgroundColor} 0px 36px 64px -25px` }}
      >
        <div className="card_left-logo">
          <img src={leftLogo} alt="logo" />
        </div>
        <div className="card_main-content">
          <div>
            <img src={props.img} alt="wallet logo" />
          </div>
          <div>
            <p className="card_sum">
              <NumberFormat
                value={props.total}
                displayType="text"
                thousandSeparator={true}
                // decimalScale={2}
                fixedDecimalScale={true}
                // prefix="-"
                // className="report-debit"
              />
            </p>
            <small>{props.subHeader}</small>
          </div>
        </div>
        <div className="card_right-logo">
          <img src={rightLogo} alt="logo" />
        </div>
      </div>
    </div>
  );
};

export default HeaderCards;

export const Card = ({ children, customStyle }: CardType): JSX.Element => {
  return <div className={`card-box ${customStyle}`}>{children}</div>;
};
