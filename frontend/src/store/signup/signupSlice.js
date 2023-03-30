import { createSlice } from '@reduxjs/toolkit';
import produce from 'immer';

const signupSlice = createSlice({
  name: 'signUpInfos',

  initialState: {
    email: '',
    password: '',
    passwordCheck: '',
    name: '',
    nickname: '',
    baekjoon: '',
    github: '',
    emailAuthId: '',
  },
  reducers: {
    saveSignUpInfos: (state, action) => {
      return action.payload;
    },
    saveExtraInfos: (state, action) => {
      return {
        ...state,
        baekjoon: action.payload.baekjoon,
        github: action.payload.github,
      };
    },
    reset(state) {
      Object.assign(state, {
        email: '',
        password: '',
        passwordCheck: '',
        name: '',
        nickname: '',
        baekjoon: '',
        github: '',
        emailAuthId: '',
      });
    },
  },
  extraReducers: builder => {},
});

export const { saveSignUpInfos, saveExtraInfos, reset } = signupSlice.actions;
export default signupSlice.reducer;
