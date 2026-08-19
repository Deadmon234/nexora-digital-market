/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './app/**/*.{js,ts,jsx,tsx}',
    './components/**/*.{js,ts,jsx,tsx}',
  ],
  theme: {
    extend: {
      colors: {
        nexora: {
          blue: '#2364AA',
          'blue-dark': '#174A80',
          'blue-deep': '#17212B',
          green: '#B8D45A',
          'green-bright': '#D5E98A',
          navy: '#17212B',
          teal: '#2D8C89',
          coral: '#F26B4F',
          ivory: '#F6F3EE',
          ivory: '#F5F8FC',
        },
      },
      backgroundImage: {
        'nexora-gradient': 'linear-gradient(135deg, #092A5C 0%, #1264C8 52%, #08A86A 100%)',
        'nexora-hero': 'linear-gradient(135deg, #092A5C 0%, #1264C8 58%, #087C78 100%)',
      },
    },
  },
  plugins: [],
};
