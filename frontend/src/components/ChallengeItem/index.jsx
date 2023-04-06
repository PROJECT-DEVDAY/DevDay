import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import classNames from 'classnames';
import Image from 'next/image';
import Link from 'next/link';
import { useRouter } from 'next/router';
import PropTypes from 'prop-types';
import Swal from 'sweetalert2';

import style from './index.module.scss';

import http from '@/api/http';
import { CHALLENGE_JOIN_URL } from '@/constants';

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
  const router = useRouter();
  const user = useSelector(state => state.user);
  const checkPartici = () => {
    http
      .get(CHALLENGE_JOIN_URL, {
        params: {
          challengeRoomId: id,
          nickname: user.userInfo.nickname,
        },
      })
      .then(res => {
        router.replace(`/participation/${id}`);
      })
      .catch(err => {
        console.log(err.response.data.code);
        if (err.response.data.code === 'J001') {
          router.replace(`/challenge/${id}`);
        } else {
          Swal.fire({
            position: 'center',
            icon: 'warning',
            title: '실패!',
            text: err.response.data.message,
            showConfirmButton: false,
            timer: 1500,
          });
        }
      });
  };
  return (
    <div onClick={checkPartici} className={style.ChallengeItem} type="button">
      <div
        className={classNames(style.ImageContainer, `relative object-cover`)}
      >
        <img
          className={classNames(style.Image, `rounded-lg`)}
          src={imgUrl || require('../../image/default-user.png')}
          alt="temp"
        />
        {participants >= 0 && (
          <div
            className={classNames(
              style.Participants,
              `absolute rounded-md p-1 font-bold text-xs`,
            )}
          >
            {participants}명
          </div>
        )}
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
    </div>
  );
};

ChallengeItem.propTypes = {
  imageUrl: PropTypes.string,
  participants: PropTypes.number.isRequired,
  leader: PropTypes.string,
  title: PropTypes.string.isRequired,
  period: PropTypes.string.isRequired,
  onClick: PropTypes.func,
};
