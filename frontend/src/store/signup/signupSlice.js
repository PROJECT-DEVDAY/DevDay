import { createSlice } from '@reduxjs/toolkit';
import { PURGE } from 'redux-persist';

const initialState = {
  email: '',
  password: '',
  passwordCheck: '',
  name: '',
  nickname: '',
  baekjoon: '',
  github: '',
  emailAuthId: '',
};
const signupSlice = createSlice({
  name: 'signUp',
  initialState,
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
  extraReducers: builder => {
    builder.addCase(PURGE, () => initialState);
  },
});

export const { saveSignUpInfos, saveExtraInfos, reset } = signupSlice.actions;
export default signupSlice.reducer;
