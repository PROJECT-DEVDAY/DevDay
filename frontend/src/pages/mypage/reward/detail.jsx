import { useEffect, useState } from 'react';

import http from '@/api/http';
import Container from '@/components/Container';
import { PRIZE_WITHDRAW_URL } from '@/constants';
import { HeaderButtons } from '@/components/HeaderButtons';
import historyStyle from './history.module.scss';
import detailStyle from './detail.module.scss';
import classNames from 'classnames';
import { Button } from '@/components/Button';
import PrivateRouter from '@/components/PrivateRouter/PrivateRouter';

const History = ({ history = {} }) => {
  const {
    userId,
    amount,
    account,
    createdAt,
    history_id: historyId,
    challenge,
    type,
  } = history;

  // 입금일 경우
  if (type === 'IN') {
    return (
      <div id={historyId} className={historyStyle.Item}>
        <div className={'flex align-center font-medium text-lg'}>
          <div className="flex-1">
            {new Date(createdAt).toLocaleDateString()}
          </div>

          <div className="flex-1 shrink-0">{'상금 획득'}</div>
          <div className="flex-1 shrink-0 text-right text-blue-700">
            + {amount}
          </div>
        </div>
        <div className="text-sm text-right">
          <p>{`${challenge.title}`}</p>
          <p>{`(${challenge.startDate}~${challenge.endDate})`}</p>
        </div>
      </div>
    );
  }

  // 출금일 경우
  return (
    <div id={historyId} className={historyStyle.Item}>
      <div className={'flex align-center font-medium text-lg'}>
        <div className="flex-1">{new Date(createdAt).toLocaleDateString()}</div>

        <div className="flex-1 shrink-0">{'상금 출금'}</div>
        <div className="flex-1 shrink-0 text-right text-red-700">
          - {amount}
        </div>
      </div>
      <div className="text-sm text-right">
        <p>{`${account?.number} ${account?.depositor}`}</p>
      </div>
    </div>
  );
};

const NAV = {
  전체: '',
  획득: 'in',
  출금: 'out',
};
const NAV_LIST = Object.keys(NAV);
const INITIAL_NAV_KEY = '전체';
const DEFAULT_PARAMS = {
  page: 0,
  size: 6,
  type: '',
};
const Detail = () => {
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [histories, setHistories] = useState([]);
  const [type, setType] = useState(INITIAL_NAV_KEY);

  const search = async (query, fetchPage) => {
    try {
      const { data } = await http.get(PRIZE_WITHDRAW_URL, {
        params: {
          ...DEFAULT_PARAMS,
          ...query,
        },
      });
      setHistories(data.data.content);
      if (fetchPage) {
        setTotalPages(data.data.totalPages + 1);
        setCurrentPage(data.data.page + 1);
      }
    } catch (e) {
      console.error(e);
    }
  };

  const selectType = value => {
    setType(value);
    search(
      {
        ...DEFAULT_PARAMS,
        type: NAV[value],
      },
      true,
    );
  };

  const movePrevPage = () => {
    setCurrentPage(prev => prev - 1);
    search(
      {
        page: currentPage - 1,
        type: NAV[type],
      },
      false,
    );
  };

  const moveNextPage = () => {
    setCurrentPage(prev => prev + 1);
    search(
      {
        page: currentPage,
        type: NAV[type],
      },
      false,
    );
  };

  useEffect(() => {
    search(
      {
        page: 0,
        type: NAV[INITIAL_NAV_KEY],
      },
      true,
    );
  }, []);

  return (
    <Container>
      <Container.SubPageHeader title="상금 내역 조회" className="border-b-2" />
      <Container.MainBody
        className={classNames('bg-white relative', detailStyle.Body)}
      >
        <div className="pt-4 pb-8">
          <HeaderButtons
            buttonClassName="border-2 border-black px-8"
            items={NAV_LIST}
            select={type}
            setSelect={selectType}
          />
        </div>
        <div className={classNames(detailStyle.List)}>
          {histories.map(history => (
            <History history={history} key={history.history_id} />
          ))}
        </div>
        <div
          className={classNames(
            detailStyle.Pagenation,
            'text-center font-bold flex items-center w-full',
          )}
        >
          <div className="shrink-0 mr-4">{`${currentPage} / ${totalPages}`}</div>
          <div className="grid grid-cols-2 gap-2 flex-1">
            <Button
              className=""
              label={'이전'}
              disabled={currentPage <= 1}
              onClick={movePrevPage}
            />
            <Button
              className=""
              label={'다음'}
              disabled={currentPage >= totalPages}
              onClick={moveNextPage}
            />
          </div>
        </div>
      </Container.MainBody>
    </Container>
  );
};

export default PrivateRouter(Detail);
