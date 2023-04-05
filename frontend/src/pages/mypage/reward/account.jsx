import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import classNames from 'classnames';
import { useRouter } from 'next/router';

import http from '@/api/http';
import { Button } from '@/components/Button';
import Container from '@/components/Container';
import { InputBox } from '@/components/InputBox';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import { ReturnArrow } from '@/components/ReturnArrow';
import { PRIZE_WITHDRAW_URL } from '@/constants';

const account = () => {
  const router = useRouter();
  const userInfo = useSelector(state => state.user);
  const [myReward, setMyReward] = useState({});

  const [inputs, setInputs] = useState({
    bankCode: '',
    number: '',
    depositor: '',
  });

  const { bankCode, number, depositor } = inputs;

  const onChangeBankInfo = e => {
    const { value, name } = e.target;
    setInputs({
      ...inputs,
      [name]: value,
    });
  };

  const withdraw = () => {
    http.post(PRIZE_WITHDRAW_URL, {
      account: {
        bankCode,
        number,
        depositor,
      },
      money: router.query.money,
    });
  };

  return (
    <Container>
      <Container.Header className="mb-10">
        <ReturnArrow title="상금 출금" />
      </Container.Header>
      <Container.Body className="m-6">
        <div className="mb-8">
          <p className="mb-2">출금 가능 상금</p>
          <p className="text-4xl font-medium">{router.query.myReward}원</p>
        </div>
        <div className="mb-6">
          <p className="text-2xl font-medium mb-4">
            입금 받을 계좌 정보를 <br /> 입력해주세요.
          </p>
          <p className="text-red-600 text-sm">
            계좌번호 오기입으로 인해 다른 계좌로 송금 시 Dev Day에서는 책임을
            지지 않습니다. 한번더 확인 부탁드립니다.
          </p>
        </div>
        <div className="mb-5">
          <p className="text-sm mb-1">은행선택</p>
          <select
            name="bankCode"
            className="rounded-xl w-full p-2 h-11 border border-black"
            onChange={onChangeBankInfo}
          >
            <option value="none">은행을 선택해주세요.</option>
            <option value="1">농협</option>
            <option value="2">신한</option>
            <option value="3">국민</option>
          </select>
        </div>
        <div className="mb-5">
          <p className="text-sm mb-1">계좌번호</p>
          <input
            className="rounded-xl w-full p-2 h-11 border border-black"
            type="text"
            placeholder="계좌번호를 입력해주세요."
            name="number"
            onChange={onChangeBankInfo}
          />
        </div>
        <div>
          <p className="text-sm mb-1">예금주</p>
          <input
            className="rounded-xl w-full p-2 h-11 border border-black"
            type="text"
            placeholder="받으시는 분 성함을 입력해주세요."
            name="depositor"
            onChange={onChangeBankInfo}
          />
        </div>
      </Container.Body>
      <Container.Footer className="px-6 pb-2">
        <Button label="출금하기" onClick={withdraw} />
      </Container.Footer>
    </Container>
  );
};

export default PrivateRouter(account);
