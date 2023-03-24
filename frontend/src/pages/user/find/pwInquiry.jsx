import React, { useState, useRef, useEffect } from 'react';
import { AiOutlineCloseCircle } from 'react-icons/Ai';

import classnames from 'classnames';
import { useRouter } from 'next/router';

import style from './inquiry.module.scss';

import { Button } from '@/components/Button';
import { InputText } from '@/components/InputText';
import { ReturnArrow } from '@/components/ReturnArrow';

const pwInquiry = () => {
  const [show, setShow] = useState(false);

  const router = useRouter();

  const showModal = () => {
    setShow(prev => !prev);
  };

  const bottomSheetRef = useRef(null);

  useEffect(() => {
    const handleClickOutside = event => {
      if (
        bottomSheetRef.current &&
        !bottomSheetRef.current.contains(event.target)
      ) {
        setShow(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [bottomSheetRef]);

  const goToLogin = () => {
    router.push('/user/login');
  };

  return (
    <div>
      <div>
        <div className="div-header sticky top-0">
          <ReturnArrow title="비밀번호 찾기" />
        </div>
        <div className="div-body p-6">
          <InputText labelName="이름" content="홍길동" />
          <InputText labelName="이메일" content="welcome@devday.com" />

          <Button
            className="inquiry-bt mt-6"
            color="primary"
            fill
            label="비밀번호 찾기"
            onClick={showModal}
          />
        </div>
      </div>

      <div
        className={classnames(
          style.BottomSheet,
          style[`${show ? 'show' : ''}`],
        )}
        ref={bottomSheetRef}
      >
        <div className={classnames(style[`modal-content`], `p-6`)}>
          <button
            type="button"
            className={classnames(style.ExitBtn)}
            onClick={showModal}
          >
            <AiOutlineCloseCircle />
          </button>
          <p className={classnames(`mb-4 mt-2`)}>
            회원님의 비밀번호는 ~ 입니다
          </p>
          <Button
            onClick={goToLogin}
            color="primary"
            fill
            label="로그인 하러 가기"
          />
        </div>
      </div>
    </div>
  );
};

export default pwInquiry;
