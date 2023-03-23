import React from 'react';

import style from './signup.module.scss';
import { Button } from '../../components/Button';
import { InputText } from '../../components/InputText';
import { ReturnArrow } from '../../components/ReturnArrow';

const signup = props => {
  return (
    <div>
      <div className="div-header sticky top-0 bg-white">
        <ReturnArrow title="회원가입" />
      </div>
      <div className="div-body p-6">
        <InputText
          className="style.redInput"
          labelName="이메일"
          content="welcome@devday.com"
        />
        <InputText
          labelName="비밀번호"
          content="12자리 이상, 대문자 소문자 특수문자"
        />
        <InputText labelName="비밀번호 확인" content="위와 일치하지 않습니다" />
        <InputText labelName="이름" content="홍길동" />
        <InputText labelName="닉네임" content="Devday챌린지" />
      </div>
      <div className="div-footer text-center sticky bottom-0 p-4 bg-white">
        <Button color="primary" fill label="다음으로" />
        <span>회원 가입</span>
      </div>
    </div>
  );
};

export default signup;
