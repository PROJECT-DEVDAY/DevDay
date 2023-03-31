import React, { useState } from 'react';

import classNames from 'classnames';
import Image from 'next/image';
import { useRouter } from 'next/router';
// import { useRouter } from 'next/router';
import PropTypes from 'prop-types';

import style from './index.module.scss';
import Link from 'next/link';

export const ChallengeItem = ({
  className,
  id,
  imgUrl,
  participants,
  leader,
  title,
  period,
  onClick,
  ...props
}) => {
  const [imgURL, setImgeFile] = useState({ imgUrl });

  const router = useRouter();

  return (
    <Link
      href={`participation/${id}`}
      className={style.ChallengeItem}
      role="button"
      tabIndex={0}
    >
      <div className={classNames(style.ImageContainer, `relative`)}>
        <Image
          className={classNames(style.Image, `rounded-lg`)}
          src={imgURL || require('../../image/default-user.png')}
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
        <div className={classNames(style.Title, `font-bold text-base`)}>
          {title}
        </div>
        <div className={classNames(style.Period, `font-medium text-xs`)}>
          {period}
        </div>
      </div>
    </Link>
  );
};

ChallengeItem.propTypes = {
  imageUrl: PropTypes.string.isRequired,
  participants: PropTypes.number.isRequired,
  leader: PropTypes.string.isRequired,
  title: PropTypes.string.isRequired,
  period: PropTypes.string.isRequired,
  onClick: PropTypes.func.isRequired,
};
