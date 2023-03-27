import React, { useState, useEffect } from 'react';

import classNames from 'classnames';
import Image from 'next/image';

import style from './extra-info.module.scss';
import { Button } from '../../../components/Button';
import { ReturnArrow } from '../../../components/ReturnArrow';

import { InputBox } from '@/components/InputBox';
import { useRouter } from 'next/router';

const signup = props => {
  const Router = useRouter();
  const join = () => {
    // api
    Router.push('/main');
  };
  const pass = () => {
    // api
    Router.push('/main');
  };
  return (
    <div className={style.signup}>
      <div className={classNames(`style.div-header`, `sticky top-0`)}>
        <ReturnArrow title="회원가입" urlname="/user/signup" />
      </div>
      <div className="div-body p-6 pb-32">
        <div className="mb-16">
          <Image
            src={require('@/image/solvedAC.png')}
            alt="solvedac"
            width={195}
            height={39}
            className="mb-4"
          />
          <InputBox placeholder="SOLVED.AC 아이디" />
          <p className={classNames(style.info, 'w-full mt-4')}>
            알고리즘 챌린지를 이용하기 위해 필요합니다.
            <br /> 해당 정보가 입력되지 않는다면 해당 서비스 이용이 제한됩니다.
          </p>
        </div>
        <div>
          <Image
            src={require('@/image/github.png')}
            alt="github"
            width={195}
            height={39}
          />
          <InputBox placeholder="GitHub 아이디" />
          <p className={classNames(style.info, 'w-full mt-4')}>
            Commit 챌린지를 이용하기 위해 필요합니다.
            <br /> 해당 정보가 입력되지 않는다면 해당 서비스 이용이 제한됩니다.
          </p>
        </div>
      </div>
      <div
        className={classNames(
          `font-sans text-center absolute w-full bottom-0 p-4`,
        )}
      >
        <div className="flex">
          <div className="w-1/2 p-2">
            <Button
              color="primary"
              fill={false}
              label="건너뛰기"
              onClick={pass}
            />
          </div>
          <div className="w-1/2 p-2">
            <Button color="primary" fill label="가입하기" onClick={join} />
          </div>
        </div>
        <div className="mt-2"> 회원 가입</div>
      </div>
    </div>
  );
};

export default signup;
