import { createSlice } from '@reduxjs/toolkit';

const counterSlice = createSlice({
  name: 'counter',
  initialState: { value: 0 },
  reducers: {
    plus: state => {
      state.value += 1;
    },
  },
});

const { actions, reducer } = counterSlice;
export const { plus } = actions;
export default reducer;
