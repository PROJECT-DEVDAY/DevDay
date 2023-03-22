import React from 'react';

import classnames from 'classnames';
import PropTypes from 'prop-types';

import './selectoption.css';

export const SelectOption = ({
  fill,
  className,
  iconname,
  title,
  content,
  ...props
}) => {
  return (
    <div
      className={classnames(
        'Select',
        className,
        fill && 'Select-background-fill',
      )}
    >
      <p className="title">{title}</p>
      <p className="content">{content}</p>
    </div>
    // <button
    //   type="button"
    //   className={classnames(
    //     'Button',
    //     `Button-${color}`,
    //     fill && 'Button-background-fill',
    //     className,
    //   )}
    //   {...props}
    // >
    //   {label}
    // </button>
  );
};

SelectOption.propTypes = {
  fill: PropTypes.bool,
  title: PropTypes.string.isRequired,
  content: PropTypes.string.isRequired,
  onClick: PropTypes.func,
};

SelectOption.defaultProps = {
  fill: true,
  onClick: undefined,
};
