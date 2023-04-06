import React, { useState } from 'react';
import { BiShow, BiHide } from 'react-icons/bi';

import classNames from 'classnames';
import PropTypes from 'prop-types';

import style from './index.module.scss';

export const InputText = ({
  content,
  icon,
  inputType,
  type,
  name,
  value,
  onClick,
  onChange,
  enabled,
  ...props
}) => {
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
        name={name}
        className={classNames(style.Content, `w-full h-6`)}
        onChange={onChange}
        {...props}
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
        name={name}
        value={value}
        placeholder={content}
        className={classNames(
          style.Content,
          `w-full h-6`,
          `disabled && 'disabled:opacity-25',
        disabled ? 'cursor-not-allowed' : 'cursor-pointer'`,
        )}
        onChange={onChange}
        enabled={enabled}
        {...props}
      />
    );
    buttonValue = (
      <button
        type="button"
        className={style.RightBtn}
        onClick={onClick}
        enabled={enabled}
      >
        {icon}
      </button>
    );
  } else if (inputType === 'center') {
    inputValue = (
      <input
        type={type}
        name={name}
        value={value}
        placeholder={content}
        className={classNames(
          style.Content,
          `w-full h-6 text-center font-medium`,
        )}
        onChange={onChange}
        {...props}
      />
    );
  } else {
    inputValue = (
      <input
        type={type}
        name={name}
        value={value}
        placeholder={content}
        className={classNames(style.Content, `w-full h-6`)}
        onChange={onChange}
        {...props}
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
  content: PropTypes.string,
  onClick: PropTypes.func,
};

InputText.defaultProps = {
  content: null,
  onClick: () => {},
};
