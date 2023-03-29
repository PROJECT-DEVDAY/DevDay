import axios from 'axios';

axios.defaults.withCredentials = true;
// axios 객체 생성
export default axios.create({
  baseURL: 'http://localhost:8000/',
  headers: {
    'Content-Type': 'application/json;charset=utf-8',
  },
});
