import React from 'react';

import classnames from 'classnames';
import PropTypes from 'prop-types';

import style from './index.module.scss';

export default function InputUser({ color, fill, className, label, ...props }) {
  return (
    <button
      type="button"
      className="py-2 px-4 font-semibold rounded-lg shadow-md text-white bg-green-500 hover:bg-green-700"
    >
      {label}
    </button>
  );
}

InputUser.propTypes = {
  color: PropTypes.string,
  fill: PropTypes.bool,
  label: PropTypes.string.isRequired,
  onClick: PropTypes.func,
};

InputUser.defaultProps = {
  color: 'primary',
  fill: true,
  onClick: undefined,
};
