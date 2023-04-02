import React, { useEffect, useState } from 'react';
import Web3 from 'web3';

function App() {
  const [web3, setWeb3] = useState(null);
  const [accounts, setAccounts] = useState([]);

  useEffect(() => {
    async function loadWeb3() {
      if (window.ethereum) {
        // Connect to Metamask
        const web3 = new Web3(window.ethereum);
        setWeb3(web3);

        try {
          // Request access to user's accounts
          await window.ethereum.enable();
          const accounts = await web3.eth.getAccounts();
          setAccounts(accounts);
        } catch (error) {
          console.error(error);
        }
      }
    }
    loadWeb3();
  }, []);

  return (
    <div>
      {web3 ? (
        <div>
          <h1>Connected to Web3</h1>
          <h2>Wallet Address: {accounts[0]}</h2>
        </div>
      ) : (
        <h1>Not connected to Web3</h1>
      )}
    </div>
  );
}

export default App;
