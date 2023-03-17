import React from 'react';
import classnames from 'classnames';

import { FiHome, FiCamera, FiUser } from 'react-icons/fi';

import style from './footer.module.scss';

const FOOTER_ITEMS = [
  { label: '홈', icon: <FiHome /> },
  { label: '인증', icon: <FiCamera />, roundBackground: true },
  { label: '마이페이지', icon: <FiUser /> },
];

export default function Footer() {
  return (
    <div className={style.Footer}>
      {FOOTER_ITEMS.map((item, index) => {
        const { label, icon, roundBackground } = item;
        const key = `footer-item-${index}`;
        return (
          <div
            className={classnames(
              style.FooterBox,
              roundBackground && style.BackgroundRound,
            )}
            key={key}
          >
            {icon}
            <span>{label}</span>
          </div>
        );
      })}
    </div>
  );
}
