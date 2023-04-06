import React from 'react';

import AttendeeSimpleStatusBox from '@/components/AttendeeSimpleStatusBox';
import Container from '@/components/Container';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';

const CheckList = () => {
  return (
    <Container>
      <Container.SubPageHeader title="1일 1커밋" />
      <Container.MainBody className="px-0 bg-white text-center">
        <h5 className="font-bold text-center text-2xl pb-4 border-b-2">
          참여자 인증 전체보기
        </h5>
        <div className="w-full mt-4">
          <div className="mb-4">
            <h4 className="text-left text-xl font-bold mb-4">23.03.02</h4>
            <div className="gap-1 grid grid-cols-3">
              <AttendeeSimpleStatusBox name="이동준" check />
              <AttendeeSimpleStatusBox name="박태환" check />
              <AttendeeSimpleStatusBox name="김기윤" />
              <AttendeeSimpleStatusBox name="이동준" check />
              <AttendeeSimpleStatusBox name="박태환" check />
              <AttendeeSimpleStatusBox name="김기윤" />
              <AttendeeSimpleStatusBox name="이동준" check />
              <AttendeeSimpleStatusBox name="박태환" check />
              <AttendeeSimpleStatusBox name="김기윤" />
            </div>
          </div>
          <div className="mb-4">
            <h4 className="text-left text-xl font-bold mb-4">23.03.02</h4>
            <div className="gap-1 grid grid-cols-3">
              <AttendeeSimpleStatusBox name="이동준" check />
              <AttendeeSimpleStatusBox name="박태환" check />
              <AttendeeSimpleStatusBox name="김기윤" />
            </div>
          </div>
          <div className="mb-4">
            <h4 className="text-left text-xl font-bold mb-4">23.03.02</h4>
            <div className="gap-1 grid grid-cols-3">
              <AttendeeSimpleStatusBox name="이동준" check />
              <AttendeeSimpleStatusBox name="박태환" check />
              <AttendeeSimpleStatusBox name="김기윤" />
            </div>
          </div>
        </div>
      </Container.MainBody>
    </Container>
  );
};

export default PrivateRouter(CheckList);
