// 유저
const USER_SERVICE = 'user-service';

export const JOIN_URL = param => `${USER_SERVICE}/join/${param}`;

export const LOGIN_URL = `${USER_SERVICE}/login`;
export const EMAIL_URL = `${USER_SERVICE}/email`;
export const CONFIRM_EMAIL_URL = `${USER_SERVICE}/confirm-email`;

export const NICKNAME_URL = `${USER_SERVICE}/nickname`;

export const MYPAGE_URL = `${USER_SERVICE}/auth`;
export const PROFILE_URL = `${USER_SERVICE}/auth/user/detail`;
export const GITHUBBAEKJOON_URL = `${USER_SERVICE}/auth/user/githubandbaekjoon`;

// 챌린지
const CHALLENGE_SERVICE = 'challenge-service';

export const CHALLENGES_URL = `${CHALLENGE_SERVICE}/auth/challenges`;
export const MY_CHALLENGES_URL = param =>
  `${CHALLENGE_SERVICE}/auth/challenges/my-challenge?status=${param}`;
export const CHALLENGES_LIST_URL = param =>
  `${CHALLENGE_SERVICE}/challenges/list?category=${param}`;
export const CHALLENGES_SEARCH_URL = `${CHALLENGE_SERVICE}/challenges`;

// 결제
const PAY_SERVICE = 'pay-service';
export const PAYMENT_CHALLENGE_SUCCESS = param =>
  `${PAY_SERVICE}/${param}/success`;
