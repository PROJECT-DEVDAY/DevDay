import React, { useEffect, useState } from 'react';
import { Provider } from 'react-redux';

import Router from 'next/router';

import Spinner from '@/components/Spinner';
import store from '@/store/store';

import '@/styles/globals.css';
import '../styles/fonts/style.css';

function App({ Component, pageProps }) {
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const handleStart = () => setLoading(true);
    const handleComplete = () => setLoading(false);
    const handleError = () => setLoading(false);

    Router.events.on('routeChangeStart', handleStart);
    Router.events.on('routeChangeComplete', handleComplete);
    Router.events.on('routeChangeError', handleError);

    return () => {
      Router.events.off('routeChangeStart', handleStart);
      Router.events.off('routeChangeComplete', handleComplete);
      Router.events.off('routeChangeError', handleError);
    };
  }, []);

  return (
    <>
      <span className="flex justify-center items-center">
        {loading && <Spinner />}
      </span>
      {!loading && (
        <Provider store={store}>
          <Component {...pageProps} />
        </Provider>
      )}
    </>
  );
}

export default App;
