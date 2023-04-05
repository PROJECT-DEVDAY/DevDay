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
export const PASSWORD_URL = `${USER_SERVICE}/auth/user/password`;
export const DELETEUSER_URL = `${USER_SERVICE}/auth/user`;
export const TOKEN_REFRESH_URL = `${USER_SERVICE}/refresh`;

// 챌린지
const CHALLENGE_SERVICE = 'challenge-service';

export const CHALLENGES_URL = `${CHALLENGE_SERVICE}/auth/challenges`;
export const MY_CHALLENGES_URL = param =>
  `${CHALLENGE_SERVICE}/auth/challenges/my-challenge?status=${param}`;
export const CHALLENGES_LIST_URL = param =>
  `${CHALLENGE_SERVICE}/challenges/list?category=${param}`;
export const CHALLENGES_SEARCH_URL = `${CHALLENGE_SERVICE}/challenges`;
export const CHALLENGE_DETAIL_URL = `${CHALLENGE_SERVICE}/challenges`;
export const CHALLENGE_JOIN_URL = `${CHALLENGE_SERVICE}/auth/challenges/join`;
export const CHALLENGE_PHOTO_RECORD_URL = `${CHALLENGE_SERVICE}/auth/challenges/photo-record`;
export const CHALLENGE_ALGO_URL = `${CHALLENGE_SERVICE}/challenges/baekjoon/users`;
export const CHALLENGE_SUBMIT_RECORD_URL = param =>
  `${CHALLENGE_SERVICE}/auth/challenges/${param}/record`;
export const CHALLENGE_COMMIT_URL = `${CHALLENGE_SERVICE}/challenges/commit/users`;
export const CHALLENGE_AUTH_ALGO_URL = `${CHALLENGE_SERVICE}/auth/challenges`;
export const CHALLENGE_USER_RECORD_DETAIL_URL = recordId =>
  `${CHALLENGE_SERVICE}/auth/challenges/photo-record/${recordId}/users`;
export const CHALLENGE_USER_RECORD_REPORT_URL = `${CHALLENGE_SERVICE}/auth/challenges/report/record`;
// 결제
const PAY_SERVICE = 'pay-service';

export const MY_DEPOSIT_PRIZE = param => `${PAY_SERVICE}/users/${param}`;
export const DEPOSIT_WITHDRAW_URL = `${PAY_SERVICE}/users/deposit`;
export const PRIZE_WITHDRAW_URL = `${PAY_SERVICE}/users/prize`;
export const GET_MY_DEPOSIT_URL = param =>
  `${PAY_SERVICE}/users/deposit?type=${param}`;
export const PAYMENT_CHALLENGE_SUCCESS = param =>
  `${PAY_SERVICE}/payments/${param}/success`;

export const PUBLIC_TOSS_CLIENT_KEY = 'test_ck_N5OWRapdA8dWR0eyPm6ro1zEqZKL';
export const LOCALE = 'ko-KR';

export const DEVDAY_ATTENDEE_TICKET = 'DevDay 참가비';
