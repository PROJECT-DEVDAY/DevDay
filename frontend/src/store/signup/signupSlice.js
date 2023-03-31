import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';

import { JOIN_URL } from '@/constants';
import http from '@/pages/api/http';

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

export const saveExtraInfosAsync = createAsyncThunk(
  'signUp/saveExtraInfosAsync',
  async (extraInfos, thunkAPI) => {
    const response = await http.post(
      JOIN_URL(thunkAPI.getState().signUp.emailAuthId),
      extraInfos,
    );
    return response.data;
  },
);

const signupSlice = createSlice({
  name: 'signUp',
  initialState,
  reducers: {
    saveSignUpInfos: (state, action) => {
      return action.payload;
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
    builder
      .addCase(saveExtraInfosAsync.pending, state => {
        return { ...state, status: 'Loading' };
      })
      .addCase(saveExtraInfosAsync.fulfilled, (state, action) => {
        return { ...state, token: action.payload, status: 'Success' };
      })
      .addCase(saveExtraInfosAsync.rejected, state => {
        return { ...state, token: null, status: 'Fail' };
      });
  },
});

export const { saveSignUpInfos, reset } = signupSlice.actions;
export default signupSlice.reducer;
