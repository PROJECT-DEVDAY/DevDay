import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';

import { LOGIN_URL } from '@/constants';
import http from '../../pages/api/http';
import axios from 'axios';

export const loginAsync = createAsyncThunk(
  'user/loginAsync',
  async ({ email, password }) => {
    const response = await http.post(LOGIN_URL, {
      email,
      password,
    });
    return response.data;
  },
);

export const userSlice = createSlice({
  name: 'user',
  initialState: {
    token: {},
  },
  reducers: {},
  extraReducers: builder => {
    builder
      .addCase(loginAsync.pending, state => {
        return { ...state, status: 'Loading' };
      })
      .addCase(loginAsync.fulfilled, (state, action) => {
        return { ...state, token: action.payload, status: 'Success' };
      })
      .addCase(loginAsync.rejected, state => {
        return { ...state, token: null, status: 'Fail' };
      });
  },
});
export default userSlice.reducer;
