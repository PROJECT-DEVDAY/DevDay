import React, { useState } from 'react';

import classNames from 'classnames';
import { useRouter } from 'next/router';

import style from './check.module.scss';
import { BtnFooter } from '../../components/BtnFooter';
import { ReturnArrow } from '../../components/ReturnArrow';

import Container from '@/components/Container';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';

const check = props => {
  const [firstChecked, setFirstChecked] = useState(false);
  const [secondChecked, setSecondChecked] = useState(false);
  const changeFirstCheck = e => {
    if (e.target.checked) {
      setFirstChecked(true);
    } else {
      setFirstChecked(false);
    }
  };
  const changeSecondCheck = e => {
    if (e.target.checked) {
      setSecondChecked(true);
    } else {
      setSecondChecked(false);
    }
  };

  return (
    <Container>
      <Container.SubPageHeader />
      <Container.MainBody className="mt-16">
        <div>
          <div className="text-lg ml-2 font-medium">
            챌린지를 여는 이 순간부터,
            <br />
            당신은 챌린지를 이끌어나갈
            <br />
            리더가 됩니다.
            <br />
          </div>

          <div className={classNames('mt-4 ml-3')}>
            그전에 2가지만 약속해주세요!!
          </div>
        </div>
        <div className={style.textBox}>
          <span className="text-4xl mr-4 pt-1">1.</span>
          <span className="break-keep text-base">
            모두에게 기분 좋은 챌린지가 되도록 노력해주실거죠?
          </span>
        </div>
        <input
          onClick={changeFirstCheck}
          className="w-6 h-6 float-right"
          type="checkbox"
        />
        <div className={style.textBox}>
          <div className="text-4xl mr-3 pt-1">2.</div>
          <div className="break-keep text-base">
            참가자들의 의지와 예치금이 <br />
            모이는 챌린지에요 <br />
            책임감 있게 관리해 주실 수 있죠?
          </div>
        </div>
        <input
          onClick={changeSecondCheck}
          className="w-6 h-6 float-right"
          type="checkbox"
        />
      </Container.MainBody>
      <Container.Footer>
        <div
          className={classNames(
            style.btn,
            `text-center absolute w-full bottom-0 pb-4 m-0`,
          )}
        >
          <BtnFooter
            content=""
            label="네! 약속할게요"
            disable={firstChecked && secondChecked}
            goToUrl="/create/type"
            warningMessage="약속을 지키지 않으실건가요ㅠㅠ"
          />
        </div>
      </Container.Footer>
    </Container>
  );
};

export default PrivateRouter(check);
