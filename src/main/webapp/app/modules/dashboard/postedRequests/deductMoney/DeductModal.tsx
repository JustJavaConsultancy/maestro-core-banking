import React from 'react';
import ReactDOM from 'react-dom';
import { CSSTransition } from 'react-transition-group';
import { Rings } from  'react-loader-spinner';

import './DeductModal.scss';

interface PromptModalProps {
    show: boolean;
    setShow: () => void;
    confirmPostHandler;
    loading: boolean;
    accountNumber;
    accountName
}
 
const PromptModal = ({ show, setShow, confirmPostHandler, loading, accountNumber, accountName }: PromptModalProps):JSX.Element => {
    return ReactDOM.createPortal( 
        <div className="success-modal">
            <CSSTransition in={show} timeout={500} classNames="prompt-bg" unmountOnExit>
                <div className="modal-background"></div>
            </CSSTransition>
            <CSSTransition in={show} timeout={500} classNames="prompt-bd" unmountOnExit>
                <div className="modal__content">
                    <div className="modal-body">
                        <h3>Advert Payment Deduction </h3>
                        <p>
                            To complete this process, please accept the 
                            charge for this advert
                        </p>
                        <div>
                            {loading &&
                             <div className= "loading__cover">
                                <Rings color="#435faa" height={80} width={80} />
                             </div>
                            }
                            <div className="md__body__input">
                                <div>
                                    <input type="text" value={accountNumber ?? "7065622034"} />
                                    <p>Name on Bank Record: <span>{accountName}</span></p>
                                </div>
                                <div>
                                    <input type="text" value="₦1,000"  disabled />
                                </div>
                            </div>
                            <div className="md__body__btn">
                                <button type="button" onClick={setShow}>Cancel</button>
                                <button type="button" onClick={() => confirmPostHandler('APPROVED')}>Request</button>
                            </div>
                        </div>
                    </div>
                </div>
            </CSSTransition>
            </div>
     , document.body);
}
 
export default PromptModal;