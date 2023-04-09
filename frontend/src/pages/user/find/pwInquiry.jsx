import React, { useState, useRef, useEffect } from 'react';
import { AiOutlineCloseCircle } from 'react-icons/ai';

import classNames from 'classnames';
import { useRouter } from 'next/router';

import style from './inquiry.module.scss';

import { Button } from '@/components/Button';
import { InputText } from '@/components/InputText';
import { ReturnArrow } from '@/components/ReturnArrow';
import { InputLabel } from '@/components/InputLabel';
import { FIND_PW_URL } from '@/constants';
import http from '@/api/http';
import Swal from 'sweetalert2';

const pwInquiry = () => {
  const [show, setShow] = useState(false);

  const router = useRouter();

  const showModal = () => {
    setShow(prev => !prev);
  };

  const bottomSheetRef = useRef(null);

  const [findPw, setFindPw] = useState();

  const [inputs, setInputs] = useState({
    name: '',
    email: '',
    nickname: '',
  });

  const { name, email, nickname } = inputs;

  const onChangeFindPwINfo = e => {
    const { value, name } = e.target;
    setInputs({
      ...inputs,
      [name]: value,
    });
  };

  const onClickeFindPw = () => {
    http
      .patch(FIND_PW_URL, {
        name,
        nickname,
        email,
      })
      .then(async data => {
        await Swal.fire({
          position: 'center',
          icon: 'success',
          title: '성공!',
          showConfirmButton: false,
          timer: 1500,
        });
        setFindPw(data.data.message);

        showModal();
      })
      .catch(() => {
        Swal.fire({
          position: 'center',
          icon: 'error',
          title: '정보를 확인해주세요!',
          showConfirmButton: false,
          timer: 1500,
        });
      });
  };

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
          <InputLabel content="이름" />
          <InputText
            type="text"
            labelName="이름"
            content="홍길동"
            name="name"
            onChange={onChangeFindPwINfo}
          />

          <div className="mt-5"></div>
          <InputLabel content="닉네임" />
          <InputText
            type="text"
            labelName="닉네임"
            content="닉네임을 입력해주세요"
            onChange={onChangeFindPwINfo}
            name="nickname"
          />
          <div className="mt-5"></div>
          <InputLabel content="이메일" />
          <InputText
            type="email"
            labelName="이메일"
            content="welcome@devday.com"
            name="email"
            onChange={onChangeFindPwINfo}
          />

          <Button
            className="inquiry-bt mt-6"
            color="primary"
            fill
            label="비밀번호 찾기"
            onClick={onClickeFindPw}
          />
        </div>
      </div>

      <div
        className={classNames(
          style.BottomSheet,
          style[`${show ? 'show' : ''}`],
        )}
        ref={bottomSheetRef}
      >
        <div className={classNames(style[`modal-content`], `p-6`)}>
          <button
            type="button"
            className={classNames(style.ExitBtn)}
            onClick={showModal}
          >
            <AiOutlineCloseCircle />
          </button>
          {findPw == 'success' && (
            <p className={classNames(`mb-2 text-center`)}>
              회원님의 임시 비밀번호를
              <br /> 이메일로 전송하였습니다.
            </p>
          )}
          {findPw != 'success' && (
            <p className={classNames(`mb-2 text-center`)}>
              올바른 정보를 입력해주세요
            </p>
          )}
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
