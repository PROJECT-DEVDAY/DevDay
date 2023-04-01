import React, { useState, useContext, useRef } from 'react';
import { BiPlus, BiMinus } from 'react-icons/bi';

import classNames from 'classnames';
import Image from 'next/image';

import style from './algo.module.scss';

import { BtnFooter } from '@/components/BtnFooter';
import Container from '@/components/Container';
import { ContentInput } from '@/components/ContentInput';
import { InputLabel } from '@/components/InputLabel';
import { InputText } from '@/components/InputText';
import { ReturnArrow } from '@/components/ReturnArrow';

const normal = props => {
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
  const [checking, setChecking] = useState([true, false]);
  const changeCheck = index => {
    if (index === 0) {
      setChecking([true, false]);
    } else {
      setChecking([false, true]);
    }
  };
  const [member, setMember] = useState(1);

  const [room, setRoom] = useState({
    category: 'FREE',
    title: '',
    hostId: '',
    entryFee: '',
    introduce: '',
    startDate: '',
    endDate: '',
  });
  const handleChange = e => {
    setRoom({
      ...room,
      [e.target.name]: e.target.value,
    });
  };
  const [imgFile, setImgeFile] = useState(
    require('../../image/backgroundImage.jpg'),
  );

  const challengeImageInput = useRef(null);

  const onClickImageInput = event => {
    event.preventDefault();
    challengeImageInput.current.click();
  };

  const onChangeImage = e => {
    if (e.target.files[0]) {
      setImgeFile(e.target.files[0]);
    } else {
      setImgeFile(require('../../image/default-user.png'));
      return;
    }

    const reader = new FileReader();

    reader.onload = () => {
      if (reader.readyState === 2) {
        setImgeFile(reader.result);
      }
    };
    reader.readAsDataURL(e.target.files[0]);
  };
  return (
    <div>
      <div className={classNames(`style.div-header`, `sticky top-0`)}>
        <ReturnArrow title="챌린지 만들기" />
      </div>
      <div className="div-body p-6 mt-4">
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
        <div>
          <InputLabel content="챌린지 이미지" />
          <div className="w-full h-40 relative">
            <Image
              src={imgFile}
              alt="프로필 이미지"
              onClick={onClickImageInput}
              fill
            />
          </div>
          <input
            style={{ display: 'none' }}
            ref={challengeImageInput}
            type="file"
            className={style.ImgInput}
            id="logoImg"
            accept="image/*"
            name="file"
            onChange={onChangeImage}
          />
        </div>
        <div className="mt-6">
          <InputLabel content="참가 비용" asterisk />
          <InputText
            inputType="text"
            name="entryFee"
            onChange={handleChange}
            type="number"
            content="최소 금액 1000원"
          />
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
                {member > 1 ? (
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
        <div className={style.datePick}>
          <label htmlFor="inputStartDate">
            <InputLabel content="챌린지 시작일" asterisk />
            <input
              id="inputStartDate"
              type="date"
              name="startDate"
              onChange={handleChange}
            />
          </label>
          {room.startDate && (
            <div>
              <InputLabel content="챌린지 종료일" asterisk />
              <input
                type="date"
                name="endDate"
                onChange={handleChange}
                min={room.startDate}
              />
            </div>
          )}
        </div>
        <div className="mt-8">
          <InputLabel content="인증 방법" asterisk={false} />
          <ContentInput
            placeholder="예) 헬스장 거울앞에서 사진찍기"
            name="introduce"
            onChange={handleChange}
          />
        </div>
        <div className="mt-8">
          <InputLabel content="챌린지 소개" asterisk={false} />
          <ContentInput
            placeholder="예) 3대 500 가능할때 까지 무한 반복한다"
            maxLength="30"
            name="introduce"
            onChange={handleChange}
          />
        </div>
      </div>
      <div
        className={classNames(`text-center sticky w-full bottom-0 pb-4 m-0`)}
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

export default normal;
