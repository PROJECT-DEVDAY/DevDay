import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import classNames from 'classnames';
import { useRouter } from 'next/router';

import style from './challenge.module.scss';
import http from '../api/http';

import { ChallengingItem } from '@/components/ChallengingItem';
import Container from '@/components/Container';
import Footer from '@/components/Footer';
import { HeaderButtons } from '@/components/HeaderButtons';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import { ReturnArrow } from '@/components/ReturnArrow';
import { MY_CHALLENGES_URL } from '@/constants';

const challenge = () => {
  const router = useRouter();
  const userInfo = useSelector(state => state.user);
  const [myChallengeInfo, setMyChallengeInfo] = useState([]);

  const headers = {
    Authorization: userInfo.accessToken,
  };

  useEffect(() => {
    http
      .get(MY_CHALLENGES_URL('PROCEED'), {
        headers,
      })
      .then(res => {
        setMyChallengeInfo(res.data.data);
      })
      .catch(e => {
        router.push('/user/login');
      });
  }, []);

  const goToChallengeDetail = id => {
    router.push('/challenge/id');
  };

  return (
    <Container className="relative">
      <div className="style.div-header sticky top-0">
        <ReturnArrow title="진행중인 챌린지" />
      </div>
      <div className="px-4">
        <HeaderButtons />
      </div>

      <div
        className={classNames(
          'm-4 p-2 rounded-2xl bg-white flex',
          style.boxShadow,
        )}
      >
        <div className={classNames('w-1/2 text-center', style.boundary)}>
          <p className="text-sm">챌린지</p>
          {/* <p className="font-medium">3개</p> */}
          <p className="font-medium">{myChallengeInfo.length}개</p>
        </div>
        <div className="w-1/2 text-center">
          <p className="text-sm">참여율</p>
          <p className="font-medium">3개</p>
          {/* <p className='font-medium'>{}개</p> */}
        </div>
      </div>

      {myChallengeInfo.map(myChallengeInfoItem => {
        return (
          <ChallengingItem
            picture={myChallengeInfoItem.backGroundUrl}
            title={myChallengeInfoItem.title}
            category={myChallengeInfoItem.category}
            date={`${myChallengeInfoItem.startDate} ${myChallengeInfoItem.endDate}`}
          />
        );
      })}
      <Container.Footer className="sticky w-full bottom-0 m-0">
        <Footer />
      </Container.Footer>
    </Container>
  );
};

export default PrivateRouter(challenge);
