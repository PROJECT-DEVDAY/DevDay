import React from 'react';

import { MainHeader } from '.';

export default {
  title: 'DevDay/Atoms/MainHeader',
  component: ReturnArrow,
};

const Template = args => <MainHeader {...args} />;

export const Primary = Template.bind({});

Primary.args = {};
