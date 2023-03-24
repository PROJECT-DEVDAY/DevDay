import React from 'react';

import { LabelBtn } from '.';

export default {
  title: 'DevDay/Atoms/LabelBtn',
  component: LabelBtn,
  argTypes: {
    color: {
      label: 'color',
      type: { name: 'select', required: false },
      options: ['warning', 'primary'],
    },
  },
};

const Template = args => <LabelBtn {...args} />;

export const Primary = Template.bind({});
Primary.args = {
  color: 'primary',
  label: 'Button',
  fill: false,
};
