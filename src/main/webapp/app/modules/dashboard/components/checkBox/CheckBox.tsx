import React from 'react';

import './CheckBox.scss';

interface CheckBoxProps {
    handleChange: () => void;
    checked: boolean
}

const CheckBox = ({ handleChange, checked }: CheckBoxProps) => {
    return (
        <label className="check__container">
            <input 
            type="checkbox" 
            onChange={handleChange} 
            checked={checked}
            />
            <span className="checkmark"></span>
        </label>
    )
}

export default CheckBox;