import React from 'react';
import { AiOutlineCheck } from 'react-icons/ai';

import cx from 'classnames';

const AttendeeStatusBox = ({ className, date, name, check, count = 0 }) => {
  return (
    <div
      className={cx(
        'flex justify-center items-center rounded-lg border-2 border-black px-3 py-4',
        className,
      )}
    >
      <div className="mr-auto">
        <div className="text-left">
          <p className="font-medium">
            <span className="font-bold text-2xl mr-2">{name}</span>
            <span className="relative">
              챌린저님
              {count > 0 && (
                <span className="float-right">
                  <span className="font-bold ml-auto text-red-600">4</span>건
                </span>
              )}
            </span>
          </p>

          <p>
            {date && <span className="mr-2">{date}</span>}
            {check ? '인증' : '미인증'}
          </p>
        </div>
      </div>
      <div className="shrink-0 text-3xl font-bold">
        {check && <AiOutlineCheck className="text-blue-700" size={50} />}
      </div>
    </div>
  );
};
export default AttendeeStatusBox;
