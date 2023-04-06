import React, { useState } from 'react';

import classNames from 'classnames';
import Image from 'next/image';

import style from './type.module.scss';
import { BtnFooter } from '../../components/BtnFooter';
import { ReturnArrow } from '../../components/ReturnArrow';
import { SelectOption } from '../../components/SelectOption';

import Container from '@/components/Container';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';

const type = props => {
  const [checking, setChecking] = useState([false, false]);
  const changeCheck = index => {
    if (index === 0) {
      setChecking([true, false]);
    } else {
      setChecking([false, true]);
    }
  };

  const notPreferArr = [
    {
      id: 0,
      title: (
        <div className="flex">
          <span className="font-medium">공개챌린지</span>
          <Image
            src={require('@/image/handshake.png')}
            className="h-8 w-8 pb-1"
            alt="handshake"
          />
        </div>
      ),
      content: [
        <label>
          여러 사람들이 모여서 챌린지를 함께 할 수 있어요!
          <br />
          함께 도전해보세요.
        </label>,
      ],
    },
    {
      id: 1,
      title: (
        <div className="flex">
          <span className="font-medium">비공개챌린지</span>
          <Image
            src={require('@/image/lock.png')}
            className="h-6 w-6 pb-1 mb-2"
            alt="lock"
          />
        </div>
      ),
      content: [
        <label>
          코드 공유로 함께할 수 있는 챌린지입니다!
          <br />
          지인들과 함께 챌린지에 도전해보세요.
        </label>,
      ],
    },
  ];
  return (
    <Container>
      <Container.SubPageHeader />
      <Container.MainBody className="mt-16">
        <div>
          <div className="text-lg ml-2 font-medium">
            어떤 챌린지를 만드실건가요?
          </div>
          <div className={classNames('mt-4 ml-3')}>
            이 후 수정 할 수 없으니 <br /> 신중하게 골라주세요!
          </div>
          {notPreferArr.map(item => {
            const { id, title, content } = item;
            return (
              <button
                className={classNames('w-full text-left', style.btn)}
                type="button"
                onClick={() => changeCheck(id)}
                key={id}
              >
                <SelectOption
                  check={checking[id]}
                  title={title}
                  content={content}
                />
              </button>
            );
          })}
        </div>
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
            label="다음으로"
            disable={checking[0] || checking[1]}
            goToUrl="/create/category"
            warningMessage="챌린지 유형을 한개 골라주세요"
          />
        </div>
      </Container.Footer>
    </Container>
  );
};

export default PrivateRouter(type);
