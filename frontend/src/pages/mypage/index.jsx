import React, { useEffect, useState } from 'react';
import { AiOutlineSetting } from 'react-icons/ai';
import { BsQuestionSquare } from 'react-icons/bs';
import { HiOutlineBell } from 'react-icons/hi';
import { useDispatch, useSelector } from 'react-redux';

import Image from 'next/image';
import { useRouter } from 'next/router';

import http from '@/api/http';
import Container from '@/components/Container';
import Footer from '@/components/Footer';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import { SelectArrow } from '@/components/SelectArrow';
import { SelectOption } from '@/components/SelectOption';
import { UserAvatar } from '@/components/UserAvatar';
import { MYPAGE_URL } from '@/constants';

const index = () => {
  const router = useRouter();
  const userInfo = useSelector(state => state.user);
  const [myPageInfo, setMyPageInfo] = useState({});

  useEffect(() => {
    http
      .get(MYPAGE_URL)
      .then(res => {
        setMyPageInfo(res.data.data);
      })
      .catch(e => {
        router.push('/user/login');
      });
  }, []);

  const goToProfile = () => {
    router.push('/mypage/profile');
  };

  const goToSettings = () => {
    router.push('/mypage/settings');
  };

  const goToMyChallenge = () => {
    router.push('/mypage/challenge');
  };

  const goToDeposit = () => {
    router.push('/mypage/deposit');
  };

  const goToReward = () => {
    router.push('/mypage/reward');
  };

  const goToCreateCheck = () => {
    router.push('/create/check');
  };

  return (
    <Container>
      <Container.Header>
        <div className="style.div-header p-4 flex justify-between items-center">
          <h1 className="font-medium text-2xl">마이페이지</h1>
          <div className="rounded-full bg-purple-300 w-10 h-10 flex justify-center items-center">
            <AiOutlineSetting className="text-2xl" onClick={goToSettings} />
          </div>
        </div>
      </Container.Header>
      <Container.Body>
        <div className="div-body p-4 relative">
          <div className="absolute top-5 left-7">
            <UserAvatar
              imageURL={myPageInfo.profileImgUrl}
              width={50}
              height={50}
            />
            {/* <UserAvatar imageURL="" width={50} height={50} /> */}
          </div>
          <SelectArrow title={myPageInfo.nickname} fill onClick={goToProfile} />
          <div className="px-6 py-8">
            <div className="flex justify-between mb-6" onClick={goToDeposit}>
              <div className="flex items-center">
                <div>
                  <Image
                    src={require('../../image/money.png')}
                    alt="프로필 이미지"
                    className="aspect-auto w-6 mr-2"
                  />
                </div>
                <p>예치금</p>
              </div>
              <p>{myPageInfo.prize}원</p>
            </div>
            <div className="flex justify-between mb-4" onClick={goToReward}>
              <div className="flex items-center">
                <Image
                  src={require('../../image/reward.png')}
                  className="aspect-auto w-6 mr-2"
                />
                <p>상금</p>
              </div>
              <p>{myPageInfo.deposit}원</p>
            </div>
          </div>
        </div>
        <div>
          <p className="px-4">챌린지 현황</p>
          <hr className="w-full" />
          <div
            className="flex justify-between my-5 mx-12"
            onClick={goToMyChallenge}
          >
            <div>
              <p className="text-center">{myPageInfo.challengingCnt}</p>
              <p>참가중</p>
            </div>
            <div>
              <p className="text-center">{myPageInfo.challengedCnt}</p>
              <p>완료</p>
            </div>
            <div>
              <p className="text-center">{myPageInfo.leaderCnt}</p>
              <p>리더</p>
            </div>
          </div>
          <hr />
        </div>
        <div className="div-body p-4">
          <div className="relative">
            <div className="absolute top-4 left-4">
              <HiOutlineBell className="text-xl" />
            </div>
            <div onClick={goToCreateCheck}>
              <SelectOption content="챌린지 개설하기" fill />
            </div>
          </div>
          <div className="relative">
            <div className="absolute top-4 left-4">
              <BsQuestionSquare className="text-xl" />
            </div>
            <SelectOption content="문의하기" fill />
          </div>
        </div>
      </Container.Body>
      <Container.MainFooterWithNavigation />
    </Container>
  );
};

export default PrivateRouter(index);
