import React, { useRef, useEffect } from 'react';

import useResizeObserver from '@react-hook/resize-observer';
import cx from 'classnames';

const useSizeHeight = target => {
  const [size, setSize] = React.useState();

  React.useLayoutEffect(() => {
    setSize(target.current.getBoundingClientRect().height);
  }, [target]);

  useResizeObserver(target, entry => setSize(entry.contentRect.height));

  return size;
};
const Footer = ({ className, children }) => {
  const footerRef = useRef();
  const height = useSizeHeight(footerRef);

  // devday-body에 footer의 높이만큼 주되 32px 정도를 여분으로 더 줍니다.
  useEffect(() => {
    const bodyRef = document.getElementById('devday-body');
    if (bodyRef) {
      bodyRef.style.marginBottom = `${height + 32}px`;
    }
  }, [height]);

  return (
    <div
      ref={footerRef}
      id="devday-footer"
      className={cx('fixed bottom-0 m-0 w-full', className)}
    >
      {children}
    </div>
  );
};

export default Footer;
