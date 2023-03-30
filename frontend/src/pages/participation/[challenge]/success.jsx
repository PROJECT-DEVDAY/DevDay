import React, { useEffect, useState } from 'react';

import axios from 'axios';
import classNames from 'classnames';
import Image from 'next/image';
import Link from 'next/link';
import { useRouter } from 'next/router';

import style from './success.module.scss';

import { Button } from '@/components/Button';
import { ReturnArrow } from '@/components/ReturnArrow';

const success = ({ result, hasError }) => {
  const router = useRouter();
  const { challenge } = router.query;

  return (
    <div className="font-medium">
      <div className={classNames(`style.div-header`, `sticky top-0`)}>
        <ReturnArrow title="참가하기" />
      </div>
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
        <Link href={`/participation/${challenge}`}>
          <Button label="참여하기" />
        </Link>
      </div>
    </div>
  );
};

// 서버사이드 렌더링 시, API를 통해 요청해옴
export const getServerSideProps = async context => {
  const { challenge } = context.params;
  let hasError = false;

  try {
    const { data } = await axios.get(
      `http://localhost:8003/payments/${challenge}/success`,
      {
        headers: {
          userId: 1,
        },
        params: context.query,
      },
    );
  } catch (e) {
    hasError = true;
  }
  return {
    props: {
      hasError,
    },
    ...(hasError && {
      redirect: {
        permanent: true,
        destination: 'fail',
      },
    }),
  };
};

export default success;
