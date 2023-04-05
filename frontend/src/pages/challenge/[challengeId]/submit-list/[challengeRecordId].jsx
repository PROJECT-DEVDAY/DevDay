import { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';

import Image from 'next/image';
import { useRouter } from 'next/router';
import Swal from 'sweetalert2';

import http from '@/api/http';
import { Button } from '@/components/Button';
import Container from '@/components/Container';
import {
  CHALLENGE_USER_RECORD_DETAIL_URL,
  CHALLENGE_USER_RECORD_REPORT_URL,
} from '@/constants';

const Page = () => {
  const router = useRouter();
  const user = useSelector(state => state.user);
  const { challengeId, challengeRecordId } = router.query;
  const [record, setRecord] = useState(null);

  const fetchRecord = async id => {
    try {
      const { data } = http.get(CHALLENGE_USER_RECORD_DETAIL_URL(id));
      setRecord(data.data);
    } catch (e) {
      console.error(e);
    }
  };

  useEffect(() => {
    if (challengeRecordId) fetchRecord(challengeRecordId);
  }, [challengeRecordId]);

  const report = async () => {
    try {
      const { data } = await http.post(CHALLENGE_USER_RECORD_REPORT_URL, {
        userId: user.userInfo.userId,
        challengeRecordId,
        challengeRoomId: challengeId,
        reportDate: new Date().toISOString().split('T')[0],
      });

      Swal.fire({
        position: 'center',
        icon: 'success',
        title: '신고 성공',
        showConfirmButton: false,
        timer: 800,
      });
    } catch (e) {
      Swal.fire({
        position: 'center',
        icon: 'error',
        title: e.message,
        showConfirmButton: false,
        timer: 800,
      });
    }
  };

  return (
    <Container>
      <Container.SubPageHeader title="" />
      <Container.MainBody className="px-0 bg-white text-center">
        <h5 className="font-bold text-center text-2xl pb-4 border-b-2">
          참여자 인증 전체보기
        </h5>
        {record && (
          <div className="px-4 pt-4">
            <h5 className="text-md mb-4">
              <span className="text-xl font-bold">{record.nickname}</span>
              챌린저님
              <span className="text-xl font-bold"> 의 인증내역</span>
            </h5>
            <div className="w-full h-40 relative">
              <Image
                src={record.photoUrl}
                alt={`${record.nickname}의 ${record.createAt} 제출 사진입니다.`}
                fill
              />
            </div>
            <div className="mt-4 mb-6 text-left w-full">
              <div className="grid grid-cols-2">
                <div className="font-medium">인증 날짜</div>
                <div>{record.createAt}</div>
              </div>
            </div>
            <div>
              <div className="w-fit ml-auto">
                <Button
                  className="px-2"
                  color="danger"
                  label="신고하기"
                  onClick={report}
                />
              </div>
            </div>
          </div>
        )}
      </Container.MainBody>
      <Container.MainFooterWithNavigation />
    </Container>
  );
};

export default Page;
