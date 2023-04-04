import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/router';
import { useForm } from 'react-hook-form';

import http from '../../api/http';
import classNames from 'classnames';

import style from './nickname.module.scss';
import { Button } from '@/components/Button';
import Container from '@/components/Container';
import { InputLabel } from '@/components/InputLabel';
import { InputText } from '@/components/InputText';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import { ReturnArrow } from '@/components/ReturnArrow';
import { NICKNAME_URL } from '@/constants';

const nickname = () => {
  const router = useRouter();
  const [nickNameValidCheck, setNickNameValidCheck] = useState(false);
  const [nickNameDuplicatedChk, setNickNameDuplicatedChk] = useState(false);

  const {
    register,
    formState: { errors },
    watch,
  } = useForm({ mode: 'onBlur' });

  // nickname check logic
  const onClickDuplicateCheck = () => {
    http.post(NICKNAME_URL, { nickname: watch('nickname') }).then((res) => {
      setNickNameDuplicatedChk(true);
      setNickNameValidCheck(prev => !prev);
    }).catch(err => {
      setNickNameDuplicatedChk(false);
    })
  };

  return (
    <Container>
      <Container.Header className="mb-10">
        <ReturnArrow title="닉네임 변경" />
      </Container.Header>
      <Container.Body className="m-6">
        {/* <div className="mt-8 mb-4">
          <InputLabel content="새 닉네임" />
        </div>
        <InputText
          content="홍길동"
          inputType="iconText"
          icon="중복확인"
          onClick={onClickDuplicateCheck}
        /> */}
        <div className={classNames(`mt-3`)}>
          <InputLabel content="닉네임" />
          <div className={classNames(style.InputText, 'flex w-full my-2 pb-1')}>
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
        <div className="mt-8 mb-4">
          <InputLabel content="이메일" />
        </div>
        <InputText content="welcome@devday.com" />
        <div className="mt-8 mb-4">
          <InputLabel content="비밀번호" />
        </div>
        <InputText content="12자리 이상, 대문자, 소문자, 특수문자 포함" />
      </Container.Body>
      <Container.Footer className="p-4">
        <Button label="확인" />
      </Container.Footer>
    </Container>
  );
};

export default PrivateRouter(nickname);
