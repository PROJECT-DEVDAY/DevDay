import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { PURGE } from 'redux-persist';

import http from '../../pages/api/http';

import { LOGIN_URL } from '@/constants';

export const loginAsync = createAsyncThunk(
  'user/loginAsync',
  async ({ email, password }) => {
    const response = await http.post(LOGIN_URL, {
      email,
      password,
    });
    return [
      { userInfo: response.data.data.userInfo },
      { accessToken: `Bearer ${response.data.data.accessToken}` },
      { refreshToken: `Bearer ${response.data.data.refreshToken}` },
    ];
  },
);
const initialState = {
  userInfo: {},
  accessToken: '',
  refreshToken: '',
},
export const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    reset(state) {
      Object.assign(state, {
        userInfo: {},
        accessToken: '',
        refreshToken: '',
      });
    },
  },
  extraReducers: builder => {
    builder
      .addCase(loginAsync.pending, state => {
        return { ...state, status: 'Loading' };
      })
      .addCase(loginAsync.fulfilled, (state, action) => {
        const [userInfo, accessToken, refreshToken] = action.payload;
        return {
          ...state,
          userInfo: userInfo.userInfo,
          accessToken: accessToken.accessToken,
          refreshToken: refreshToken.refreshToken,
          status: 'Success',
        };
      })
      .addCase(loginAsync.rejected, state => {
        return { ...state, token: null, status: 'Fail' };
      })
      .addCase(PURGE, () => initialState);
  },
});
export const { reset } = userSlice.actions;
export default userSlice.reducer;
