import React, { useState } from 'react';
import { ethers } from 'ethers';

function App() {
  const [address, setAddress] = useState('');

  const connectWallet = async () => {
    // Check if the user has Coinbase Wallet installed
    if (window.ethereum && window.ethereum.isCoinbaseWallet) {
      try {
        // Request access to the user's Coinbase Wallet account
        await window.ethereum.request({ method: 'eth_requestAccounts' });

        // Create an ethers provider and get the user's address
        const provider = new ethers.providers.Web3Provider(window.ethereum);
        const signer = provider.getSigner();
        const connectedAddress = await signer.getAddress();
        setAddress(connectedAddress);
      } catch (error) {
        console.error(error);
      }
    } else {
      console.error('Coinbase Wallet is not installed');
    }
  };

  return (
    <div>
      <button onClick={connectWallet}>Connect to Coinbase Wallet</button>
      {address && <p>Your wallet address is: {address}</p>}
    </div>
  );
}

export default App;
