import React from 'react';

import AttendeeStatusBox from '@/components/AttendeeStatusBox';
import AttendeeStatusBoxList from '@/components/AttendeeStatusBoxList';
import Container from '@/components/Container';

const LIST = [
  {
    name: '이동준',
    date: new Date().toLocaleString(),
    check: true,
  },
  {
    name: '박태환',
    date: new Date().toLocaleString(),
    check: true,
  },
  {
    name: '최형운',
    date: new Date().toLocaleString(),
    check: true,
  },
  {
    name: '신대득',
    date: new Date().toLocaleString(),
    check: true,
  },
  {
    name: '홍금비',
    date: new Date().toLocaleString(),
    check: true,
  },
  {
    name: '김기윤',
    date: new Date().toLocaleString(),
    check: true,
  },
];
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
    <Container>
      <Container.SubPageHeader title={challengeInfo.name} />
      <Container.MainBody className="px-0 bg-white text-center">
        <h5 className="font-bold text-center text-2xl pb-4 border-b-2">
          참여자 인증 상세보기
        </h5>
        <input type="date" className="font-bold py-4" />
        <div className="px-4">
          <AttendeeStatusBoxList list={LIST} />
        </div>
      </Container.MainBody>
      <Container.MainFooterWithNavigation />
    </Container>
  );
};

export default SubmitList;
