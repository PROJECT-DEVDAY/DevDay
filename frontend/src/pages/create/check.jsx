import React, { useState } from 'react';

import classNames from 'classnames';
import { useRouter } from 'next/router';

import style from './check.module.scss';
import { BtnFooter } from '../../components/BtnFooter';
import { ReturnArrow } from '../../components/ReturnArrow';
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
    <div>
      <div className={classNames(`style.div-header`, `sticky top-0`)}>
        <ReturnArrow />
      </div>
      <div className="div-body p-6 pb-32 mt-16">
        <div>
          <div className="text-lg font-medium">
            챌린지를 여는 이 순간부터,
            <br />
            당신은 챌린지를 이끌어나갈
            <br />
            리더가 됩니다.
            <br />
          </div>

          <div className={classNames('mt-4')}>
            그전에 2가지만 약속해주세요!!
          </div>
        </div>
        <div>
          <div className={style.textBox}>
            <span className="text-4xl mr-4 pt-1">1.</span>
            <span className="break-keep text-lg">
              모두에게 기분 좋은 챌린지가 되도록 노력해주실거죠?
            </span>
          </div>
          <input
            onClick={changeFirstCheck}
            className="w-6 h-6 float-right"
            type="checkbox"
          />
        </div>
        <div>
          <div className={style.textBox}>
            <div className="text-4xl mr-4 pt-1">2.</div>
            <div className="break-keep text-lg">
              참가자들의 의지와 예치금이 모일 챌린지예요. 책임감 있게 관리해
              주실 수 있죠?
            </div>
          </div>
          <input
            onClick={changeSecondCheck}
            className="w-6 h-6 float-right"
            type="checkbox"
          />
        </div>
      </div>
      <div className={classNames(`text-center absolute w-full bottom-0 m-0`)}>
        <BtnFooter
          content=""
          label="네! 약속할게요"
          disable={firstChecked && secondChecked}
          goToUrl="/create/type"
          warningMessage="약속을 지키지 않으실건가요ㅠㅠ"
        />
      </div>
    </div>
  );
};

export default PrivateRouter(check);
