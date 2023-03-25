import React from 'react';

import classnames from 'classnames';
import { useRouter } from 'next/router';
import PropTypes from 'prop-types';

import style from './index.module.scss';
import { Button } from '../Button';

export const BtnFooter = ({
  content,
  className,
  label,
  color,
  disable,
  gotourl,
  ...props
}) => {
  const router = useRouter();
  const goTo = () => {
    router.push(`${gotourl}`);
  };
  return (
    <div className={style.btnFooter}>
      <p className={style.content}>{content}</p>
      <div className={classnames(style.button, !disable && style.check)}>
        <Button onClick={goTo} className="mt-3 mb-3" label={label} />
      </div>
      <div className={classnames(disable && style.check, style.text)}>
        선택하지 않은 사항이 있어요.
      </div>
    </div>
  );
};

BtnFooter.propTypes = {
  content: PropTypes.string.isRequired,
  label: PropTypes.string.isRequired,
  color: PropTypes.string,
  disable: PropTypes.bool,
};

BtnFooter.defaultProps = {
  color: null,
  disable: false,
};
