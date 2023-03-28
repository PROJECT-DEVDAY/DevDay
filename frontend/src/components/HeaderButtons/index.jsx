import React, { useState } from 'react';

import classNames from 'classnames';

import style from './index.module.scss';

export const HeaderButtons = props => {
  const HEADER_ITEMS = ['전체', '기본', '알고리즘', 'commit'];

  const [selectedItem, setSelectedItem] = useState('전체');

  const handleItemChange = event => {
    setSelectedItem(event.target.value);
  };

  return (
    <div
      className={classNames(
        style.RadioGroup,
        `flex justify-between items-center`,
      )}
    >
      {HEADER_ITEMS.map(item => (
        <label
          className={classNames(
            selectedItem === item ? style.selected : '',
            `inline-block rounded-2xl px-5 py-2 font-medium`,
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
