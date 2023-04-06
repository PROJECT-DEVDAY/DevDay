import React, { useState, useEffect } from 'react';

import Web3 from 'web3';

// import MyContract from './MyContract.json';

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

const web32 = new Web3(Web3.givenProvider);

web32.eth.net
  .getNetworkType()
  .then(networkType => {
    if (networkType === 'main') {
      console.log('Connected to Mainnet');
    } else {
      console.log('Connected to Testnet');
    }
  })
  .catch(error => {
    console.error('Error while getting network type:', error);
  });

const contractInstance = new web3.eth.Contract(
  contractABI,
  '0x04598B755dccdf1Da5680E2C20C233eCb602Ea96',
  // {
  //   gasPrice: '3000000', // default gas price in wei, 20 gwei in this case
  // },
);

function App() {
  const [account, setAccount] = useState('');
  const [challenges, setChallenges] = useState([]);
  const [userChallengeId, setUserChallenge] = useState('');
  const [title, setTitle] = useState('');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [name, setName] = useState('');
  const [successRate, setSuccessRate] = useState('');

  const handleSubmit = async e => {
    e.preventDefault();
    if (
      userChallengeId !== '' &&
      title !== '' &&
      startDate !== '' &&
      endDate !== '' &&
      name !== '' &&
      successRate !== ''
    ) {
      await contractInstance.methods
        .addChallenge(
          userChallengeId,
          title,
          startDate,
          endDate,
          name,
          successRate,
        )
        .send({ from: account });
      setUserChallenge('');
      setTitle('');
      setStartDate('');
      setEndDate('');
      setName('');
      setSuccessRate('');
    } else {
      window.alert('Please fill all the details!');
    }
  };

  useEffect(() => {
    const getAccount = async () => {
      const accounts = await web3.eth.getAccounts();
      console.log(accounts[0]);
      console.log(contractInstance);
      setAccount(accounts[0]);

      console.log(Web3.givenProvider);
    };

    getAccount();

    // const getChallenges = async () => {
    //   // const challengeCount = await contractInstance.methods
    //   //   .getChallengeCount()
    //   //   .call();
    //   const challengess = await Promise.all(
    //     Array(parseInt(4))
    //       .fill()
    //       .map((_, index) => contractInstance.methods.challenges(index).call()),
    //   );
    //   setChallenges(challengess);
    // };

    const getChallenges = async () => {
      console.log('값좀 가져와바 ');
      const walletAddress = await web3.eth
        .getAccounts()
        .then(accounts => accounts[0]);
      console.log('@@@@@@@@@@@@@@@@');
      const challengess = await contractInstance.methods
        .getChallengesForAddress(walletAddress)
        .call();

      console.log(`${challengess} 가져와보세요`);
      setChallenges(challengess);
    };

    getChallenges().then(console.log(challenges));
  }, []);

  return (
    <div className="App">
      <h1>Challenges</h1>
      <form onSubmit={handleSubmit}>
        <input
          type="number"
          placeholder="userChallengeId"
          value={userChallengeId}
          onChange={e => setUserChallenge(e.target.value)}
        />
        <br />
        <input
          type="text"
          placeholder="title"
          value={title}
          onChange={e => setTitle(e.target.value)}
        />
        <br />
        <input
          type="text"
          placeholder="startDate"
          value={startDate}
          onChange={e => setStartDate(e.target.value)}
        />
        <br />
        <input
          type="text"
          placeholder="end Date"
          value={endDate}
          onChange={e => setEndDate(e.target.value)}
        />
        <input
          type="text"
          placeholder="name"
          value={name}
          onChange={e => setName(e.target.value)}
        />
        <input
          type="text"
          placeholder="successRate"
          value={successRate}
          onChange={e => setSuccessRate(e.target.value)}
        />
        <br />
        <button type="submit">Add Challenge</button>
      </form>
      <br />
      <h2>Challenges List</h2>
      <ul>
        {challenges.map((challenge, index) => (
          <li key={index}>
            <p>userChallengeId: {challenge.userChallengeId}</p>
            <p>Title: {challenge.title}</p>
            <p>startDate: {challenge.startDate}</p>
            <p>end Date: {challenge.endDate}</p>
            <p>name : {challenge.name}</p>
            <p>success : {challenge.successRate}</p>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App;
