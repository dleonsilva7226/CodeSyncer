/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: 'class', // optional but recommended so OS doesn’t auto-flip
  content: [
    './src/app/**/*.{js,ts,jsx,tsx}',
    './src/components/**/*.{js,ts,jsx,tsx}',
    './src/pages/**/*.{js,ts,jsx,tsx}',
  ],
  theme: { extend: {} },
  plugins: [],
};
