import React from 'react';

import { CheckBoxBtn } from '.';

// More on default export: https://storybook.js.org/docs/react/writing-stories/introduction#default-export
export default {
  title: 'DevDay/Atoms/CheckBoxBtn',
  component: CheckBoxBtn,
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
const Template = args => <CheckBoxBtn {...args} />;

export const Primary = Template.bind({});
// More on args: https://storybook.js.org/docs/react/writing-stories/args
Primary.args = {
  color: null,
  content: 'content',
  label: 'Button',
};