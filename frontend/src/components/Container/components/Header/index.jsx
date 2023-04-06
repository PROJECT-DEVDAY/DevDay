import React from 'react';

import cx from 'classnames';

const Header = ({ children, className }) => {
  return <div className={cx('h-16 px-4', className)}>{children}</div>;
};

export default Header;
