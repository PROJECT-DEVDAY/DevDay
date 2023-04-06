module.exports = {
  content: [
    './pages/**/*.{js,ts,jsx,tsx}',
    './components/**/*.{js,ts,jsx,tsx}',
    // Storybook의 Story 위치를 src 폴더로 정했으므로, 아래 규칙을 하나 더합니다.
    './src/**/*.{js,ts,jsx,tsx}',
  ],
  theme: {
    // 아래 링크에 대한 설명에 따라서, 미디어쿼리 규칙을 여기에도 설정합니다.
    screens: {
      mob: '375px',
      tablet: '768px',
      laptop: '1024px',
      laptopl: '1440px',
      desktop: '1280px',
    },
    extend: {
      fontFamily: {
        sans: ['Gmarket-Sans-Light'],
        medium: ['Gmarket-Sans-Medium'],
        bold: ['Gmarket-Sans-Bold'],
      },
    },
  },
  plugins: [],
};
