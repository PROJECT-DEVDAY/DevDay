import React from 'react';
import { SlArrowLeft } from 'react-icons/sl';

import classNames from 'classnames';
import { useRouter } from 'next/router';
import PropTypes from 'prop-types';

import style from './index.module.scss';

export const ReturnArrow = ({
  className,
  iconname,
  title,
  content,
  urlname,
  ...props
}) => {
  const router = useRouter();
  const goToBack = () => {
    router.back();
  };
  return (
    <div className={classNames(style.ReturnArrow, className, `mt-2`)}>
      <button type="button" onClick={goToBack} className={style.arrowdiv}>
        <SlArrowLeft className={style.arrow} width={30} />
      </button>
      <div>
        <p className={classNames(style.arrowtitle, `text-xl`, `font-bold`, `pr-4`)}>
          {title}
        </p>
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
