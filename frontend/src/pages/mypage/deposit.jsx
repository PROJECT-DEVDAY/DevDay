import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useRouter } from 'next/router';

import http from '../api/http';
import { MY_DEPOSIT_PRIZE } from '@/constants';

import Container from '@/components/Container';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import { ReturnArrow } from '@/components/ReturnArrow';
import { InputText } from '@/components/InputText';

import { Button } from '@/components/Button';
import { SelectArrow } from '@/components/SelectArrow';
import { BtnFooter } from '@/components/BtnFooter';

const deposit = () => {
  const router = useRouter();
  const userInfo = useSelector(state => state.user);
  const [myDeposit, setMyDeposit] = useState({});

  const headers = {
    Authorization: userInfo.accessToken,
  };

  useEffect(() => {
    http
      .get(MY_DEPOSIT_PRIZE(userInfo.userInfo.userId), {
        headers
      })
      .then(res => {
        setMyDeposit(res.data.data);
      })
      .catch(e => {
        router.push('/user/login');
      });
  }, []);

  return (
    <Container>
      <Container.Header className="mb-10">
        <ReturnArrow title="보유 예치금" />
      </Container.Header>
      <Container.Body className="m-6">
        <div className="mb-10">
          <p className="mb-2">출금 가능 예치금</p>
          <p className="text-4xl font-medium">{myDeposit.deposit}원</p>
        </div>
        <div className="mb-4">
          <InputText
            content={'출금할 금액을 입력하세요'}
            inputType={'center'}
          />
        </div>
        <Button label={'확인'} />
        <div className="mt-10">
          <SelectArrow title={'예치금 사용 현황'} className={'text-sm'} />
          <SelectArrow title={'예치금 전체 조회'} className={'text-sm'} />
        </div>
      </Container.Body>
      <Container.Footer>
        <BtnFooter />
      </Container.Footer>
    </Container>
  );
};

export default PrivateRouter(deposit);
