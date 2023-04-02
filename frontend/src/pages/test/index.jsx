import React, { useState, useEffect } from 'react';
import Web3 from 'web3';

function App() {
  const [walletAddress, setWalletAddress] = useState('');

  useEffect(() => {
    async function loadWeb3() {
      if (window.ethereum) {
        const web3 = new Web3(window.ethereum);
        try {
          // Request account access if needed
          await window.ethereum.enable();

          // Get the user's wallet address
          const accounts = await web3.eth.getAccounts();
          setWalletAddress(accounts[0]);
        } catch (error) {
          console.error(error);
        }
      } else {
        console.log('Please install MetaMask to use this application');
      }
    }

    loadWeb3();
  }, []);

  return (
    <div>
      <h1>My Wallet Address: {walletAddress}</h1>
    </div>
  );
}

export default App;
