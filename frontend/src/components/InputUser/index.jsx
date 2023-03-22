import React from 'react';

import classnames from 'classnames';
import PropTypes from 'prop-types';

import style from './index.module.scss';

export default function InputUser({
  labelName,
  content,
  className,
  label,
  ...props
}) {
  return (
    <div>
      <div className="relative mb-3 xl:w-96" data-te-input-wrapper-init>
        <input
          type="text"
          className={classnames(
            style.InputUser,
            'peer block min-h-[auto] w-full bg-transparent py-[0.32rem] px-3 leading-[1.6] outline-none transition-all duration-200 ease-linear focus:placeholder:opacity-100 data-[te-input-state-active]:placeholder:opacity-100 motion-reduce:transition-none dark:text-neutral-200 dark:placeholder:text-neutral-200 [&:not([data-te-input-placeholder-active])]:placeholder:opacity-0',
          )}
          value={content}
          id="exampleFormControlInput1"
          placeholder="Example label"
        />
        <label
          htmlFor="exampleFormControlInput1"
          className={classnames(
            'pointer-events-none absolute top-0 left-3 mb-0 max-w-[90%] origin-[0_0] truncate pt-[0.37rem] leading-[1.6] text-neutral-500 transition-all duration-200 ease-out peer-focus:-translate-y-[0.9rem] peer-focus:scale-[0.8] peer-focus:text-primary peer-data-[te-input-state-active]:-translate-y-[0.9rem] peer-data-[te-input-state-active]:scale-[0.8] motion-reduce:transition-none dark:text-neutral-200 dark:peer-focus:text-neutral-200',
          )}
        >
          {labelName}
        </label>
      </div>
    </div>
  );
}

InputUser.propTypes = {
  labelName: PropTypes.string,
  content: PropTypes.string,
};

InputUser.defaultProps = {
  labelName: '비밀번호',
  content: null,
};
