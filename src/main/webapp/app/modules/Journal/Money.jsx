import React from 'react';
import MoneyFormat from 'react-number-format';

export default function(props) {
    return(
       <MoneyFormat
            value = {props.amount}
            displayType = 'text'
            thousandSeparator = {true}
            prefix = "₦"
            renderText = {(i)=><p>{i}</p>}
       />
    )
}