import React from 'react';

import { useRouter } from 'next/router';
import { useDispatch } from 'react-redux';

import Container from '@/components/Container';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import { ReturnArrow } from '@/components/ReturnArrow';
import { SelectArrow } from '@/components/SelectArrow';

import { reset } from '@/store/user/userSlice';

import { persistor } from '@/pages/_app';
import Swal from 'sweetalert2';

const index = () => {
  const router = useRouter();

  const dispatch = useDispatch();

  const goToChangeNickName = () => {
    router.push('/mypage/settings/nickname');
  };

  const goToChangePassword = () => {
    router.push('/mypage/settings/password');
  };

  const goToChangeChallengeInfo = () => {
    router.push('/mypage/settings/challengeInfo');
  };

  const onClickLogout = async () => {
    Swal.fire({
      title: '로그아웃 하실 건가요?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: '네',
      cancelButtonText: '아니요',
    }).then(async result => {
      if (result.isConfirmed) {
        try {
          dispatch(reset());
          await persistor.purge();
        } catch (e) {
          console.log(e);
          Swal.fire({
            icon: 'error',
            title: '실패!',
            text: '로그인에 실패했어요',
          });
        }
      }
    });
  };

  const goToDeleteAccount = () => {
    router.push('/mypage/settings/delete-account');
  };

  return (
    <Container>
      <Container.Header className="mb-10">
        <ReturnArrow title="설정" />
      </Container.Header>
      <Container.Body className="m-6">
        <SelectArrow title="닉네임 변경" onClick={goToChangeNickName} />
        <SelectArrow title="비밀번호 변경" onClick={goToChangePassword} />
        <SelectArrow
          title="Github, Solved.ac 계정 설정"
          onClick={goToChangeChallengeInfo}
        />
        <SelectArrow title="로그아웃" color onClick={onClickLogout} />
        <SelectArrow title="회원탈퇴" color onClick={goToDeleteAccount} />
      </Container.Body>
    </Container>
  );
};

export default PrivateRouter(index);
