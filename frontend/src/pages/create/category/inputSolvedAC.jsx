import React, { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';

import classNames from 'classnames';
import Image from 'next/image';
import { useRouter } from 'next/router';
import Swal from 'sweetalert2';

import style from './inputSolvedAc.module.scss';

import http from '@/api/http';
import { BtnFooter } from '@/components/BtnFooter';
import Container from '@/components/Container';
import { InputBox } from '@/components/InputBox';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import { GITHUBBAEKJOON_URL } from '@/constants';
import { addExtraId } from '@/store/user/userSlice';

const inputSolvedAc = props => {
  const [solvedAcId, setSolvedAcId] = useState('');
  const onChangeSolvedAc = e => {
    setSolvedAcId(e.target.value);
  };
  const dispatch = useDispatch();
  const user = useSelector(state => state.user);
  const githubId = user.userInfo.github;
  // const [userInfomation, SetUserInfomation] = useState(user.userInfo);

  const router = useRouter();

  const onClickSaveSolvedAcId = async () => {
    await http
      .patch(GITHUBBAEKJOON_URL, { github: githubId, baekjoon: solvedAcId })
      .then(async () => {
        dispatch(addExtraId([githubId, solvedAcId]));

        await Swal.fire({
          position: 'center',
          icon: 'success',
          title: '성공!',
          showConfirmButton: false,
          timer: 1500,
        });

        router.push('/create/algo');
      })
      .catch(error => {
        Swal.fire({
          icon: 'error',
          title: '다시 한번 확인해주세요!',
          text: error.message,
        });
      });
  };
  return (
    <Container>
      <Container.SubPageHeader title="추가 정보 입력" />

      <Container.MainBody className="mt-16">
        <div className="text-lg ml-2 font-medium">
          알고리즘 챌린지를 위해서는 <br /> SolvedAc 아이디를 입력하셔야 <br />
          합니다.
        </div>

        <Image
          src={require('@/image/solved_ac.png')}
          alt="solvedAc"
          className="w-1/2 ml-2 mt-6"
        />
        <InputBox
          placeholder="solvedAc 아이디"
          onChange={onChangeSolvedAc}
          value={solvedAcId}
        />
        <div className="mt-4 ml-2 text-sm">
          알고리즘 챌린지를 이용하기 위해서 필요합니다
          <br />
          해당 정보가 입력되지 않는다면 해당 서비스
          <br /> 이용이 제합됩니다.
        </div>
      </Container.MainBody>
      <Container.Footer>
        <div
          className={classNames(
            style.btn,
            `text-center absolute w-full bottom-0 pb-4 m-0`,
          )}
        >
          <BtnFooter
            content=""
            label="다음"
            disable={!!solvedAcId}
            onClick={onClickSaveSolvedAcId}
            warningMessage="알고리즘 챌린지는 solved Ac ID가 필요해요."
          />
        </div>
      </Container.Footer>
    </Container>
  );
};
export default PrivateRouter(inputSolvedAc);
