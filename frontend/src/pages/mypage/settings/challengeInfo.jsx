import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';

import Image from 'next/image';
import { useRouter } from 'next/router';
import Swal from 'sweetalert2';

import http from '../../api/http';

import { Button } from '@/components/Button';
import Container from '@/components/Container';
import { InputBox } from '@/components/InputBox';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import { ReturnArrow } from '@/components/ReturnArrow';
import { GITHUBBAEKJOON_URL } from '@/constants';
import { addExtraId } from '@/store/user/userSlice';

const challengeInfo = () => {
  const router = useRouter();
  const dispatch = useDispatch();
  const userInfo = useSelector(state => state.user);

  const headers = {
    Authorization: userInfo.accessToken,
  };

  const [inputs, setInputs] = useState({
    baekjoon: userInfo.userInfo.baekjoon,
    github: userInfo.userInfo.github,
  });
  const { baekjoon, github } = inputs;

  const onChangeId = e => {
    const { value, name } = e.target;
    setInputs({
      ...inputs,
      [name]: value,
    });
  };

  const changeChallengeInfo = () => {
    http
      .patch(
        GITHUBBAEKJOON_URL,
        {
          github,
          baekjoon,
        },
        {
          headers,
        },
      )
      .then(async () => {
        await dispatch(addExtraId([github, baekjoon]));
        router.push('/mypage');
      })
      .catch(err => {});
  };

  const onClickChangeChallengeInfo = () => {
    Swal.fire({
      title: '변경하시겠어요?',
      text: '나중에 다시 수정할 수 있습니다!',
      icon: 'info',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: '네',
      cancelButtonText: '아니요',
    }).then(result => {
      if (result.isConfirmed) {
        changeChallengeInfo();
      }
    });
  };

  return (
    <Container>
      <Container.Header className="mb-10">
        <ReturnArrow title="Github, Solved.ac 계정 설정" />
      </Container.Header>
      <Container.Body className="m-6">
        <div className="mb-10">
          <Image
            src={require('../../../image/solved_ac.png')}
            alt="solved_ac_logo"
            className="w-60"
          />
          <InputBox
            placeholder="SOLVED.AC 아이디"
            name="baekjoon"
            onChange={onChangeId}
            defaultValue={userInfo.userInfo.baekjoon}
          />
          <p className="pt-4">
            알고리즘 챌린지를 이용하기 위해 필요합니다. <br /> 해당 정보가
            입력되지 않는다면 해당 서비스 이용이 제한됩니다.
          </p>
        </div>
        <div>
          <Image
            src={require('../../../image/github.png')}
            alt="github_logo"
            className="w-60"
          />
          <InputBox
            placeholder="GitHub 아이디"
            name="github"
            onChange={onChangeId}
            defaultValue={userInfo.userInfo.github}
          />
          <p className="pt-4">
            Commit 챌린지를 이용하기 위해 필요합니다. <br /> 해당 정보가
            입력되지 않는다면 해당 서비스 이용이 제한됩니다.
          </p>
        </div>
      </Container.Body>
      <Container.Footer className="p-4">
        <Button label="확인" onClick={onClickChangeChallengeInfo} />
      </Container.Footer>
    </Container>
  );
};

export default PrivateRouter(challengeInfo);
