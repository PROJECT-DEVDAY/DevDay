import { configureStore, combineReducers } from '@reduxjs/toolkit';
import counter from '../store/counter/countSlice';
import signup from '../store/signup/signupSlice';
const combinedReducer = combineReducers({
  counter,
  signup,
});

const store = configureStore({
  reducer: combinedReducer,
});

export default store;
