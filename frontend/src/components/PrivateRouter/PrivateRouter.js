import { useRouter } from 'next/router';
import React from 'react';
import { useSelector } from 'react-redux';
import { Route, Redirect } from 'react-router-dom';

export const PrivateRouter = ({ childeren }) => {
  const router = useRouter();
  const isLoggedIn = useSelector(state => state.user.token);
  if (!isLoggedIn) {
    router.push('/user/login');
    return null;
  }
  return childeren;
};
