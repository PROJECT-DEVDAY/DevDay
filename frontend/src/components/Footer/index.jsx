import React from 'react';
import { FiHome, FiCamera, FiUser } from 'react-icons/fi';

import classNames from 'classnames';

import style from './footer.module.scss';

const FOOTER_ITEMS = [
  { label: '홈', icon: <FiHome size={28} /> },
  { label: '인증', icon: <FiCamera size={36} />, roundBackground: true },
  { label: '마이페이지', icon: <FiUser size={28} /> },
];

export default function Footer() {
  return (
    <div className={style.Footer}>
      {FOOTER_ITEMS.map((item, index) => {
        const { label, icon, roundBackground } = item;
        const key = `footer-item-${index}`;
        return (
          <div
            className={classNames(
              style.FooterBox,
              roundBackground && style.BackgroundRound,
              `pt-3`,
            )}
            key={key}
          >
            {icon}
            <span className={classNames(`pt-2 font-bold`)}>{label}</span>
          </div>
        );
      })}
    </div>
  );
}
