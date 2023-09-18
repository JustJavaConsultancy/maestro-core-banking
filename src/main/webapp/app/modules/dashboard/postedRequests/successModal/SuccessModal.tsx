import React from 'react';
import ReactDOM from 'react-dom';
import { CSSTransition } from 'react-transition-group';

import '../deductMoney/DeductModal';
import './SuccessModal.scss';
import SuccessMarkImg from '../../../../../content/images/success_mark.svg';

interface SuccessModalProps {
    show: boolean;
    setShow: () => void;
    accountNumber?: number;
}
 
const SuccessModal = ({ show, setShow, accountNumber }: SuccessModalProps):JSX.Element => {
    return ReactDOM.createPortal( 
        <div className="success-modal">
            <CSSTransition in={show} timeout={500} classNames="prompt-bg" unmountOnExit>
                <div className="modal-background"></div>
            </CSSTransition>
            <CSSTransition in={show} timeout={500} classNames="prompt-bd" unmountOnExit>
                <div className="modal__content success">
                    <div>
                        <img src={SuccessMarkImg} alt="success mark" />
                    </div>
                    <div className="modal-body">
                        <h3>Payment Deduction Successful </h3>
                        {accountNumber && 
                            <p>
                            You’ve successfully charged <strong>₦1,000</strong> to <strong>{accountNumber}</strong> for an Advert Post.
                            They will be notified accordingly
                            </p>
                        }
                        <div className="md__body__btn">
                            <button type="button" onClick={setShow}>Finish</button>
                        </div>
                    </div>
                </div>
            </CSSTransition>
            </div>
     , document.body);
}
 
export default SuccessModal;