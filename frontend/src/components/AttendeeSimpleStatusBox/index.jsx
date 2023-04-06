import React from 'react';
import { AiOutlineCheck } from 'react-icons/ai';

import cx from 'classnames';

const AttendeeSimpleStatusBox = ({ className, name, check }) => {
  return (
    <div
      className={cx(
        'flex flex-col justify-center align-center rounded-lg border-2 border-black p-2',
        className,
      )}
    >
      <div className="shrink-0 font-bold mb-2 h-20">
        {check && (
          <AiOutlineCheck className="ml-auto mr-auto text-blue-700" size={80} />
        )}
      </div>
      <div>
        <span className="ml-auto mr-auto font-bold text-2xl">{name}</span>
      </div>
    </div>
  );
};

export default AttendeeSimpleStatusBox;
