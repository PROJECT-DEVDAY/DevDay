import React from 'react';

import { useRouter } from 'next/router';
import classNames from 'classnames';

import Footer from '@/components/Footer';
import { HeaderButtons } from '@/components/HeaderButtons';
import { ReturnArrow } from '@/components/ReturnArrow';

import style from './challenge.module.scss';

const challenge = () => {
  const router = useRouter();

  return (
    <div>
      <div className="style.div-header sticky top-0">
        <ReturnArrow title="진행중인 챌린지" />
      </div>
      <div className="px-4">
        <HeaderButtons />
      </div>

      <div className={classNames("m-4 p-2 rounded-2xl bg-white flex", style.boxShadow)}>
        <div className={classNames('w-1/2 text-center', style.boundary)}>
          <p className='text-sm'>챌린지</p>
          <p className='font-medium'>3개</p>
          {/* <p className='font-medium'>{}개</p> */}
        </div>
        <div className='w-1/2 text-center'>
          <p className='text-sm'>참여율</p>
          <p className='font-medium'>3개</p>
          {/* <p className='font-medium'>{}개</p> */}
        </div>
      </div>

      <div className="sticky w-full bottom-0 m-0">
        <Footer />
      </div>
    </div>
  );
};

export default challenge;
