import React, { useState } from 'react';

import classnames from 'classnames';
import Image from 'next/image';
// import { useRouter } from 'next/router';
import PropTypes from 'prop-types';

import style from './index.module.scss';

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

  // const router = useRouter();
  // const goTo = () => {
  //   router.push(`${goToUrl}`);
  // };
  const goToChallengDetail = () => {
    onClick(id);
  };
  const handleKeyPress = event => {
    if (event.key === 'Enter' || event.key === ' ') {
      goToChallengDetail();
    }
  };
  return (
    <div
      className={style.ChallengeItem}
      role="button"
      tabIndex={0}
      onClick={goToChallengDetail}
      onKeyPress={handleKeyPress}
    >
      <div className={classnames(style.ImageContainer, `relative`)}>
        <Image
          className={classnames(style.Image, `rounded-lg`)}
          src={imgURL || require('../../image/default-user.png')}
          alt="temp"
        />
        <div
          className={classnames(
            style.Participants,
            `absolute rounded-md p-1 font-bold text-xs`,
          )}
        >
          {participants}명
        </div>
      </div>
      <div className={classnames(`flex flex-col ml-1 pt-1`)}>
        <div className={classnames(style.Leader, `font-medium text-xs`)}>
          {leader}
        </div>
        <div className={classnames(style.Title, `font-bold text-base`)}>
          {title}
        </div>
        <div className={classnames(style.Period, `font-medium text-xs`)}>
          {period}
        </div>
      </div>
    </div>
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
