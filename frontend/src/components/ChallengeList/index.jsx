import React from 'react';

import classNames from 'classnames';
import PropTypes from 'prop-types';

import style from './index.module.scss';

export const ChallengeList = ({ date, array, category, count, ...props }) => {
  return (
    <div className={classNames(style.algoDate, 'font-medium')}>
      <p>{date}</p>
      {array.length > 0 && category === 'ALGO' && count <= array.length && (
        <div className={style.algoNumber}>
          {array.map(item => (
            <p>{item}</p>
          ))}
        </div>
      )}
      {array.length === 0 && category === 'ALGO' && (
        <div className={style.algoZero}>
          <p>
            푼 문제가 <br /> 없습니다.
          </p>
        </div>
      )}
      {array.length > 0 && category === 'ALGO' && count > array.length && (
        <div className={style.algoZero}>
          {array.map(item => (
            <p>{item}</p>
          ))}
        </div>
      )}
      {category === 'COMMIT' && array > 0 && count <= array && (
        <div className={style.CommitNumber}>
          <p>{array}회</p>
        </div>
      )}
      {category === 'COMMIT' && array === 0 && (
        <div className={style.commitZero}>
          <p>실패</p>
        </div>
      )}
      {category === 'COMMIT' && array > 0 && count > array && (
        <div className={style.commitZero}>
          <p>{array}회</p>
        </div>
      )}
    </div>
  );
};

ChallengeList.propTypes = {
  date: PropTypes.string,
};
