import React from 'react';
import { SlArrowRight } from 'react-icons/sl';

import classNames from 'classnames';
import PropTypes from 'prop-types';

import style from './index.module.scss';

export const SelectArrow = ({
  fill,
  className,
  iconname,
  title,
  content,
  color,
  onClick,
  ...props
}) => {
  return (
    <div
      className={classNames(
        style.SelectArrow,
        fill && style[`SelectArrow-background-fill`],
      )}
    >
      <div style={{ flex: 1 }}>
        <p
          className={classNames(
            style.ArrowTitle,
            color && style[`SelectArrow-color-warning`],
          )}
        >
          {title}
        </p>
        <p className={style.ArrowContent}>{content}</p>
      </div>
      <button
        type="button"
        className={classNames(style.ArrowDiv)}
        onClick={onClick}
      >
        <SlArrowRight className={style.Arrow} width={30} />
      </button>
    </div>
  );
};

SelectArrow.propTypes = {
  fill: PropTypes.bool,
  color: PropTypes.bool,
  title: PropTypes.string.isRequired,
  content: PropTypes.string.isRequired,
  onClick: PropTypes.func,
};

SelectArrow.defaultProps = {
  fill: false,
  onClick: undefined,
};
