import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import { useRouter } from 'next/router';
import Swal from 'sweetalert2';

import http from '../../api/http';

import Container from '@/components/Container';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import { ReturnArrow } from '@/components/ReturnArrow';
import { GET_MY_DEPOSIT_URL } from '@/constants';

const GUTTER_SIZE = 10;

const currentSituation = () => {
  const router = useRouter();
  const userInfo = useSelector(state => state.user);
  const [myDepositInfo, setMyDepositInfo] = useState({});

  const headers = {
    Authorization: userInfo.accessToken,
  };

  useEffect(() => {
    http
      .get(GET_MY_DEPOSIT_URL, {
        headers,
      })
      .then(res => {
        setMyDepositInfo(res.data.data);
      })
      .catch(e => {
        router.push('/user/login');
      });
  }, []);

  return (
    <Container>
      <Container.Header className="mb-10">
        <ReturnArrow title="예치금 사용 현황" />
      </Container.Header>
    </Container>
  );
};

export default PrivateRouter(currentSituation);
