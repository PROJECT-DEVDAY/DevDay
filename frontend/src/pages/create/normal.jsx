import React, { useState, useContext, useRef } from 'react';
import { BiPlus, BiMinus } from 'react-icons/bi';
import { useSelector } from 'react-redux';

import classNames from 'classnames';
import Image from 'next/image';
import Swal from 'sweetalert2';

import style from './normal.module.scss';
import { httpForm } from '../api/http';

import { BtnFooter } from '@/components/BtnFooter';
import Container from '@/components/Container';
import { ContentInput } from '@/components/ContentInput';
import { InputLabel } from '@/components/InputLabel';
import { InputText } from '@/components/InputText';
import { ReturnArrow } from '@/components/ReturnArrow';
import { CHALLENGES_URL } from '@/constants';

const normal = props => {
  const [member, setMember] = useState(1);

  const user = useSelector(state => state.user);

  const [challenge, setChallenge] = useState({
    category: 'FREE',
    title: '',
    hostId: user.userInfo.userId,
    entryFee: 1000,
    introduce: '',
    startDate: '',
    endDate: '',
    backGroudFile: '',
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

  const [imgFile, setImgeFile] = useState(require('../../image/add-image.png'));

  const [isSelect, setIsSelect] = useState(false);

  const onClickImageInput = event => {
    event.preventDefault();
    challengeImgInput.current.click();
  };

  const onChangeImage = e => {
    const reader = new FileReader();

    reader.onload = ({ target }) => {
      challengeImgInput.current.src = target.result;
      setIsSelect(true);
      setImgeFile(target.result);
    };

    if (!challengeImgInput.current.files[0]) {
      Swal.fire({
        position: 'center',
        icon: 'warning',
        title: '사진을 선택해주세요',
        showConfirmButton: false,
        timer: 1000,
      });
      return;
    }
    reader.readAsDataURL(challengeImgInput.current.files[0]);
  };

  const correctImgInputRef = useRef(null);

  const [correctImg, setCorrectImg] = useState(
    require('../../image/correct.jpg'),
  );

  const [correctImgSelect, setCorrectImgSelect] = useState(false);

  const onClickCorrectImage = event => {
    event.preventDefault();
    correctImgInputRef.current.click();
  };

  const onChangeCorrectImg = e => {
    const reader = new FileReader();

    reader.onload = ({ target }) => {
      correctImgInputRef.current.src = target.result;
      setCorrectImgSelect(true);
      setCorrectImg(target.result);
    };

    if (!correctImgInputRef.current.files[0]) {
      Swal.fire({
        position: 'center',
        icon: 'warning',
        title: '사진을 선택해주세요',
        showConfirmButton: false,
        timer: 1000,
      });
      return;
    }
    reader.readAsDataURL(correctImgInputRef.current.files[0]);
  };

  const wrongImgInput = useRef(null);

  const [wrongImg, setWrongImg] = useState(require('../../image/wrong.jpg'));

  const [wrongImgSelect, setWrongImgSelect] = useState(false);

  const onClickWrongImg = event => {
    event.preventDefault();
    wrongImgInput.current.click();
  };

  const onChangeWrongImg = e => {
    const reader = new FileReader();

    reader.onload = ({ target }) => {
      wrongImgInput.current.src = target.result;
      setWrongImgSelect(true);
      setWrongImg(target.result);
    };

    if (!wrongImgInput.current.files[0]) {
      Swal.fire({
        position: 'center',
        icon: 'warning',
        title: '사진을 선택해주세요',
        showConfirmButton: false,
        timer: 1000,
      });
      return;
    }
    reader.readAsDataURL(wrongImgInput.current.files[0]);
  };

  const onClickCreateChallenge = () => {
    if (!(isSelect && correctImgSelect && wrongImgSelect)) {
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
    data.append('backGroundFile', challengeImgInput.current.files[0] || null);

    data.append('certSuccessFile', correctImgInputRef.current.files[0] || null);
    data.append('certFailFile', wrongImgInput.current.files[0] || null);
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
          .then(async () => {
            await Swal.fire({
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
            placeholder="예) 매일 헬스하기 3주 도전"
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
              onClick={onClickImageInput}
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
          <InputLabel content="챌린지 소개" asterisk />
          <ContentInput
            placeholder="예) 3대 500 가능할때까지 무한 반복한다. 인증은 헬스장 거울앞에서 한다."
            maxLength="30"
            name="introduce"
            onChange={handleChange}
          />
        </div>

        <div className="flex mt-6">
          <div className="w-1/2 mr-2">
            <InputLabel content="인증 예시" />
          </div>
        </div>
        <div className="flex ">
          <div className="w-1/2 mr-2">
            <InputLabel content="올바른 인증" smallTextSize asterisk />
            <div className="w-full h-40 relative ">
              <Image
                src={correctImg}
                alt="correctImg"
                onClick={onClickCorrectImage}
                fill
              />
            </div>
            <input
              style={{ display: 'none' }}
              ref={correctImgInputRef}
              type="file"
              className={style.ImgInput}
              id="logoImg"
              accept="image/*"
              name="file"
              onChange={onChangeCorrectImg}
            />
          </div>
          <div className="w-1/2 ml-2">
            <InputLabel content="올바르지 않은 인증" smallTextSize asterisk />
            <div className="w-full h-40 relative">
              <Image
                src={wrongImg}
                alt="wrongImg"
                onClick={onClickWrongImg}
                fill
              />
            </div>
            <input
              style={{ display: 'none' }}
              ref={wrongImgInput}
              type="file"
              className={style.ImgInput}
              id="logoImg"
              accept="image/*"
              name="file"
              onChange={onChangeWrongImg}
            />
          </div>
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
          warningMessage="Commit 챌린지는 GitHub ID가 필요해요."
        />
      </div>
    </div>
  );
};
export default normal;
