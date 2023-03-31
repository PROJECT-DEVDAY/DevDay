import { configureStore, combineReducers } from '@reduxjs/toolkit';
import persistReducer from 'redux-persist/lib/persistReducer';
import storage from 'redux-persist/lib/storage';

import login from './loginTemp/loginSlice';
import signUp from './signup/signupSlice';

const combinedReducer = combineReducers({
  signUp,
  login,
});

const persistConfig = {
  key: 'root',
  storage,
  whitelist: ['signUp', 'login'],
};

const persistedReducer = persistReducer(persistConfig, combinedReducer);

const store = configureStore({
  reducer: persistedReducer,
});

export default store;
