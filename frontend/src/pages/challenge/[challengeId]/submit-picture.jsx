import React, { useEffect, useRef, useState } from 'react';

import classNames from 'classnames';
import Image from 'next/image';
import { useRouter } from 'next/router';
import Swal from 'sweetalert2';

import http from '@/api/http';
import { Button } from '@/components/Button';
import Container from '@/components/Container';
import { CHALLENGE_PHOTO_RECORD_URL } from '@/constants';

const SubmitPicture = () => {
  const router = useRouter();
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

    if (!inputRef.current.files[0]) {
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
  };

  const onClick = async () => {
    if (!isSelect) {
      Swal.fire({
        position: 'center',
        icon: 'warning',
        title: '사진을 선택해주세요',
        showConfirmButton: false,
        timer: 1000,
      });
      return;
    }
    // formData 설정
    const formData = new FormData();
    formData.append('photoCertFile', inputRef.current.files[0]);
    formData.append('challengeRoomId', challengeInfo.id);
    // 전송
    try {
      const result = await http.post(CHALLENGE_PHOTO_RECORD_URL, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      Swal.fire({
        position: 'center',
        icon: 'success',
        title: '이미지 선택 완료',
        showConfirmButton: false,
        timer: 1000,
      });
      router.back(); // 이전페이지로 이동하기
    } catch (e) {
      Swal.fire({
        position: 'center',
        icon: 'error',
        title: '이미지를 전송을 \n 실패했습니다.',
        showConfirmButton: false,
        timer: 1000,
      });
    }
  };
  return (
    <Container>
      <Container.SubPageHeader title={challengeInfo.name} />
      <Container.Body className="mt-4 px-4">
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
            제출 후에는 챌린지 인증사진을 수정할 수 없습니다.
          </p>
          <p className="text-xl pt-5">제출하시겠습니까?</p>
        </div>
      </Container.Body>
      <Container.Footer className="flex items-center border-t-2 p-4 z-30 bg-white">
        <Button label="인증하기" onClick={onClick} disabled={!isSelect} />
      </Container.Footer>
    </Container>
  );
};

export default SubmitPicture;
