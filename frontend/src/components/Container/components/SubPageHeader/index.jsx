import React, { memo } from 'react';
import { SlArrowLeft, SlArrowRight } from 'react-icons/sl';

import cx from 'classnames';
import { useRouter } from 'next/router';

import Header from '../Header';

const SubPageHeader = ({ sticky = true, className, title, goNext = null }) => {
  const router = useRouter();

  const goToBack = () => {
    router.back();
  };

  const goToNext = () => {
    goNext();
  };

  return (
    <Header
      className={cx(
        sticky && 'sticky top-0 touch-none',
        'bg-white flex items-center justify-between bg-white',
        className,
      )}
    >
      <button
        type="button"
        onClick={goToBack}
        className="w-8 h-8 flex items-center justify-start"
      >
        <SlArrowLeft />
      </button>
      <div id="sub-header-title" className="font-bold">
        {title}
      </div>
      <div className="w-8 h-8">
        {goNext && (
          <button
            type="button"
            onClick={goToNext}
            className="w-8 h-8 flex items-center justify-end"
          >
            <SlArrowRight />
          </button>
        )}
      </div>
    </Header>
  );
};

export default memo(SubPageHeader);
