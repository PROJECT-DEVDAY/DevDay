import React, { useState } from 'react';
import {
  AiOutlineSearch,
  AiOutlinePlus,
  AiOutlineCloseCircle,
} from 'react-icons/ai';

import classnames from 'classnames';
import Image from 'next/image';
import PropTypes from 'prop-types';

import style from './main.module.scss';

import { HeaderButtons } from '@/components/HeaderButtons';

const main = props => {
  const [searchBoxStatus, setSearchBoxStatus] = useState(false);

  const search = () => {};
  const searchOnKeyPress = e => {
    if (e.key === 'Enter') {
      search();
    }
  };
  const addChallenge = () => {};

  const searchBoxStatusChange = () => {
    setSearchBoxStatus(prev => !prev);
  };

  const HEADER_ITEMS = [
    {
      label: 'search',
      icon: <AiOutlineSearch size={30} />,
      onClick: searchBoxStatusChange,
    },
    {
      label: 'add',
      icon: <AiOutlinePlus size={30} />,
      onClick: addChallenge,
    },
  ];
  return (
    <div className={classnames(style.MainHeader, `py-6`)}>
      <div className={classnames(`flex justify-between px-6`, style.HeaderTop)}>
        <Image src={require('@/image/MainLogo.png')} width={70} height={70} />
        <div className={classnames(`flex HeaderIcons`)}>
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

      <hr className={style.Divider} />

      <div className={classnames(`p-4`, style.HeaderBottom)}>
        {searchBoxStatus && (
          <div
            className={classnames(
              `flex items-center justify-center h-full`,
              style.HeaderSearch,
            )}
          >
            <div
              className={classnames(
                `flex items-center w-full`,
                style.searchContainer,
              )}
            >
              <input
                className={classnames(`py-2 pl-4 rounded-3xl`)}
                placeholder="검색하실 내용을 입력해주세요"
                onKeyPress={searchOnKeyPress}
              />
              <AiOutlineSearch size={28} onClick={search} />
              <AiOutlineCloseCircle size={28} onClick={searchBoxStatusChange} />
            </div>
          </div>
        )}

        <div className={classnames(`py-4`, style.HeaderButtons)}>
          <HeaderButtons />
        </div>
      </div>
    </div>
  );
};

export default main;
