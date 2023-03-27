import React, { useState } from 'react';

import classNames from 'classnames';
import Image from 'next/image';

import style from './category.module.scss';
import { BtnFooter } from '../../components/BtnFooter';
import { ReturnArrow } from '../../components/ReturnArrow';
import { SelectOption } from '../../components/SelectOption';

const check = props => {
  const [checking, setChecking] = useState([false, false, false]);
  const [goUrl, setGoUrl] = useState('');
  const changeCheck = index => {
    if (index === 0) {
      setChecking([true, false, false]);
      setGoUrl('/create/category/inputSolvedAC');
    } else if (index === 1) {
      setChecking([false, true, false]);
      setGoUrl('/create/category/inputGithub');
    } else {
      setChecking([false, false, true]);
      setGoUrl('/create/normal');
    }
  };

  const notPreferArr = [
    {
      id: 0,
      title: <span>알고리즘 챌린지</span>,
      content: [
        '알고리즘 챌린지에 도전해보세요!',
        <br />,
        'solvedAC를 연동해 자동 검증됩니다.',
      ],
      iconUrl: require('@/image/solvedac.png'),
    },
    {
      id: 1,
      title: <span>커밋 챌린지</span>,
      content: [
        '1일 1커밋 챌린지로 잔디를 쌓아보세요!',
        <br />,
        'github를 연동해 자동 검증됩니다.',
      ],
      iconUrl: require('@/image/github-mark.png'),
    },
    {
      id: 2,
      title: <span>자유주제 챌린지</span>,
      content: [
        '도전해보고 싶은 챌린지를 만들어보세요!',
        <br />,
        '검증은 사진을 올려서 할수 있습니다',
      ],
      iconUrl: require('@/image/people.png'),
    },
  ];
  return (
    <div>
      <div className={classNames(`style.div-header`, `sticky top-0`)}>
        <ReturnArrow />
      </div>
      <div className="div-body p-6 pb-32 mt-16">
        <div>
          <div className="text-lg font-medium">
            어떤 카테고리 챌린지를 도전해 <br /> 보실건가요?
          </div>
          <div className={classNames('mt-4 mb-4')}>
            이 후 수정 할 수 없으니 <br /> 신중하게 골라주세요!
          </div>
          {notPreferArr.map(item => {
            const { id, title, content, iconUrl } = item;
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
                  iconUrl={iconUrl}
                />
              </button>
            );
          })}
          <div className="text-center mt-4 font-medium">카테고리 선택</div>
        </div>
      </div>
      <div
        className={classNames(
          style.btn,
          `text-center absolute w-full bottom-0 pb-4 m-0`,
        )}
      >
        <BtnFooter
          label="다음으로"
          disable={checking[0] || checking[1] || checking[2]}
          goToUrl={goUrl}
          warningMessage="어떤 카테고리의 챌린지를 만드실건가요?"
        />
      </div>
    </div>
  );
};

export default check;
