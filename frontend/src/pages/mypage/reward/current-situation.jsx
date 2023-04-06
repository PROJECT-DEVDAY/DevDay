import { useEffect, useState } from 'react';

import http from '@/api/http';
import Container from '@/components/Container';
import { PRIZE_SUMMARY_URL, LOCALE } from '@/constants';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';

const CurrentSituation = () => {
  const [myData, setMyData] = useState(null);

  const requestInfo = async () => {
    const { data } = await http.get(PRIZE_SUMMARY_URL);
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
      <Container.SubPageHeader title="상금 사용 현황" />
      <Container.MainBody className="px-8 pt-8 bg-white">
        <div>
          <div className="grid grid-cols-2 font-medium text-lg my-4">
            <p>획득 상금</p>
            <p className="text-right">
              {new Intl.NumberFormat(LOCALE).format(myData.in)}원
            </p>
          </div>
          <div className="grid grid-cols-2 font-medium text-lg my-4">
            <p>인출한 상금</p>
            <p className="text-right text-red-600">
              -{new Intl.NumberFormat(LOCALE).format(myData.out)}원
            </p>
          </div>
          <div className="grid grid-cols-2 font-bold text-xl py-4  border-t-2">
            <p>현재 보유 상금</p>
            <p className="text-right">
              {new Intl.NumberFormat(LOCALE).format(myData.in - myData.out)}원
            </p>
          </div>
        </div>
      </Container.MainBody>
    </Container>
  );
};

export default PrivateRouter(CurrentSituation);
