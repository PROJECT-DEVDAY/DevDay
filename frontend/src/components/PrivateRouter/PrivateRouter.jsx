import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';

import { reset } from '@/store/user/userSlice';
import jwtDecode from 'jwt-decode';
import { useRouter } from 'next/router';
import Swal from 'sweetalert2';

const Toast = Swal.mixin({
  toast: true,
  position: 'top',
  showConfirmButton: false,
  timer: 2000,
  timerProgressBar: true,
  didOpen: toast => {
    toast.addEventListener('mouseenter', Swal.stopTimer);
    toast.addEventListener('mouseleave', Swal.resumeTimer);
  },
});

const PrivateRouter = WrappedComponent => {
  const Auth = props => {
    const user = useSelector(state => state.user);
    const router = useRouter();
    const dispatch = useDispatch();

    useEffect(() => {
      const loginRedirect = () => {
        if (user?.accessToken) {
          // 토큰 검증
          const { exp } = jwtDecode(user.accessToken);
          const expireDate = new Date(exp * 1000);
          if (expireDate <= new Date()) {
            Toast.fire({
              icon: 'warning',
              title: '인증기간이 만료되었습니다. 다시 로그인을 해주세요.',
            });
            dispatch(reset());
            return router.replace('/user/login');
          }

          // 정상적이라면 넘어가기
          return;
        }
        // 토근이 없다면 로그인 화면
        Toast.fire({
          icon: 'warning',
          title: '로그인 후 이용가능합니다.',
        });

        dispatch(reset());
        router.replace('/user/login');
      };

      loginRedirect();
    }, [user]);

    return <WrappedComponent {...props} />;
  };
  return Auth;
};
export default PrivateRouter;
