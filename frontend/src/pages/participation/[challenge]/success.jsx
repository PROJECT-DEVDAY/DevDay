import React from 'react';

import classNames from 'classnames';
import Image from 'next/image';

import style from './success.module.scss';

import { Button } from '@/components/Button';
import { ReturnArrow } from '@/components/ReturnArrow';

const success = props => {
  return (
    <div>
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
        <Button label="참여하기" />
      </div>
    </div>
  );
};

export default success;
