import React, { useState, forwardRef } from 'react';

import classNames from 'classnames';
import Image from 'next/image';
import Link from 'next/link';
import { useRouter } from 'next/router';
// import { useRouter } from 'next/router';
import PropTypes from 'prop-types';

import style from './index.module.scss';

export const ChallengeItem = forwardRef(
  (
    {
      className,
      id,
      imgUrl,
      participants,
      leader,
      title,
      period,
      onClick,
      status,
      ...props
    },
    ref,
  ) => {
    const router = useRouter();

    return (
      <Link
        ref={ref}
        href={status ? `participation/${id}` : `challenge/${id}`}
        className={style.ChallengeItem}
        role="button"
        tabIndex={0}
      >
        <div
          className={classNames(style.ImageContainer, `relative object-cover`)}
        >
          <img
            className={classNames(style.Image, `rounded-lg`)}
            src={imgUrl || require('../../image/default-user.png')}
            alt="temp"
          />
          <div
            className={classNames(
              style.Participants,
              `absolute rounded-md p-1 font-bold text-xs`,
            )}
          >
            {participants}명
          </div>
        </div>
        <div className={classNames(`flex flex-col ml-1 pt-1`)}>
          <div className={classNames(style.Leader, `font-medium text-xs`)}>
            {leader}
          </div>
          <div
            className={classNames(
              style.Title,
              `font-bold text-base overflow-hidden text-ellipsis whitespace-nowrap`,
            )}
          >
            {title}
          </div>
          <div className={classNames(style.Period, `font-medium text-xs`)}>
            {period}
          </div>
        </div>
      </Link>
    );
  },
);

ChallengeItem.propTypes = {
  imageUrl: PropTypes.string,
  participants: PropTypes.number.isRequired,
  leader: PropTypes.string.isRequired,
  title: PropTypes.string.isRequired,
  period: PropTypes.string.isRequired,
  onClick: PropTypes.func,
  status: PropTypes.status,
};
ChallengeItem.defaultProps = {
  status: false,
};
