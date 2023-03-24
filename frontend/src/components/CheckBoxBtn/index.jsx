import React from 'react';

import classnames from 'classnames';
import PropTypes from 'prop-types';

import style from './index.module.scss';
import { Button } from '../Button';

export const CheckBoxBtn = ({ content, className, label, color, ...props }) => {
  return (
    <div className={style.checkBoxBtn}>
      <label htmlFor="agree" className={style.content}>
        <p>{content}</p>
        <input
          id="agree"
          type="checkbox"
          className={classnames(style.checkBox, 'checked:bg-blue-500 ...')}
        />
      </label>
      <Button className={(style.button, `mt-3`)} label={label} />
    </div>
  );
};

CheckBoxBtn.propTypes = {
  content: PropTypes.string.isRequired,
  label: PropTypes.string.isRequired,
  color: PropTypes.string,
};

CheckBoxBtn.defaultProps = {
  color: null,
};
