import React, { useState, useContext, useRef } from 'react';
import { BiPlus, BiMinus } from 'react-icons/bi';
import { useSelector } from 'react-redux';

import classNames from 'classnames';
import Image from 'next/image';
import Swal from 'sweetalert2';

import style from './algo.module.scss';
import { httpForm } from '../api/http';

import { BtnFooter } from '@/components/BtnFooter';
import Container from '@/components/Container';
import { ContentInput } from '@/components/ContentInput';
import { InputLabel } from '@/components/InputLabel';
import { InputText } from '@/components/InputText';
import { ReturnArrow } from '@/components/ReturnArrow';
import { CHALLENGES_URL } from '@/constants';

const algo = props => {
  const [member, setMember] = useState(1);
  const [algoithmCount, setAlgoithmCount] = useState(1);

  const user = useSelector(state => state.user);

  const [challenge, setChallenge] = useState({
    category: 'ALGO',
    title: '',
    hostId: user.userInfo.userId,
    entryFee: 1000,
    introduce: '',
    startDate: '',
    endDate: '',
    backGroundFile: '',
  });

  const handleChange = e => {
    if (e.target.name === 'entryFee') {
      const entry = parseInt(e.target.value);
      setChallenge({
        ...challenge,
        [e.target.name]: entry,
      });
    } else {
      setChallenge({
        ...challenge,
        [e.target.name]: e.target.value,
      });
    }
  };

  const challengeImgInput = useRef(null);

  const [imgFile, setImgFile] = useState(require('../../image/add-image.png'));
  const [isSelect, setIsSelect] = useState(false);

  const onClickImgInput = event => {
    event.preventDefault();
    challengeImgInput.current.click();
  };

  const onChangeImg = e => {
    const reader = new FileReader();

    reader.onload = ({ target }) => {
      challengeImgInput.current.src = target.result;
      setIsSelect(true);
      setImgFile(target.result);
    };

    if (!challengeImgInput.current.files[0]) {
      return;
    }
    reader.readAsDataURL(challengeImgInput.current.files[0]);
  };

  const onClickCreateChallenge = () => {
    if (!isSelect) {
      Swal.fire({
        position: 'center',
        icon: 'warning',
        title: '사진을 선택해주세요',
        showConfirmButton: false,
        timer: 1000,
      });
      return;
    }
    const data = new FormData();
    data.append('title', challenge.title);
    data.append('hostId', user.userInfo.userId);
    data.append('category', challenge.category);
    data.append('entryFee', challenge.entryFee);
    data.append('introduce', challenge.introduce);
    data.append('startDate', challenge.startDate);
    data.append('endDate', challenge.endDate);
    data.append('maxParticipantsSize', member);
    data.append('algorithmCount', algoithmCount);
    data.append('backGroundFile', challengeImgInput.current.files[0] || null);
    data.append('nickname', user.userInfo.nickname);

    Swal.fire({
      title: '챌린지를 \n 생성하시겠습니까?',
      text: '챌린지는 수정할 수 없어요!',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: '네',
      cancelButtonText: '아니요',
    }).then(result => {
      if (result.isConfirmed) {
        httpForm
          .post(CHALLENGES_URL, data, {
            headers: { Authorization: user.accessToken },
          })
          .then(() => {
            Swal.fire({
              position: 'center',
              icon: 'success',
              title: '성공!',
              showConfirmButton: false,
              timer: 1500,
            });
          })
          .catch(error => {
            Swal.fire({
              icon: 'error',
              title: '다시 한번 확인해주세요!',
              text: error.message,
            });
          });
      }
    });
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
          <p className="text-right">{challenge.title.length}/30</p>
        </div>
        <div>
          <InputLabel content="챌린지 이미지" asterisk />
          <div className="w-full h-40 relative">
            <Image
              src={imgFile}
              alt="프로필 이미지"
              onClick={onClickImgInput}
              fill
            />
          </div>
          <input
            style={{ display: 'none' }}
            ref={challengeImgInput}
            type="file"
            className={style.ImgInput}
            id="logoImg"
            accept="image/*"
            name="file"
            onChange={onChangeImg}
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
        </div>
        <div className="mt-6 flex">
          <InputLabel content="최소 알고리즘 커밋 수" asterisk />
          <div className={classNames('flex', style.changeMember)}>
            {algoithmCount > 1 ? (
              <button
                type="button"
                className={classNames(style.plusMinus, 'rounded-l-lg')}
                onClick={() => {
                  setAlgoithmCount(algoithmCount - 1);
                }}
              >
                <BiMinus className="m-auto" />
              </button>
            ) : (
              <button
                type="button"
                className={classNames(style.plusMinus, 'rounded-l-lg')}
                onClick={() => setAlgoithmCount(algoithmCount - 1)}
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
              {algoithmCount}개
            </div>
            <button
              type="button"
              className={classNames(style.plusMinus, 'rounded-r-lg')}
              onClick={() => setAlgoithmCount(algoithmCount + 1)}
            >
              <BiPlus className="m-auto" />
            </button>
          </div>
        </div>
        <div className={style.datePick}>
          <label htmlFor="inputStartDate">
            <InputLabel content="챌린지 시작일" asterisk />
            <input
              id="inputStartDate"
              type="date"
              name="startDate"
              onChange={handleChange}
              max={challenge.endDate}
            />
          </label>
          {challenge.startDate && (
            <div>
              <InputLabel content="챌린지 종료일" asterisk />
              <input
                type="date"
                name="endDate"
                onChange={handleChange}
                min={challenge.startDate}
              />
            </div>
          )}
        </div>
        <div className="mt-8">
          <InputLabel content="챌린지 소개" />
          <ContentInput
            placeholder="예) 1일 1알고리즘 실천해서 코테 뿌셔봅시다"
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
          onClick={onClickCreateChallenge}
          warningMessage="알고리즘 챌린지는 solved.AC ID가 필요해요."
        />
      </div>
    </div>
  );
};

export default algo;
