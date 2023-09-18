import React from 'react';
import './Spinner.scss';

const Spinner = () => {
    return ( 
        <div 
            className="lds-dual-ring"
            style={{borderColor: "red"}}
        >
        </div>
     );
}
 
export default Spinner;