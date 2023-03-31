import React from 'react';

import cx from 'classnames';
import PropTypes from 'prop-types';

import Body from './components/Body';
import Footer from './components/Footer';
import Header from './components/Header';
import MainBody from './components/MainBody';
import MainFooter from './components/MainFooter';
import MainHeader from './components/MainHeader';
import style from './index.module.scss';

const Container = ({ children, className }) => {
  return (
    <div
      id="devday-container"
      className={cx('container-sm', style.Container, className)}
    >
      {children}
    </div>
  );
};

Container.Header = Header;
Container.MainHeader = MainHeader;
Container.Body = Body;
Container.MainBody = MainBody;
Container.Footer = Footer;
Container.MainFooter = MainFooter;

export default Container;
