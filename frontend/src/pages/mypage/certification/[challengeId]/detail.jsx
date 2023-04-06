import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import challenge from '../../challenge';
import { useRouter } from 'next/router';
import http from '@/api/http';
import { CHALLENGE_CERTIFICATION_DETAIL } from '@/constants';

const detail = () => {
  const router = useRouter();
  const { challengeId } = router.query;

  const [challengeDetail, setChallengeDetail] = useState();

  const user = useState(state => state.user);

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

  return (
    <Container>
      <div>({user.name})</div>
    </Container>
  );
};
export default detail;
