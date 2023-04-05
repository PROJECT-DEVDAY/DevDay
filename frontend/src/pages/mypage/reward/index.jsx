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
import { MY_DEPOSIT_PRIZE } from '@/constants';

const index = () => {
  const router = useRouter();
  const userInfo = useSelector(state => state.user);
  const [myReward, setMyReward] = useState({});

  const headers = {
    Authorization: userInfo.accessToken,
  };

  useEffect(() => {
    http
      .get(MY_DEPOSIT_PRIZE(userInfo.userInfo.userId), {
        headers,
      })
      .then(res => {
        setMyReward(res.data.data);
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

  const next = () => {
    if (money > myReward.prize) {
      Swal.fire({
        position: 'center',
        icon: 'error',
        title: '출금 가능 금액을 초과하였습니다',
        showConfirmButton: false,
        timer: 1200,
      });
      return;
    }

    router.push({
      pathname: '/mypage/reward/account',
      query: {
        money,
        myReward: myReward.prize,
      },
    });
  };

  return (
    <Container>
      <Container.Header className="mb-10">
        <ReturnArrow title="상금 조회" />
      </Container.Header>
      <Container.Body className="m-6">
        <div className="mb-10">
          <p className="mb-2">출금 가능 상금</p>
          <p className="text-4xl font-medium">{myReward.prize}원</p>
        </div>
        <div>
          <InputText
            content="출금할 금액을 입력하세요"
            inputType="center"
            name="money"
            onChange={onChangeMoney}
          />
        </div>
        <p className="mb-4 text-sm text-right text-red-500">
          상금은 최소 5,000원부터 출금 가능해요!
        </p>
        <Button label="다음" onClick={next} />
        <div className="mt-10">
          <SelectArrow title="상금 사용 현황" className="text-sm" />
          <SelectArrow title="상금 전체 조회" className="text-sm" />
        </div>
      </Container.Body>
      <Container.Footer>
        <BtnFooter />
      </Container.Footer>
    </Container>
  );
};

export default PrivateRouter(index);
