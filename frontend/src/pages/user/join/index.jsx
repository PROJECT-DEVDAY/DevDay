import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { BiShow, BiHide } from 'react-icons/bi';
import { useDispatch, useSelector } from 'react-redux';

import classNames from 'classnames';
import { useRouter } from 'next/router';
import Swal from 'sweetalert2';

import style from './index.module.scss';
import { Button } from '../../../components/Button';
import { ReturnArrow } from '../../../components/ReturnArrow';
import http from '@/api/http';

import Container from '@/components/Container';
import { InputLabel } from '@/components/InputLabel';
import { CONFIRM_EMAIL_URL, EMAIL_URL, NICKNAME_URL } from '@/constants';
import { saveSignUpInfos } from '@/store/signup/signupSlice';
import { useRef } from 'react';
import password from '@/pages/mypage/settings/password';

const signup = props => {
  const [emailValidCheck, setEmailValidCheck] = useState(false);
  const [emailAuthenticated, setEmailAuthenticated] = useState(false);

  // 인증 고유번호
  const [emailAuthId, setEmailAuthId] = useState(-1);
  const [emailAuthToken, setEmailAuthtoken] = useState('');

  const [nickNameValidCheck, setNickNameValidCheck] = useState(false);
  const [nickNameDuplicatedChk, setNickNameDuplicatedChk] = useState(false);

  const [showPassword, setShowPassword] = useState(false);
  const [showPasswordCheck, setShowPasswordCheck] = useState(false);

  const router = useRouter();
  const dispatch = useDispatch();
  const signUpInfos = useSelector(state => state.signUp);

  const [remainingTime, setRemainingTime] = useState(4 * 60 + 59);
  const Timer = () => {
    useEffect(() => {
      const countdown = setInterval(() => {
        setRemainingTime(prevTime => prevTime - 1);
      }, 1000);

      return () => clearInterval(countdown);
    }, []);

    useEffect(() => {
      if (remainingTime === 0) {
        setEmailValidCheck(false);
      }
    }, [remainingTime]);

    const minutes = Math.floor(remainingTime / 60);
    const seconds = remainingTime % 60;

    return (
      <div className="text-red-500">
        {minutes}:{seconds < 10 ? `0${seconds}` : seconds}
      </div>
    );
  };

  const toggleShowPassword = () => {
    setShowPassword(prev => !prev);
  };

  const toggleShowPasswordCheck = () => {
    setShowPasswordCheck(prev => !prev);
  };

  const {
    handleSubmit,
    register,
    formState: { errors },
    watch,
    reset,
  } = useForm({ mode: 'onBlur' });

  useEffect(() => {
    if (signUpInfos) {
      const { email, password, passwordCheck, name, nickname } = signUpInfos;
      reset({ email, password, passwordCheck, name, nickname });
    }
  }, [signUpInfos, reset]);

  const passowrd = useRef({});
  password.current = watch('password', '');

  const validatePasswordCheck = value => {
    if (value === password.current) {
      return true;
    } else {
      return '동일한 비밀번호를 입력해주세요';
    }
  };

  // email check logic
  const onChangeAuthToken = event => {
    setEmailAuthtoken(event.target.value);
  };

  const onClickEmailValidation = async () => {
    setEmailAuthenticated(false);

    try {
      setRemainingTime(4 * 60 + 59);

      const { data } = await http.post(EMAIL_URL, { email: watch('email') });

      setEmailValidCheck(true);
      setEmailAuthId(data.data);
    } catch (error) {
      setEmailValidCheck(false);
      let errorMessage;
      if (
        error &&
        error.response &&
        error.response.data &&
        error.response.data.message
      ) {
        errorMessage = error.response.data.message;
      } else {
        errorMessage = '알 수 없는 오류가 발생했습니다.';
      }

      Swal.fire({
        icon: 'error',
        title: '인증 실패',
        text: errorMessage,
      });
    }
  };
  const onClickEmailAuthTokenCheck = async () => {
    try {
      await http.patch(CONFIRM_EMAIL_URL, {
        id: emailAuthId,
        authToken: emailAuthToken,
      });

      setRemainingTime(0);
      setEmailAuthenticated(true);
      setEmailValidCheck(false);

      Swal.fire({
        icon: 'success',
        title: '인증 성공',
        text: '인증에 성공했습니다!',
      });
    } catch (error) {
      setEmailValidCheck(false);
      setEmailAuthenticated(false);

      let errorMessage;
      if (
        error &&
        error.response &&
        error.response.data &&
        error.response.data.message
      ) {
        errorMessage = error.response.data.message;
      } else {
        errorMessage = '알 수 없는 오류가 발생했습니다.';
      }
      Swal.fire({
        icon: 'error',
        title: '인증 실패',
        text: errorMessage,
      });
    }
  };

  // nickname check logic
  const onClickDuplicateCheck = async () => {
    try {
      await http.post(NICKNAME_URL, { nickn: watch('nickname') });

      setNickNameDuplicatedChk(true);
      setNickNameValidCheck(prev => !prev);
    } catch (error) {
      setNickNameDuplicatedChk(false);
    }
  };
  const retryNickname = () => {
    setNickNameDuplicatedChk(false);
    setNickNameValidCheck(false);
  };

  const onSubmit = data => {
    const inputData = { ...data, emailAuthId };
    dispatch(saveSignUpInfos(inputData));
    router.push('./join/extra-info');
  };

  return (
    <Container>
      <Container.SubPageHeader title="회원가입" />
      <Container.MainBody>
        <form>
          <div className="div-body p-6 pb-32">
            <InputLabel content="이메일" />
            <div
              className={classNames(style.InputText, 'flex w-full my-2 pb-1')}
            >
              <input
                className={classNames(style.Content, `w-full h-6`)}
                type="text"
                placeholder="welcome@devday.com"
                readOnly={emailValidCheck || emailAuthenticated}
                {...register('email', {
                  required: true,
                  pattern: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                })}
              />
              {errors?.email?.type !== 'pattern' &&
                errors?.email?.type !== 'required' &&
                !emailValidCheck && (
                  <button
                    type="button"
                    className={style.RightBtn}
                    onClick={onClickEmailValidation}
                  >
                    인증하기
                  </button>
                )}
            </div>
            {errors?.email?.type === 'required' && (
              <span className={classNames(`font-medium`, style.Error)}>
                이메일을 입력해주세요!
              </span>
            )}
            {errors?.email?.type === 'pattern' && (
              <span className={classNames(`font-medium`, style.Error)}>
                유효하지 않는 이메일 입니다.
              </span>
            )}

            {!(errors?.email?.type === 'required') &&
              !(errors?.email?.type === 'pattern') &&
              emailValidCheck && (
                <div
                  className={classNames(
                    `flex p-2 rounded-lg`,
                    style.emailCheck,
                  )}
                >
                  <input
                    type="text"
                    className={classNames(
                      'w-full focus:outline-none',
                      style.emailInput,
                    )}
                    onChange={onChangeAuthToken}
                  />
                  {{ emailAuthenticated } && <Timer />}
                  <button
                    type="button"
                    className="ml-2 whitespace-nowrap"
                    onClick={onClickEmailAuthTokenCheck}
                  >
                    확인
                  </button>
                </div>
              )}

            <div className={classNames(`mt-7`)}>
              <InputLabel content="비밀번호" />
              <div
                className={classNames(style.InputText, 'flex w-full my-2 pb-1')}
              >
                <input
                  type={showPassword ? 'text' : 'password'}
                  placeholder="8자리 이상의 대문자, 소문자, 특수문자 "
                  className={classNames(style.Content, `w-full h-6`)}
                  {...register('password', {
                    required: '비밀번호를 입력하세요',
                    minLength: {
                      value: 8,
                      message: '최소 8자리 이상 입력하세요',
                    },
                    pattern: {
                      value:
                        /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,}$/,
                      message:
                        '대문자, 소문자, 숫자, 특수문자가 하나 이상 포함되어야 합니다',
                    },
                  })}
                />
                <button type="button" onClick={toggleShowPassword}>
                  {showPassword ? <BiShow /> : <BiHide />}
                </button>
              </div>
              {errors.password && (
                <span className={classNames(style.Error, 'font-medium')}>
                  {errors.password.message}
                </span>
              )}
            </div>
            <div className={classNames(`mt-5`)}>
              <InputLabel content="비밀번호 확인" />
              <div
                className={classNames(style.InputText, 'flex w-full my-2 pb-1')}
              >
                <input
                  type={showPasswordCheck ? 'text' : 'password'}
                  placeholder="다시 한번 입력해주세요"
                  className={classNames(style.Content, `w-full h-6`)}
                  {...register('passwordCheck', {
                    required: '비밀번호를 입력하세요',
                    minLength: {
                      value: 8,
                      message: '최소 8자리 이상 입력하세요',
                    },
                    pattern: {
                      value:
                        /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,}$/,
                      message:
                        '최소 8자리, 대/소문자/숫자/특수문자가 각각 하나 이상 포함되어야 합니다',
                    },
                    validate: validatePasswordCheck,
                  })}
                />
                <button type="button" onClick={toggleShowPasswordCheck}>
                  {showPasswordCheck ? <BiShow /> : <BiHide />}
                </button>
              </div>
              {errors.passwordCheck && (
                <span className={classNames(style.Error, 'font-medium')}>
                  {errors.passwordCheck.message}
                </span>
              )}
            </div>
            <div className={classNames(`mt-7`)}>
              <InputLabel content="이름" />
              <div
                className={classNames(style.InputText, 'flex w-full my-2 pb-1')}
              >
                <input
                  className={classNames(style.Content, `w-full h-6 `)}
                  type="text"
                  placeholder="이름을 입력해주세요"
                  {...register('name', {
                    required: true,
                    pattern: /^[가-힣]{2,4}$/,
                  })}
                />
              </div>
              <div className={(`font-medium`, style.Error)}>
                {errors && errors.name && errors.name.type === 'required' && (
                  <span className={classNames(`font-medium`, style.Error)}>
                    이름을 입력해주세요!
                  </span>
                )}
                {errors && errors.name && errors.name.type === 'pattern' && (
                  <span className={classNames(`font-medium`, style.Error)}>
                    조건에 맞지 않는 이름 입니다.
                  </span>
                )}
              </div>
            </div>

            <div className={`mt-5`}>
              <InputLabel content="닉네임" />
              <div
                className={classNames(style.InputText, 'flex w-full my-2 pb-1')}
              >
                <input
                  type="text"
                  className={classNames(style.Content, `w-full h-6`)}
                  placeholder="닉네임을 입력해주세요"
                  readOnly={nickNameValidCheck}
                  {...register('nickname', {
                    required: true,
                    pattern: /^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,16}$/,
                  })}
                />
                {errors?.nickname?.type !== 'pattern' &&
                  errors?.nickname?.type !== 'required' &&
                  !nickNameValidCheck && (
                    <button
                      type="button"
                      className={classNames(style.RightBtn, 'font-medium')}
                      onClick={onClickDuplicateCheck}
                    >
                      중복확인
                    </button>
                  )}
              </div>

              <div className={(`font-medium`, style.Error)}>
                {errors?.nickname?.type === 'required' && (
                  <span className={classNames(`font-medium`, style.Error)}>
                    닉네임 입력해주세요!
                  </span>
                )}
                {errors?.nickname?.type === 'pattern' && (
                  <span className={classNames(`font-medium`, style.Error)}>
                    조건에 맞지 않는 닉네임 입니다.
                  </span>
                )}
              </div>
              {nickNameValidCheck && (
                <div
                  className={classNames(
                    nickNameDuplicatedChk ? style.pass : style.nonPass,
                    style.nickNameCheck,
                    `p-2 flex items-center relative justify-between`,
                  )}
                >
                  {nickNameDuplicatedChk ? (
                    <label className="pass">사용가능한 닉네임입니다!</label>
                  ) : (
                    <label className="non-pass">중복된 닉네임입니다!</label>
                  )}
                  <button class="font-medium" onClick={retryNickname}>
                    재입력
                  </button>
                </div>
              )}
            </div>
          </div>
        </form>
      </Container.MainBody>
      <Container.MainFooter className="bg-white">
        <div
          className={classNames(
            `font-sans text-center absolute w-full bottom-0 p-4`,
          )}
        >
          <Button
            type="submit"
            onClick={handleSubmit(onSubmit)}
            color="primary"
            fill
            label="다음으로"
          />
        </div>
      </Container.MainFooter>
    </Container>
  );
};

export default signup;
