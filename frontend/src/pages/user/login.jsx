import React, { useState, useRef, useEffect } from 'react';
import { AiOutlineCloseCircle } from 'react-icons/ai';

import classNames from 'classnames';
import Image from 'next/image';
import { useRouter } from 'next/router';
import Swal from 'sweetalert2';
import { LOGIN_URL } from '@/constants';
import style from './login.module.scss';
import { Button } from '../../components/Button';
import { InputText } from '../../components/InputText';
import { ReturnArrow } from '../../components/ReturnArrow';
import http from '../api/http';

import { InputLabel } from '@/components/InputLabel';

const login = props => {
  const router = useRouter();
  const [service, setService] = useState(false);
  const [show, setShow] = useState(false);
  const [check, setCheck] = useState(false);
  const [user, setUser] = useState({ email: '', password: '' });

  const showModal = () => {
    setShow(prev => !prev);
  };
  const clickCheck = () => {
    setCheck(prev => !prev);
    localStorage.removeItem('savedEmail');
  };
  useEffect(() => {
    const saveEmail = localStorage.getItem('savedEmail');
    const accessToken = sessionStorage.getItem('accessToken');
    if (accessToken) {
      Swal.fire({
        position: 'center',
        icon: 'error',
        title: '로그인 되어있습니다',
        showConfirmButton: false,
        timer: 1600,
      }).then(() => {
        router.push('/');
      });
    }

    if (saveEmail) {
      setCheck(true);
    }
    setUser({
      ...user,
      email: saveEmail,
      password: '',
    });
  }, []);
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

  const handleChange = e => {
    setUser({
      ...user,
      [e.target.name]: e.target.value,
    });
  };

  const onClickLogin = e => {
    e.preventDefault();
    http
      .post(LOGIN_URL, user)
      .then(data => {
        sessionStorage.setItem('accessToken', data.data.data.accessToken);
        sessionStorage.setItem('refreshToken', data.data.data.refreshToken);
      })
      .then(() => {
        Swal.fire({
          position: 'center',
          icon: 'success',
          title: '로그인 성공',
          showConfirmButton: false,
          timer: 1600,
        }).then(() => {
          if (check) {
            localStorage.setItem('savedEmail', user.email);
          }
          router.push('/');
        });
      })
      .catch(error => {
        Swal.fire({
          icon: 'error',
          title: '로그인 실패',
          text: error.response.data.message,
        });
      });
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
        <form onSubmit={onClickLogin}>
          <div>
            <InputLabel content="이메일" />
            <InputText
              name="email"
              type="email"
              value={user.email}
              inputType="text"
              content="welcome@devday.com"
              onChange={handleChange}
            />
          </div>
          <div className={classNames(`mt-3`)}>
            <InputLabel content="비밀번호" />
            <InputText
              name="password"
              type="password"
              content="12자리 이상, 대문자 소문자 특수문자"
              onChange={handleChange}
            />
          </div>
        </form>
        <div className={classNames(`mt-3`, style.option)}>
          <label htmlFor="toggle" className="flex">
            <label htmlFor="toggle" className={style.togglelabel}>
              <input
                className={style.toggle}
                type="checkbox"
                id="toggle"
                checked={check}
                onChange={clickCheck}
              />
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
        <Button
          type="submit"
          color="primary"
          fill
          label="로그인"
          onClick={onClickLogin}
        />
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
