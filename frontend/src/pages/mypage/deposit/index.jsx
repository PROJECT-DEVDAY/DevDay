import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import { useRouter } from 'next/router';
import Swal from 'sweetalert2';

import http from '@/api/http';
import { BtnFooter } from '@/components/BtnFooter';
import { Button } from '@/components/Button';
import Container from '@/components/Container';
import { InputText } from '@/components/InputText';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import { ReturnArrow } from '@/components/ReturnArrow';
import { SelectArrow } from '@/components/SelectArrow';
import { DEPOSIT_WITHDRAW_URL, MY_DEPOSIT_PRIZE } from '@/constants';

const index = () => {
  const router = useRouter();
  const userInfo = useSelector(state => state.user);
  const [myDeposit, setMyDeposit] = useState({});

  const headers = {
    Authorization: userInfo.accessToken,
  };

  useEffect(() => {
    http
      .get(MY_DEPOSIT_PRIZE(userInfo.userInfo.userId), {
        headers,
      })
      .then(res => {
        setMyDeposit(res.data.data);
      })
      .catch(e => {
        router.push('/user/login');
      });
  }, []);

  const [inputs, setInputs] = useState({
    money: '',
  });

  const { money } = inputs;

  const onChangeMoney = e => {
    const { value, name } = e.target;
    setInputs({
      ...inputs,
      [name]: value,
    });
  };

  const withdraw = () => {
    if (money > myDeposit.deposit) {
      Swal.fire({
        position: 'center',
        icon: 'error',
        title: '출금 가능 금액을 초과하였습니다',
        showConfirmButton: false,
        timer: 1200,
      });
      return;
    }

    http
      .post(
        DEPOSIT_WITHDRAW_URL,
        { money },
        {
          headers,
        },
      )
      .then(res => {
        console.log(res);
      })
      .catch(err => {
        console.log(err);
      });
  };

  const goToCurrentSituation = () => {
    router.push('/mypage/deposit/current-situation');
  };

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
            content="출금할 금액을 입력하세요"
            inputType="center"
            name="money"
            onChange={onChangeMoney}
          />
        </div>
        <Button label="출금하기" onClick={withdraw} />
        <div className="mt-10">
          <SelectArrow
            title="예치금 사용 현황"
            className="text-sm"
            onClick={goToCurrentSituation}
          />
          <SelectArrow title="예치금 전체 조회" className="text-sm" />
        </div>
      </Container.Body>
      <Container.Footer>
        <BtnFooter />
      </Container.Footer>
    </Container>
  );
};

export default PrivateRouter(index);
