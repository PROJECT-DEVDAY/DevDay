import React, { useEffect, useState, useRef, useCallback } from 'react';
import RGL, { WidthProvider } from 'react-grid-layout';

import Image from 'next/image';
import { useRouter } from 'next/router';
import propTypes from 'prop-types';

import http from '@/api/http';
import Container from '@/components/Container';
import { CHALLENGE_DETAIL_URL, CHALLENGE_SUBMIT_RECORD_URL } from '@/constants';
import { getDatesStartToLast } from '@/utils';

const SubmitList = ({ challengeInfo, today, range }) => {
  const [curDate, setDate] = useState(today);
  const [item, setItem] = useState([]);
  const router = useRouter();

  const changeDate = e => {
    setDate(e.target.value);
  };

  useEffect(() => {
    if (curDate && challengeInfo) {
      http
        .get(CHALLENGE_SUBMIT_RECORD_URL(challengeInfo.id), {
          params: {
            date: curDate,
          },
        })
        .then(({ data }) => {
          setItem(data.data);
        })
        .catch(e => {
          console.error(e);
        });
    }
  }, [curDate, challengeInfo]);

  const isEmpty = item.length === 0;
  return (
    <Container>
      <Container.SubPageHeader title={challengeInfo.title} />
      <Container.MainBody className="px-0 bg-white text-center">
        <h5 className="font-bold text-center text-2xl pb-4 border-b-2">
          참여자 인증 상세보기
        </h5>
        <select
          name="date"
          id="date-picker"
          onChange={changeDate}
          defaultValue={curDate}
          className="my-4"
        >
          {range.map(r => (
            <option key={r} value={r}>
              {r}
            </option>
          ))}
        </select>

        <div>
          {isEmpty && (
            <div>
              <p className="font-bold">업로드 된 인증샷이 없습니다.</p>
            </div>
          )}
          <div className="grid grid-cols-3 gap-2">
            {item.map((d, i) => {
              const goToRecord = id => {
                router.push(`/challenge/${challengeInfo.id}/submit-list/${id}`);
              };
              return (
                <div
                  onClick={() => goToRecord(d.challengeRecordId)}
                  className="relative w-full h-24 border-2"
                >
                  <Image fill src={d.photoUrl} alt="user-submit-record" />
                </div>
              );
            })}
          </div>
        </div>
      </Container.MainBody>
      <Container.MainFooterWithNavigation />
    </Container>
  );
};

SubmitList.defaultProps = {
  challengeInfo: {
    id: 1,
    title: '1일 1회의',
  },
  today: '2023-04-05',
};

SubmitList.propTypes = {
  challengeInfo: propTypes.shape({
    id: propTypes.number,
    title: propTypes.string,
  }),
  today: propTypes.string,
};

export const getServerSideProps = async context => {
  const { challengeId } = context.query;
  try {
    const { data } = await http.get(`${CHALLENGE_DETAIL_URL}/${challengeId}`);
    let today = new Date().toISOString().split('T')[0];
    const range = getDatesStartToLast(data.startDate, data.endDate);

    // 날짜 validation
    if (new Date(today) > new Date(range[range.length - 1])) {
      today = range[range.length - 1];
    } else if (new Date(today) < new Date(range[0])) {
      [today] = range;
    }

    return {
      props: {
        challengeInfo: {
          title: data.title,
          id: data.id,
        },
        today,
        range,
      },
    };
  } catch (e) {
    console.error('challenge-service로부터 정보를 조회할 수 없어요!', e);
  }

  return {
    props: {
      challengeInfo: null,
      today: null,
      range: [],
    },
  };
};

export default SubmitList;

// [
//   {
//     challengeRecordId: 7,
//     createAt: '2023-04-03',
//     photoUrl:
//       'https://devday-bucket.s3.ap-northeast-2.amazonaws.com/default_image/1c2f8604-3db8-4cd5-a485-4e9cd4eec55e-Free_Default_Url.png',
//   },
//   {
//     challengeRecordId: 8,
//     createAt: '2023-04-03',
//     photoUrl:
//       'https://devday-bucket.s3.ap-northeast-2.amazonaws.com/default_image/1c2f8604-3db8-4cd5-a485-4e9cd4eec55e-Free_Default_Url.png',
//   },
//   {
//     challengeRecordId: 9,
//     createAt: '2023-04-03',
//     photoUrl:
//       'https://devday-bucket.s3.ap-northeast-2.amazonaws.com/default_image/1c2f8604-3db8-4cd5-a485-4e9cd4eec55e-Free_Default_Url.png',
//   },
// ]
