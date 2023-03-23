import React, { useEffect, useState, useRef } from 'react';

import style from './index.module.scss';

import { FiPlus } from 'react-icons/fi';

export const UserAvatar = props => {
  const [imgFile, setImgeFile] = useState('');

  const logoImgInput = useRef();

  const onImgeInputBtnClick = event => {
    event.preventDefault();
    logoImgInput.current.click();
  };

  const onChange = e => {
    if (e.target.files[0]) {
      setImgeFile(e.target.files[0]);
    } else {
      setImgeFile(require('../../image/default-user.png'));
      return;
    }

    const reader = new FileReader();

    reader.onload = () => {
      if (reader.readyState === 2) {
        setImgeFile(reader.result);
      }
    };
    reader.readAsDataURL(e.target.files[0]);
  };

  return (
    <div className={style.userAvatar}>
      <img
        src={imgFile ? imgFile : require('../../image/default-user.png')}
        alt="프로필 이미지"
        className={style.image}
        onClick={onImgeInputBtnClick}
      />
      <input
        style={{ display: 'none' }}
        ref={logoImgInput}
        type="file"
        className={style.imgInput}
        id="logoImg"
        accept="image/*"
        name="file"
        onChange={onChange}
      />
    </div>
  );
};

UserAvatar.propTypes = {};

UserAvatar.defaultProps = {};
