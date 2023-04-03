import React, { useState, useEffect } from 'react';

import { useRouter } from 'next/router';
import http from '../../api/http';

import Container from '@/components/Container';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import { ReturnArrow } from '@/components/ReturnArrow';
import { InputText } from '@/components/InputText';

import { Button } from '@/components/Button';

const deleteAccount = () => {
  const router = useRouter();

  return (
    <Container>
      <Container.Header className="mb-10">
        <ReturnArrow title="회원 탈퇴" />
      </Container.Header>
      <Container.Body className="m-6">
        <div>
          <p>회원 탈퇴시 1주일 동안 재가입하실 수 없습니다.</p>
          <p>그래도 탈퇴하시겠습니까?</p>
        </div>
        <div className="mt-10">
          <p>
            탈퇴시 <span className="text-red-600">"탈퇴하겠습니다"</span> 라고
            기입해주세요
          </p>
          <InputText content={'탈퇴하겠습니다'} inputType={"center"}/>
        </div>
      </Container.Body>
      <Container.Footer className={'p-4'}>
        <Button label={'확인'} />
      </Container.Footer>
    </Container>
  );
};

export default PrivateRouter(deleteAccount);
