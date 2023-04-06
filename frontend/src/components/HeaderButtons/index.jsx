import React, { useState, Fragment } from 'react';

import classNames from 'classnames';
import PropTypes from 'prop-types';

import style from './index.module.scss';

export const HeaderButtons = ({
  items,
  select,
  setSelect,
  buttonClassName,
}) => {
  const handleItemChange = event => {
    setSelect(event.target.value);
  };

  return (
    <div
      className={classNames(
        style.RadioGroup,
        `flex justify-between items-center`,
      )}
    >
      {items.map((item, index) => (
        <label
          key={index}
          className={classNames(
            buttonClassName,
            select === item && style.selected,
            `inline-block rounded-2xl px-4 py-2 font-medium text-sm bg-white break-keep`,
          )}
        >
          <input
            type="radio"
            name="items"
            value={item}
            checked={select === item}
            onChange={handleItemChange}
          />
          {item}
        </label>
      ))}
    </div>
  );
};

HeaderButtons.propTypes = {
  items: PropTypes.arrayOf(PropTypes.string),
  select: PropTypes.string,
  setSelect: PropTypes.func,
};

HeaderButtons.defaultProps = {
  select: '전체',
  items: ['전체', '기본', '알고리즘', 'commit'],
  setSelect: () => {},
};
