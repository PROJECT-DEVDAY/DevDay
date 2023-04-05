import React, { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';

import { useRouter } from 'next/router';
import Swal from 'sweetalert2';

import http from '@/api/http';

import { Button } from '@/components/Button';
import Container from '@/components/Container';
import { InputText } from '@/components/InputText';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import { ReturnArrow } from '@/components/ReturnArrow';
import { DELETEUSER_URL } from '@/constants';

const deleteAccount = () => {
  const router = useRouter();
  const userInfo = useSelector(state => state.user);

  const headers = {
    Authorization: userInfo.accessToken,
  };

  const [inputs, setInputs] = useState({
    deleteCheck: '',
  });

  const { deleteCheck } = inputs;

  const onChangeId = e => {
    const { value, name } = e.target;
    setInputs({
      ...inputs,
      [name]: value,
    });
  };

  const deleteUser = () => {
    if (deleteCheck !== '탈퇴하겠습니다') {
      Swal.fire({
        position: 'center',
        icon: 'error',
        title: '탈퇴 문구가 일치하지 않습니다.',
        showConfirmButton: false,
        timer: 1200,
      });
      return;
    }

    http
      .delete(DELETEUSER_URL, { headers })
      .then(res => {
        router.push('/user/login');
      })
      .catch(err => {
        Swal.fire({
          position: 'center',
          icon: 'error',
          title: '로그인되지 않았습니다.',
          showConfirmButton: false,
          timer: 1200,
        });
        router.push('/user/login');
      });
  };

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
          <InputText
            content="탈퇴하겠습니다"
            inputType="center"
            name="deleteCheck"
            onChange={onChangeId}
          />
        </div>
      </Container.Body>
      <Container.Footer className="p-4">
        <Button label="확인" onClick={deleteUser} />
      </Container.Footer>
    </Container>
  );
};

export default PrivateRouter(deleteAccount);
