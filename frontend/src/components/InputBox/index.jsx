import React from 'react';
import { CiMail } from 'react-icons/ci';

import classNames from 'classnames';
import PropTypes from 'prop-types';

import style from './index.module.scss';

export const InputBox = ({ className, placeholder, ...props }) => {
  return (
    <div
      className={classNames(`flex p-2 h-14 mt-6 w-full rounded-xl items-center`, style.box)}
      {...props}
    >
      <CiMail className={classNames(`text-2xl`, style.icon)} />
      <input
        className={classNames(`rounded-none w-full pl2`, style.inputbox)}
        type="text"
        placeholder={placeholder}
      />
    </div>
  );
};

InputBox.propTypes = {
  placeholder: PropTypes.string,
};

InputBox.defaultProps = {
  placeholder: null,
};
