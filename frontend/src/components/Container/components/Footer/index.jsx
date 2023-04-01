import React, { useRef, useEffect } from 'react';
import cx from 'classnames';

import useResizeObserver from '@react-hook/resize-observer';

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

  useEffect(() => {
    let bodyRef = document.getElementById('devday-body');
    console.log(height);
    bodyRef.style.paddingBottom = height + 'px';
    bodyRef.style.marginBottom = height + 'px';
  });

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
