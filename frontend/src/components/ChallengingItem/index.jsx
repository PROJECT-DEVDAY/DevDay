import React from 'react';

import PropTypes from 'prop-types';
import Image from 'next/image';

export const ChallengingItem = ({
  picture,
  title,
  category,
  date,
  ...props
}) => {
  return (
    <div className="m-4 p-2 rounded-2xl bg-white flex items-center">
      <div className="w-1/4 me-4">
        <Image src={picture} className="aspect-auto w-20 rounded" width={0} height={0} />
      </div>
      <div className="w-3/4">
        <p className="font-medium">[챌린지] {title}</p>
        <div className="bg-gray-300 rounded text-center box-content w-2/12 my-1 px-1">
          <p className="text-xs p-1 very-very-small">{category}</p>
        </div>
        <p className="text-xs font-medium very-very-small">{date}</p>
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
