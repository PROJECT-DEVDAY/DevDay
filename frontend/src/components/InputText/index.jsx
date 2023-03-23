import React from 'react';

import classnames from 'classnames';
import PropTypes from 'prop-types';

import style from './index.module.scss';

export const InputText = ({ labelName, content }) => {
  return (
    <div className="w-full mb-5">
      <div className={classnames('font-bold', style.LabelName)}>
        {labelName}
      </div>
      <input
        type="text"
        className={classnames(style.InputText)}
        placeholder={content}
      />
    </div>
  );
};

InputText.propTypes = {
  labelName: PropTypes.string,
  content: PropTypes.string,
};

InputText.defaultProps = {
  labelName: '비밀번호',
  content: null,
};
