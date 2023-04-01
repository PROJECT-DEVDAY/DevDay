import React from 'react';

import { useRouter } from 'next/router';
import { useSelector } from 'react-redux';

export const PrivateRouter = ({ childeren }) => {
  const router = useRouter();
  const isLoggedIn = useSelector(state => state.user.token);
  if (!isLoggedIn) {
    router.push('/user/login');
    return null;
  }
  return childeren;
};
