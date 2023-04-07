import React, { useState, useRef, useEffect } from 'react';
import { AiOutlineCloseCircle } from 'react-icons/ai';

import classNames from 'classnames';
import { useRouter } from 'next/router';

import style from './inquiry.module.scss';

import { Button } from '@/components/Button';
import { InputText } from '@/components/InputText';
import { ReturnArrow } from '@/components/ReturnArrow';
import http from '@/api/http';
import { FIND_ID_URL } from '@/constants';
import Swal from 'sweetalert2';
import { InputLabel } from '@/components/InputLabel';

const idInquiry = () => {
  const [show, setShow] = useState(false);

  const router = useRouter();

  const showModal = () => {
    setShow(prev => !prev);
  };

  const bottomSheetRef = useRef(null);

  const [findId, setFindId] = useState();

  const [inputs, setInputs] = useState({
    name: '',
    nickname: '',
  });

  const { name, nickname } = inputs;

  const onChangeFindIdINfo = e => {
    const { value, name } = e.target;
    setInputs({
      ...inputs,
      [name]: value,
    });
  };

  const onClickeFindId = () => {
    http
      .post(FIND_ID_URL, {
        name,
        nickname,
      })
      .then(async data => {
        await Swal.fire({
          position: 'center',
          icon: 'success',
          title: '성공!',
          showConfirmButton: false,
          timer: 1500,
        });

        setFindId(data.data.data);

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
          <ReturnArrow title="아이디 찾기" />
        </div>
        <div className="div-body p-6">
          <InputLabel content="이름" />
          <InputText
            type="text"
            labelName="이름"
            content="이름을 입력해주세요"
            onChange={onChangeFindIdINfo}
            name="name"
          />
          <div className="mt-5"></div>

          <InputLabel content="닉네임" className="mt-2" />
          <InputText
            type="text"
            labelName="닉네임"
            content="닉네임을 입력해주세요"
            onChange={onChangeFindIdINfo}
            name="nickname"
          />

          <Button
            className="inquiry-bt mt-6"
            color="primary"
            fill
            label="아이디 찾기"
            onClick={onClickeFindId}
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
        <div className={classNames(style[`modal-content`], `px-6 py-3`)}>
          <button
            type="button"
            className={classNames(style.ExitBtn)}
            onClick={showModal}
          >
            <AiOutlineCloseCircle />
          </button>
          {findId && (
            <p className={classNames(`mb-2 text-center`)}>
              회원님의 아이디는
              <br /> {findId} 입니다
            </p>
          )}
          {!findId && (
            <p className={classNames(`mb-2 text-center`)}>
              아이디 찾기를 해주세요
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

export default idInquiry;
