import React from 'react';

import { useRouter } from 'next/router';

import Footer from '@/components/Footer';
import { HeaderButtons } from '@/components/HeaderButtons';
import { ReturnArrow } from '@/components/ReturnArrow';

const challenge = () => {
  const router = useRouter();

  return (
    <div>
      <div className="style.div-header sticky top-0">
        <ReturnArrow title="진행중인 챌린지" />
      </div>
      <div className="py-6 px-4">
        <HeaderButtons />
      </div>

      <div className="sticky w-full bottom-0 m-0">
        <Footer />
      </div>
    </div>
  );
};

export default challenge;
