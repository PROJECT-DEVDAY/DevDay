import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import classNames from 'classnames';
import Image from 'next/image';
import Link from 'next/link';
import { useRouter } from 'next/router';

import http from '@/api/http';
import { Button } from '@/components/Button';
import Container from '@/components/Container';
import { PAYMENT_CHALLENGE_SUCCESS, CHALLENGE_JOIN_URL } from '@/constants';

const success = ({ challengeId, paymentInfo }) => {
  const [loading, setLoading] = useState(true);
  const user = useSelector(state => state.user);
  const router = useRouter();

  const payConfirm = async (nickname, accessToken) => {
    let hasError = false;
    try {
      const { data } = await http.post(PAYMENT_CHALLENGE_SUCCESS(challengeId), {
        paymentInfo,
        nickname,
      });
      const { approve } = data;
    } catch (e) {
      console.error(e);
      hasError = true;
    }
    if (hasError) {
      //  return router.replace(`/participation/${challengeId}/fail`);
    }
    // approve가 false 일 때, 처리할 것
    setLoading(false);
    const datas = {
      challengeRoomId: challengeId,
      nickname,
    };
    http.post(CHALLENGE_JOIN_URL, datas);
  };

  useEffect(() => {
    payConfirm(user.userInfo.nickname, user.accessToken);
  }, [user]);

  return (
    <Container>
      <Container.SubPageHeader
        title={loading ? '결제중...' : '결제 완료'}
        disableBefore
      />
      <Container.MainBody>
        {loading ? (
          <Image
            src={require('@/image/loading.gif')}
            alt="loading"
            className="w-20 h-20 ml-auto mr-auto mt-20"
          />
        ) : (
          <>
            <Image
              src={require('@/image/devdaying.gif')}
              className="w-full p-6 pt-16"
              alt="man"
            />
            <div className="text-center font-bold text-4xl">
              <p>결제 완료</p>
              <p className="mt-4">한번 가보자구</p>
            </div>
            <div className="p-16">
              <Link href={`/challenge/${challengeId}`}>
                <Button label="참여하기" />
              </Link>
            </div>
          </>
        )}
      </Container.MainBody>
    </Container>
  );
};

// 서버사이드 렌더링 시, API를 통해 요청해옴
export const getServerSideProps = async context => {
  const { challengeId } = context.params;
  const paymentInfo = context.query;

  return {
    props: {
      paymentInfo,
      challengeId,
    },
  };
};

export default success;
