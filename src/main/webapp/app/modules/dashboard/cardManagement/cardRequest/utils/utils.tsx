import React, { ChangeEvent } from 'react';

type FilterType = {
  text: string;
  when: string;
  value: string;
  onChange: (e: ChangeEvent<HTMLInputElement>) => void;
};

function FilterDate({ when, text, onChange, value }: FilterType) {
  return (
    <div>
      <div className="mr-4">
        <label className="mr-2" htmlFor={when}>
          {text}
        </label>
        <input type="date" name={when} id={when} value={value} onChange={onChange} />
      </div>
    </div>
  );
}

export default FilterDate;
