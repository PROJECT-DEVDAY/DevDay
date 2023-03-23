import React from 'react';

import classnames from 'classnames';
import PropTypes from 'prop-types';

import style from './index.module.scss';
import Button from '../Button';

export const BtnFooter = ({ content, className, label, ...props }) => {
  return (
    <div className={style.BfBox}>
      <label htmlFor="agree" className={style.text}>
        <p>{content}</p>
        <input
          id="agree"
          type="checkbox"
          className={classnames(style.CheckBox, 'checked:bg-blue-500 ...')}
        />
      </label>
      <Button />
    </div>
  );
};

BtnFooter.propTypes = {
  content: PropTypes.string,
};

BtnFooter.defaultProps = {
  content: 'primary',
};
