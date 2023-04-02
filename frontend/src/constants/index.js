// 유저
const USER_SERVICE = 'user-service';

export const JOIN_URL = param => `${USER_SERVICE}/join/${param}`;

export const LOGIN_URL = `${USER_SERVICE}/login`;
export const EMAIL_URL = `${USER_SERVICE}/email`;
export const CONFIRM_EMAIL_URL = `${USER_SERVICE}/confirm-email`;

export const NICKNAME_URL = `${USER_SERVICE}/nickname`;

export const MYPAGE_URL = `${USER_SERVICE}/auth/user`;
export const PROFILE_URL = `${USER_SERVICE}/auth/user/detail`;

// 챌린지
const CHALLENGE_SERVICE = 'challenge-service';

export const CHALLENGES_URL = `${CHALLENGE_SERVICE}/challenges`;
export const MY_CHALLENGES_URL = `${CHALLENGE_SERVICE}/challenges/my-challenge`;
export const CHALLENGES_LIST_URL = param =>
  `${CHALLENGE_SERVICE}/challenges/list?category=${param}`;

// 결제
const PAY_SERVICE = 'pay-service';
export const PAYMENT_CHALLENGE_SUCCESS = param =>
  `${PAY_SERVICE}/${param}/success`;
