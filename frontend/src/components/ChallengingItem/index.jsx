import React from 'react';

import Image from 'next/image';
import PropTypes from 'prop-types';

export const ChallengingItem = ({
  picture,
  title,
  category,
  date,
  ...props
}) => {
  return (
    <div className="m-4 p-2 rounded-2xl bg-white flex">
      <div className="w-1/4 me-4">
        <Image />
      </div>
      <div className="w-3/4">
        <p>[챌린지] {title}</p>
        <div />
        <p />
      </div>
    </div>
  );
};

ChallengingItem.propTypes = {
  picture: PropTypes.string,
  title: PropTypes.string,
  category: PropTypes.string,
  date: PropTypes.string,
};

ChallengingItem.defaultProps = {};
