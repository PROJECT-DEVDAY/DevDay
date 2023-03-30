import React from 'react';

import { useRouter } from 'next/router';

import { ReturnArrow } from '@/components/ReturnArrow';
import { UserAvatar } from '@/components/UserAvatar';
import { SelectArrow } from '@/components/SelectArrow';
import http from '@/pages/api/http';
import { PROFILE_URL } from '../api/constants';

const profile = props => {
  const router = useRouter();

  const user = http.get(PROFILE_URL, {
    Authorization: localStorage.getItem('accessToken'),
  });

  const privateInfo = () => {
    router.push('');
  };

  const challengeCertification = () => {
    router.push('');
  };

  const logout = () => {
    localStorage.removeItem('accessToken');
    router.push('/user/login');
  };

  return (
    <div>
      <div className={`style.div-header sticky top-0`}>
        <ReturnArrow title="프로필" />
      </div>
      <div className="div-body pt-6 pb-4">
        <div className="pt-20 flex justify-center">
          <UserAvatar imageURL={user.profileImgUrl} />
        </div>
        <p className="text-center font-medium mb-10">{user.nickname}</p>
      </div>
      <hr className="border-gray-400 w-full" />
      <div className="div-body p-6">
        <SelectArrow title={'개인 정보 설정'} onClick={privateInfo}/>
        <SelectArrow title={'챌린지 인증서 목록'} onClick={challengeCertification}/>
        <SelectArrow title={'로그아웃'} color={true} onClick={logout}/>
      </div>
    </div>
  );
};

export default profile;
