import React from 'react';

import { ReturnArrow } from '.';

export default {
  title: 'DevDay/Atoms/ReturnArrow',
  component: ReturnArrow,
};

const Template = args => <ReturnArrow {...args} />;

export const Primary = Template.bind({});

Primary.args = {
  title: '이전 페이지',
};
