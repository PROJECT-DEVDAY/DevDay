import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import axios from 'axios';

import { LOGIN_URL } from '@/constants';

export const loginAsync = createAsyncThunk(
  'user/loginAsync',
  async ({ email, password }) => {
    const response = await axios.post(LOGIN_URL, { email, password });
    return response.data.token;
  },
);

export const userSlice = createSlice({
  name: 'user',
  initialState: {
    token: null,
    status: 'idle',
  },
  reducers: {},
  extraReducers: builder => {
    builder
      .addCase(loginAsync.pending, state => {
        return { ...state, status: 'Loading' };
      })
      .addCase(loginAsync.fulfilled, (state, action) => {
        return { ...state, token: action.payload, status: 'Success' };
      });
    builder.addCase(loginAsync.rejected, state => {
      return { ...state, token: null, status: 'Fail' };
    });
  },
});
export default userSlice.reducer;
