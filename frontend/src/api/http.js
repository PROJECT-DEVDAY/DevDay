import axios from 'axios';

import { TOKEN_REFRESH_URL } from '@/constants';

axios.defaults.withCredentials = true;

// axios 객체 생성
const http = axios.create({
  baseURL: 'http://j8c209.p.ssafy.io:8000/',
  headers: {
    'Content-Type': 'application/json;charset=utf-8',
  },
});

// accessToken이 있을 경우 처리 headers에 삽입
http.interceptors.request.use(
  config => {
    if (typeof window !== 'undefined') {
      const accessToken = localStorage.getItem('accessToken');

      if (config.headers && accessToken) {
        config.headers.Authorization = accessToken;
        return config;
      }
    }
    return config;
  },
  err => {
    return Promise.reject(err);
  },
);

http.interceptors.response.use(
  response => response,
  async err => {
    if (err.response && err.response.status == 402) {
      const refreshToken = localStorage.getItem('refreshToken');
      const accessToken = localStorage.getItem('accessToken');
      try {
        const { data } = await http.get(TOKEN_REFRESH_URL, {
          headers: {
            Authorization: accessToken,
            RefreshToken: refreshToken,
          },
        });
        localStorage.setItem('accessToken', data?.accessToken || '');
        localStorage.setItem('refreshToken', data?.refreshToken || '');
        return http.request(err.config);
      } catch (ex) {
        console.error(ex);
        // 현재 가지고 있던 토큰 지우기
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        window.location.href = '/user/login';
        return Promise.reject(err);
      }
    }
  },
);

const httpForm = axios.create({
  baseURL: 'http://j8c209.p.ssafy.io:8000/',
  headers: {
    'Content-Type': 'multipart/form-data',
  },
});

// accessToken이 있을 경우 처리 headers에 삽입
httpForm.interceptors.request.use(
  config => {
    if (typeof window !== 'undefined') {
      const accessToken = localStorage.getItem('accessToken');

      if (config.headers && accessToken) {
        config.headers.Authorization = accessToken;
        return config;
      }
    }
    return config;
  },
  err => {
    return Promise.reject(err);
  },
);

httpForm.interceptors.response.use(
  response => response,
  async err => {
    if (err.response && err.response.status == 402) {
      const refreshToken = localStorage.getItem('refreshToken');
      const accessToken = localStorage.getItem('accessToken');
      try {
        const { data } = await http.get(TOKEN_REFRESH_URL, {
          headers: {
            Authorization: accessToken,
            RefreshToken: refreshToken,
          },
        });
        localStorage.setItem('accessToken', data?.accessToken || '');
        localStorage.setItem('refreshToken', data?.refreshToken || '');
        return httpForm.request(err.config);
      } catch (ex) {
        console.error(ex);
        // 현재 가지고 있던 토큰 지우기
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        window.location.href = '/user/login';
        return Promise.reject(err);
      }
    }
  },
);

export { httpForm };
export default http;
