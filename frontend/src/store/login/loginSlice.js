import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';

export const loginAsync = createAsyncThunk();
// 'loginState/loginAsync',
// async data => {},

const loginSlice = createSlice({
  name: 'loginState',
  initialState: {
    token: null,
    status: 'Ready',
  },
  extraReducers: builder => {
    builder.addCase(loginAsync.pending, state => {
      return { ...state, status: 'Loading' };
    });

    builder.addCase(loginAsync.fulfilled, (state, action) => {
      return { ...state, token: action.payload, status: 'Complete' };
    });

    builder.addCase(loginAsync.rejected, state => {
      return { ...state, status: 'fail' };
    });
  },
});

export default loginSlice;
// export { loginAsync };
