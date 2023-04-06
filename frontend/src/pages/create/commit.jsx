import React, { useState, useContext, useRef } from 'react';
import { BiPlus, BiMinus } from 'react-icons/bi';
import { useSelector } from 'react-redux';

import classNames from 'classnames';
import Image from 'next/image';
import { useRouter } from 'next/router';
import Swal from 'sweetalert2';

import style from './algo.module.scss';

import { httpForm } from '@/api/http';
import { BtnFooter } from '@/components/BtnFooter';
import Container from '@/components/Container';
import { ContentInput } from '@/components/ContentInput';
import { InputLabel } from '@/components/InputLabel';
import { InputText } from '@/components/InputText';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import { ReturnArrow } from '@/components/ReturnArrow';
import { CHALLENGES_URL } from '@/constants';

const commit = props => {
  const router = useRouter();
  const [member, setMember] = useState(1);
  const [commitCount, setCommitCount] = useState(1);

  const user = useSelector(state => state.user);

  const today = new Date();

  const year = today.getFullYear();
  const month = `0${today.getMonth() + 1}`.slice(-2);
  const day = `0${today.getDate()}`.slice(-2);

  const dateString = `${year}-${month}-${day}`;

  const [challenge, setChallenge] = useState({
    category: 'COMMIT',
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
    data.append('commitCount', commitCount);
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
          .then(res => {
            Swal.fire({
              position: 'center',
              icon: 'success',
              title: '성공!',
              showConfirmButton: false,
              timer: 1500,
            });
            router.push(`/participation/${res.data.id}`);
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
    <Container>
      <Container.SubPageHeader title="챌린지 만들기" />
      <Container.MainBody className="mt-4">
        <div>
          <InputLabel content="챌린지 제목" asterisk />
          <ContentInput
            placeholder="예) 매일 매일 커밋하기 챌린지"
            maxLength="30"
            name="title"
            minlength="10"
            onChange={handleChange}
          />
          {challenge.title.length < 10 && (
            <div className="flex justify-between">
              <p className="font-medium text-red-500">10자 이상 입력하세요</p>
              <p className="text-right">{challenge.title.length}/30</p>
            </div>
          )}
          {challenge.title.length >= 10 && (
            <p className="text-right">{challenge.title.length}/30</p>
          )}
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
          <InputLabel content="최소 커밋 수" asterisk />
          <div className={classNames('flex', style.changeMember)}>
            {commitCount > 1 ? (
              <button
                type="button"
                className={classNames(style.plusMinus, 'rounded-l-lg')}
                onClick={() => {
                  setCommitCount(commitCount - 1);
                }}
              >
                <BiMinus className="m-auto" />
              </button>
            ) : (
              <button
                type="button"
                className={classNames(style.plusMinus, 'rounded-l-lg')}
                onClick={() => setCommitCount(commitCount - 1)}
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
              {commitCount}개
            </div>
            <button
              type="button"
              className={classNames(style.plusMinus, 'rounded-r-lg')}
              onClick={() => setCommitCount(commitCount + 1)}
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
              // min={dateString}
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
            placeholder="예) 매일 매일 커밋해서 잔디밭 꽉꽉 채웁시다!!"
            name="introduce"
            onChange={handleChange}
            minlength="10"
          />
          {challenge.introduce.length < 10 && (
            <div className="flex justify-between">
              <p className="font-medium text-red-500">10자 이상 입력하세요</p>
            </div>
          )}
        </div>
      </Container.MainBody>
      <Container.MainFooter className="text-center">
        <BtnFooter
          content=""
          label="다음"
          disable
          onClick={onClickCreateChallenge}
          warningMessage="Commit 챌린지는 GitHub ID가 필요해요."
        />
      </Container.MainFooter>
    </Container>
  );
};

export default PrivateRouter(commit);
