import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { useRouter } from 'next/router';

import http from '../api/http';

import { ReturnArrow } from '@/components/ReturnArrow';
import { SelectArrow } from '@/components/SelectArrow';
import { UserAvatar } from '@/components/UserAvatar';
import { PROFILE_URL } from '@/constants';

const profile = () => {
  const router = useRouter();
  const userInfo = useSelector(state => state.user);
  const [profileInfo, setProfileInfo] = useState({});

  const headers = {
    Authorization: userInfo.accessToken,
  };

  useEffect(() => {
    http
      .get(PROFILE_URL, {
        headers,
      })
      .then(res => {
        setProfileInfo(res.data.data);
      })
      .catch(e => {});
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
          <UserAvatar
            imageURL={profileInfo.profileImgUrl}
            width={150}
            height={150}
          />
        </div>
        <p className="text-center text-xl font-medium mt-4 mb-10">{profileInfo.nickname}</p>
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
