import React from 'react';

import classNames from 'classnames';
import PropTypes from 'prop-types';

import style from './index.module.scss';

export const InputLabel = ({ content, asterisk, ...props }) => {
  return (
    <div className={classNames('flex w-full', style.inputLabel)}>
      <span
        className={classNames(
          'text-lg font-medium',
          props.smallTextSize ? 'text-sm' : 'text-lg',
        )}
      >
        {content}
      </span>
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
