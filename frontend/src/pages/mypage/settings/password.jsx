import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { BiShow, BiHide } from 'react-icons/bi';
import { useDispatch, useSelector } from 'react-redux';

import classNames from 'classnames';
import { useRouter } from 'next/router';
import Swal from 'sweetalert2';

import style from './password.module.scss';
import http from '@/api/http';

import { Button } from '@/components/Button';
import Container from '@/components/Container';
import { InputLabel } from '@/components/InputLabel';
import { InputText } from '@/components/InputText';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import { ReturnArrow } from '@/components/ReturnArrow';
import { PASSWORD_URL } from '@/constants';
import { persistor } from '@/pages/_app';
import { reset } from '@/store/user/userSlice';

const password = () => {
  const router = useRouter();
  const dispatch = useDispatch();
  const userInfo = useSelector(state => state.user);

  const [showNowPassword, setShowNowPassword] = useState(false);
  const [showNewPassword, setShowNewPassword] = useState(false);
  const [showPasswordCheck, setShowPasswordCheck] = useState(false);

  const toggleShowNowPassword = () => {
    setShowNowPassword(prev => !prev);
  };

  const toggleShowNewPassword = () => {
    setShowNewPassword(prev => !prev);
  };

  const toggleShowPasswordCheck = () => {
    setShowPasswordCheck(prev => !prev);
  };

  const {
    register,
    formState: { errors },
    watch,
  } = useForm({ mode: 'onBlur' });

  const changePassword = () => {
    if (watch('password').trim() === '') {
      Swal.fire({
        position: 'center',
        icon: 'error',
        title: '현재 비밀번호를 입력해주세요',
        showConfirmButton: false,
        timer: 1200,
      });
      return;
    }

    if (
      !/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,}/.test(
        watch('newPassword'),
      )
    ) {
      Swal.fire({
        position: 'center',
        icon: 'error',
        title: '조건에 맞지 않는 비밀번호입니다',
        showConfirmButton: false,
        timer: 1200,
      });
      return;
    }

    if (watch('password') === watch('newPassword')) {
      Swal.fire({
        position: 'center',
        icon: 'error',
        title: '현재 사용중인 비밀번호로는 변경할 수 없습니다.',
        showConfirmButton: false,
        timer: 1200,
      });
      return;
    }

    if (watch('newPassword') !== watch('passwordCheck')) {
      Swal.fire({
        position: 'center',
        icon: 'error',
        title: '비밀번호를 확인해주세요',
        showConfirmButton: false,
        timer: 1200,
      });
      return;
    }

    http
      .patch(PASSWORD_URL, {
        password: watch('password'),
        newPassword: watch('newPassword'),
      })
      .then(() => {
        dispatch(reset());
        persistor.purge();
      })
      .catch(err => {
        Swal.fire({
          position: 'center',
          icon: 'error',
          title: err.response.data.message,
          showConfirmButton: false,
          timer: 1200,
        });
      });
  };

  return (
    <Container>
      <Container.Header className="mb-10">
        <ReturnArrow title="비밀번호 수정" />
      </Container.Header>
      <Container.Body className="m-6">
        <div className="mt-8 mb-4">
          <InputLabel content="현재 비밀번호" />
        </div>
        <div className={classNames(style.InputText, 'flex w-full my-2 pb-1')}>
          <input
            type={showNowPassword ? 'text' : 'password'}
            placeholder="8자리 이상의 대문자, 소문자, 특수문자 "
            className={classNames(style.Content, `w-full h-6`)}
            {...register('password', {
              required: true,
            })}
          />
          <button type="button" onClick={toggleShowNowPassword}>
            {showNowPassword ? <BiShow /> : <BiHide />}
          </button>
        </div>
        {errors && errors.password && errors.password.type === 'required' && (
          <span className={classNames(`font-medium`, style.Error)}>
            비밀번호를 입력해주세요!
          </span>
        )}
        <div className={classNames(`mt-5`)}>
          <InputLabel content="새 비밀번호" />
          <div className={classNames(style.InputText, 'flex w-full my-2 pb-1')}>
            <input
              type={showNewPassword ? 'text' : 'password'}
              placeholder="8자리 이상의 대문자, 소문자, 특수문자 "
              className={classNames(style.Content, `w-full h-6`)}
              {...register('newPassword', {
                required: true,
                pattern:
                  /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,}/,
              })}
            />
            <button type="button" onClick={toggleShowNewPassword}>
              {showNewPassword ? <BiShow /> : <BiHide />}
            </button>
          </div>
          {errors &&
            errors.newPassword &&
            errors.newPassword.type === 'required' && (
              <span className={classNames(`font-medium`, style.Error)}>
                비밀번호를 입력해주세요!
              </span>
            )}
          {errors &&
            errors.newPassword &&
            errors.newPassword.type === 'pattern' && (
              <span className={classNames(`font-medium`, style.Error)}>
                조건에 맞지 않는 비밀번호 입니다.
              </span>
            )}
        </div>
        <div className={classNames(`mt-5`)}>
          <InputLabel content="비밀번호 확인" />
          <div className={classNames(style.InputText, 'flex w-full my-2 pb-1')}>
            <input
              type={showPasswordCheck ? 'text' : 'password'}
              placeholder="다시 한번 입력해주세요"
              className={classNames(style.Content, `w-full h-6`)}
              {...register('passwordCheck', {
                required: true,
                pattern:
                  /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,}/,
              })}
            />
            <button type="button" onClick={toggleShowPasswordCheck}>
              {showPasswordCheck ? <BiShow /> : <BiHide />}
            </button>
          </div>
        </div>
      </Container.Body>
      <Container.Footer className="p-4">
        <Button label="확인" onClick={changePassword} />
      </Container.Footer>
    </Container>
  );
};

export default PrivateRouter(password);
