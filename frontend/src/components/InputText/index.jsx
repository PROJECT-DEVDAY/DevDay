import React, { useState } from 'react';
import { BiShow, BiHide } from 'react-icons/bi';

import classNames from 'classnames';
import PropTypes from 'prop-types';

import style from './index.module.scss';

export const InputText = ({ content, icon, inputType, type, onClick }) => {
  const [showPassword, setShowPassword] = useState(false);

  const toggleShowPassword = () => {
    setShowPassword(prev => !prev);
  };

  let inputValue = null;
  let buttonValue = null;

  if (inputType === 'password') {
    inputValue = (
      <input
        type={showPassword ? 'text' : 'password'}
        placeholder={content}
        className={classNames(style.Content, `w-full h-6`)}
      />
    );
    buttonValue = (
      <button type="button" onClick={toggleShowPassword}>
        {showPassword ? <BiShow /> : <BiHide />}
      </button>
    );
  } else if (inputType === 'iconText') {
    inputValue = (
      <input
        type={type}
        placeholder={content}
        className={classNames(style.Content, `w-full h-6`)}
      />
    );
    buttonValue = (
      <button type="button" className={style.RightBtn} onClick={onClick}>
        {icon}
      </button>
    );
  } else {
    inputValue = (
      <input
        type={type}
        placeholder={content}
        className={classNames(style.Content, `w-full h-6`)}
      />
    );
  }

  return (
    <div className={classNames(style.InputText, 'flex w-full my-2 pb-1')}>
      {inputValue}
      {buttonValue}
    </div>
  );
};

InputText.propTypes = {
  type: PropTypes.string.isRequired,
  labelName: PropTypes.string,
  content: PropTypes.string,
  onClick: PropTypes.func,
};

InputText.defaultProps = {
  content: null,
  onClick: () => {},
};
