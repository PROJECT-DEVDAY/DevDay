import React, { useState, useEffect } from 'react';

import { useRouter } from 'next/router';

import http from '../../api/http';

import { Button } from '@/components/Button';
import Container from '@/components/Container';
import { InputLabel } from '@/components/InputLabel';
import { InputText } from '@/components/InputText';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import { ReturnArrow } from '@/components/ReturnArrow';
import { PASSWORD_URL } from '@/constants';

const password = () => {
  const router = useRouter();

  return (
    <Container>
      <Container.Header className="mb-10">
        <ReturnArrow title="비밀번호 수정" />
      </Container.Header>
      <Container.Body className="m-6">
        <div className="mt-8 mb-4">
          <InputLabel content="현재 비밀번호" />
        </div>
        <InputText content="12자리 이상, 대문자, 소문자, 특수문자 포함" />
        <div className="mt-8 mb-4">
          <InputLabel content="새 비밀번호" />
        </div>
        <InputText content="12자리 이상, 대문자, 소문자, 특수문자 포함" />
        <div className="mt-8 mb-4">
          <InputLabel content="비밀번호 확인" />
        </div>
        <InputText content="12자리 이상, 대문자, 소문자, 특수문자 포함" />
      </Container.Body>
      <Container.Footer className="p-4">
        <Button label="확인" />
      </Container.Footer>
    </Container>
  );
};

export default PrivateRouter(password);
