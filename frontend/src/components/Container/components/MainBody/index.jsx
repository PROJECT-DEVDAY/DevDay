import cx from 'classnames';

import Body from '../Body';

const MainBody = ({ className, children }) => {
  return <Body className={cx('mb-24', 'px-4', className)}>{children}</Body>;
};

export default MainBody;
