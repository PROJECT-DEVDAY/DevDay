import React, { useState } from 'react';

import classNames from 'classnames';
import Image from 'next/image';

import style from './inputGithub.module.scss';

import { BtnFooter } from '@/components/BtnFooter';
import { InputBox } from '@/components/InputBox';
import { ReturnArrow } from '@/components/ReturnArrow';

const inputGithub = props => {
  const [text, setText] = useState('');
  const setTextValue = e => {
    setText(e.target.value);
  };
  return (
    <div>
      <div className={classNames(`style.div-header`, `sticky top-0`)}>
        <ReturnArrow title="추가 정보 입력" />
      </div>
      <div className="div-body p-6 pb-32">
        <div className="font-medium text-xl mt-12 p-4">
          Commit 챌린지를 위해서는 <br /> Github 아이디를 입력하셔야 <br />
          합니다.
        </div>

        <Image
          src={require('@/image/github.png')}
          alt="solvedAC"
          className="w-1/2 mt-8"
        />
        <InputBox
          placeholder="GitHub 아이디"
          onChange={setTextValue}
          value={text}
        />
        <div className="mt-4 ">
          Commit 챌린지를 이용하기 위해서 필요합니다
          <br />
          해당 정보가 입력되지 않는다면 해당 서비스 이용이
          <br />
          제합됩니다.
        </div>
      </div>
      <div
        className={classNames(
          style.btn,
          `text-center absolute w-full bottom-0 pb-4 m-0`,
        )}
      >
        <BtnFooter
          label="다음"
          disable={text}
          goToUrl="/create/commit"
          warningMessage="Commit 챌린지는 GitHub ID가 필요해요."
        />
      </div>
    </div>
  );
};

export default inputGithub;
