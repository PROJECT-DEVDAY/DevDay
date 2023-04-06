import React from 'react';

import PropTypes from 'prop-types';

import style from './index.module.scss';

export const ContentInput = ({ maxlength, placeholder, ...props }) => {
  return (
    <textarea
      className={style.ContentInput}
      type="text"
      placeholder={placeholder}
      maxLength={maxlength}
      {...props}
    />
  );
};

ContentInput.propTypes = {
  placeholder: PropTypes.string,
};

ContentInput.defaultProps = {
  placeholder: null,
};
