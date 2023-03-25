import React, { useState } from 'react';

import classnames from 'classnames';

import style from './index.module.scss';

export const HeaderButtons = props => {
  const HEADER_ITEMS = ['전체', '기본', '알고리즘', 'commit'];

  const [selectedItem, setSelectedItem] = useState('');

  const handleItemChange = event => {
    setSelectedItem(event.target.value);
  };

  return (
    <div className={classnames(style.RadioGroup, `flex justify-between`)}>
      {HEADER_ITEMS.map(item => (
        <label
          className={classnames(
            selectedItem === item ? style.selected : '',
            `px-4 py-1 font-medium`,
          )}
        >
          <input
            type="radio"
            name="items"
            value={item}
            checked={selectedItem === item}
            onChange={handleItemChange}
          />
          {item}
        </label>
      ))}
    </div>
  );
};
