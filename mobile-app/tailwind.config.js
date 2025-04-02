/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./app/**/*.{js,jsx,ts,tsx}", "./components/**/*.{js,jsx,ts,tsx}"],
  presets: [require("nativewind/preset")],
  theme: {
    extend: {
      colors: {
        // Meristem Green Color Palette
        "meristem-green": {
          light: "#e6f0ec",
          "light-hover": "#d9e8e2",
          "light-active": "#b0cfc3",
          DEFAULT: "#00643c",
          hover: "#005a36",
          active: "#005030",
          dark: "#004b2d",
          "dark-hover": "#003c24",
          "dark-active": "#002d1b",
          darker: "#002315",
        },

        // Trileaf Green Color Palette
        "trileaf-green": {
          light: "#fafcf5",
          "light-hover": "#f8fbf1",
          "light-active": "#f0f6e1",
          DEFAULT: "#cee39f",
          hover: "#b9cc8f",
          active: "#a5b67f",
          dark: "#9baa77",
          "dark-hover": "#7c885f",
          "dark-active": "#5d6648",
          darker: "#484f38",
        },

        // Secondary Color Palette
        secondary: {
          light: "#f4eef6",
          "light-hover": "#efe5f1",
          "light-active": "#ddcae2",
          DEFAULT: "#9253a1",
          hover: "#834b91",
          active: "#754281",
          dark: "#6e3e79",
          "dark-hover": "#583261",
          "dark-active": "#422548",
          darker: "#331d38",
        },

        // Error State Color Palette
        error: {
          light: "#ffe6e6",
          "light-hover": "#ffd9d9",
          "light-active": "#ffb0b0",
          DEFAULT: "#ff0000",
          hover: "#e60000",
          active: "#cc0000",
          dark: "#bf0000",
          "dark-hover": "#990000",
          "dark-active": "#730000",
          darker: "#590000",
        },

        // Warning Color Palette
        warning: {
          light: "#fff6e6",
          "light-hover": "#fff2d9",
          "light-active": "#ffe3b0",
          DEFAULT: "#ffa500",
          hover: "#e69500",
          active: "#cc8400",
          dark: "#bf7c00",
          "dark-hover": "#996300",
          "dark-active": "#734a00",
          darker: "#593a00",
        },

        // Grey Color Palette
        "meristem-grey": {
          50: "#e6f0ec",
          100: "#d9e8e2",
          200: "#acb1b7",
          300: "#868d96",
          400: "#6f7782",
          500: "#4b5563",
          600: "#444d5a",
          700: "#353c46",
          800: "#292f36",
          900: "#20242a",
        },

        // Neutral Color Palette
        neutral: {
          white: "#ffffff",
          "white-hover": "#fbfbfb",
          "white-active": "#f3f3f3",
          "light-active": "#ececec",
          DEFAULT: "#f7f7f7",
          hover: "#eeeeee",
          active: "#e5e5e5",
          dark: "#f4f4f4",
        },
      },
      fontFamily: {
        "poppins-regular": ["Poppins-Regular"],
        "poppins-bold": ["Poppins-Bold"],
        "poppins-semibold": ["Poppins-SemiBold"],
        "poppins-medium": ["Poppins-Medium"],
        "poppins-light": ["Poppins-Light"],
        "poppins-italic": ["Poppins-Italic"],
      },
    },
  },
  plugins: [],
};
