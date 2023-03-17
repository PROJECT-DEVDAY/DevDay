import React from 'react';
import PropTypes from 'prop-types';
import classnames from 'classnames';

import './inputbox.css';
import { CiMail } from 'react-icons/ci';

export const InputBox = ({ className, content, placeholder, ...props }) => {
  return (
    <div className={classnames('box', className)} {...props}>
      <CiMail className="icon" />
      <input
        className="inputbox"
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
  content: null,
  placeholder: null,
};
