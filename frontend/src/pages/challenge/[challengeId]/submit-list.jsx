import React from 'react';

import Footer from '@/components/Footer';
import { ReturnArrow } from '@/components/ReturnArrow';

const SubmitList = () => {
  const challengeInfo = {
    id: 1,
    name: '1일 1회의',
  };

  const userInfo = {
    id: 1,
    name: 'pthwan',
  };

  return (
    <div className="font-medium">
      <div className="style.div-header sticky top-0">
        <ReturnArrow title={challengeInfo.name} />
      </div>
      <div className="div-body p-6 pb-12 mt-8" />
      <div className="sticky w-full bottom-0 m-0">
        <Footer />
      </div>
    </div>
  );
};

export default SubmitList;
