import React from 'react';
import { SlArrowRight } from 'react-icons/sl';

import classnames from 'classnames';
import PropTypes from 'prop-types';

import './selectoption.css';

export const SelectArrow = ({
  fill,
  className,
  iconname,
  title,
  content,
  ...props
}) => {
  return (
    <div
      className={classnames(
        'SelectArrow',
        className,
        fill && 'SelectArrow-background-fill',
      )}
    >
      <div style={{ flex: 1 }}>
        <p className="arrowtitle">{title}</p>
        <p className="arrowcontent">{content}</p>
      </div>
      <div className="arrowdiv">
        <SlArrowRight className="arrow" width={30} />
      </div>
    </div>
  );
};

SelectArrow.propTypes = {
  fill: PropTypes.bool,
  title: PropTypes.string.isRequired,
  content: PropTypes.string.isRequired,
  onClick: PropTypes.func,
};

SelectArrow.defaultProps = {
  fill: true,
  onClick: undefined,
};
