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
        },
      },
      backgroundImage: {
        'nexora-gradient': 'linear-gradient(135deg, #2563EB 0%, #1E3A8A 50%, #0D9488 100%)',
        'nexora-hero': 'linear-gradient(135deg, #1E3A8A 0%, #2563EB 45%, #0D9488 100%)',
      },
    },
  },
  plugins: [],
};
