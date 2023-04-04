import React, { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';

import Image from 'next/image';
import { useRouter } from 'next/router';

import http from '../../api/http';

import { Button } from '@/components/Button';
import Container from '@/components/Container';
import { InputBox } from '@/components/InputBox';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';
import { ReturnArrow } from '@/components/ReturnArrow';
import { GITHUBBAEKJOON_URL } from '@/constants';

const challengeInfo = () => {
  const router = useRouter();
  const userInfo = useSelector(state => state.user);

  const headers = {
    Authorization: userInfo.accessToken,
  };

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

  const changeChallengeInfo = () => {
    http
      .patch(
        GITHUBBAEKJOON_URL,
        {
          baekjoon,
          github,
        },
        {
          headers,
        },
      )
      .then(res => {
        router.push('/mypage');
      })
      .catch(err => {});
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
        <Button label="확인" onClick={changeChallengeInfo} />
      </Container.Footer>
    </Container>
  );
};

export default PrivateRouter(challengeInfo);
