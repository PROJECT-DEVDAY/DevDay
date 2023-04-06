import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import http from '@/api/http';
import { CertificationItem } from '@/components/CertificationItem';
import Container from '@/components/Container';
import { MY_CHALLENGES_URL } from '@/constants';
import { getStartWithEndDate } from '@/utils';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';

const INITIAL_PARAMS = {
  status: 'DONE',
  size: 20,
  offset: '',
  search: '',
};
const user = useSelector(state => state.user);

const index = () => {
  const [certificationList, setCertificationList] = useState([]);

  const getCertificationList = async () => {
    const data = await http.get(MY_CHALLENGES_URL, {
      params: {
        ...INITIAL_PARAMS,
      },
    });
    setCertificationList(data.data.data);
  };
  useEffect(() => {
    getCertificationList();
  }, []);
  return (
    <Container>
      <Container.SubPageHeader title="인증서목록" />
      <Container.MainBody>
        <div className="pt-4">
          {certificationList &&
            certificationList.map((item, i) => {
              const { id, title, startDate, endDate, backgroundUrl } = item;

              const period = getStartWithEndDate(startDate, endDate);
              return (
                <div className="mb-2 p-2 border-2 border-solid border-gray bg-white rounded-md ">
                  <CertificationItem
                    key={id}
                    challengeId={id}
                    imgUrl={backgroundUrl}
                    title={title}
                    period={period}
                    participants={-1}
                  />
                </div>
              );
            })}
        </div>
      </Container.MainBody>
      <Container.MainFooter>
        <div className="p-4">
          <div className="text-lg font-medium">
            성공적으로 마친 챌린지를 볼 수 있습니다.
          </div>
          <div>
            {user.userInfo.nickname}님이 완료된 챌린지는
            <span className="text-red-500 font-bold text-lg">
              {certificationList.length}
            </span>
            개입니다.
          </div>
        </div>
      </Container.MainFooter>
    </Container>
  );
};

export default PrivateRouter(index);
