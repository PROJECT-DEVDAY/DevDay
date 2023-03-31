import cx from 'classnames';

const Body = ({ className, children }) => {
  return (
    <div id="devday-body" className={cx(className)}>
      {children}
    </div>
  );
};

export default Body;
