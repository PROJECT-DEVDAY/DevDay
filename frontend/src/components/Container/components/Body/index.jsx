import React, { forwardRef } from 'react';

import cx from 'classnames';

const Body = forwardRef(({ className, children }, ref) => {
  return (
    <div ref={ref} id="devday-body" className={cx(className)}>
      {children}
    </div>
  );
});

export default Body;
