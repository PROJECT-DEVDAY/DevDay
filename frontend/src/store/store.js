import { configureStore, combineReducers } from '@reduxjs/toolkit';

import counter from './counter/countSlice';
import signup from './signup/signupSlice';

const combinedReducer = combineReducers({
  counter,
  signup,
});

const store = configureStore({
  reducer: combinedReducer,
});

export default store;
