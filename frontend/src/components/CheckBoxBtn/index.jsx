import React from 'react';

import classNames from 'classnames';
import PropTypes from 'prop-types';

import style from './index.module.scss';
import { Button } from '../Button';

export const CheckBoxBtn = ({
  check,
  content,
  className,
  label,
  color,
  onClick,
  ...props
}) => {
  return (
    <div className={style.checkBoxBtn}>
      <label htmlFor="agree" className={style.content}>
        <p>{content}</p>
        {check && (
          <input
            id="agree"
            type="checkbox"
            className={classNames(style.checkBox, 'checked:bg-blue-500 ...')}
          />
        )}
      </label>
      <Button
        className={(style.button, `mt-3`)}
        label={label}
        onClick={onClick}
      />
    </div>
  );
};

CheckBoxBtn.propTypes = {
  content: PropTypes.string.isRequired,
  label: PropTypes.string.isRequired,
  color: PropTypes.string,
  check: PropTypes.bool,
  onClick: PropTypes.func,
};

CheckBoxBtn.defaultProps = {
  color: null,
  check: true,
  onClick: () => {},
};
