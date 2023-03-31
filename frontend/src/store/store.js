import { configureStore, combineReducers } from '@reduxjs/toolkit';
import persistReducer from 'redux-persist/lib/persistReducer';
import storage from 'redux-persist/lib/storage';

import challengeCreate from './challengeCreate/challengeCreateSlice';
import login from './loginTemp/loginSlice';
import signUp from './signup/signupSlice';

const combinedReducer = combineReducers({
  signUp,
  login,
  challengeCreate,
});

const persistConfig = {
  key: 'root',
  storage,
  whitelist: ['signUp', 'login', 'challengeCreate'],
};

const persistedReducer = persistReducer(persistConfig, combinedReducer);

const store = configureStore({
  reducer: persistedReducer,
});

export default store;
