import React, { useEffect, useState } from 'react';

import { useSelector } from 'react-redux';
import challenge from '../../challenge';
import { useRouter } from 'next/router';
import http from '@/api/http';
import Container from '@/components/Container';
import { CHALLENGE_CERTIFICATION_DETAIL } from '@/constants';
import { Button } from '@/components/Button';

export const detail = () => {
  const router = useRouter();
  const { challengeId } = router.query;

  const [challengeDetail, setChallengeDetail] = useState();

  const user = useSelector(state => state.user);

  useEffect(() => {
    if (challengeId) {
      http
        .post(`${CHALLENGE_CERTIFICATION_DETAIL}/${challengeId}`)
        .then(res => {
          setChallengeDetail(res.data);
        })
        .catch(error => {
          console.error(error);
        });
    }
  }, []);

  const SaveButton = () => {
    console.log(challengeDetail);
  };

  return (
    <Container>
      <Container.MainHeader>
        <div>{user.name}</div>
      </Container.MainHeader>

      <Container.MainFooter>
        <Button label={'저장하기'}></Button>
      </Container.MainFooter>
    </Container>
  );
};

export default detail;
