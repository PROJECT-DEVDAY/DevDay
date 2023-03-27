import React, { useState } from 'react';
import {
  AiOutlineSearch,
  AiOutlinePlus,
  AiOutlineCloseCircle,
} from 'react-icons/ai';

import classnames from 'classnames';
import Image from 'next/image';
import PropTypes from 'prop-types';

import style from './main.module.scss';

import { ChallengeItem } from '@/components/ChallengeItem';
import Footer from '@/components/Footer';
import { HeaderButtons } from '@/components/HeaderButtons';
import { useRouter } from 'next/router';

const main = props => {
  const Router = useRouter();
  const [searchBoxStatus, setSearchBoxStatus] = useState(false);

  const search = () => {};
  const searchOnKeyPress = e => {
    if (e.key === 'Enter') {
      search();
    }
  };
  const addChallenge = () => {
    Router.push('/create/check');
  };

  const searchBoxStatusChange = () => {
    setSearchBoxStatus(prev => !prev);
  };

  const goToChallengDetail = () => {};

  const HEADER_ITEMS = [
    {
      label: 'search',
      icon: <AiOutlineSearch size={30} />,
      onClick: searchBoxStatusChange,
    },
    {
      label: 'add',
      icon: <AiOutlinePlus size={30} />,
      onClick: addChallenge,
    },
  ];

  const CHALLENGE_ITEMS = [
    {
      id: 1,
      imgUrl: '@/image/default-user.png',
      participants: '4',
      leader: '박태환',
      title: '1일 1알고리즘 도전하기',
      period: '03.26 ~ 04.12',
    },
    {
      id: 2,
      imgUrl: '@/image/default-user.png',
      participants: '11',
      leader: '홍금비',
      title: '1일 1커밋 도전하기',
      period: '03.30 ~ 04.12',
    },
    {
      id: 3,
      imgUrl: '@/image/default-user.png',
      participants: '55',
      leader: '신대득',
      title: '매일매일 자소서쓰기',
      period: '04.01 ~ 04.12',
    },
    {
      id: 4,
      imgUrl: require('@/image/default-user.png'),
      participants: '100',
      leader: '최형운',
      title: '런닝머신 1시간뛰기',
      period: '03.26 ~ 04.26',
    },
  ];

  return (
    <div className={classnames(style.MainHeader, `pt-6`)}>
      <div
        className={classnames(`flex justify-between px-6`, style.TopOfHeader)}
      >
        <Image src={require('@/image/MainLogo.png')} width={70} height={70} />
        <div className={classnames(`flex HeaderIcons`)}>
          {HEADER_ITEMS.map(item => {
            const { label, icon, onClick } = item;
            const key = `header-item-${label}`;
            return (
              <button
                type="button"
                className={classnames(
                  style.HeaderBox,
                  `flex items-center p-1`,
                  key,
                )}
                key={key}
                onClick={onClick}
              >
                {icon}
              </button>
            );
          })}
        </div>
      </div>

      <hr className={style.Divider} />

      <div className={classnames(`p-4`, style.BottomOfHeader)}>
        {searchBoxStatus && (
          <div
            className={classnames(
              `flex items-center justify-center h-full`,
              style.HeaderSearch,
            )}
          >
            <div
              className={classnames(
                `flex items-center w-full`,
                style.searchContainer,
              )}
            >
              <input
                className={classnames(`py-2 pl-4 rounded-3xl`)}
                placeholder="검색하실 내용을 입력해주세요"
                onKeyPress={searchOnKeyPress}
              />
              <AiOutlineSearch size={28} onClick={search} />
              <AiOutlineCloseCircle size={28} onClick={searchBoxStatusChange} />
            </div>
          </div>
        )}

        <div className={classnames(`pt-4`, style.HeaderButtons)}>
          <HeaderButtons />
        </div>
      </div>
      <div
        className={classnames(
          style.MainBody,
          `flex flex-wrap justify-around px-6`,
        )}
      >
        {CHALLENGE_ITEMS.map(item => {
          const { id, imgURL, participants, leader, title, period } = item;
          const key = `challenge-item-${id}`;
          return (
            <ChallengeItem
              id={id}
              imgUrl={imgURL}
              participants={participants}
              leader={leader}
              title={title}
              period={period}
              onClick={goToChallengDetail}
            />
          );
        })}
      </div>

      <div className={classnames('absolute w-full bottom-0 m-0')}>
        <Footer />
      </div>
    </div>
  );
};

export default main;
