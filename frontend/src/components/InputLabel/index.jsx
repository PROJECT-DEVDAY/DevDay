import React from 'react';

import classnames from 'classnames';
import PropTypes from 'prop-types';

import style from './index.module.scss';

export const InputLabel = ({ content, asterisk }) => {
  return (
    <div className={classnames('flex w-full', style.inputLabel)}>
      <span className="text-lg font-medium">{content}</span>
      {asterisk && (
        <span className="text-red-500 text-lg font-bold ml-1">*</span>
      )}
    </div>
  );
};

InputLabel.propTypes = {
  content: PropTypes.string,
};

InputLabel.defaultProps = {
  content: null,
};
