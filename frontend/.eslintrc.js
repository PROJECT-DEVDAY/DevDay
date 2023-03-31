module.exports = {
  env: {
    browser: true,
    es6: true,
    node: true,
  },
  extends: [
    'airbnb',
    'plugin:prettier/recommended',
    'plugin:storybook/recommended',
    'plugin:@next/next/recommended',
  ],
  plugins: ['prettier'],
  // eslint-plugin-prettier를 적용시켜줍니다
  rules: {
    'import/order': [
      'error',
      {
        groups: [
          'builtin',
          'internal',
          'external',
          ['sibling', 'parent', 'index'],
          'type',
          'unknown',
        ],
        pathGroups: [
          {
            pattern: '{react*,react*/**}',
            group: 'external',
            position: 'before',
          },
          {
            pattern: '@saas-fe/**/*.style',
            group: 'unknown',
          },
          {
            pattern: '@pages/**/*.style',
            group: 'unknown',
          },
          {
            pattern: '@components/**/*.style',
            group: 'unknown',
          },
          {
            pattern: './**/*.style',
            group: 'unknown',
          },
          {
            pattern: '../**/*.style',
            group: 'unknown',
          },
          {
            pattern: '*.style',
            group: 'unknown',
          },
        ],
        pathGroupsExcludedImportTypes: ['react', 'unknown'],
        'newlines-between': 'always',
        alphabetize: {
          order: 'asc',
          caseInsensitive: true,
        },
      },
    ],
    'prettier/prettier': ['error', { endOfLine: 'auto' }],
    'react/react-in-jsx-scope': 0,
    'react/jsx-filename-extension': [1, { extensions: ['.js', '.jsx'] }],
    'react/jsx-props-no-spreading': 'off',
    'react/no-unused-prop-types': ['off'],
    'no-unused-vars': 'off',
    'global-require': 'off',
    'react/prop-types': 'off',
    'react/require-default-props': 'off',
    'import/newline-after-import': 'error',
    'import/prefer-default-export': 'off',
    'import/no-unresolved': 'off',
    'import/extensions': 'off',

    'import/no-extraneous-dependencies': [
      1,
      {
        devDependencies: true,
        optionalDependencies: false,
        peerDependencies: false,
      },
    ],
    'react/function-component-definition': [
      2,
      {
        namedComponents: [
          'arrow-function',
          'function-declaration',
          'function-expression',
        ],
      },
    ],

    'jsx-a11y/no-static-element-interactions': 'off',
    'jsx-a11y/click-events-have-key-events': 'off',
    'jsx-a11y/label-has-associated-control': [
      2,
      {
        labelAttributes: ['htmlFor'],
      },
    ],
  },
};
