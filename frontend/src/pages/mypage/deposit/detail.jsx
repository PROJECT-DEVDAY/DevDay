import { useEffect, useState } from 'react';

import classNames from 'classnames';

import detailStyle from './detail.module.scss';
import historyStyle from './history.module.scss';

import http from '@/api/http';
import { Button } from '@/components/Button';
import Container from '@/components/Container';
import { HeaderButtons } from '@/components/HeaderButtons';
import { DEPOSIT_WITHDRAW_URL, CHALLENGE_DETAIL_URL } from '@/constants';

const History = ({ history = {} }) => {
  const { userId, amount, createdAt, transaction_history_id, type } = history;
  const [data, setData] = useState('');
  const [check, setCheck] = useState([]);
  if (!check.includes(transaction_history_id)) {
    http.get(`${CHALLENGE_DETAIL_URL}/${transaction_history_id}`).then(res => {
      setData(res.data);

      setCheck(checkValue => {
        return [...checkValue, transaction_history_id];
      });
    });
  }
  console.log(check);
  // 입금일 경우
  if (type === 'REFUND') {
    return (
      <div id={transaction_history_id} className={historyStyle.Item}>
        <div className="flex align-center font-medium text-lg">
          <div className="flex-1 whitespace-nowrap">
            {new Date(createdAt).toLocaleDateString()}
          </div>
          <div className="font-medium text-lg text-right pl-4">
            <p>{data.title}</p>
          </div>
        </div>
        <div className="flex align-center">
          <div className="flex-1 shrink-0">예치금 환급</div>
          <div className="flex-1 shrink-0 font-medium text-lg text-right text-blue-700">
            + {amount}원
          </div>
        </div>
      </div>
    );
  }
  if (type === 'PAY') {
    return (
      <div id={transaction_history_id} className={historyStyle.Item}>
        <div className="flex align-center font-medium text-lg">
          <div className="flex-1 whitespace-nowrap">
            {new Date(createdAt).toLocaleDateString()}
          </div>
          <div className="font-medium text-lg text-right pl-4">
            <p>{data.title}</p>
          </div>
        </div>
        <div className="flex align-center ">
          <div className="flex-1 shrink-0">예치금 사용</div>
          <div className="flex-1 shrink-0 font-medium text-lg text-right text-red-700">
            - {amount}원
          </div>
        </div>
      </div>
    );
  }
  if (type === 'CHARGE') {
    return (
      <div id={transaction_history_id} className={historyStyle.Item}>
        <div className="flex align-center font-medium text-lg">
          <div className="flex-1 whitespace-nowrap">
            {new Date(createdAt).toLocaleDateString()}
          </div>
          <div className="font-medium text-lg text-right pl-4">
            <p>{data.title}</p>
          </div>
        </div>
        <div className="flex align-center">
          <div className="flex-1 shrink-0">예치금 충전</div>
          <div className="flex-1 shrink-0 font-medium text-lg text-right text-blue-700">
            + {amount}원
          </div>
        </div>
      </div>
    );
  }
  if (type === 'CANCEL') {
    return (
      <div id={transaction_history_id} className={historyStyle.Item}>
        <div className="flex align-center font-medium text-lg">
          <div className="flex-1 whitespace-nowrap">
            {new Date(createdAt).toLocaleDateString()}
          </div>
          <div className="font-medium text-lg text-right pl-4">
            <p>{data.title}</p>
          </div>
        </div>
        <div className="flex align-center">
          <div className="flex-1 shrink-0">예치금 환불</div>
          <div className="flex-1 shrink-0  text-lg font-medium text-right text-red-700">
            - {amount}원
          </div>
        </div>
      </div>
    );
  }
};

const NAV = {
  전체: '',
  환금: 'refund',
  사용: 'pay',
  충전: 'charge',
  환불: 'cancel',
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
      const { data } = await http.get(DEPOSIT_WITHDRAW_URL, {
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
      <Container.SubPageHeader
        title="예치금 내역 조회"
        className="border-b-2"
      />
      <Container.MainBody className={classNames('relative')}>
        <div className="pt-4 pb-4 mb-4 bg-white">
          <HeaderButtons
            buttonClassName="border-2 border-black"
            items={NAV_LIST}
            select={type}
            setSelect={selectType}
          />
        </div>
        <div className={classNames(detailStyle.List)}>
          {histories.map(history => (
            <div className="mb-4">
              <History history={history} key={history.history_id} />
            </div>
          ))}
        </div>
      </Container.MainBody>
      <Container.MainFooter
        className={classNames(
          detailStyle.Pagenation,
          'text-center font-bold flex items-center w-full p-4',
        )}
      >
        <div className="shrink-0 mr-4">{`${currentPage} / ${totalPages}`}</div>
        <div className="grid grid-cols-2 gap-2 flex-1">
          <Button
            className=""
            label="이전"
            disabled={currentPage <= 1}
            onClick={movePrevPage}
          />
          <Button
            className=""
            label="다음"
            disabled={currentPage >= totalPages}
            onClick={moveNextPage}
          />
        </div>
      </Container.MainFooter>
    </Container>
  );
};

export default Detail;
