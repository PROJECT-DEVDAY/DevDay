import React from 'react';

import classnames from 'classnames';
import PropTypes from 'prop-types';

import style from './index.module.scss';

export const SelectOption = ({ fill, check, title, content, ...props }) => {
  return (
    <div
      className={classnames(!check && style.Select, check && style.Selected)}
    >
      <div className={style.Title}>{title}</div>
      <div className={style.Content}>{content}</div>
    </div>
  );
};

SelectOption.propTypes = {
  fill: PropTypes.bool,
  onClick: PropTypes.func,
};

SelectOption.defaultProps = {
  fill: true,
  onClick: undefined,
};
