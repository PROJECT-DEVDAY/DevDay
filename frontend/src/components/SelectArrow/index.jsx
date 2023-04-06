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
  arrow,
  ...props
}) => {
  return (
    <div
      className={classNames(
        style.SelectArrow,
        className,
        fill && style[`SelectArrow-background-fill`],
      )}
      onClick={onClick}
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
      {arrow && (
        <button type="button" className={classNames(style.ArrowDiv)}>
          <SlArrowRight className={style.Arrow} width={30} />
        </button>
      )}
    </div>
  );
};

SelectArrow.propTypes = {
  fill: PropTypes.bool,
  color: PropTypes.bool,
  title: PropTypes.string.isRequired,
  content: PropTypes.string.isRequired,
  onClick: PropTypes.func,
  arrow: PropTypes.bool,
};

SelectArrow.defaultProps = {
  fill: false,
  onClick: undefined,
  arrow: true,
};
