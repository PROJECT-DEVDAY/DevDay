import React, { useEffect, useState, useRef, useCallback } from 'react';
import RGL, { WidthProvider } from 'react-grid-layout';

import _ from 'lodash';
import propTypes from 'prop-types';

import http from '@/api/http';
import Container from '@/components/Container';
import { CHALLENGE_DETAIL_URL } from '@/constants';
import { getDatesStartToLast } from '@/utils';

const ReactGridLayout = WidthProvider(RGL);

const layoutProps = {
  className: 'layout',
  cols: 3,
  // rowHeight: 80,
  isDraggable: false,
  isResizable: false,
};

const SubmitList = ({ items = 40, challengeInfo, today, range, ...props }) => {
  const [curDate, setDate] = useState(today);
  const [layout, setLayout] = useState([]);

  const changeDate = e => {
    setDate(e.target.value);
  };

  const generateLayout = () => {
    return _.map(new Array(items), (item, i) => {
      const y = _.result(props, 'y') || Math.ceil(Math.random() * 4) + 1;
      return {
        x: i % 3,
        y: i % 3,
        w: 1,
        h: 1,
        i: i.toString(),
      };
    });
  };

  const generateDOM = () => {
    return _.map(_.range(items), i => {
      return (
        <div key={i} className="border-2 border-black">
          <span className="text">{i}</span>
        </div>
      );
    });
  };

  useEffect(() => {
    setLayout(generateLayout());
  }, []);

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
        >
          {range.map(item => {
            return (
              <option key={item} value={item}>
                {item}
              </option>
            );
          })}
        </select>

        <div>
          <ReactGridLayout layout={layout} {...layoutProps}>
            {generateDOM()}
          </ReactGridLayout>
        </div>
        {/*
        <div className="px-4">
          <AttendeeStatusBoxList list={LIST} />
        </div> */}
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
