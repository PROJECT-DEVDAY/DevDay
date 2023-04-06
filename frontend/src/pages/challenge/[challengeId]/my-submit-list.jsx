import React, { useEffect, useState, useRef, useCallback } from 'react';
import { useSelector } from 'react-redux';

import _ from 'lodash';
import Image from 'next/image';
import { useRouter } from 'next/router';

import style from './challenge.module.scss';

import http from '@/api/http';
import Container from '@/components/Container';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import {
  CHALLENGE_DETAIL_URL,
  CHALLENGE_ALGO_URL,
  CHALLENGE_COMMIT_URL,
  CHALLENGE_AUTH_ALGO_URL,
} from '@/constants';
import { getDatesStartToLast } from '@/utils';

const mySubmitList = ({ ...props }) => {
  const [date, setDate] = useState('');
  const router = useRouter();
  const user = useSelector(state => state.user);
  const [challengeInfo, setChallenge] = useState('');
  const { challengeId } = router.query;
  const [solvedList, setSolvedList] = useState([]);
  const [getDate, setGetDate] = useState('');
  const [getRange, setGetRange] = useState('');
  useEffect(() => {
    if (challengeId) {
      http.get(`${CHALLENGE_DETAIL_URL}/${challengeId}`).then(res => {
        setChallenge(res.data);
        const range = getDatesStartToLast(res.data.startDate, res.data.endDate);
        setGetRange(range);
        console.log(challengeInfo);
        if (res.data.category === 'COMMIT') {
          range.map(item => {
            http
              .get(
                `${CHALLENGE_COMMIT_URL}/date?userId=${user.userInfo.userId}&selectDate=${item}`,
              )
              .then(res => {
                const count = res.data.data.commitCount;
                console.log(count);
                setSolvedList(solvedList => {
                  return [...solvedList, [item.slice(2, 10), count]];
                });
              })
              .catch(err => {
                setSolvedList(solvedList => {
                  return [...solvedList, [item.slice(2, 10), 0]];
                });
              });
          });
        } else if (res.data.category === 'FREE') {
          range.map(item => {
            http
              .get(
                `${CHALLENGE_AUTH_ALGO_URL}/${challengeId}/record/users?view='ALL'`,
              )
              .then(res => {
                setSolvedList(res.data.data);
              })
              .catch(err => {});
          });
        }
      });
    }
  }, [challengeId]);
  const handleChange = e => {
    setDate(e.target.value);
    http
      .get(
        `${CHALLENGE_ALGO_URL}/date?userId=${user.userInfo.userId}&selectDate=${e.target.value}`,
      )
      .then(res => {
        setSolvedList(res.data.data.solvedList);
      });
  };

  return (
    <Container>
      <Container.SubPageHeader title={challengeInfo.name} />
      <Container.MainBody className="px-0 bg-white text-center">
        <h5 className="font-bold text-center text-2xl pb-4 border-b-2">
          참여자 인증 상세보기
        </h5>
        {challengeInfo.category === 'ALGO' && (
          <div>
            <select
              name="date"
              id="date-picker"
              onChange={handleChange}
              className="my-4"
            >
              {getRange.map(r => {
                return (
                  <option key={r} value={r}>
                    {r}
                  </option>
                );
              })}
            </select>
            <div className="grid grid-cols-3 gap-3 mt-4 p-4">
              {solvedList &&
                solvedList.map(item => (
                  <div className={style.box}>
                    <p className="m-auto text-xl text-medium">{item}</p>
                  </div>
                ))}
            </div>
          </div>
        )}
        {challengeInfo.category === 'COMMIT' && (
          <div>
            <div className="grid grid-cols-3 gap-3 mt-4 p-4">
              {solvedList &&
                solvedList.map(item => (
                  <div className={style.commitBox}>
                    <p className="pb-2 whitespace-nowrap">{item[0]}</p>
                    <p className="m-auto text-xl text-medium text-black">
                      {item[1]}
                    </p>
                  </div>
                ))}
            </div>
          </div>
        )}
        {challengeInfo.category === 'FREE' && (
          <div>
            <div className="grid grid-cols-3 gap-3 mt-4 p-4">
              {solvedList &&
                solvedList.map(item => (
                  <div className={style.freeBox}>
                    <p>{item.createAt.slice(2, 10)}</p>
                    <Image
                      src={item.photoUrl}
                      alt="free"
                      width={100}
                      height={100}
                      loader={({ src }) => src}
                      className="w-full style.boxStyle"
                    />
                  </div>
                ))}
            </div>
          </div>
        )}
      </Container.MainBody>
      <Container.MainFooterWithNavigation />
    </Container>
  );
};

export default PrivateRouter(mySubmitList);
