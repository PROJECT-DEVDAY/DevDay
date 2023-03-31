import React, { useState, useContext } from 'react';
import { BiPlus, BiMinus } from 'react-icons/bi';
import { useSelector } from 'react-redux';

import classNames from 'classnames';

import style from './algo.module.scss';

import { BtnFooter } from '@/components/BtnFooter';
import { ContentInput } from '@/components/ContentInput';
import { DayPicker } from '@/components/DayPicker';
import { InputLabel } from '@/components/InputLabel';
import { ReturnArrow } from '@/components/ReturnArrow';

const algo = props => {
  const { startDate, endDate } = useSelector(state => state.challengeCreate);
  const start = new Date(startDate).toLocaleDateString();
  const end = new Date(endDate).toLocaleDateString();

  const memberCheckButton = [
    {
      id: 0,
      title: '제한 없음',
      content: '인원 제한이 없는 챌린지',
    },
    {
      id: 1,
      title: '인원 제한',
      content: '인원을 제한하는 챌린지',
    },
  ];
  const [checking, setChecking] = useState([false, false]);
  const changeCheck = index => {
    if (index === 0) {
      setChecking([true, false]);
    } else {
      setChecking([false, true]);
    }
  };
  const [member, setMember] = useState(0);

  const [room, setRoom] = useState({
    category: 'ALGO',
    title: '',
    hostId: '',
    entryFee: '',
    introduce: '',
    algorithmCount: '',
  });
  const handleChange = e => {
    setRoom({
      ...room,
      [e.target.name]: e.target.value,
    });
  };
  const [openDatePicker, setOpenDatePicker] = useState(false);
  const showDatePicker = () => {
    setOpenDatePicker(!openDatePicker);
  };
  return (
    <div>
      <div className={classNames(`style.div-header`, `sticky top-0`)}>
        <ReturnArrow title="챌린지 만들기" />
      </div>
      <div className="div-body p-6 pb-32 mt-4">
        <div>
          <InputLabel content="챌린지 제목" asterisk />
          <ContentInput
            placeholder="예) 1일 1알고리즘 3주 도전"
            maxLength="30"
            name="title"
            onChange={handleChange}
          />
          <p className="text-right">{room.title.length}/30</p>
        </div>
        <div className="mt-6">
          <InputLabel content="참여 인원" asterisk />
          <div className="flex">
            {memberCheckButton.map(item => {
              const { id, title, content } = item;
              return (
                <button
                  type="button"
                  onClick={() => changeCheck(id)}
                  key={id}
                  className={classNames(
                    checking[id] === false ? style.membercheck : style.selected,
                  )}
                >
                  <div className="font-medium text-xl">{title}</div>
                  <div className="text-sm">{content}</div>
                </button>
              );
            })}
          </div>
          {checking[1] && (
            <div className="mt-6 flex">
              <InputLabel content="참여 인원 수" asterisk />
              <div className={classNames('flex', style.changeMember)}>
                {member > 0 ? (
                  <button
                    type="button"
                    className={classNames(style.plusMinus, 'rounded-l-lg')}
                    onClick={() => setMember(member - 1)}
                  >
                    <BiMinus className="m-auto" />
                  </button>
                ) : (
                  <button
                    type="button"
                    className={classNames(style.plusMinus, 'rounded-l-lg')}
                    onClick={() => setMember(member - 1)}
                    disabled
                  >
                    <BiMinus className="m-auto" />
                  </button>
                )}
                <div
                  className={classNames(
                    'font-medium whitespace-nowrap text-center',
                    style.plusMinus,
                  )}
                >
                  {member}명
                </div>
                <button
                  type="button"
                  className={classNames(style.plusMinus, 'rounded-r-lg')}
                  onClick={() => setMember(member + 1)}
                >
                  <BiPlus className="m-auto" />
                </button>
              </div>
            </div>
          )}
        </div>
        <div className="mt-8">
          <div className={style.bws}>
            <InputLabel content="챌린지 기간" asterisk />
            <button type="button" onClick={showDatePicker} className="w-1/2">
              입력
            </button>
          </div>
          <p>
            {start} ~ {end}
          </p>
          <div>
            <div>
              {openDatePicker && <DayPicker showDatePicker={showDatePicker} />}
            </div>
          </div>
        </div>
        <div className="mt-8">
          <InputLabel content="챌린지 소개" asterisk={false} />
          <ContentInput
            placeholder="예) 1일 1알고리즘 실천해서 코테 뿌셔봅시다"
            maxLength="30"
            name="introduce"
            onChange={handleChange}
          />
        </div>
      </div>
      <div
        className={classNames(`text-center absolute w-full bottom-0 pb-4 m-0`)}
      >
        <BtnFooter
          content=""
          label="다음"
          disable
          goToUrl="/create/algo"
          warningMessage="알고리즘 챌린지는 solved.AC ID가 필요해요."
        />
      </div>
    </div>
  );
};

export default algo;
