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
          blue: '#2563EB',
          'blue-dark': '#1D4ED8',
          'blue-deep': '#1E3A8A',
          green: '#84CC16',
          'green-bright': '#A3E635',
          navy: '#0F172A',
          teal: '#0D9488',
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
