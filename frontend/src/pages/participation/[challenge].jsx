import React from 'react';
import { Doughnut } from 'react-chartjs-2';

import classNames from 'classnames';
import Image from 'next/image';

import style from './ChallengeIntro.module.scss';

import { Button } from '@/components/Button';

const challengeintro = props => {
  const expData = {
    datasets: [
      {
        labels: ['A', 'B', 'C', 'D'],
        data: [1, 1, 1, 1],
        borderWidth: 2,
        hoverBorderWidth: 3,
        backgroundColor: ['#ffaf14', '#fff962', '#ffaf14', '#fff962'],
        fill: true,
      },
    ],
  };
  return (
    <div>
      <Image
        src={require('@/image/devdaying.gif')}
        alt="logo"
        className={classNames('w-full')}
      />
      <div className={classNames('p-6', style.boxStyle)}>
        <p className="font-medium text-l">리더이름</p>
        <p className="font-medium text-4xl">챌린지 명</p>
        <p className="font-medium text-l">현재 인원 수</p>
        <div className={classNames(style.fee, 'font-medium text-l')}>
          <p>참가비</p>
          <p>0000000원</p>
        </div>
      </div>
      <div className={classNames('p-6', style.boxStyle)}>
        <p className="font-medium text-xl">챌린지 리더</p>
        <div className={classNames('flex mt-4')}>
          <Image
            src={require('@/image/default-user.png')}
            alt="user"
            className="w-16 h-16 mr-4"
          />
          <div className="w-full font-medium text-l">
            <p className={classNames(style.fee, style.mid)}>홍길동</p>
            <div className="flex">
              <div className={classNames(style.fee, style.mid)}>
                <p>챌린지 개설</p>
                <p className="ml-4">0개</p>
              </div>
              <div
                className={classNames(
                  style.fee,
                  style.mid,
                  'border-l-2 border-solid border-extra',
                )}
              >
                <p>평점</p>
                <p className="ml-4">0점</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className={classNames('p-6', style.boxStyle)}>
        <p className="font-medium text-xl">챌린지 상금 안내</p>
        <p className="font-medium text-l">상금</p>
        <p className="font-medium text-l">
          = (참가비/챌린지 기간)*챌린지 인원 / 성공인원
        </p>
        <p className="text-xs">
          하루치 참가비를 성공한 인원이 나눠서 가져가는 구조입니다.
        </p>
        <div className="flex">
          <div className="w-3/5">
            <Doughnut
              options={{
                legend: {
                  display: true,
                  position: 'right',
                },
              }}
              data={expData}
              height={120}
            />
          </div>
          <div className="w-2/5 break-all p-4">
            <p>예시</p>
            <p className="text-xs">
              참가비가 10000원, 기간이 5일, 참가인원이 6명, 당일 챌린지
              성공인원은 4명이라고 가정할때, 하루 참가비는 2000*6=12000원이다.
              이것을 성공한 4명이서 나누어 가져가는 구조이다. 챌린지를 실패한
              사람은 한푼도 못가져간다.
            </p>
          </div>
        </div>
      </div>
      <div className={classNames('p-6')}>
        <p className="font-medium text-xl">저의 챌린지를 소개해요!</p>
        <p>1일 1알고리즘 다같이 해보자구욧!</p>
      </div>
      <div className={classNames('p-6')}>
        <p className="font-medium text-xl">이렇게 인증 해주세요</p>
        <p>1. Solved.Ac에서 문제를 풀고 제출해주세요</p>
        <p>2. 어플에서 제출이 적용되었는지 확인해주세요</p>
        <p>3. 끝</p>
      </div>
      <div className={classNames('p-6')}>
        <p className="font-medium text-xl">챌린지 진행시 꼭 알아주세요!</p>
        <p>1. 00시 00분 ~ 23시 59분 사이에 인증하셔야 합니다.</p>
        <p>2. 어플에서 제출이 적용되었는지 확인해주세요</p>
      </div>
      <div className={classNames('p-6')}>
        <p className="font-medium text-xl">주의사항</p>
        <p>챌린지 시작 후 취소 불가</p>
        <p>
          챌린지는 여러 회원님들이 함께 하는 대회인만큼, 챌린지 시작후에는
          환불이 불가능합니다.
        </p>
      </div>
      <div
        className={classNames(
          style.footer,
          `text-center sticky w-full bottom-0 m-0 flex`,
        )}
      >
        <div className="text-left">
          <p>03.20(월) ~ 04.02(일)</p>
          <p>매일 2주 동안</p>
        </div>
        <div className="w-1/2">
          <Button label="참여하기" />
        </div>
      </div>
    </div>
  );
};

export default challengeintro;
