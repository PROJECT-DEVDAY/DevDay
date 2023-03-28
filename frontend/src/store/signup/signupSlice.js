import { createSlice } from '@reduxjs/toolkit';
import produce from 'immer';

const signupSlice = createSlice({
  name: 'user',
  initialState: {},
  reducers: {
    saveSignUpInfos: (state, action) => {
      return action.payload;
    },
  },
});

export const { saveSignUpInfos } = signupSlice.actions;
export default signupSlice.reducer;
