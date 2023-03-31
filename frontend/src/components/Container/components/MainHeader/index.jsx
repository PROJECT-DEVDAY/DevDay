import cx from 'classnames';
import Image from 'next/image';

import logo from './img/main_logo.png';
import Header from '../Header';

const MainHeader = ({ className, children }) => {
  return (
    <Header
      className={cx(
        'flex items-center border-b-2 sticky top-0 z-30 bg-white',
        className,
      )}
    >
      <Image
        src={logo}
        className="h-14 flex-none w-fit"
        alt="devday main logo"
      />
      <div className="flex items-center ml-auto">{children}</div>
    </Header>
  );
};
export default MainHeader;
