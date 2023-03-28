import { configureStore, combineReducers } from '@reduxjs/toolkit';

import counter from './counter/countSlice';
import signUp from './signup/signupSlice';

const combinedReducer = combineReducers({
  counter,
  signUp,
});

const store = configureStore({
  reducer: combinedReducer,
});

export default store;
