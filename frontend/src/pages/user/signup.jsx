import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { BiShow, BiHide } from 'react-icons/bi';
import { useDispatch, useSelector } from 'react-redux';

import classNames from 'classnames';
import { useRouter } from 'next/router';
import Swal from 'sweetalert2';

import style from './signup.module.scss';
import { Button } from '../../components/Button';
import { ReturnArrow } from '../../components/ReturnArrow';
import http from '../api/http';

import { InputLabel } from '@/components/InputLabel';
import { saveSignUpInfos } from '@/store/signup/signupSlice';
import { CONFIRM_EMAIL_URL, EMAIL_URL, NICKNAME_URL } from '@/constants';

const signup = props => {
  const [emailValidCheck, setEmailValidCheck] = useState(false);
  const [emailAuthenticated, setEmailAuthenticated] = useState(false);

  // 인증 고유번호
  const [emailAuthId, setEmailAuthId] = useState(-1);
  const [emailAuthToken, setEmailAuthtoken] = useState('');

  const [nickNameValidCheck, setNickNameValidCheck] = useState(false);
  const [nickNameDuplicatedChk, setNickNameDuplicatedChk] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const router = useRouter();
  const dispatch = useDispatch();
  const signUpInfos = useSelector(state => state.signUp);

  const validate = values => {
    const errors = {};

    if (values.password !== values.passwordCheck) {
      errors.passwordCheck = '비밀번호가 일치하지 않습니다.';
    }
    return errors;
  };

  const [minutes, setMinutes] = useState(parseInt(4, 10));
  const [seconds, setSeconds] = useState(parseInt(59, 10));
  const Timer = () => {
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
            setMinutes(parseInt(minutes, 10) - parseInt(1, 10));
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

  const toggleShowPassword = () => {
    setShowPassword(prev => !prev);
  };

  const {
    handleSubmit,
    register,
    formState: { errors },
    watch,
    reset,
  } = useForm({ validate, mode: 'onBlur' });

  useEffect(() => {
    if (signUpInfos) {
      const { email, password, passwordCheck, name, nickname } = signUpInfos;
      reset({ email, password, passwordCheck, name, nickname });
    }
  }, [signUpInfos, reset]);

  // email check logic
  const onChangeAuthToken = event => {
    setEmailAuthtoken(event.target.value);
  };

  const onClickEmailValidation = () => {
    setMinutes(parseInt(4, 10));
    setSeconds(parseInt(59, 10));

    http
      .post(EMAIL_URL, {
        email: watch('email'),
      })
      .then(data => {
        setEmailValidCheck(true);
        setEmailAuthId(data.data.data);
      })
      .catch(error => {
        setEmailValidCheck(false);

        Swal.fire({
          icon: 'error',
          title: '인증 실패',
          text: error.response.data.message,
        });
      });
  };

  const onClickEmailAuthTokenCheck = () => {
    http
      .patch(CONFIRM_EMAIL_URL, {
        id: emailAuthId,
        authToken: emailAuthToken,
      })
      .then(() => {
        setEmailAuthenticated(true);
        setSeconds(parseInt(0, 10));
        setMinutes(parseInt(0, 10));

        Swal.fire({
          icon: 'success',
          title: '인증 성공',
          text: '인증에 성공했습니다!',
        });
      })
      .then(setEmailValidCheck(false))
      .catch(error => {
        Swal.fire({
          icon: 'error',
          title: '인증 실패',
          text: error.response.data.message,
        });
        setEmailAuthenticated(false);
      });
  };

  // nickname check logic
  const onClickDuplicateCheck = () => {
    http
      .post(NICKNAME_URL, { nickn: watch('nickname') })
      .then(() => {
        setNickNameDuplicatedChk(true);
      })
      .then(() => {
        setNickNameValidCheck(prev => !prev);
      })
      .catch(() => {
        setNickNameDuplicatedChk(false);
      });
  };

  const onSubmit = data => {
    if (emailAuthenticated && nickNameDuplicatedChk) {
      const inputData = { ...data, emailAuthId };
      dispatch(saveSignUpInfos(inputData));
      router.push('./signup/extra-info');
    } else {
      Swal.fire({
        icon: 'error',
        title: '잠시만요!',
        text: '이메일 인증, 닉네임 중복체크를 해주세요',
      });
    }
  };

  return (
    <div className={style.signup}>
      <div className={classNames(`style.div-header`, `sticky top-0`)}>
        <ReturnArrow title="회원가입" />
      </div>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div className="div-body p-6 pb-32">
          <InputLabel content="이메일" />
          <div className={classNames(style.InputText, 'flex w-full my-2 pb-1')}>
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
            <button
              type="button"
              className={style.RightBtn}
              onClick={onClickEmailValidation}
            >
              인증하기
            </button>
          </div>
          {errors && errors.email && errors.email.type === 'required' && (
            <span className={classNames(`font-medium`, style.Error)}>
              이메일을 입력해주세요!
            </span>
          )}
          {errors && errors.email && errors.email.type === 'pattern' && (
            <span className={classNames(`font-medium`, style.Error)}>
              유효하지 않는 이메일 입니다.
            </span>
          )}

          {!(errors && errors.email && errors.email.type === 'required') &&
            !(errors && errors.email && errors.email.type === 'pattern') &&
            emailValidCheck && (
              <div
                className={classNames(`flex p-2 rounded-lg`, style.emailCheck)}
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

          <div className={classNames(`mt-5`)}>
            <InputLabel content="비밀번호" />
            <div
              className={classNames(style.InputText, 'flex w-full my-2 pb-1')}
            >
              <input
                type={showPassword ? 'text' : 'password'}
                placeholder="12자리 이상, 대문자 + 소문자 + 특수문자"
                className={classNames(style.Content, `w-full h-6`)}
                {...register('password', {
                  required: true,
                  pattern:
                    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,}/,
                })}
              />
              <button type="button" onClick={toggleShowPassword}>
                {showPassword ? <BiShow /> : <BiHide />}
              </button>
            </div>
            {errors &&
              errors.password &&
              errors.password.type === 'required' && (
                <span className={classNames(`font-medium`, style.Error)}>
                  비밀번호를 입력해주세요!
                </span>
              )}
            {errors &&
              errors.password &&
              errors.password.type === 'pattern' && (
                <span className={classNames(`font-medium`, style.Error)}>
                  조건에 맞지 않는 비밀번호 입니다.
                </span>
              )}
          </div>
          <div className={classNames(`mt-3`)}>
            <InputLabel content="비밀번호 확인" />
            <div
              className={classNames(style.InputText, 'flex w-full my-2 pb-1')}
            >
              <input
                type={showPassword ? 'text' : 'password'}
                placeholder="다시 한번 입력해주세요"
                className={classNames(style.Content, `w-full h-6`)}
                {...register('passwordCheck', {
                  required: true,
                  pattern:
                    /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,}/,
                })}
              />
              <button type="button" onClick={toggleShowPassword}>
                {showPassword ? <BiShow /> : <BiHide />}
              </button>
            </div>
            <div className={(`font-medium`, style.Error)}>
              {errors && errors.passwordCheck && (
                <span className={classNames(`font-medium`, style.Error)}>
                  동일한 비밀번호를 입력해주세요!
                </span>
              )}
            </div>
          </div>
          <div className={classNames(`mt-5`)}>
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

          <div className={classNames(`mt-3`)}>
            <InputLabel content="닉네임" />
            <div
              className={classNames(style.InputText, 'flex w-full my-2 pb-1')}
            >
              <input
                className={classNames(style.Content, `w-full h-6`)}
                type="text"
                readOnly={nickNameValidCheck}
                placeholder="닉네임을 입력해주세요"
                {...register('nickname', {
                  required: true,
                  pattern: /^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,16}$/,
                })}
              />
              <button
                type="button"
                className={style.RightBtn}
                onClick={onClickDuplicateCheck}
              >
                중복확인
              </button>
            </div>

            <div className={(`font-medium`, style.Error)}>
              {errors &&
                errors.nickname &&
                errors.nickname.type === 'required' && (
                  <span className={classNames(`font-medium`, style.Error)}>
                    닉네임 입력해주세요!
                  </span>
                )}
              {errors &&
                errors.nickname &&
                errors.nickname.type === 'pattern' && (
                  <span className={classNames(`font-medium`, style.Error)}>
                    조건에 맞지 않는 닉네임 입니다.
                  </span>
                )}
            </div>
            {!(
              errors &&
              errors.nickname &&
              errors.nickname.type === 'required'
            ) &&
              !(
                errors &&
                errors.nickname &&
                errors.nickname.type === 'pattern'
              ) &&
              nickNameValidCheck &&
              (nickNameDuplicatedChk ? (
                <div
                  className={classNames(
                    `flex justify-between p-2 rounded-lg`,
                    style.nickNamePass,
                  )}
                >
                  <p> 사용가능한 닉네임입니다 </p>
                  <button
                    type="button"
                    onClick={() => {
                      setNickNameValidCheck(prev => !prev);
                    }}
                  >
                    다시 입력하기
                  </button>
                </div>
              ) : (
                <div
                  className={classNames(
                    `flex justify-between p-2 rounded-lg`,
                    style.nickNameFail,
                  )}
                >
                  <p> 중복된 닉네임입니다 </p>
                  <button
                    type="button"
                    onClick={() => {
                      setNickNameValidCheck(prev => !prev);
                    }}
                  >
                    다시 입력하기
                  </button>
                </div>
              ))}
          </div>
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
