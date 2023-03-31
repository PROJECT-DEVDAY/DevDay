import { createSlice } from '@reduxjs/toolkit';

const challengeCreateSlice = createSlice({
  name: 'challengeCreate',
  initialState: {
    startDate: null,
    endDate: null,
  },
  reducers: {
    setChangeDate: (state, action) => {
      return {
        ...state,
        startDate: action.payload.startDate,
        endDate: action.payload.endDate,
      };
    },
  },
});

export const { setChangeDate } = challengeCreateSlice.actions;
export default challengeCreateSlice.reducer;
