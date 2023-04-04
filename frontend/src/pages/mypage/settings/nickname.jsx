import React, { useState, useEffect } from 'react';

import { useRouter } from 'next/router';

import http from '../../api/http';

import { Button } from '@/components/Button';
import Container from '@/components/Container';
import { InputLabel } from '@/components/InputLabel';
import { InputText } from '@/components/InputText';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import { ReturnArrow } from '@/components/ReturnArrow';
import { NICKNAME_URL } from '@/constants';
import { useSelector } from 'react-redux';

import style from './nickname.module.scss';
import classNames from 'classnames';
import { useForm } from 'react-hook-form';

const nickname = () => {
  const router = useRouter();
  const [nickNameValidCheck, setUserNickNameValidCheck] = useState(false);
  const [nickNameDuplicatedChk, setUserNickNameDuplicatedChk] = useState(false);

  const user = useSelector(state => state.user);

  const validate = values => {
    const errors = {};

    return errors;
  };
  const {
    register,
    formState: { errors },
    reset,
  } = useForm({ validate, mode: 'onBlur' });

  const [inputs, setInputs] = useState({
    newNickname: '',
    email: '',
    password: '',
  });

  const { newNickname, email, password } = inputs;
  const onChangeInputs = e => {
    const { value, name } = e.target;
    setInputs({
      ...inputs,
      [name]: value,
    });
  };

  // nickname check logic
  const onClickDuplicateCheck = async () => {
    await http
      .post(NICKNAME_URL, { newNickname })
      .then(() => {
        setUserNickNameDuplicatedChk(true);
        setUserNickNameValidCheck(true);
      })
      .catch(() => {
        setUserNickNameDuplicatedChk(false);
      });
  };
  const retryNickname = () => {
    setUserNickNameDuplicatedChk(false);
    setUserNickNameValidCheck(false);
  };
  return (
    <Container>
      <Container.Header className="mb-10">
        <ReturnArrow title="닉네임 변경" />
      </Container.Header>
      <Container.Body className="m-6">
        <div className="mt-8 mb-4">
          <InputLabel content="새 닉네임" />
        </div>

        <div className={classNames(style.InputText, 'flex w-full my-2 pb-1')}>
          <input
            type="text"
            name="newNickname"
            placeholder="닉네임을 입력해주세요"
            className={classNames(style.Content, `w-full h-6`)}
            onChange={onChangeInputs}
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

        <div className="mt-8 mb-4">
          <InputLabel content="이메일" />
        </div>
        <InputText
          content="welcome@devday.com"
          name={'email'}
          onChange={onChangeInputs}
        />
        <div className="mt-8 mb-4">
          <InputLabel content="비밀번호" />
        </div>
        <InputText
          content="12자리 이상, 대문자, 소문자, 특수문자 포함"
          name={'password'}
          onChange={onChangeInputs}
        />
      </Container.Body>
      <Container.Footer className="p-4">
        <Button label="확인" />
      </Container.Footer>
    </Container>
  );
};

export default PrivateRouter(nickname);
