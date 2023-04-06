import React from 'react';

import classNames from 'classnames';
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
  goToUrl,
  type,
  warningMessage,
  ...props
}) => {
  const router = useRouter();
  const goTo = () => {
    if (props.onClick) {
      props.onClick();
    }

    if (goToUrl) {
      router.push(goToUrl);
    }
  };
  return (
    <div className={style.btnFooter}>
      {content && <p className={style.content}>{content}</p>}
      <div className={classNames(`p-4`, style.button, !disable && style.check)}>
        <Button onClick={goTo} label={label} type={type} />
      </div>
      <div className={classNames(disable && style.check, style.text)}>
        {warningMessage}
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
