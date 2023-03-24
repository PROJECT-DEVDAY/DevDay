import React from 'react';

import classNames from 'classnames';

import style from './signup.module.scss';
import { Button } from '../../components/Button';
import { InputText } from '../../components/InputText';
import { ReturnArrow } from '../../components/ReturnArrow';

const signup = props => {
  return (
    <div className={style.signup}>
      <div className={classNames(`style.div-header`, `sticky top-0`)}>
        <ReturnArrow title="회원가입" />
      </div>
      <div className="div-body p-6">
        <InputText labelName="이메일" content="welcome@devday.com" />
        <InputText
          labelName="비밀번호"
          content="12자리 이상, 대문자 소문자 특수문자"
        />
        <InputText labelName="비밀번호 확인" content="위와 일치하지 않습니다" />
        <InputText labelName="이름" content="홍길동" />
        <InputText labelName="닉네임" content="Devday챌린지" />
      </div>
      <div className={classNames(`font-sans text-center sticky bottom-0 p-4`)}>
        <Button color="primary" fill label="다음으로" />
        <div className="mt-2"> 회원 가입</div>
      </div>
    </div>
  );
};

export default signup;
