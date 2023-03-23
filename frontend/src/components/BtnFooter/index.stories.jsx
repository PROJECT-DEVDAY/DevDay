import React from 'react';

import { BtnFooter } from '.';

// More on default export: https://storybook.js.org/docs/react/writing-stories/introduction#default-export
export default {
  title: 'DevDay/Atoms/BtnFooter',
  component: BtnFooter,
  // More on argTypes: https://storybook.js.org/docs/react/api/argtypes
  argTypes: {
    color: {
      label: 'color',
      type: { name: 'select', required: false },
      options: ['warning', 'primary'],
    },
  },
};

// More on component templates: https://storybook.js.org/docs/react/writing-stories/introduction#using-args
const Template = args => <BtnFooter {...args} />;

export const Primary = Template.bind({});
// More on args: https://storybook.js.org/docs/react/writing-stories/args
Primary.args = {
  content: '결제 조건 및 서비스 약관에 동의합니다',
  label: null,
  color: null,
};
