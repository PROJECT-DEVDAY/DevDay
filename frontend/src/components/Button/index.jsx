/* eslint-disable react/button-has-type */
import React from 'react';

import classnames from 'classnames';
import PropTypes from 'prop-types';

import style from './index.module.scss';

export const Button = ({ color, fill, className, label, type, ...props }) => {
  return (
    <button
      type={type || 'button'}
      className={classnames(
        style.Button,
        style[`Button-${color}`],
        fill && style['Button-background-fill'],
        className,
        `font-medium`,
      )}
      {...props}
    >
      {label}
    </button>
  );
};

Button.propTypes = {
  type: PropTypes.string,
  color: PropTypes.string,
  fill: PropTypes.bool,
  label: PropTypes.string.isRequired,
  onClick: PropTypes.func,
};

Button.defaultProps = {
  type: 'button',
  color: 'primary',
  fill: true,
  onClick: undefined,
};
