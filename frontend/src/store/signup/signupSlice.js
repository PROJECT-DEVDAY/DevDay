import { createSlice } from '@reduxjs/toolkit';
import produce from 'immer';

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
      return produce(state, changedState => {
        Object.assign(changedState, action.payload);
      });
    },
  },
});

const { actions, reducer } = signupSlice;
export const { save } = actions;
export default reducer;
