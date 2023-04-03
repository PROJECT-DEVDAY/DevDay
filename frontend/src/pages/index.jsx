import React, { useEffect, useState, useRef } from 'react';
import {
  AiOutlineSearch,
  AiOutlinePlus,
  AiOutlineCloseCircle,
} from 'react-icons/ai';

import classNames from 'classnames';
import Link from 'next/link';

import http from './api/http';
import style from './index.module.scss';

import { ChallengeItem } from '@/components/ChallengeItem';
import Container from '@/components/Container';
import { HeaderButtons } from '@/components/HeaderButtons';
import { CHALLENGES_SEARCH_URL } from '@/constants';
import { getStartWithEndDate } from '@/utils';

const NAV = {
  전체: 'ALL',
  기본: 'FREE',
  commit: 'COMMIT',
  알고리즘: 'ALGO',
};
const INITIAL_NAV_KEY = '전체';
const NAV_LIST = Object.keys(NAV);

const main = ({ initialList, ...props }) => {
  const [challengeList, setChallengeList] = useState(initialList);
  const [lastChallengeId, setLastChallengeId] = useState('');
  const [type, setType] = useState(INITIAL_NAV_KEY);
  const searchRef = useRef();
  const [searchMode, setSearchMode] = useState(false);

  const search = async params => {
    const { data } = await http.get(CHALLENGES_SEARCH_URL, {
      params: {
        search: '',
        category: NAV[type],
        size: 20,
        offset: lastChallengeId,
        ...params,
      },
    });

    setChallengeList(data);
  };

  const searchOnKeyPress = e => {
    if (e.key === 'Enter') {
      search({ search: searchRef.current.value, offset: '' });
    }
  };
  const toggleSearchMode = () => {
    setSearchMode(prev => !prev);
  };

  const initialSearchQuery = () => {
    searchRef.current.value = '';
    search({ search: '', offset: '' });
  };

  const selectNavigation = value => {
    setType(value);
    search({ category: NAV[value], offset: '' });
  };

  useEffect(() => {
    if (challengeList && challengeList.length > 0) {
      setLastChallengeId(challengeList[challengeList.length - 1].id);
    }
  }, [challengeList]);

  return (
    <Container>
      <Container.MainHeader>
        <button
          type="button"
          className={classNames(style.HeaderBox, `flex items-center p-1`)}
          onClick={toggleSearchMode}
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
        {searchMode && (
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
                ref={searchRef}
                onKeyPress={searchOnKeyPress}
              />
              <AiOutlineCloseCircle size={28} onClick={initialSearchQuery} />
            </div>
          </div>
        )}
        <div className="mb-4">
          <HeaderButtons
            items={NAV_LIST}
            select={type}
            setSelect={selectNavigation}
          />
        </div>
        <div className="grid gap-0 grid-cols-1 mob:grid-cols-2 mob:gap-4">
          {challengeList &&
            challengeList.map(item => {
              const {
                id,
                backGroundUrl,
                curParticipantsSize,
                nickname,
                title,
                startDate,
                endDate,
              } = item;

              const period = getStartWithEndDate(startDate, endDate);

              return (
                <ChallengeItem
                  key={id}
                  id={id}
                  imgUrl={backGroundUrl}
                  participants={curParticipantsSize}
                  leader={nickname}
                  title={title}
                  period={period}
                />
              );
            })}
        </div>
      </Container.MainBody>
      <Container.MainFooterWithNavigation />
    </Container>
  );
};

export const getServerSideProps = async () => {
  let initialList = [];
  try {
    const { data } = await http.get(CHALLENGES_SEARCH_URL, {
      params: {
        search: '',
        category: 'ALL',
        offset: null,
        size: 20,
      },
    });
    initialList = [...initialList, ...data];
  } catch (e) {
    // console.error('초기 리스트를 가져올 수 없습니다.', e);
  }

  return {
    props: {
      initialList,
    },
  };
};

export default main;

// [
//   {
//     id: 2,
//     title: '딩제목제목제모게좀ㄱ족멕족',
//     hostId: 1,
//     category: 'ALGO',
//     startDate: '2023-03-31',
//     endDate: '2023-04-30',
//     backGroundUrl:
//       'https://devday-bucket.s3.ap-northeast-2.amazonaws.com/default_image/921ad56f-d98b-4f0c-9ef5-f9a528059bc1-Algo_Default_Url.png',
//     curParticipantsSize: 100,
//   },
//   {
//     id: 3,
//     title: '박태환 이눔!',
//     hostId: 1,
//     category: "'COMMIT",
//     startDate: '2023-03-31',

//     endDate: '2023-04-30',
//     backGroundUrl:
//       'https://devday-bucket.s3.ap-northeast-2.amazonaws.com/default_image/921ad56f-d98b-4f0c-9ef5-f9a528059bc1-Algo_Default_Url.png',
//     curParticipantsSize: 1,
//   },
//   {
//     id: 1,
//     title: '박태환 얼른와랏!',
//     hostId: 1,
//     category: "'COMMIT",
//     startDate: '2023-03-31',
//     endDate: '2023-04-30',
//     backGroundUrl:
//       'https://devday-bucket.s3.ap-northeast-2.amazonaws.com/default_image/921ad56f-d98b-4f0c-9ef5-f9a528059bc1-Algo_Default_Url.png',
//     curParticipantsSize: 1,
//   },
// ],
