import React from 'react';
import { CiMail } from 'react-icons/ci';

import classnames from 'classnames';
import PropTypes from 'prop-types';

import style from './index.module.scss';

export const InputBox = ({ className, content, placeholder, ...props }) => {
  return (
    <div className={style.box} {...props}>
      <CiMail className={style.icon} />
      <input
        className={style.inputbox}
        value={content}
        type="text"
        placeholder={placeholder}
      />
    </div>
  );
};

InputBox.propTypes = {
  content: PropTypes.string.isRequired,
  placeholder: PropTypes.string,
};

InputBox.defaultProps = {
  placeholder: null,
};
