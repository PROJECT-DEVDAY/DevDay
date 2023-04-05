import React, { useEffect, useState, useRef } from 'react';
import {
  AiOutlineSearch,
  AiOutlinePlus,
  AiOutlineCloseCircle,
} from 'react-icons/ai';

import classNames from 'classnames';
import Image from 'next/image';
import Link from 'next/link';

import http from '@/api/http';
import style from './index.module.scss';

import { ChallengeItem } from '@/components/ChallengeItem';
import Container from '@/components/Container';
import { HeaderButtons } from '@/components/HeaderButtons';
import { CHALLENGES_SEARCH_URL } from '@/constants';
import { getStartWithEndDate } from '@/utils';

import _debounce from 'lodash/debounce';
import loadingImage from '@/image/loading.gif';

const NAV = {
  전체: 'ALL',
  기본: 'FREE',
  commit: 'COMMIT',
  알고리즘: 'ALGO',
};
const NAV_LIST = Object.keys(NAV);
const INITIAL_NAV_KEY = '전체';
const INITIAL_PARAMS = {
  search: '',
  category: 'ALL',
  offset: '',
  size: 20,
};

const fetch = async params => {
  try {
    const { data } = await http.get(CHALLENGES_SEARCH_URL, {
      params: {
        ...INITIAL_PARAMS,
        ...params,
      },
    });
    return data;
  } catch (e) {
    console.error('데이터를 fetch하는 과정에서 문제가 생겼어요.', e);
    return [];
  }
};

const main = ({ initialList, ...props }) => {
  const [searchMode, setSearchMode] = useState(false);
  const [loading, setLoading] = useState(false);
  const searchParams = useRef(INITIAL_PARAMS);
  const searchRef = useRef();
  const observerRef = useRef();
  const loadingRef = useRef();
  const [challengeList, setChallengeList] = useState(initialList);
  const lastChallengeId = useRef();
  const [type, setType] = useState(INITIAL_NAV_KEY);

  const observerCallback = (entries, io) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        pagenating();
      }
    });
  };

  const selectType = value => {
    setType(value);
    updateSearchParams({
      category: NAV[value],
      offset: '',
    });
  };

  const pagenating = _debounce(() => {
    updateSearchParams(
      {
        offset: lastChallengeId.current,
      },
      true,
    );
  }, 500);

  const search = async (params, added = false) => {
    const data = await fetch(params);
    if (data && data.length > 0) {
      if (added) {
        setChallengeList(prev => [...prev, ...data]);
      } else {
        setChallengeList(data);
      }
    } else {
      setLoading(false);
    }
  };

  const updateSearchParams = (params, added = false) => {
    searchParams.current = {
      ...searchParams.current,
      ...params,
    };
    search(searchParams.current, added);
  };

  const searchOnKeyPress = e => {
    if (e.key === 'Enter') {
      updateSearchParams({
        search: searchRef.current.value,
        offset: '',
      });
    }
  };
  const toggleSearchMode = () => {
    setSearchMode(prev => !prev);
  };

  const initialSearchQuery = () => {
    searchRef.current.value = '';
    updateSearchParams({
      search: '',
      offset: '',
    });
  };

  useEffect(() => {
    if (challengeList && challengeList.length > 0) {
      lastChallengeId.current = challengeList[challengeList.length - 1].id;
      setLoading(true);
    }
  }, [challengeList]);

  /**
   * loadingRef가 있으면 observer 설정
   */
  useEffect(() => {
    observerRef.current = new IntersectionObserver(observerCallback, {
      threshold: 0.5,
    });
    if (loadingRef?.current) {
      observerRef?.current.observe(loadingRef?.current);
    }
  });

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
            setSelect={selectType}
          />
        </div>
        <div className="grid gap-0 grid-cols-1 mob:grid-cols-2 mob:gap-4">
          {challengeList &&
            challengeList.map((item, i) => {
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
                  className="bg-black"
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
        {loading && (
          <Image
            src={loadingImage}
            width="60"
            height="60"
            className="mr-auto ml-auto mt-8"
            ref={loadingRef}
            alt="loading icon"
          />
        )}
      </Container.MainBody>
      <Container.MainFooterWithNavigation />
    </Container>
  );
};

export const getServerSideProps = async () => {
  let initialList = [];
  try {
    const { data } = await http.get(CHALLENGES_SEARCH_URL, {
      params: INITIAL_PARAMS,
    });
    initialList = [...initialList, ...data];
  } catch (e) {
    console.error('초기 리스트를 가져올 수 없습니다.', e);
  }
  return {
    props: {
      initialList,
    },
  };
};

export default main;
