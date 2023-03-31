import React, { useEffect, useRef, useState } from 'react';
import axios from 'axios';
import Image from 'next/image';

import classNames from 'classnames';

import { CheckBoxBtn } from '@/components/CheckBoxBtn';
import { ReturnArrow } from '@/components/ReturnArrow';

const SubmitPicture = ({}) => {
  const challengeInfo = {
    id: 1,
    name: '1일 1회의',
  };

  const userInfo = {
    id: 1,
    name: 'pthwan',
  };

  const [isSelect, setIsSelect] = useState(false);
  const inputRef = useRef();
  const imageRef = useRef();

  const onClickImage = () => {
    inputRef.current.click();
  };

  const onChageImage = e => {
    const reader = new FileReader();
    reader.onload = ({ target }) => {
      imageRef.current.src = target.result;
      setIsSelect(true);
    };
    reader.readAsDataURL(inputRef.current.files[0]);
  };

  const onClick = () => {
    alert('이미지를 전송합니다.');
  };
  return (
    <div className="font-medium">
      <div className="style.div-header sticky top-0">
        <ReturnArrow title={challengeInfo.name} />
      </div>
      <div className="div-body p-6 pb-12 mt-8">
        <div className="col-span-full">
          <div
            onClick={onClickImage}
            className="flex justify-center align-middle rounded-lg border border-dashed border-gray-900/25 h-80"
          >
            <Image
              className={classNames(
                'w-full h-full object-contain',
                !isSelect && 'hidden',
              )}
              ref={imageRef}
              id="image-upload-preview"
              alt="업로드할 이미지를 미리볼 수 있습니다."
            />
            <div
              className={classNames(
                'text-center flex justify-center flex-col mx-6 my-10',
                isSelect && 'hidden',
              )}
            >
              <input
                id="image-upload"
                name="image-upload"
                type="file"
                className="sr-only"
                accept="image/*"
                ref={inputRef}
                onChange={onChageImage}
              />
              <p className="pl-1">
                사진첩에서 <br /> 이미지를 선택해주세요.
              </p>
              <p className="text-xs leading-5 text-gray-600">
                PNG, JPG, GIF up to 10MB
              </p>
            </div>
          </div>

          <div className="mt-5 text-center text-sm text-slate-400 h-10">
            {isSelect && (
              <span>사진을 클릭하시면 다시 고를 수 있습니다 :)</span>
            )}
          </div>
        </div>
        <div className="pt-13">
          <p className="text-xl">
            제출 후에는 챌린지 인증사진을 <br /> 수정할 수 없습니다.
          </p>
          <p className="text-xl pt-5">제출하시겠습니까?</p>
        </div>
      </div>
      <div className="absolute w-full bottom-0">
        <CheckBoxBtn check={false} label="인증하기" onClick={onClick} />
      </div>
    </div>
  );
};

export default SubmitPicture;
