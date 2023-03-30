import React from 'react';

import { ChallengeList } from '.';

// More on default export: https://storybook.js.org/docs/react/writing-stories/introduction#default-export
export default {
  title: 'DevDay/Atoms/ChallengeList',
  component: ChallengeList,
  // More on argTypes: https://storybook.js.org/docs/react/api/argtypes
  argTypes: {
    imageURL: String,
    participants: Number,
    leader: String,
    title: String,
    period: String,
  },
};

// More on component templates: https://storybook.js.org/docs/react/writing-stories/introduction#using-args
const Template = args => <ChallengeList {...args} />;

export const Primary = Template.bind({});
// More on args: https://storybook.js.org/docs/react/writing-stories/args
Primary.args = {
  imageURL: '@/image/default-user.png',
  participants: 4,
  leader: '박태환',
  title: '챌린지 제목',
  period: '03.26 ~ 04.12',
};
