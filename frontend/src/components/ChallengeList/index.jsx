import React from 'react';

import classNames from 'classnames';
import PropTypes from 'prop-types';

import style from './index.module.scss';

export const ChallengeList = ({ date, array, category, ...props }) => {
  return (
    <div className={classNames(style.algoDate, 'font-medium')}>
      <p>{date}</p>
      {array.length > 0 && category === 'ALGO' && (
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
      {category === 'COMMIT' && array > 0 && (
        <div className={style.CommitNumber}>
          <p>{array}회</p>
        </div>
      )}
      {category === 'COMMIT' && array === 0 && category === 'COMMIT' && (
        <div className={style.commitZero}>
          <p>실패</p>
        </div>
      )}
    </div>
  );
};

ChallengeList.propTypes = {
  date: PropTypes.string,
};
