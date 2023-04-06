import React from 'react';

import { UserAvatar } from '.';

export default {
  title: 'DevDay/Atoms/UserAvatar',
  component: UserAvatar,
  argTypes: {},
};

const Template = args => <UserAvatar {...args} />;

export const Primary = Template.bind({});
Primary.args = {};
