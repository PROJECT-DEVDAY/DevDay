import React from 'react';
import { CiMail } from 'react-icons/ci';

import classNames from 'classnames';
import PropTypes from 'prop-types';

import style from './index.module.scss';

export const InputBox = ({ className, placeholder, ...props }) => {
  return (
    <div className={style.box} {...props}>
      <CiMail className={style.icon} />
      <input className={style.inputbox} type="text" placeholder={placeholder} />
    </div>
  );
};

InputBox.propTypes = {
  placeholder: PropTypes.string,
};

InputBox.defaultProps = {
  placeholder: null,
};
