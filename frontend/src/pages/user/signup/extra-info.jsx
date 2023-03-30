import React, { useState } from 'react';
import { CiMail } from 'react-icons/ci';
import { useDispatch, useSelector } from 'react-redux';

import classNames from 'classnames';
import Image from 'next/image';
import { useRouter } from 'next/router';
import Swal from 'sweetalert2';

import style from './extra-info.module.scss';
import { Button } from '../../../components/Button';
import { ReturnArrow } from '../../../components/ReturnArrow';

import { JOIN_URL } from '@/pages/api/constants';
import http from '@/pages/api/http';
import { saveExtraInfos, reset } from '@/store/signup/signupSlice';

const signup = props => {
  const router = useRouter();
  const dispatch = useDispatch();
  const signUpInfos = useSelector(state => state.signUp);

  const [inputs, setInputs] = useState({
    baekjoon: '',
    github: '',
  });
  const { baekjoon, github } = inputs;

  const onChangeId = e => {
    const { value, name } = e.target;
    setInputs({
      ...inputs,
      [name]: value,
    });
  };

  const onClickJoin = () => {
    let timerInterval;

    dispatch(
      saveExtraInfos({
        baekjoon,
        github,
      }),
    );

    http
      .post(JOIN_URL(signUpInfos.emailAuthId), signUpInfos)
      .then(() => {
        Swal.fire({
          icon: 'success',
          title: '회원가입 성공',
          timer: 1000,
          timerProgressBar: true,
          text: 'Dev Day를 즐겨보세요',
          didOpen: () => {
            Swal.showLoading();
            const b = Swal.getHtmlContainer().querySelector('b');
            timerInterval = setInterval(() => {
              b.textContent = Swal.getTimerLeft();
            }, 100);
          },
          willClose: () => {
            clearInterval(timerInterval);
          },
        })
          .then(dispatch.reset())
          .then(router.push('./user/login'));
      })
      .catch(error => {
        Swal.fire({
          icon: 'error',
          title: '실패!',
          text: error.message,
        });
      });
  };
  const onClickPass = () => {
    dispatch(
      saveExtraInfos({
        baekjoon,
        github,
      }),
    );

    Swal.fire({
      title: '다음에 입력하실 건가요?',
      text: '나중에 다시 입력할 수 있습니다!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: '네',
      cancelButtonText: '아니요',
    }).then(result => {
      if (result.isConfirmed) {
        http
          .post(JOIN_URL(signUpInfos.emailAuthId), signUpInfos)
          .then(() => {
            router.push('./user/login');
          })
          .catch(error => {
            Swal.fire({
              icon: 'error',
              title: '실패!',
              text: error.message,
            });
          });
      }
    });
  };
  return (
    <div className={style.signup}>
      <div className="style.div-header sticky top-0">
        <ReturnArrow title="회원가입" />
      </div>
      <div className="div-body p-6 pb-32">
        <div className="mb-16">
          <Image
            src={require('@/image/solved_ac.png')}
            alt="solved_ac"
            width={240}
            height={39}
            className="mb-4 ml-2"
          />

          <div
            className={classNames(
              `flex p-2 h-14 mt-6 w-full rounded-xl`,
              style.box,
            )}
          >
            <CiMail className={style.icon} />
            <input
              className={classNames(`rounded-none w-full pl-2`, style.inputbox)}
              name="baekjoon"
              type="text"
              placeholder="solveAc 아이디"
              onChange={onChangeId}
            />
          </div>
          <p className={classNames(style.info, 'w-full mt-4')}>
            알고리즘 챌린지를 이용하기 위해 필요합니다.
            <br /> 해당 정보가 입력되지 않는다면 해당 서비스 이용이 제한됩니다.
          </p>
        </div>
        <div>
          <Image
            src={require('@/image/github.png')}
            alt="github"
            width={195}
            height={39}
          />
          <div
            className={classNames(
              `flex p-2 h-14 mt-6 w-full rounded-xl`,
              style.box,
            )}
          >
            <CiMail className={style.icon} />
            <input
              name="github"
              className={classNames(`rounded-none w-full pl-2`, style.inputbox)}
              type="text"
              placeholder="gitHub 아이디"
              onChange={onChangeId}
            />
          </div>
          <p className={classNames(style.info, 'w-full mt-4')}>
            Commit 챌린지를 이용하기 위해 필요합니다.
            <br /> 해당 정보가 입력되지 않는다면 해당 서비스 이용이 제한됩니다.
          </p>
        </div>
      </div>
      <div
        className={classNames(
          `font-sans text-center absolute w-full bottom-0 p-4`,
        )}
      >
        <div className="flex">
          <div className="w-1/2 p-2">
            <Button
              color="primary"
              fill={false}
              label="건너뛰기"
              onClick={onClickPass}
            />
          </div>
          <div className="w-1/2 p-2">
            <Button
              color="primary"
              fill
              label="가입하기"
              onClick={onClickJoin}
            />
          </div>
        </div>
        <div className="mt-2"> 회원 가입</div>
      </div>
    </div>
  );
};

export default signup;
