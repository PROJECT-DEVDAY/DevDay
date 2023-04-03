import React, { useEffect, useState } from 'react';
import { Doughnut } from 'react-chartjs-2';

import Chart from 'chart.js/auto';
import classNames from 'classnames';
import Image from 'next/image';
import Link from 'next/link';
import { useRouter } from 'next/router';
import Swal from 'sweetalert2';

import style from './index.module.scss';
import http from '../../api/http';

import { Button } from '@/components/Button';
import { CHALLENGE_DETAIL_URL } from '@/constants';

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
  console.log(data);
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
        src={data.backGroundUrl}
        alt="logo"
        width={100}
        height={100}
        loader={({ src }) => `${src}`}
        className={classNames('w-full')}
      />
      <div className={classNames('p-6', style.boxStyle)}>
        <p className="font-medium text-l">{data.hostNickname}</p>
        <p className="font-medium text-4xl">{data.title}</p>
        <div className={classNames(style.fee, 'font-medium text-l')}>
          <p className="font-medium text-l">현재 인원 수</p>
          <p>{data.userCount}명</p>
        </div>
        <div className={classNames(style.fee, 'font-medium text-l')}>
          <p>참가비</p>
          <p>{data.entryFee}원</p>
        </div>
      </div>
      <div className={classNames('p-6', style.boxStyle)}>
        <p className="font-medium text-xl">챌린지 리더</p>
        <div className={classNames('flex mt-4')}>
          {!data.hostProfileImage && (
            <Image
              src={require('@/image/default-user.png')}
              alt="user"
              className="w-16 h-16 mr-4"
            />
          )}
          {data.hostProfileImage && (
            <Image
              src={data.hostProfileImage}
              loader={({ src }) => `${src}`}
              alt="user"
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
          <p>1. Solved.Ac에서 문제를 풀고 제출해주세요</p>
          <p>2. 사이트에서 제출이 적용되었는지 확인해주세요</p>
          <p>3. 끝</p>
        </div>
      )}
      {data.category === 'FREE' && (
        <div className={classNames('p-6')}>
          <p className="font-medium text-xl">이렇게 인증 해주세요</p>
          <div className="grid-cols-2 grid gap-2">
            <div className="text-center">
              <Image
                src={data.successUrl}
                loader={({ src }) => `${src}`}
                width={100}
                height={200}
                alt="user"
                className="w-full"
              />
              <p className="font-medium text-xl">좋은 예시</p>
            </div>
            <div className="text-center">
              <Image
                src={data.failUrl}
                loader={({ src }) => `${src}`}
                width={100}
                height={200}
                alt="user"
                className="w-full"
              />
              <p className="font-medium text-xl ">나쁜 예시</p>
            </div>
          </div>
        </div>
      )}
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
          <Button
            label="참여하기"
            onClick={() => {
              Swal.fire({
                position: 'center',
                icon: 'warning',
                title: '구현하기!',
                showConfirmButton: false,
                timer: 1000,
              });
            }}
          />
        </div>
      </div>
    </div>
  );
};

export default challengeintro;
