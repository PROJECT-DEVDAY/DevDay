import React from 'react';

import PropTypes from 'prop-types';

import style from './index.module.scss';

export const ContentInput = ({ content, placeholder }) => {
  return (
    <textarea
      className={style.ContentInput}
      value={content}
      type="text"
      placeholder={placeholder}
    />
  );
};

ContentInput.propTypes = {
  content: PropTypes.string.isRequired,
  placeholder: PropTypes.string,
};

ContentInput.defaultProps = {
  placeholder: null,
};
