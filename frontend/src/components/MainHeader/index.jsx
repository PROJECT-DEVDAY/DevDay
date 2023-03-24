import React from 'react';
import { AiOutlineSearch, AiOutlinePlus } from 'react-icons/ai';
import { FcSearch } from 'react-icons/fc';

import classnames from 'classnames';
import Image from 'next/image';
import PropTypes from 'prop-types';

import style from './index.module.scss';

const search = () => {};
const addChallenge = () => {};
const HEADER_ITEMS = [
  { label: '검색', icon: <AiOutlineSearch />, onClick: search },
  { label: '챌린지 생성', icon: <AiOutlinePlus />, onClick: addChallenge },
];

export const MainHeader = props => {
  return (
    <div className={style.MainHeader}>
      <div>
        <image className="image" src={require('../../image/MainLogo.png')} />
      </div>
      <div className="flex">
        {HEADER_ITEMS.map((item, index) => {
          const { lable, icon } = item;
          const key = `header-item-${index}`;
          return (
            <div className={classnames(style.HeaderBox)} key={key}>
              {icon}
            </div>
          );
        })}
      </div>
    </div>
  );
};

MainHeader.propTypes = {};
