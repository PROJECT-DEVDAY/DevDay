import React from 'react';
import { FiHome, FiCamera, FiUser } from 'react-icons/fi';

import classNames from 'classnames';
import { useRouter } from 'next/router';

import style from './footer.module.scss';

const FOOTER_ITEMS = [
  { label: '홈', icon: <FiHome size={28} />, to: '/' },
  {
    label: '인증',
    icon: <FiCamera size={36} />,
    roundBackground: true,
    to: '/auth',
  },
  { label: '마이페이지', icon: <FiUser size={28} />, to: '/mypage' },
];

export default function Footer() {
  const router = useRouter();

  return (
    <div className={style.Footer}>
      {FOOTER_ITEMS.map((item, index) => {
        const { label, icon, roundBackground, to } = item;
        const key = `footer-item-${index}`;
        const handleClick = () => router.push(to);

        return (
          <button
            type="button"
            className={classNames(
              style.FooterBox,
              roundBackground && style.BackgroundRound,
              `pt-3`,
            )}
            key={key}
            onClick={handleClick}
          >
            {icon}
            <span className={classNames(`pt-2 font-bold`)}>{label}</span>
          </button>
        );
      })}
    </div>
  );
}
