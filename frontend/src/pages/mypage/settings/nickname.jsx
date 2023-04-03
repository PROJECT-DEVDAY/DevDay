import React from 'react';

import { useRouter } from 'next/router';

import Container from '@/components/Container';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import { ReturnArrow } from '@/components/ReturnArrow';
import { InputLabel } from '@/components/InputLabel';
import { InputText } from '@/components/InputText';
import { Button } from '@/components/Button';

const nickname = () => {
  const router = useRouter();

  return (
    <Container>
      <Container.Header className="mb-10">
        <ReturnArrow title="닉네임 변경" />
      </Container.Header>
      <Container.Body className="m-6">
        <div className="mt-8 mb-4">
          <InputLabel content={'새 닉네임'} />
        </div>
        <InputText content={'홍길동'} inputType={"iconText"} icon={"중복확인"} onClick={""}/>
        <div className="mt-8 mb-4">
          <InputLabel content={'이메일'} />
        </div>
        <InputText content={'welcome@devday.com'} />
        <div className="mt-8 mb-4">
          <InputLabel content={'비밀번호'} />
        </div>
        <InputText content={'12자리 이상, 대문자, 소문자, 특수문자 포함'} />
      </Container.Body>
      <Container.Footer className={"p-4"}>
        <Button label={"확인"}/>
      </Container.Footer>
    </Container>
  );
};

export default PrivateRouter(nickname);
