console.log("Script loaded");

// Change theme work
let currentTheme = getTheme();

// Apply the theme when the DOM is fully loaded
document.addEventListener("DOMContentLoaded", () => {
  changePageTheme(currentTheme, "");
});

// Function to change the theme
function changeTheme() {
  const changeThemeButton = document.querySelector("#theme_change_button");

  changeThemeButton.addEventListener("click", () => {
    const oldTheme = currentTheme;
    console.log("Change theme button clicked");

    // Toggle the theme
    currentTheme = currentTheme === "dark" ? "light" : "dark";
    console.log("Current Theme:", currentTheme);

    changePageTheme(currentTheme, oldTheme);
  });
}

// Set theme to localStorage
function setTheme(theme) {
  localStorage.setItem("theme", theme);
}

// Get theme from localStorage
function getTheme() {
  const theme = localStorage.getItem("theme");
  return theme ? theme : "light";
}

// Change current page theme
function changePageTheme(theme, oldTheme) {
  // Update the theme in localStorage
  setTheme(theme);

  // Remove the old theme if it exists
  if (oldTheme) {
    document.querySelector("html").classList.remove(oldTheme);
  }

  // Apply the new theme
  document.querySelector("html").classList.add(theme);

  // Update the button text
  const buttonSpan = document.querySelector("#theme_change_button span");
  buttonSpan.textContent = theme === "light" ? "Dark" : "Light";
}

// Initialize the theme change functionality
changeTheme();
