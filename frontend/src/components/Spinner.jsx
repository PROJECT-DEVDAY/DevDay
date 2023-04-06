import React from 'react';

import classNames from 'classnames';
import Image from 'next/image';

import style from './Spinner.module.scss';

const Spinner = ({ content }) => {
  return (
    <div>
      <div className={classNames(style.spinner)}>
        <Image
          src={require('@/image/devdaying.gif')}
          className="w-full"
          alt="man"
        />
      </div>
      <p className="text-center font-bold text-4xl mt-20">{content}</p>
    </div>
  );
};

Spinner.defaultProps = {
  content: '코딩중.....♡',
};

export default Spinner;
