import React, { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
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
import Link from 'next/link';
import { useRouter } from 'next/router';

import style from './challenge.module.scss';

import Container from '@/components/Container';
import { ChallengeList } from '@/components/ChallengeList';
import Footer from '@/components/Footer';
import { SelectArrow } from '@/components/SelectArrow';
import http from '@/pages/api/http';
import { CHALLENGE_DETAIL_URL, CHALLENGE_AUTH_ALGO_URL } from '@/constants';
import { Button } from '@/components/Button';

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
  const router = useRouter();
  const user = useSelector(state => state.user);
  const { challengeId } = router.query;
  const [challenge, setChallenge] = useState('');
  const [myAlgoKey, setMyAlgoKey] = useState('');
  const [myAlgo, setMyAlgo] = useState('');
  useEffect(() => {
    if (challengeId) {
      http.get(`${CHALLENGE_DETAIL_URL}/${challengeId}`).then(res => {
        setChallenge(res.data);
        http
          .get(`${CHALLENGE_AUTH_ALGO_URL}/recent`, {
            headers: { Authorization: user.accessToken },
          })
          .then(response => {
            if (response) {
              const key = Object.keys(response.data.data.solvedMap);
              const value = Object.values(response.data.data.solvedMap);
              console.log(key);
              console.log(value);
              setMyAlgoKey(key);
              setMyAlgo(response.data.data.solvedMap);
            }
          });
      });
    }
  }, [challengeId]);
  const week = Math.abs(
    (Date.parse(challenge.endDate) - Date.parse(challenge.startDate)) /
      (1000 * 60 * 60 * 24 * 7),
  ).toFixed(1);
  const title = [
    `매일 ${week}주동안`,
    <br />,
    `${challenge.startDate} ~ ${challenge.endDate}`,
  ];

  const HEADER_ITEMS = ['나의 인증 현황', '참가자 인증 현황'];
  const [selectedItem, setSelectedItem] = useState('참가자 인증 현황');

  const handleItemChange = event => {
    const targetItem = event.target.value;
    setSelectedItem(targetItem);
  };

  return (
    <Container>
      <Container.SubPageHeader />
      <div className="relative">
        <Image
          src={challenge.backGroundUrl}
          alt="logo"
          width={100}
          height={100}
          loader={({ src }) => `${src}`}
          className={classNames('w-full', style.boxStyle)}
        />
        <p className="absolute bottom-10 w-4/5 left-10 text-4xl font-bold text-white">
          {challenge.title}
        </p>
      </div>
      <div className={classNames('p-6', style.boxStyle)}>
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
      <div className=" mb-20">
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
              {myAlgoKey &&
                myAlgoKey.map(
                  item =>
                    Date.parse(item) >= Date.parse(challenge.startDate) && (
                      <ChallengeList date={item} array={myAlgo[item]} />
                    ),
                )}
            </div>
            <div className="p-4">
              <Link href={`/challenge/${challengeId}/my-submit-list`}>
                <SelectArrow title="나의 전체 인증 현황" />
              </Link>
            </div>
          </div>
        )}
        {selectedItem === '참가자 인증 현황' && (
          <div className={classNames(style.peopleBox, 'p-4')}>
            <Bar options={options} data={data} />
            <Link
              href={`/challenge/${challengeId}/submit-list`}
              className="p-4"
            >
              <SelectArrow title="참여자 인증 전체보기" />
            </Link>
          </div>
        )}
      </div>
      <Container.MainFooter className="p-4">
        <Link href={`/challenge/${challengeId}/submit-picture`}>
          <Button label="인증하기" />
        </Link>
      </Container.MainFooter>
    </Container>
  );
};

export default challenge;
