import React, { useState } from 'react';
import { Bar } from 'react-chartjs-2';
import { FiCamera } from 'react-icons/fi';
import { MdDateRange } from 'react-icons/md';

import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from 'chart.js/auto';
import classNames from 'classnames';
import Image from 'next/image';

import style from './challenge.module.scss';

import { ChallengeList } from '@/components/ChallengeList';
import Footer from '@/components/Footer';
import { CheckBoxBtn } from '@/components/CheckBoxBtn';
import { SelectArrow } from '@/components/SelectArrow';
import Link from 'next/link';
import { useRouter } from 'next/router';

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
);

const labels = ['신대득', '박태환', '이동준', '홍금비', '김기윤', '최형운'];

const options = {
  responsive: true,
  scales: {
    x: {
      grid: {
        display: false,
      },
    },
    y: {
      grid: {
        display: false,
      },
    },
  },
};

const data = {
  labels,
  datasets: [
    {
      label: '성공 횟수',
      data: [1, 1, 2, 2, 3, 3],
      backgroundColor: 'rgba(255, 99, 132, 0.5)',
      borderWidth: 0,
    },
  ],
};

const challenge = props => {
  const title = ['매일 1주동안', <br />, '2023.3.6(월) ~ 2023.3.12(일)'];
  const HEADER_ITEMS = ['나의 인증 현황', '참가자 인증 현황'];
  const router = useRouter();
  const [selectedItem, setSelectedItem] = useState('나의 인증 현황');
  const { challengeId } = router.query;
  const handleItemChange = event => {
    setSelectedItem(event.target.value);
  };
  const array = [
    ['3월 28일', ['골드 2 127번', '골드 2 140번']],
    ['3월 28일', ['골드 2 127번', '골드 2 140번']],
    ['3월 28일', []],
    ['3월 28일', []],
    ['3월 28일', ['골드 2 127번', '골드 2 140번']],
  ];
  return (
    <>
      <div className="relative">
        <div>
          <Image
            src={require('@/image/backgroundImage.jpg')}
            alt="logo"
            className={classNames('w-full', style.boxStyle)}
          />
        </div>
        <p className="absolute bottom-20 left-10 text-4xl font-bold text-white">
          다이어트 챌린지
        </p>
      </div>
      <div className="bg-[url(" />
      <div className={classNames('div-body p-6', style.boxStyle)}>
        <div className="flex">
          <MdDateRange className="m-auto" size="30" />
          <SelectArrow
            title={title}
            content="인증가능 : 월,화,수,목,금,토,일(00:00~23:59)"
          />
        </div>
        <div className="flex mt-4">
          <FiCamera className="m-auto" size="30" />
          <SelectArrow title="인증 방법 및 인증샷 예시" />
        </div>
      </div>
      <div
        className={classNames(
          style.RadioGroup,
          `flex justify-between items-center`,
        )}
      >
        {HEADER_ITEMS.map(item => (
          <label
            className={classNames(
              selectedItem === item ? style.selected : '',
              `inline-block w-1/2 text-center p-4 font-medium`,
            )}
          >
            <input
              type="radio"
              name="items"
              value={item}
              checked={selectedItem === item}
              onChange={handleItemChange}
            />
            {item}
          </label>
        ))}
      </div>
      {selectedItem === '나의 인증 현황' && (
        <div>
          <div className={classNames('p-4 m-4 font-medium', style.greybox)}>
            <div className="w-1/3 border-solid border-border border-r-2 text-center">
              <p>진행률</p>
              <p className="font-bold">85.7%</p>
            </div>
            <div className="w-1/3 border-solid border-border border-r-2 text-center">
              <p>예치금+상금</p>
              <p className="font-bold">8000원</p>
            </div>
            <div className="w-1/3 flex m-auto">
              <div className="w-1/2 text-center ml-4">
                <p className="text-green-500">성공</p>
                <p className="font-bold">4회</p>
              </div>
              <div className="w-1/2 text-center">
                <p className="text-red-500">실패</p>
                <p className="font-bold">2회</p>
              </div>
            </div>
          </div>
          <div className="p-6 grid gap-4 grid-cols-3">
            {array.map(item => (
              <ChallengeList date={item[0]} array={item[1]} />
            ))}
          </div>
        </div>
      )}
      {selectedItem === '참가자 인증 현황' && (
        <div className={classNames(style.peopleBox)}>
          <Bar options={options} data={data} />
        </div>
      )}
      <div className={classNames('sticky w-full bottom-0 m-0')}>
        <Link href={`/challenge/${challengeId}/submit-picture`}>
          <CheckBoxBtn check={false} label="인증하기" />
        </Link>
      </div>
    </>
  );
};

export default challenge;
