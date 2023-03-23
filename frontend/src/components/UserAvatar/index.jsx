import React, { useEffect, useState, useRef } from 'react';

import style from './index.module.scss';

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
      setImgeFile(
        'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png',
      );
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
      <image src={imgFile} alt="프로필 이미지" className={style.image} />
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
      <button
        type="button"
        className={style.inputBtn}
        onClick={onImgeInputBtnClick}
      >
        등록
      </button>
    </div>
  );
};

UserAvatar.propTypes = {};

UserAvatar.defaultProps = {};
