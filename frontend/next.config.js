const path = require('path');

/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: false,
  images: {
    domains: ['devday-bucket.s3.ap-northeast-2.amazonaws.com'],
  },
  sassOptions: {
    includePaths: [path.join(__dirname, 'src/styles')],
  },
  eslint: {
    ignoreDuringBuilds: true,
  },
  async rewrites() {
    return [];
  },
};

module.exports = nextConfig;
