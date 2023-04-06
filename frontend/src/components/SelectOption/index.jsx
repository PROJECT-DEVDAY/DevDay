import React from 'react';

import classNames from 'classnames';
import Image from 'next/image';
import PropTypes from 'prop-types';

import style from './index.module.scss';

export const SelectOption = ({
  fill,
  check,
  title,
  content,
  iconUrl,
  ...props
}) => {
  return (
    <div
      className={classNames(
        !check && style.Select,
        check && style.Selected,
        'flex',
        fill && style[`SelectOption-background-fill`],
      )}
    >
      <div>
        <div className={style.Title}>{title}</div>
        <div className={style.Content}>{content}</div>
      </div>
      {iconUrl && (
        <div className={style.iconImage}>
          <Image src={iconUrl} alt={title} />
        </div>
      )}
    </div>
  );
};

SelectOption.propTypes = {
  fill: PropTypes.bool,
  onClick: PropTypes.func,
  iconUrl: PropTypes.string,
};

SelectOption.defaultProps = {
  fill: false,
  onClick: undefined,
  iconUrl: null,
};
