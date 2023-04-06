import cx from 'classnames';

import Footer from '../Footer';

const MainFooter = ({ className, children }) => {
  return <Footer className={cx('h-20 max-h-24', className)}>{children}</Footer>;
};

export default MainFooter;
