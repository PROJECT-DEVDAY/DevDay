import React, { useEffect, useState, useRef } from 'react';

import Image from 'next/image';

import style from './index.module.scss';

export const UserAvatar = ({ imageURL, width, height, ...props }) => {
  const [imgFile, setImgeFile] = useState(require('../../image/default-user.png'));

  const logoImgInput = useRef(null);

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
    <div className={style.UserAvatar}>
      <Image
        src={imgFile}
        alt="프로필 이미지"
        className="style.Image rounded-full"
        onClick={onImgeInputBtnClick}
        width={width}
        height={height}
      />
      <input
        style={{ display: 'none' }}
        ref={logoImgInput}
        type="file"
        className={style.ImgInput}
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
