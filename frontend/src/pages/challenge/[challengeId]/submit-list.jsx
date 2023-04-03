import React, { useEffect, useState, useRef, useCallback } from 'react';
import _ from 'lodash';
import RGL, { WidthProvider } from 'react-grid-layout';

import Container from '@/components/Container';

const ReactGridLayout = WidthProvider(RGL);

const challengeInfo = {
  id: 1,
  name: '1일 1회의',
};

const layoutProps = {
  className: 'layout',
  cols: 3,
  // rowHeight: 80,
  isDraggable: false,
  isResizable: false,
};

const SubmitList = ({ items = 40, ...props }) => {
  const observerRef = useRef();
  const bodyRef = useRef();
  const [layout, setLayout] = useState([]);

  const ioCallback = useCallback((entries, io) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        io.unobserve(entry.target);
        // TODO: 새로운 리스트 가져오기
        setTimeout(() => {
          // 새로운 리스트 가져오기 호출
          // io.observe 넣기
          console.log('아이템 넣기');
        }, 1500);
      }
    });
  }, []);

  useEffect(() => {
    observerRef.current = new IntersectionObserver(ioCallback, {
      threshold: 0.3,
    });
  }, []);

  const observeItem = ref => {
    observerRef.current.observe(ref);
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
      <Container.SubPageHeader title={challengeInfo.name} />
      <Container.MainBody ref={bodyRef} className="px-0 bg-white text-center">
        <h5 className="font-bold text-center text-2xl pb-4 border-b-2">
          참여자 인증 상세보기
        </h5>
        <input type="date" className="font-bold" />
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

export default SubmitList;
