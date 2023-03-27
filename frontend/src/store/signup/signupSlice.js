import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  // email , password, passwordCheck, name, nickname
  email: '',
  password: '',
  passwordCheck: '',
  name: '',
  nickname: '',
  solvedAc: '',
  gitHub: '',
};

const signupSlice = createSlice({
  name: 'signupSlice',
  initialState,
  reducers: {
    save(state, action) {
      state = action.payload;
    },
  },
});

const { actions, reducer } = signupSlice;
export const { save } = actions;
export default reducer;
