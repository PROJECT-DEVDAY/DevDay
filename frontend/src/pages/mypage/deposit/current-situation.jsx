import { useEffect, useState } from 'react';

import http from '@/api/http';
import Container from '@/components/Container';
import { DEPOSIT_SUMMARY_URL, LOCALE } from '@/constants';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';

const CurrentSituation = () => {
  const [myData, setMyData] = useState(null);

  const requestInfo = async () => {
    const { data } = await http.get(DEPOSIT_SUMMARY_URL);
    console.log(data);
    setMyData(data.data);
  };

  useEffect(() => {
    requestInfo();
  }, []);

  if (!myData) {
    return null;
  }

  return (
    <Container>
      <Container.SubPageHeader title="예치금 사용 현황" />
      <Container.MainBody className="px-8 pt-8 bg-white break-keep">
        <div>
          <div className="grid grid-cols-2 font-medium text-lg my-4">
            <p>총 예치금</p>
            <p className="text-right">
              {new Intl.NumberFormat(LOCALE).format(myData.charge)}원
            </p>
          </div>
          <div className="grid grid-cols-2 font-medium text-lg my-4">
            <p>환불받은 예치금</p>
            <p className="text-right text-red-600">
              -{new Intl.NumberFormat(LOCALE).format(myData.cancel)}원
            </p>
          </div>
          <div className="grid grid-cols-2 font-medium text-lg my-4">
            <p>벌금</p>
            <p className="text-right text-red-600">
              -{new Intl.NumberFormat(LOCALE).format(myData.penalty)}원
            </p>
          </div>
          <div className="grid grid-cols-2 font-bold text-xl py-4  border-t-2">
            <p>진행중인 챌린지 예치금</p>
            <p className="text-right">
              {new Intl.NumberFormat(LOCALE).format(myData.challenging)}원
            </p>
          </div>
        </div>
      </Container.MainBody>
    </Container>
  );
};

export default PrivateRouter(CurrentSituation);
