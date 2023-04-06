import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import { useRouter } from 'next/router';
import Web3 from 'web3';

import http from '@/api/http';
import { Button } from '@/components/Button';
import Container from '@/components/Container';
import { CHALLENGE_CERTIFICATION_DETAIL } from '@/constants';

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
      <Container.SubPageHeader />
      <div>{user.userInfo.name}</div>

      <Container.MainFooter className={`p-4`}>
        <Button label="저장하기" onClick={handleSubmit} />
      </Container.MainFooter>
    </Container>
  );
};

export default detail;
