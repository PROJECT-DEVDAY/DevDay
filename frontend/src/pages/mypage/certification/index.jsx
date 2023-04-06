import { CertificationItem } from '@/components/CertificationItem';
import Container from '@/components/Container';
import React, { useEffect, useState } from 'react';
import nickname from '../settings/nickname';
import http from '@/api/http';
import axios from 'axios';
import { MY_CHALLENGES_URL } from '@/constants';
import { getStartWithEndDate } from '@/utils';

const INITIAL_PARAMS = {
  status: 'ALL',
  size: 20,
  offset: '',
  search: '',
};

const index = () => {
  const [certificationList, setCertificationList] = useState([]);
  const user = user;

  const getCertificationList = async () => {
    const data = await http.get(MY_CHALLENGES_URL, {
      params: {
        ...INITIAL_PARAMS,
      },
    });
    console.log(data);
    setCertificationList(data.data.data);
  };
  useEffect(() => {
    getCertificationList();
  }, []);
  return (
    <Container>
      <Container.SubPageHeader title={'인증서목록'} />
      <Container.MainBody>
        <div>
          <div className="text-lg ml-2 font-medium">
            성공적으로 마친
            <br />
            챌린지를 볼 수 있습니다.
          </div>
          <div>
            홍길동님이 완료된 챌린지는 {certificationList.length}개입니다.
          </div>
        </div>
        <div>
          {certificationList &&
            certificationList.map((item, i) => {
              const { id, title, startDate, endDate, backgroundUrl } = item;

              const period = getStartWithEndDate(startDate, endDate);
              return (
                <CertificationItem
                  key={id}
                  challengeId={id}
                  imgUrl={backgroundUrl}
                  title={title}
                  period={period}
                  participants={-1}
                ></CertificationItem>
              );
            })}
        </div>
      </Container.MainBody>
    </Container>
  );
};

export default index;
