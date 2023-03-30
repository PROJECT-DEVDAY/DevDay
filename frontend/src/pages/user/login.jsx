import React, { useState, useRef, useEffect } from 'react';
import { AiOutlineCloseCircle } from 'react-icons/ai';

import classNames from 'classnames';
import Image from 'next/image';
import { useRouter } from 'next/router';

import style from './login.module.scss';
import { Button } from '../../components/Button';
import { InputText } from '../../components/InputText';
import { ReturnArrow } from '../../components/ReturnArrow';

import { InputLabel } from '@/components/InputLabel';

const login = props => {
  const router = useRouter();
  const [service, setService] = useState(false);
  const [show, setShow] = useState(false);

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleEmailChange = e => {
    setEmail(e.target.value);
  };
  const handlePasswordChange = e => {
    setPassword(e.target.value);
  };

  const showModal = () => {
    setShow(prev => !prev);
  };

  const bottomSheetRef = useRef(null);

  useEffect(() => {
    const handleClickOutside = event => {
      if (
        bottomSheetRef.current &&
        !bottomSheetRef.current.contains(event.target)
      ) {
        setShow(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [bottomSheetRef]);
  const goToId = () => {
    router.push('/user/find/idInquiry');
  };
  const goToPw = () => {
    router.push('/user/find/pwInquiry');
  };
  return (
    <div>
      <div className={classNames(`style.div-header`, `sticky top-0`)}>
        <ReturnArrow title="로그인" />
      </div>
      <div className="div-body p-6 ">
        <Image
          src={require('@/image/main_logo.png')}
          className="w-full"
          alt="loginImage"
        />
        <div>
          <InputLabel content="이메일" />
          <InputText
            inputType="email"
            content="welcome@devday.com"
            value={email}
            onChange={handleEmailChange}
          />
        </div>
        <div className={classNames(`mt-3`)}>
          <InputLabel content="비밀번호" />
          <InputText
            inputType="password"
            content="12자리 이상, 대문자 소문자 특수문자"
            value={password}
            onChange={handlePasswordChange}
          />
        </div>
        <div className={classNames(`mt-3`, style.option)}>
          <label htmlFor="toggle" className="flex">
            <label htmlFor="toggle" className={style.togglelabel}>
              <input className={style.toggle} type="checkbox" id="toggle" />
              <div className={style.togglelabelhandle} />
            </label>
            <div className={classNames('ml-4', style.font)}>
              아이디 기억하기
            </div>
          </label>
          <button onClick={showModal} className={style.font} type="button">
            아이디/비밀번호 찾기
          </button>
        </div>
      </div>
      <div className={classNames(`text-center absolute w-full bottom-0 p-4`)}>
        <Button color="primary" fill label="로그인" />
        <div className="mt-2">
          회원 가입할래?
          <button
            type="button"
            className={style.signup}
            onClick={() => router.push('/user/signup')}
          >
            회원가입
          </button>
        </div>
      </div>
      <div
        className={classNames(
          style[`bottom-sheet`],
          style[`${show ? 'show' : ''}`],
        )}
        ref={bottomSheetRef}
      >
        <div className={classNames(style[`modal-content`], 'pl-6 pr-6 h-12')}>
          <span className="flex">
            <div className="w-1/2 m-2">
              <Button
                className="mt-4"
                onClick={goToId}
                color="primary"
                fill
                label="아이디 찾기"
              />
            </div>
            <div className="w-1/2 m-2">
              <Button
                className="mt-4"
                onClick={goToPw}
                color="primary"
                fill
                label="비밀번호 찾기"
              />
            </div>
          </span>
        </div>
      </div>
    </div>
  );
};

export default login;
