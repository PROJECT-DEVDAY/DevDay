import React, { useEffect, useState, useRef, useCallback } from 'react';
import { useSelector } from 'react-redux';

import _ from 'lodash';
import { useRouter } from 'next/router';

import style from './challenge.module.scss';

import http from '@/api/http';
import Container from '@/components/Container';
import {
  CHALLENGE_DETAIL_URL,
  CHALLENGE_ALGO_URL,
  CHALLENGE_COMMIT_URL,
} from '@/constants';

const mySubmitList = ({ ...props }) => {
  const [date, setDate] = useState('');
  const router = useRouter();
  const user = useSelector(state => state.user);
  const [challengeInfo, setChallenge] = useState('');
  const { challengeId } = router.query;
  const [solvedList, setSolvedList] = useState([]);
  const [getDate, setGetDate] = useState('');
  useEffect(() => {
    if (challengeId) {
      http.get(`${CHALLENGE_DETAIL_URL}/${challengeId}`).then(res => {
        setChallenge(res.data);
        console.log(challengeInfo);
        if (res.data.category === 'COMMIT') {
          const startDate =
            Date.parse(res.data.startDate) / (1000 * 60 * 60 * 24);
          const endDate = Date.parse(res.data.endDate) / (1000 * 60 * 60 * 24);
          console.log(startDate, endDate);
          setGetDate(endDate - startDate);
          for (let index = 1; index < endDate - startDate; index++) {
            const today = startDate + index;
            http
              .get(
                `${CHALLENGE_COMMIT_URL}/date?userId=${user.userInfo.userId}&selectDate=${today}`,
              )
              .then(res => {
                console.log(res);
                // setSolvedList(solvedList => {
                //   return [...solvedList, res.data.data.solvedList];
                // });
              });
          }
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
            <input type="date" className="font-bold" onChange={handleChange} />
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
                  <div className={style.box}>
                    <p>{item[0]}</p>
                    <p className="m-auto text-xl text-medium">{item[1]}</p>
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

export default mySubmitList;
