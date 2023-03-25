import React from 'react';
import { AiOutlineSearch, AiOutlinePlus } from 'react-icons/ai';

import classnames from 'classnames';
import Image from 'next/image';
import PropTypes from 'prop-types';

import style from './main.module.scss';

import { HeaderButtons } from '@/components/HeaderButtons';

const search = () => {};
const addChallenge = () => {};
const HEADER_ITEMS = [
  { label: 'search', icon: <AiOutlineSearch size={24} />, onClick: search },
  {
    label: 'add',
    icon: <AiOutlinePlus size={24} />,
    onClick: addChallenge,
  },
];

const main = props => {
  return (
    <div className={classnames(style.MainHeader, `p-6`)}>
      <div className="flex justify-between">
        <Image src={require('@/image/MainLogo.png')} width={70} height={70} />
        <div className="flex">
          {HEADER_ITEMS.map(item => {
            const { label, icon, onClick } = item;
            const key = `header-item-${label}`;
            return (
              <button
                type="button"
                className={classnames(
                  style.HeaderBox,
                  `flex items-center p-1`,
                  key,
                )}
                key={key}
                onClick={onClick}
              >
                {icon}
              </button>
            );
          })}
        </div>
      </div>
      <HeaderButtons />
    </div>
  );
};

export default main;
