import React from 'react';

import { useRouter } from 'next/router';

import { PROFILE_URL } from '@/constants';

import { ReturnArrow } from '@/components/ReturnArrow';
import { SelectArrow } from '@/components/SelectArrow';
import { UserAvatar } from '@/components/UserAvatar';

const profile = () => {
  const router = useRouter();

  const profileInfo = http.get(PROFILE_URL, {
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
    localStorage.removeItem('refreshToken');
    router.push('/user/login');
  };

  return (
    <div>
      <div className="style.div-header sticky top-0">
        <ReturnArrow title="프로필" />
      </div>
      <div className="div-body pt-6 pb-4">
        <div className="pt-20 flex justify-center">
          <UserAvatar imageURL={profileInfo.profileImgUrl} width={150} height={150} />
        </div>
        <p className="text-center font-medium mb-10">{profileInfo.nickname}</p>
      </div>
      <hr className="border-gray-400 w-full" />
      <div className="div-body p-6">
        <SelectArrow title="개인 정보 설정" onClick={privateInfo} />
        <SelectArrow
          title="챌린지 인증서 목록"
          onClick={challengeCertification}
        />
        <SelectArrow title="로그아웃" color onClick={logout} />
      </div>
    </div>
  );
};

export default profile;
