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

import { persistor } from '@/pages/_app';
import http from '@/pages/api/http';
import Container from '@/components/Container';

import {
  saveExtraInfos,
  reset,
  saveExtraInfosAsync,
} from '@/store/signup/signupSlice';

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
  const signsInfoReset = async () => {
    try {
      // 리덕스 스토어 초기화
      dispatch(reset());

      // 캐시 데이터 초기화
      await persistor.purge();
    } catch (error) {
      // console.error(error);
    }
  };

  const onClickJoin = () => {
    dispatch(
      saveExtraInfosAsync({
        ...signUpInfos,
        baekjoon,
        github,
      }),
    )
      .unwrap()
      .then(data => {
        Swal.fire({
          position: 'center',
          icon: 'success',
          title: '회원가입 성공!',
          showConfirmButton: false,
          timer: 1500,
        });
      })
      .then(signsInfoReset)
      .then(router.push('/user/login'))
      .catch(error => {
        Swal.fire({
          icon: 'error',
          title: '실패!',
          text: error.message,
        });
      });
  };
  const onClickPass = () => {
    Swal.fire({
      title: '다음에\n 입력하실 건가요?',
      text: '나중에 다시 입력할 수 있습니다!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: '네',
      cancelButtonText: '아니요',
    }).then(result => {
      if (result.isConfirmed) {
        onClickJoin();
      }
    });
  };

  return (
    <Container>
      <Container.SubPageHeader title="회원가입" />
      <Container.MainBody className="mt-8">
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
              placeholder="solved.ac 아이디"
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
              placeholder="github 아이디"
              onChange={onChangeId}
            />
          </div>
          <p className={classNames(style.info, 'w-full mt-4')}>
            Commit 챌린지를 이용하기 위해 필요합니다.
            <br /> 해당 정보가 입력되지 않는다면 해당 서비스 이용이 제한됩니다.
          </p>
        </div>
      </Container.MainBody>
      <Container.MainFooter className="bg-white">
        <div
          className={classNames(
            `font-sans text-center absolute w-full bottom-0 p-4`,
          )}
        >
          <div className="flex">
            <div className="w-1/2 pr-1">
              <Button
                color="primary"
                fill={false}
                label="건너뛰기"
                onClick={onClickPass}
              />
            </div>
            <div className="w-1/2 pl-1">
              <Button
                color="primary"
                fill
                label="가입하기"
                onClick={onClickJoin}
              />
            </div>
          </div>
        </div>
      </Container.MainFooter>
    </Container>
  );
};

export default signup;
