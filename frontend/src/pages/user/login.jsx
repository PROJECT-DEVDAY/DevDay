import React, { useState, useRef, useEffect } from 'react';
import { useDispatch } from 'react-redux';

import classNames from 'classnames';
import Image from 'next/image';
import { useRouter } from 'next/router';
import Swal from 'sweetalert2';

import style from './login.module.scss';
import { Button } from '../../components/Button';
import { InputText } from '../../components/InputText';
import { ReturnArrow } from '../../components/ReturnArrow';

import Container from '@/components/Container';
import { InputLabel } from '@/components/InputLabel';
import { loginAsync } from '@/store/user/userSlice';

const login = props => {
  const router = useRouter();
  const dispatch = useDispatch();

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
        timer: 1400,
      }).then(router.push('/'));
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

  const onSubmitLogin = async event => {
    event.preventDefault();

    dispatch(loginAsync(user))
      .unwrap()
      .then(
        await Swal.fire({
          position: 'center',
          icon: 'success',
          title: '로그인 성공',
          showConfirmButton: false,
          timer: 1200,
        }),

        router.push('/'),
      )
      .catch(error => {
        Swal.fire({
          position: 'center',
          icon: 'error',
          title: error.message,
          showConfirmButton: false,
          timer: 1200,
        });
      });
  };

  const onSubmitLoginEnter = event => {
    if (event.key === 'Enter') {
      onSubmitLogin(event);
    }
  };
  return (
    <Container>
      <Container.SubPageHeader title="로그인" />
      <Container.MainBody>
        <Image
          src={require('@/image/main_logo.png')}
          className="w-full"
          alt="loginImage"
        />
        <div className="px-3">
          <form className="my-5">
            <div>
              <InputLabel content="이메일" />
              <InputText
                name="email"
                type="email"
                value={user.email || ''}
                inputType="text"
                content="welcome@devday.com"
                onChange={handleChange}
              />
            </div>
            <div className={classNames(`mt-6`)}>
              <InputLabel content="비밀번호" />
              <InputText
                name="password"
                type="password"
                content="8자리 이상, 대문자 소문자 특수문자"
                onChange={handleChange}
              />
            </div>

            <div className={classNames(`mt-4 flex justify-between`)}>
              <label htmlFor="toggle" className="flex">
                <div className="mr-2 text-sm">아이디 기억하기</div>
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
              </label>
              <button className="text-sm" onClick={showModal} type="button">
                아이디/비밀번호 찾기
              </button>
            </div>
          </form>
          <Button
            type="submit"
            onClick={onSubmitLogin}
            onKeyUp={onSubmitLoginEnter}
            color="primary"
            fill
            label="로그인"
          />
          <div className="text-center  w-full bottom-0 p-4">
            <div className="mt-2">
              아직 회원이 아니신가요?
              <button
                type="button"
                className={classNames(style.signup, 'ml-2')}
                onClick={() => router.push('/user/join')}
              >
                회원가입
              </button>
            </div>
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
                  className="mt-4 text-sm"
                  onClick={goToId}
                  color="primary"
                  fill
                  label="아이디 찾기"
                />
              </div>
              <div className="w-1/2 m-2 ">
                <Button
                  className="mt-4 "
                  onClick={goToPw}
                  color="primary"
                  fill
                  label="비밀번호 찾기"
                />
              </div>
            </span>
          </div>
        </div>
      </Container.MainBody>
    </Container>
  );
};

export default login;
