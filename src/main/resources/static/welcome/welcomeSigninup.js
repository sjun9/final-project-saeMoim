    /**
 * Variables
 */
const signupButton = document.getElementById('signup-button'),
    loginButton = document.getElementById('login-button'),
    gobackButton = document.getElementById('goback'),
    userForms = document.getElementById('user_options-forms')

/**
 * Add event listener to the "Sign Up" button
 */
signupButton.addEventListener('click', () => {
  userForms.classList.remove('bounceRight')
  userForms.classList.add('bounceLeft')
}, false)

/**
 * Add event listener to the "Login" button
 */
loginButton.addEventListener('click', () => {
  userForms.classList.remove('bounceLeft')
  userForms.classList.add('bounceRight')
}, false)

goback = () => {
  document.querySelector(".et-hero-tabs-title").classList.toggle("remove");
  document.querySelector(".et-hero-tabs-content").classList.toggle("remove");
  document.querySelector(".et-hero-tabs-button").classList.toggle("remove");
  // document.querySelector(".et-hero-tabs-container").classList.toggle("remove");
  // document.querySelector(".et-main").classList.toggle("remove");

  document.querySelector(".user").classList.toggle("remove");
}