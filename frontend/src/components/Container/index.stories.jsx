import React from 'react';

import Container from '.';
import Header from './components/Header';

// More on default export: https://storybook.js.org/docs/react/writing-stories/introduction#default-export
export default {
  title: 'DevDay/Atoms/Container',
  component: Container,
  // More on argTypes: https://storybook.js.org/docs/react/api/argtypes
  argTypes: {},
};

// More on component templates: https://storybook.js.org/docs/react/writing-stories/introduction#using-args
const Template = args => (
  <Container {...args}>
    <Header />
  </Container>
);

export const DefaultContainer = Template.bind({});
// More on args: https://storybook.js.org/docs/react/writing-stories/args
DefaultContainer.args = {};
