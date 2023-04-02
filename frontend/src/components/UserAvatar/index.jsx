import React, { useEffect, useState, useRef } from 'react';

import Image from 'next/image';
import Swal from 'sweetalert2';

import style from './index.module.scss';

export const UserAvatar = ({ imageURL, width, height, ...props }) => {
  const [imgFile, setImgeFile] = useState(
    require('../../image/default-user.png'),
  );

  const inputRef = useRef(null);

  const onClickImageInput = event => {
    event.preventDefault();
    inputRef.current.click();
  };

  const onChange = e => {
    const reader = new FileReader();
    reader.onload = ({ target }) => {
      inputRef.current.src = target.result;
    };

    if (!e.current.files[0]) {
      Swal.fire({
        position: 'center',
        icon: 'warning',
        title: '사진을 선택해주세요',
        showConfirmButton: false,
        timer: 1000,
      });
      return;
    }

    reader.readAsDataURL(inputRef.current.files[0]);
    setImgeFile(inputRef.current.files[0]);
  };

  return (
    <div className={style.UserAvatar}>
      <Image
        src={imgFile}
        alt="프로필 이미지"
        className="style.Image rounded-full"
        onClick={onClickImageInput}
        width={width}
        height={height}
      />
      <input
        style={{ display: 'none' }}
        ref={inputRef}
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
