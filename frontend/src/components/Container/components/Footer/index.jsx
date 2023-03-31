import cx from 'classnames';

const Footer = ({ className, children }) => {
  return (
    <div
      id="devday-footer"
      className={cx('fixed bottom-0 m-0 w-full', className)}
    >
      {children}
    </div>
  );
};

export default Footer;
