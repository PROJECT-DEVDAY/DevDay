import React from 'react';
import { SlArrowLeft } from 'react-icons/sl';

import classnames from 'classnames';
import PropTypes from 'prop-types';

import style from './index.module.scss';

export const ReturnArrow = ({
  className,
  iconname,
  title,
  content,
  ...props
}) => {
  return (
    <div className={classnames(style.ReturnArrow, className, `mt-2`)}>
      <div className={style.arrowdiv}>
        <SlArrowLeft className={style.arrow} width={30} />
      </div>
      <div>
        <p className={classnames(style.arrowtitle, `font-bold`)}>{title}</p>
      </div>
      <div className={style.space} />
    </div>
  );
};

ReturnArrow.propTypes = {
  title: PropTypes.string.isRequired,
  onClick: PropTypes.func,
};

ReturnArrow.defaultProps = {
  onClick: undefined,
};
