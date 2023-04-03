import React, { useState, useEffect } from 'react';

import { useRouter } from 'next/router';
import http from '../../api/http';

import { NICKNAME_URL } from '@/constants';

import { Button } from '@/components/Button';
import Container from '@/components/Container';
import { InputLabel } from '@/components/InputLabel';
import { InputText } from '@/components/InputText';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import { ReturnArrow } from '@/components/ReturnArrow';

const nickname = () => {
  const router = useRouter();
  const [nickNameValidCheck, setNickNameValidCheck] = useState(false);
  const [nickNameDuplicatedChk, setNickNameDuplicatedChk] = useState(false);

  // nickname check logic
  const onClickDuplicateCheck = async () => {
    try {
      await http.post(NICKNAME_URL, { nickn: watch('nickname') });

      setNickNameDuplicatedChk(true);
      setNickNameValidCheck(prev => !prev);
    } catch (error) {
      setNickNameDuplicatedChk(false);
    }
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
        <InputText
          content={'홍길동'}
          inputType={'iconText'}
          icon={'중복확인'}
          onClick={onClickDuplicateCheck}
        />
        <div className="mt-8 mb-4">
          <InputLabel content="이메일" />
        </div>
        <InputText content="welcome@devday.com" />
        <div className="mt-8 mb-4">
          <InputLabel content="비밀번호" />
        </div>
        <InputText content="12자리 이상, 대문자, 소문자, 특수문자 포함" />
      </Container.Body>
      <Container.Footer className={'p-4'}>
        <Button label={'확인'} />
      </Container.Footer>
    </Container>
  );
};

export default PrivateRouter(nickname);
