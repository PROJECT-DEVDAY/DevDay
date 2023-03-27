import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { useDispatch } from 'react-redux';

import classNames from 'classnames';
import { useRouter } from 'next/router';

import style from './signup.module.scss';
import { Button } from '../../components/Button';
import { InputText } from '../../components/InputText';
import { ReturnArrow } from '../../components/ReturnArrow';

import { save } from '@/store/signup/signupSlice';

const signup = props => {
  const [signUpInfos, setSignUpInfos] = useState({
    id: '',
    password: '',
    passwordCheck: '',
    name: '',
    nickName: '',
    solved_ac: '',
    gitHub: '',
  });

  const [emailValidCheck, setEmailValidCheck] = useState(false);

  //
  const [nickNameDuplicateChk, setnickNameDuplicateChk] = useState(false);

  const router = useRouter();
  const dispatch = useDispatch();

  const onClickEmailValidation = () => {
    setEmailValidCheck(true);
    // TODO: 이메일 전송 API 구현
  };
  const onClickDuplicateCheck = () => {
    setnickNameDuplicateChk(true);
    // TODO: 이메일 전송 API 구현
  };
  const Timer = () => {
    const [minutes, setMinutes] = useState(parseInt(0, 10));
    const [seconds, setSeconds] = useState(parseInt(5, 10));
    useEffect(() => {
      const countdown = setInterval(() => {
        if (parseInt(seconds, 10) > parseInt(0, 10)) {
          setSeconds(parseInt(seconds, 10) - parseInt(1, 10));
        }
        if (parseInt(seconds, 10) === parseInt(1, 10)) {
          if (parseInt(minutes, 10) === parseInt(0, 10)) {
            clearInterval(countdown);
            setEmailValidCheck(false);
          } else {
            setMinutes(
              parseInt(minutes, 10),
              parseInt(seconds, 10) - parseInt(1, 10),
            );
            setSeconds(parseInt(59, 10));
          }
        }
      }, 1000);
      return () => clearInterval(countdown);
    }, [minutes, seconds]);
    return (
      <div className="text-red-500">
        {minutes}:{seconds < 10 ? `0${seconds}` : seconds}
      </div>
    );
  };

  const {
    handleSubmit,
    register,
    formState: { errors },
  } = useForm();
  const onSubmit = data => {
    // console.log(data);
  };

  const goToNextSignUpPage = () => {};

  return (
    <div className={style.signup}>
      <div className={classNames(`style.div-header`, `sticky top-0`)}>
        <ReturnArrow title="회원가입" />
      </div>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div className="div-body p-6 pb-32">
          <InputText
            type="email"
            inputType="iconText"
            labelName="이메일"
            content="welcome@devday.com"
            icon="인증하기"
            onClick={onClickEmailValidation}
          />
          {emailValidCheck && (
            <div className={style.emailCheck}>
              <input
                type="text"
                className={classNames(
                  'w-full focus:outline-none',
                  style.emailInput,
                )}
              />
              <Timer />
              <button type="button" className="ml-2 whitespace-nowrap">
                확인
              </button>
            </div>
          )}
          <InputText
            type="password"
            inputType="password"
            labelName="비밀번호"
            content="12자리 이상, 대문자 소문자 특수문자"
          />
          <InputText
            type="password"
            inputType="password"
            labelName="비밀번호 확인"
            content="위와 일치하지 않습니다"
          />
          <InputText
            type="text"
            inputType="text"
            labelName="이름"
            content="홍길동"
          />
          <InputText
            type="text"
            inputType="iconText"
            labelName="닉네임"
            content="Devday챌린지"
            icon="중복확인"
            onClick={onClickDuplicateCheck}
          />
          {nickNameDuplicateChk && <div>가즈아</div>}
        </div>
        <div
          className={classNames(
            `font-sans text-center absolute w-full bottom-0 p-4`,
          )}
        >
          <Button
            type="submit"
            // onClick={() => router.push('/user/signup/extra-info')}
            color="primary"
            fill
            label="다음으로"
          />
          <div className="mt-2"> 회원 가입</div>
        </div>
      </form>
    </div>
  );
};

export default signup;
