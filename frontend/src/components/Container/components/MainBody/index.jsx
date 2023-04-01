import cx from 'classnames';

import Body from '../Body';

const MainBody = ({ className, children }) => {
  return <Body className={cx('px-4', 'pb-8', className)}>{children}</Body>;
};

export default MainBody;
