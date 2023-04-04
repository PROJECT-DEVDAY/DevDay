import React, { useEffect, useState, useRef, useCallback } from 'react';
import { useRouter } from 'next/router';
import { useSelector } from 'react-redux';

import _ from 'lodash';
import style from './challenge.module.scss';
import Container from '@/components/Container';
import http from '../../api/http';
import { CHALLENGE_DETAIL_URL, CHALLENGE_ALGO_URL } from '@/constants';

const mySubmitList = ({ ...props }) => {
  const [date, setDate] = useState('');
  const router = useRouter();
  const user = useSelector(state => state.user);
  const [challengeInfo, setChallenge] = useState('');
  const { challengeId } = router.query;
  const [solvedList, setSolvedList] = useState('');
  useEffect(() => {
    if (challengeId) {
      http.get(`${CHALLENGE_DETAIL_URL}/${challengeId}`).then(res => {
        setChallenge(res.data);
      });
    }
  }, [challengeId]);
  const handleChange = e => {
    setDate(e.target.value);
    const submit = {
      userID: user.userInfo.userId,
      selectDate: e.target.value,
    };
    console.log(submit);
    http
      .get(
        `${CHALLENGE_ALGO_URL}/date?userId=${user.userInfo.userId}&selectDate=${e.target.value}`,
      )
      .then(res => {
        setSolvedList(res.data.data.solvedList);
        console.log(res.data.data.solvedList);
      });
  };

  return (
    <Container>
      <Container.SubPageHeader title={challengeInfo.name} />
      <Container.MainBody className="px-0 bg-white text-center">
        <h5 className="font-bold text-center text-2xl pb-4 border-b-2">
          참여자 인증 상세보기
        </h5>
        <input type="date" className="font-bold" onChange={handleChange} />
        <div className="grid grid-cols-3 gap-3 mt-4 p-4">
          {solvedList &&
            solvedList.map(item => (
              <div className={style.box}>
                <p className="m-auto text-xl text-medium">{item}</p>
              </div>
            ))}
        </div>
      </Container.MainBody>
      <Container.MainFooterWithNavigation />
    </Container>
  );
};

export default mySubmitList;
