import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { BiShow, BiHide } from 'react-icons/bi';
import { useDispatch } from 'react-redux';

import classNames from 'classnames';
import { useRouter } from 'next/router';

import style from './signup.module.scss';
import { Button } from '../../components/Button';
import { ReturnArrow } from '../../components/ReturnArrow';

import { InputLabel } from '@/components/InputLabel';
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
  const [nickNameDuplicateChk, setnickNameDuplicateChk] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

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

  const toggleShowPassword = () => {
    setShowPassword(prev => !prev);
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

  const validate = values => {
    const errors = {};

    if (values.password !== values.passwordCheck) {
      errors.passwordCheck = '비밀번호가 일치하지 않습니다.';
    }

    return errors;
  };
  const {
    handleSubmit,
    register,
    formState: { errors },
  } = useForm({ validate, mode: 'onBlur' });

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
          <InputLabel content="이메일" />
          <div className={classNames(style.InputText, 'flex w-full my-2 pb-1')}>
            <input
              className={classNames(style.Content, `w-full h-6`)}
              type="text"
              placeholder="welcome@devday.com"
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
                />
                <Timer />
                <button type="button" className="ml-2 whitespace-nowrap">
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
                placeholder="닉네임을 입력해주세요"
                {...register('nickName', {
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
                errors.nickName &&
                errors.nickName.type === 'required' && (
                  <span className={classNames(`font-medium`, style.Error)}>
                    닉네임 입력해주세요!
                  </span>
                )}
              {errors &&
                errors.nickName &&
                errors.nickName.type === 'pattern' && (
                  <span className={classNames(`font-medium`, style.Error)}>
                    조건에 맞지 않는 닉네임 입니다.
                  </span>
                )}
            </div>
            {!(
              errors &&
              errors.nickName &&
              errors.nickName.type === 'required'
            ) &&
              !(
                errors &&
                errors.nickName &&
                errors.nickName.type === 'pattern'
              ) &&
              nickNameDuplicateChk && (
                <div
                  className={classNames(
                    `flex p-2 rounded-lg`,
                    style.nickNameCheck,
                  )}
                >
                  <div
                    className={classNames(
                      'w-11/12 focus:outline-none',
                      style.emailInput,
                    )}
                  >
                    중복된 닉네임입니다. check
                  </div>
                </div>
              )}
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
