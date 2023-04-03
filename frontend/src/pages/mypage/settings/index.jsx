import React from 'react';

import { useRouter } from 'next/router';

import Container from '@/components/Container';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import { ReturnArrow } from '@/components/ReturnArrow';
import { SelectArrow } from '@/components/SelectArrow';

const index = () => {
  const router = useRouter();

  const goToChangeNickName = () => {
    router.push('/mypage/settings/nickname');
  };

  const goToChangePassword = () => {
    router.push('/mypage/settings/password');
  };

  const goToChangeChallengeInfo = () => {
    router.push('/mypage/settings/challengeInfo');
  };

  const logout = () => {};

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
<<<<<<< HEAD
        <SelectArrow title="로그아웃" color onClick="" />
=======
        <SelectArrow title="로그아웃" color onClick={logout} />
>>>>>>> e8baa8cf67226d9e9a039e40e73e97baae225852
        <SelectArrow title="회원탈퇴" color onClick={goToDeleteAccount} />
      </Container.Body>
    </Container>
  );
};

export default PrivateRouter(index);
