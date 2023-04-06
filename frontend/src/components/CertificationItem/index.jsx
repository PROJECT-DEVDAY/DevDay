import React, { useState } from 'react';

import classNames from 'classnames';
import Image from 'next/image';
import Link from 'next/link';
import PropTypes from 'prop-types';

import { CiMail } from 'react-icons/ci';
import { SlArrowRight } from 'react-icons/sl';

import style from './index.module.scss';

export const CertificationItem = ({
  className,
  id,
  imgUrl,
  title,
  period,
  ...props
}) => {
  return (
    <Link
      href={`./certification/${id}/detail`}
      className={style.CertificationItem}
      role="button"
      tabIndex={0}
    >
      <div
        className={classNames(
          style.SelectArrow,
          `w-full rounded-xl box-border flex `,
        )}
      >
        <div
          className={classNames(
            style.ImageContainer,
            `relative object-cover mr-2`,
          )}
        >
          <Image
            className={classNames(style.Image, `rounded-lg`)}
            src={imgUrl || require('../../image/default-user.png')}
            alt="temp"
            fill
          />
        </div>

        <div style={{ flex: 1 }}>
          <p className={'font-medium '}>{title}</p>
          <p className={'text-xs'}>{period}</p>
        </div>
        <button type="button" className={''}>
          <SlArrowRight className={style.Arrow} width={30} />
        </button>
      </div>
    </Link>
  );
};

CertificationItem.propTypes = {
  imageUrl: PropTypes.string,
  title: PropTypes.string.isRequired,
  period: PropTypes.string.isRequired,
};
