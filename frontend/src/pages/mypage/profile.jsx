import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { useRouter } from 'next/router';
import Swal from 'sweetalert2';

import http from '@/api/http';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import { ReturnArrow } from '@/components/ReturnArrow';
import { SelectArrow } from '@/components/SelectArrow';
import { UserAvatar } from '@/components/UserAvatar';
import { PROFILE_URL } from '@/constants';
import { persistor } from '@/pages/_app';
import { reset } from '@/store/user/userSlice';

const profile = () => {
  const router = useRouter();
  const dispatch = useDispatch();

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
      .catch(e => {
        router.push('/user/login');
      });
  }, []);

  const privateInfo = () => {
    router.push('/mypage/settings');
  };

  const challengeCertification = () => {
    router.push('');
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
          Swal.fire({
            icon: 'error',
            title: '실패!',
            text: '로그인에 실패했어요',
          });
        }
      }
    });
  };

  return (
    <div>
      <div className="style.div-header sticky top-0">
        <ReturnArrow title="프로필" />
      </div>
      <div className="div-body pt-6 pb-4">
        <div className="pt-20 flex justify-center">
          <UserAvatar
            imageURL={profileInfo.profileImg}
            width={150}
            height={150}
          />
        </div>
        <p className="text-center text-xl font-medium mt-4 mb-10">
          {profileInfo.nickname}
        </p>
      </div>
      <hr className="border-gray-400 w-full" />
      <div className="div-body p-6">
        <SelectArrow title="개인 정보 설정" onClick={privateInfo} />
        <SelectArrow
          title="챌린지 인증서 목록"
          onClick={challengeCertification}
        />
        <SelectArrow title="로그아웃" color onClick={onClickLogout} />
      </div>
    </div>
  );
};

export default PrivateRouter(profile);
