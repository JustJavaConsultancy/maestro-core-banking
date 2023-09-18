import React from 'react';
import ReactDOM from 'react-dom';
import { CSSTransition } from 'react-transition-group';

import './PromptModal.scss';

interface PromptModalProps {
    show: boolean;
    setShow: () => void;
    setCancel: () => void;
    title: string;
    requestLoading: boolean
}
 
const PromptModal = ({ show, setShow, title, setCancel, requestLoading }: PromptModalProps):JSX.Element => {
    return ReactDOM.createPortal( 
        <div className="success-modal">
            <CSSTransition in={show} timeout={500} classNames="prompt-bg" unmountOnExit>
                <div className="modal-background" onClick={setCancel}></div>
            </CSSTransition>
            <CSSTransition in={show} timeout={500} classNames="prompt-bd" unmountOnExit>
                <div className="modals-content">
                    <div className="modal-body">
                        <h3>{title} this request(s)? </h3>
                        <p>
                            Are you sure you want to {title.toLowerCase()} this request(s)? 
                            Please note that this action can not be reversed 
                        </p>
                        <div>
                            <button type="button" onClick={setCancel}>Cancel</button>
                            <button type="button" onClick={setShow}>{requestLoading ? 'Loading...' : title}</button>
                        </div>
                    </div>
                </div>
            </CSSTransition>
            </div>
     , document.body);
}
 
export default PromptModal;