import React, { useEffect } from 'react';
import { useSelector } from 'react-redux';

import Swal from 'sweetalert2';

const PrivateRouter = WrappedComponent => {
  const Auth = props => {
    const user = useSelector(state => state.user);
    useEffect(() => {
      const loginRedirect = async () => {
        if (!user.accessToken) {
          // 이거 끝날떄까지 대기
          // await Router.push('/user/login');
          return window.location.replace('/user/login');

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

          Toast.fire({
            icon: 'warning',
            title: '로그인 후 이용가능합니다.',
          });
        }
      };

      loginRedirect();
    }, [user]);

    return <WrappedComponent {...props} />;
  };
  return Auth;
};
export default PrivateRouter;
