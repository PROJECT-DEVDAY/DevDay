import React, { useState } from 'react';

import { loadTossPayments } from '@tosspayments/payment-sdk';
import classNames from 'classnames';
import Image from 'next/image';
import { useRouter } from 'next/router';
import { PropTypes } from 'prop-types';

import style from './pay.module.scss';

import { Button } from '@/components/Button';
import { CheckBoxBtn } from '@/components/CheckBoxBtn';
import Container from '@/components/Container';
import { ReturnArrow } from '@/components/ReturnArrow';

const pay = ({ challengeInfo }) => {
  const router = useRouter();
  const [loading, setLoading] = useState(true);
  const { challenge } = router.query;

  const payShot = async () => {
    const tossPayments = await loadTossPayments(
      process.env.NEXT_PUBLIC_TOSS_CLIENT_KEY,
    ); // 회원 결제

    await tossPayments.requestPayment('카드', {
      amount: challengeInfo.entryFee,
      orderId: Math.random().toString(36).slice(2),
      orderName: process.env.NEXT_PUBLIC_DEVDAY_ENTRY_FEE,
      successUrl: `${window.location.origin}/participation/${challengeInfo.id}/success`,
      failUrl: `${window.location.origin}/participation/${challengeInfo.id}/fail`,
      windowTarget: 'self',
    });
  };

  return (
    <Container>
      <Container.SubPageHeader title="참가하기" />
      <Container.MainBody className="pt-8">
        <div className="pb-8">
          <Image
            src={require('@/image/lock.png')}
            className="h-16 w-16 rounded float-left"
            alt="lock"
          />
          <div className={classNames(style.title)}>
            <div className="ml-4">
              <p className="text-2xl">{challengeInfo.title}</p>
              <p>매일, 2주 동안</p>
              <p>03.20(월) ~ 04.02(일)</p>
            </div>
            <div>
              <p>{`${challengeInfo.participantCount}명 참가`}</p>
            </div>
          </div>
          <div className="mt-8">
            <p className="text-4xl">참가비</p>
            <p>적극적 참여를 유도하기 위해 참가비를 냅니다.</p>
            <div className={classNames('w-full', style.redline)}>
              <span className="text-3xl">
                {new Intl.NumberFormat(process.env.NEXT_PUBLIC_LOCALE, {
                  maximumSignificantDigits: 3,
                }).format(challengeInfo.entryFee)}
                원
              </span>
              <span>(고정)</span>
            </div>
          </div>
        </div>
        <div className={classNames('mb-8 p-4', style.alpha)}>
          <p>매일 챌린지 결과에 따라 상금이 차등 지급됩니다.</p>
          <p>빠짐없이 챌린지를 하신다면</p>
          <p>참가비+α를 받을 수 있을거예용</p>
        </div>
        <div>
          <p className="text-3xl">참가비 결제</p>
          <div className={style.spacebet}>
            <p>결제 금액</p>
            <p>1,000원</p>
          </div>
        </div>
      </Container.MainBody>
      <Container.Footer className="flex items-center flex-col border-t-2 p-4 z-30 bg-white">
        <span className="mb-2">결제 조건 및 서비스 약관에 동의합니다.</span>
        <Button label="인증하기" onClick={payShot} />
      </Container.Footer>
    </Container>
  );
};

pay.defaultProps = {
  challengeInfo: {
    id: 1,
    title: '1일 1알고리즘',
    participantCount: 50,
    startDate: new Date(),
    endDate: new Date(),
    entryFee: 1000,
  },
};

pay.propTypes = {
  challengeInfo: PropTypes.shape({
    title: PropTypes.string,
    participantCount: PropTypes.number,
  }),
};

export default pay;
