import React, { useState } from 'react';
import {
  AiOutlineSearch,
  AiOutlinePlus,
  AiOutlineCloseCircle,
} from 'react-icons/ai';

import classNames from 'classnames';
import Image from 'next/image';
import Link from 'next/link';
import { useRouter } from 'next/router';

import style from './index.module.scss';

import { ChallengeItem } from '@/components/ChallengeItem';
import Container from '@/components/Container';
import Footer from '@/components/Footer';
import { HeaderButtons } from '@/components/HeaderButtons';

const main = props => {
  const Router = useRouter();
  const [searchBoxStatus, setSearchBoxStatus] = useState(false);

  const search = () => {};
  const searchOnKeyPress = e => {
    if (e.key === 'Enter') {
      search();
    }
  };

  const searchBoxStatusChange = () => {
    setSearchBoxStatus(prev => !prev);
  };

  const goToChallengDetail = () => {};

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
    <Container>
      <Container.MainHeader>
        <button
          type="button"
          className={classNames(style.HeaderBox, `flex items-center p-1`)}
          onClick={searchBoxStatusChange}
        >
          <AiOutlineSearch size={30} />
        </button>
        <Link
          href="/create/check"
          className={classNames(style.HeaderBox, `flex items-center p-1`)}
        >
          <AiOutlinePlus size={30} />
        </Link>
      </Container.MainHeader>
      <Container.MainBody className="pt-4">
        {searchBoxStatus && (
          <div
            className={classNames(
              `flex items-center justify-center h-full mb-4`,
              style.HeaderSearch,
            )}
          >
            <div
              className={classNames(
                `flex items-center w-full`,
                style.searchContainer,
              )}
            >
              <input
                className={classNames(`py-2 pl-4 rounded-3xl`)}
                placeholder="검색하실 내용을 입력해주세요"
                onKeyPress={searchOnKeyPress}
              />
              <AiOutlineSearch size={28} onClick={search} />
              <AiOutlineCloseCircle size={28} onClick={searchBoxStatusChange} />
            </div>
          </div>
        )}
        <div className="mb-4">
          <HeaderButtons />
        </div>
        <div className="grid gap-0 grid-cols-1 mob:grid-cols-2 mob:gap-2">
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
      </Container.MainBody>
      <Container.MainFooter>
        <Footer />
      </Container.MainFooter>
    </Container>
  );
};

export default main;
