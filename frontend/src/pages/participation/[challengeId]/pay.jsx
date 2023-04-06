import React, { useState } from 'react';

import { loadTossPayments } from '@tosspayments/payment-sdk';
import classNames from 'classnames';
import Image from 'next/image';
import { PropTypes } from 'prop-types';

import style from './pay.module.scss';

import http from '@/api/http';
import { Button } from '@/components/Button';
import Container from '@/components/Container';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import {
  CHALLENGE_DETAIL_URL,
  PUBLIC_TOSS_CLIENT_KEY,
  LOCALE,
  DEVDAY_ATTENDEE_TICKET,
} from '@/constants';
import { getStartWithEndDate, getWeekDiff } from '@/utils';

const pay = ({ challengeInfo }) => {
  const {
    id,
    startDate,
    endDate,
    entryFee,
    curParticipantsSize,
    backGroundUrl,
    title,
  } = challengeInfo;
  const payShot = async () => {
    const tossPayments = await loadTossPayments(PUBLIC_TOSS_CLIENT_KEY); // 회원 결제

    await tossPayments.requestPayment('카드', {
      amount: entryFee,
      orderId: Math.random().toString(36).slice(2),
      orderName: DEVDAY_ATTENDEE_TICKET,
      successUrl: `${window.location.origin}/participation/${id}/success`,
      failUrl: `${window.location.origin}/participation/${id}/fail`,
      windowTarget: 'self',
    });
  };

  const { week, day } = getWeekDiff(startDate, endDate);
  return (
    <Container>
      <Container.SubPageHeader title="참가하기" />
      <Container.MainBody className="pt-8">
        <div className="pb-8">
          <Image
            src={backGroundUrl}
            width={100}
            height={100}
            className="h-20 w-20 rounded float-left"
            alt="lock"
            loader={({ src }) => `${src}`}
          />
          <div className={classNames(style.title)}>
            <div className="ml-4">
              <p className="text-2xl">{title}</p>
              <p>
                매일, {week > 0 && `${week}주`} {day > 0 && ` ${day}일`}동안
              </p>
              <p>{getStartWithEndDate(startDate, endDate)}</p>
              <p>{`${curParticipantsSize}명 참가`}</p>
            </div>
          </div>
          <div className="mt-8">
            <p className="text-4xl">참가비</p>
            <p>적극적 참여를 유도하기 위해 참가비를 냅니다.</p>
            <div className={classNames('w-full', style.redline)}>
              <span className="text-3xl">
                {new Intl.NumberFormat(LOCALE).format(entryFee)}원
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
            <p>{new Intl.NumberFormat(LOCALE).format(entryFee)}원</p>
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

export const getServerSideProps = async context => {
  const { challengeId } = context.query;
  let challengeInfo = null;

  try {
    const { data } = await http.get(`${CHALLENGE_DETAIL_URL}/${challengeId}`);
    if (data) challengeInfo = data;
  } catch (e) {
    // console.error('챌린지 정보를 가져올 수 없습니다.', e);
  }

  return {
    props: {
      challengeInfo,
    },
  };
};

export default PrivateRouter(pay);
