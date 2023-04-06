import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import { useRouter } from 'next/router';
import Web3 from 'web3';

import http from '@/api/http';
import { Button } from '@/components/Button';
import Container from '@/components/Container';
import { CHALLENGE_CERTIFICATION_DETAIL } from '@/constants';

// import style from './detail.modules.scss';

import { getStartWithEndDate } from '@/utils';
import classNames from 'classnames';
import Image from 'next/image';

const web3 = new Web3(
  Web3.givenProvider ||
    'https://sepolia.infura.io/v3/ec420a0b4477455bbd302b977d63a3ef',
);

const contractABI = [
  {
    inputs: [
      {
        internalType: 'uint256',
        name: 'userChallengeId',
        type: 'uint256',
      },
      {
        internalType: 'string',
        name: 'title',
        type: 'string',
      },
      {
        internalType: 'string',
        name: 'startDate',
        type: 'string',
      },
      {
        internalType: 'string',
        name: 'endDate',
        type: 'string',
      },
      {
        internalType: 'string',
        name: 'successRate',
        type: 'string',
      },
      {
        internalType: 'string',
        name: 'name',
        type: 'string',
      },
    ],
    name: 'addChallenge',
    outputs: [],
    stateMutability: 'nonpayable',
    type: 'function',
  },
  {
    inputs: [
      {
        internalType: 'address',
        name: 'addr',
        type: 'address',
      },
    ],
    name: 'getChallengesForAddress',
    outputs: [
      {
        components: [
          {
            internalType: 'uint256',
            name: 'userChallengeId',
            type: 'uint256',
          },
          {
            internalType: 'string',
            name: 'title',
            type: 'string',
          },
          {
            internalType: 'string',
            name: 'startDate',
            type: 'string',
          },
          {
            internalType: 'string',
            name: 'endDate',
            type: 'string',
          },
          {
            internalType: 'string',
            name: 'successRate',
            type: 'string',
          },
          {
            internalType: 'string',
            name: 'name',
            type: 'string',
          },
        ],
        internalType: 'struct Challenge[]',
        name: '',
        type: 'tuple[]',
      },
    ],
    stateMutability: 'view',
    type: 'function',
  },
];

// blockchain
const contractInstance = new web3.eth.Contract(
  contractABI,
  '0x1958EdD63CCDB0eb3288FC1D19664b349da5B4D3',
);

export const detail = () => {
  const router = useRouter();
  const { challengeId } = router.query;

  const [challengeDetail, setChallengeDetail] = useState({
    name: '',
    title: '',
    startDate: '',
    endDate: '',
    progressRate: '',
    userChallengeId: '',
  });

  const user = useSelector(state => state.user);

  const [account, setAccount] = useState('');

  // const [userChallengeId, setUserChallenge] = useState('');
  // const [title, setTitle] = useState('');
  // const [startDate, setStartDate] = useState('');
  // const [endDate, setEndDate] = useState('');
  // const [name, setName] = useState('');
  // const [successRate, setSuccessRate] = useState('');

  useEffect(() => {
    if (challengeId) {
      http
        .post(`${CHALLENGE_CERTIFICATION_DETAIL}/${challengeId}`)
        .then(res => {
          console.log(res.data);
          setChallengeDetail(res.data);
        })
        .catch(error => {
          console.error(error);
        });
    }
  }, []);

  useEffect(() => {
    const getAccount = async () => {
      const accounts = await web3.eth.getAccounts();

      console.log(`contractInstance : ${contractInstance}`);

      setAccount(accounts[0]);
      console.log(`web3 GivenProvider : ${Web3.givenProvider}`);

      console.log(`account : ${accounts[0]}`);
      console.log(`계정정보 : ${accounts[0]}`);
    };
    getAccount();
  }, []);
  // blockchain

  const handleSubmit = async e => {
    e.preventDefault();
    if (
      // 빈값 안들ㅇ가게
      challengeDetail.userChallengeId !== '' &&
      challengeDetail.title !== '' &&
      challengeDetail.startDate !== '' &&
      challengeDetail.endDate !== '' &&
      challengeDetail.name !== '' &&
      challengeDetail.progressRate !== ''
    ) {
      await contractInstance.methods
        .addChallenge(
          challengeDetail.userChallengeId,
          challengeDetail.title,
          challengeDetail.startDate,
          challengeDetail.endDate,
          challengeDetail.name,
          challengeDetail.progressRate,
        )
        .send({ from: account });
    } else {
      window.alert('Please fill all the details!');
    }
  };

  return (
    <Container>
      <Container.SubPageHeader title={'인증서'} />

      <Container.Body className={'px-10 pb-8 w-full h-full'}>
        <div className="w-full mt-20 text-center font-bold text-xl">
          CERTIFICATE OF COMPLETION
        </div>
        <div className="w-full text-center mt-2 font-bold text-base">
          챌린지 달성 확인서
        </div>

        <div
          className="w-full mt-10 pb-1 text-center text-2xl font-bold"
          style={{ borderBottom: '3px solid black' }}
        >
          {user.userInfo.name}
        </div>
        <div
          className="w-full mt-1 text-center text-sm"
          style={{ color: 'var(--gray-extra)' }}
        >
          NAME
        </div>

        <div
          className="w-full mt-10 pb-1 text-center text-2xl font-bold"
          style={{ borderBottom: '3px solid black' }}
        >
          {challengeDetail.title}
        </div>

        <div
          className="w-full mt-1  text-center text-sm"
          style={{ color: 'var(--gray-extra)' }}
        >
          CHALLENGE
        </div>

        <div
          className="w-full mt-4 text-center text-base"
          style={{ 'font-family': 'Gmarket-Sans-Medium' }}
        >
          위의 챌린지를 성공적으로 수행했음을
          <br />
          인정하여 인증서를 수여합니다.
        </div>

        <div className="w-full mt-14 text-right text-bold text-base">
          챌린지 기간 : {challengeDetail.startDate} ~ {challengeDetail.endDate}
        </div>
        <div className="w-full mt-4 text-right text-bold text-base">
          달성률 : {challengeDetail.progressRate} %{' '}
        </div>

        <div className="imageContainer flex justify-center" style={{}}>
          <div className="w-44 h-32 mt-10 relative">
            <Image
              src={require('../../../../image/main_logo.png')}
              className="w-full"
              alt="logo"
              fill
            />
          </div>
        </div>
      </Container.Body>

      <Container.Footer>
        <Button label={'저장하기'} onClick={handleSubmit}></Button>
      </Container.Footer>
    </Container>
  );
};

export default detail;
