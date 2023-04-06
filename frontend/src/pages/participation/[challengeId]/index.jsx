import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import Slider from 'react-slick';

import classNames from 'classnames';
import Image from 'next/image';
import Link from 'next/link';
import { useRouter } from 'next/router';
import Swal from 'sweetalert2';

import style from './index.module.scss';

import http from '@/api/http';
import { Button } from '@/components/Button';
import Container from '@/components/Container';
import { CHALLENGE_DETAIL_URL, CHALLENGE_JOIN_URL, LOCALE } from '@/constants';
import { getStartWithEndDate, getWeekDiff } from '@/utils';

import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';

const challengeintro = props => {
  const router = useRouter();
  const { challengeId } = router.query;
  const [data, setData] = useState('');
  useEffect(() => {
    if (challengeId) {
      http.get(`${CHALLENGE_DETAIL_URL}/${challengeId}`).then(res => {
        setData(res.data);
      });
    }
  }, [challengeId]);

  const settings = {
    dots: true,
    infinite: true,
    speed: 1000,
    slidesToShow: 1,
    slidesToScroll: 1,
    autoplay: true,
    autoplaySpeed: 4000,
  };
  const user = useSelector(state => state.user);

  const clickJoin = () => {
    const datas = {
      challengeRoomId: challengeId,
      nickname: user.userInfo.nickname,
    };
    http
      .get(CHALLENGE_JOIN_URL, {
        params: {
          challengeRoomId: challengeId,
          nickname: user.userInfo.nickname,
        },
      })
      .then(res => {
        router.push(`/participation/${challengeId}/pay`);
      })
      .catch(err => {
        console.log(err);
        if (err.response.data.code === 'J001') {
          router.replace(`/challenge/${challengeId}`);
        } else if (err.response.data.code === 'J001') {
          router.replace(`/challenge/${challengeId}`);
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

  const { week, day } = getWeekDiff(data.startDate, data.endDate);
  return (
    <Container>
      <Container.SubPageHeader />
      <Image
        src={data.backGroundUrl}
        alt="logo"
        width={100}
        height={100}
        loader={({ src }) => `${src}`}
        className={classNames('w-full')}
      />
      <div className={classNames('p-6', style.boxStyle)}>
        <p className="font-medium text-l">{data.hostNickname}</p>
        <p className="font-medium text-4xl ">{data.title}</p>
        <div className={classNames(style.fee, 'font-medium text-l mt-4')}>
          <p className="font-medium text-l">현재 인원 수</p>
          <p>
            {data.curParticipantsSize} / {data.maxParticipantsSize}명
          </p>
        </div>
        <div className={classNames(style.fee, 'font-medium text-l mt-2')}>
          <p>참가비</p>
          <p> {new Intl.NumberFormat(LOCALE).format(data.entryFee)}원</p>
        </div>
      </div>
      <div className={classNames('p-6', style.boxStyle)}>
        <p className="font-medium text-xl">챌린지 리더</p>
        <div className={classNames('flex mt-4')}>
          {!data.hostProfileImage && (
            <Image
              src={require('@/image/default-user.png')}
              alt="user"
              width={100}
              height={100}
              className="w-16 h-16 mr-4"
            />
          )}
          {data.hostProfileImage && (
            <Image
              src={data.hostProfileImage}
              loader={({ src }) => `${src}`}
              alt="user"
              width={100}
              height={100}
              className="w-16 h-16 mr-4"
            />
          )}
          <div className="w-full font-medium text-l">
            <p className={classNames(style.fee, style.mid)}>
              {data.hostNickname}
            </p>
            <div className="flex">
              <div className={classNames(style.fee, style.mid)}>
                <p>챌린지 개설</p>
                <p className="ml-4">{data.hostCount}개</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className={classNames('p-6')}>
        <p className="font-medium text-xl">챌린지 상금 안내(웹툰)</p>
        <Slider {...settings}>
          <div>
            <Image
              src={require('@/image/scene1.png')}
              alt="scene1"
              className="w-full"
            />
          </div>
          <div>
            <Image
              src={require('@/image/scene2.png')}
              alt="scene1"
              className="w-full"
            />
          </div>
          <div>
            <Image
              src={require('@/image/scene3.png')}
              alt="scene1"
              className="w-full"
            />
          </div>
          <div>
            <Image
              src={require('@/image/scene4.png')}
              alt="scene1"
              className="w-full"
            />
          </div>
          <div>
            <Image
              src={require('@/image/scene5.png')}
              alt="scene1"
              className="w-full"
            />
          </div>
        </Slider>
      </div>
      <div className={classNames('p-6')}>
        <p className="font-medium text-xl">저의 챌린지를 소개해요!</p>
        <p>{data.introduce}</p>
      </div>
      <div className={classNames('p-6')}>
        <p className="font-medium text-xl">챌린지 기간은요!</p>
        <p>
          {data.startDate} ~ {data.endDate}
        </p>
      </div>
      {data.category === 'ALGO' && (
        <div className={classNames('p-6')}>
          <p className="font-medium text-xl">
            매일 풀어야되는 알고리즘 갯수는요!
          </p>
          <p>
            <span className="font-medium text-xl text-red-500">
              {data.algorithmCount}개
            </span>
            입니다!!
          </p>
        </div>
      )}
      {(data.category === 'ALGO' || data.category === 'COMMIT') && (
        <div className={classNames('p-6')}>
          <p className="font-medium text-xl">이렇게 인증 해주세요</p>
          {data.category === 'ALGO' && (
            <p>1. Solved.Ac에서 문제를 풀고 제출해주세요</p>
          )}
          {data.category === 'COMMIT' && (
            <p>1. GitHub에 공부한 내용을 Commit 해주세요</p>
          )}
          <p>2. 사이트에서 제출이 적용되었는지 확인해주세요</p>
          <p>3. 끝</p>
        </div>
      )}
      {data.category === 'FREE' && (
        <div className={classNames('p-6')}>
          <p className="font-medium text-xl">이렇게 인증 해주세요</p>
          <div className="grid-cols-2 grid gap-2">
            <div className="relative border-solid border-2 border-slate-200">
              <Image
                src={data.successUrl}
                loader={({ src }) => `${src}`}
                width={100}
                height={200}
                alt="user"
                className="w-full"
              />
              <p className="font-medium text-xl absolute bottom-0 bg-white w-full text-center">
                좋은 예시
              </p>
            </div>
            <div className="relative border-solid border-2 border-slate-200 ">
              <Image
                src={data.failUrl}
                loader={({ src }) => `${src}`}
                width={100}
                height={200}
                alt="user"
                className="w-full"
              />
              <p className="font-medium text-xl absolute bottom-0 bg-white w-full text-center">
                나쁜 예시
              </p>
            </div>
          </div>
        </div>
      )}
      <div className={classNames('p-6')}>
        <p className="font-medium text-xl">챌린지 진행시 꼭 알아주세요!</p>
        <p>1. 00시 00분 ~ 23시 59분 사이에 인증하셔야 합니다.</p>
        <p>2. 어플에서 제출이 적용되었는지 확인해주세요</p>
      </div>
      <div className={classNames('p-6 mb-16')}>
        <p className="font-medium text-xl">주의사항</p>
        <p>챌린지 시작 후 취소 불가</p>
        <p>
          챌린지는 여러 회원님들이 함께 하는 대회인만큼, 챌린지 시작후에는
          환불이 불가능합니다.
        </p>
      </div>
      <Container.MainFooter>
        <div className="flex justify-between p-4">
          <div className="text-left">
            <p>{getStartWithEndDate(data.startDate, data.endDate)}</p>
            <p>
              매일 {week > 0 && `${week}주 `}
              {day > 0 && `${day}일 `}동안
            </p>
          </div>
          <div className="w-1/2">
            <Button label="참여하기" onClick={clickJoin} />
          </div>
        </div>
      </Container.MainFooter>
    </Container>
  );
};

export default challengeintro;
