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
  const [hasError, setHasError] = useState(null);

  const payConfirm = async (nickname, accessToken) => {
    let error = false;
    try {
      const { data } = await http.post(PAYMENT_CHALLENGE_SUCCESS(challengeId), {
        paymentInfo,
        nickname,
      });
      const { approve, message } = data.data;
      if (!approve) {
        error = {
          code: '0000',
          message,
        };
      }
    } catch (e) {
      if (e.response.data) {
        error = {
          ...e.response.data,
        };
      }
    }
    // approve가 false 일 때, 처리할 것
    setLoading(false);
    setHasError(error);
  };

  useEffect(() => {
    payConfirm(user.userInfo.nickname, user.accessToken);
  }, [user]);

  if (hasError) {
    return (
      <Container>
        <Container.SubPageHeader title="결제 실패" disableBefore />
        <Container.MainBody>
          {hasError && (
            <div>
              <h2 className="text-2xl font-bold my-8">결제에 실패했습니다.</h2>
              <p className="text-lg">{hasError.message}</p>
              <p className="text-sm my-4">CODE: {hasError.code}</p>
            </div>
          )}
        </Container.MainBody>
        <Container.MainFooter className="px-4 py-4">
          <div className="gap-2 grid grid-cols-2">
            <Link href="/">
              <Button color="danger" label="메인으로" />
            </Link>
            <Link href={`/participation/${challengeId}/pay`}>
              <Button label="다시 결제하기" />
            </Link>
          </div>
        </Container.MainFooter>
      </Container>
    );
  }

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
  console.log(challengeId, paymentInfo);
  return {
    props: {
      paymentInfo,
      challengeId,
    },
  };
};

export default success;
