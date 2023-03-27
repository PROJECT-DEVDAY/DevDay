import React, { useState } from 'react';
import { BiPlus, BiMinus } from 'react-icons/bi';
import { TbTilde } from 'react-icons/tb';

import classNames from 'classnames';

import style from './commit.module.scss';

import { BtnFooter } from '@/components/BtnFooter';
import { ContentInput } from '@/components/ContentInput';
import { DayPicker } from '@/components/DayPicker';
import { InputLabel } from '@/components/InputLabel';
import { ReturnArrow } from '@/components/ReturnArrow';

const commit = props => {
  const [text, setText] = useState('');
  const setTextValue = e => {
    setText(e.target.value);
  };
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
  return (
    <div>
      <div className={classNames(`style.div-header`, `sticky top-0`)}>
        <ReturnArrow title="챌린지 만들기" />
      </div>
      <div className="div-body p-6 pb-32 mt-4">
        <div>
          <InputLabel content="챌린지 제목" asterisk />
          <ContentInput
            placeholder="예) 매일 매일 커밋하기 챌린지"
            onChange={setTextValue}
            value={text}
            maxLength="30"
          />
          <p className="text-right">{text.length}/30</p>
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
                    className={classNames(style.plusMinus, 'rounded-l-lg')}
                    onClick={() => setMember(member - 1)}
                  >
                    <BiMinus className="m-auto" />
                  </button>
                ) : (
                  <button
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
          <InputLabel content="챌린지 기간" asterisk />
          <div className="flex">
            <div className="w-2/5">
              <DayPicker />
            </div>
            <div className="mx-2">
              <TbTilde size={18} className="h-full align-middle" />
            </div>
            <div className="w-2/5">
              <DayPicker />
            </div>
          </div>
        </div>
        <div className="mt-8">
          <InputLabel content="챌린지 소개" asterisk={false} />
          <ContentInput
            placeholder="예) 매일 매일 커밋해서 잔디밭 꽉꽉 채웁시다!"
            onChange={setTextValue}
            value={text}
            maxLength="30"
          />
        </div>
      </div>
      <div
        className={classNames(`text-center absolute w-full bottom-0 pb-4 m-0`)}
      >
        <BtnFooter
          label="다음"
          disable
          goToUrl="/create/algo"
          warningMessage="알고리즘 챌린지는 solved.AC ID가 필요해요."
        />
      </div>
    </div>
  );
};

export default commit;
