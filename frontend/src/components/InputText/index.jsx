import React from 'react';
import { useState } from 'react';
import classnames from 'classnames';
import PropTypes from 'prop-types';
import { BiShow, BiHide } from 'react-icons/bi';

import style from './index.module.scss';

export const InputText = ({ labelName, content, icon, type, onClick }) => {
  const [showPassword, setShowPassword] = useState(false);

  const toggleShowPassword = () => {
    setShowPassword(prev => !prev);
  };

  let inputType = null;
  let buttonType = null;

  if (type == 'password') {
    inputType = (
      <input
        type={showPassword ? 'text' : 'password'}
        placeholder={content}
        className={classnames(style.Content, `w-full h-6`)}
      />
    );
    buttonType = (
      <button onClick={toggleShowPassword}>
        {showPassword ? <BiShow /> : <BiHide />}
      </button>
    );
  } else if (type == 'iconText') {
    inputType = (
      <input
        type="text"
        placeholder={content}
        className={classnames(style.Content, `w-full h-6`)}
      />
    );
    buttonType = (
      <button className={style.RightBtn} onClick={onClick}>
        {icon}
      </button>
    );
  } else {
    inputType = (
      <input
        type="text"
        placeholder={content}
        className={classnames(style.Content, `w-full h-6`)}
      />
    );
  }

  return (
    <div className={classnames(style.InputText, 'w-full mb-5 pb-2 pt-2')}>
      <div className={classnames('font-medium', style.LabelName)}>
        {labelName}
      </div>
      <div className="flex">
        {inputType}
        {buttonType}
      </div>
    </div>
  );
};

InputText.propTypes = {
  labelName: PropTypes.string,
  content: PropTypes.string,
  onClick: PropTypes.func,
};

InputText.defaultProps = {
  labelName: '비밀번호',
  content: null,
  onClick: () => {},
};
