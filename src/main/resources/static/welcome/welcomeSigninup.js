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


 function checkUsernameDuplication(){
    const settings = {
  "url": "http://localhost:8080/sign-up/username",
  "method": "POST",
  "timeout": 0,
  "headers": {
    "Content-Type": "application/json"
  },
  "data": JSON.stringify({
    "username": $('#username').val()
  }),
};

$.ajax(settings).done(function (response) {
  alert("사용 가능한 닉네임 입니다.")
  console.log(response).fail(function(response){
    if(response.responseJSON.httpStatus === 'BAD_REQUEST'){
      alert("중복되는 닉네임 입니다.")
    }else{
      alert("서버에 문제가 발생 했습니다. 잠시 후 다시 시도 해주세요.")
    }
  });
});



}

function login(){
  const settings = {
    "url": "http://localhost:8080/sign-in",
    "method": "POST",
    "timeout": 0,
    "headers": {
      "Content-Type": "application/json"
    },
    "data": JSON.stringify({
      "email": $('#signin-email').val(),
      "password": $('#signin-password').val()
    }),
  };
  
  $.ajax(settings).done(function (response, status, xhr) {
    console.log(response);
    console.log(xhr.getResponseHeader('Authorization'))
    
    alert("로그인 성공");
    
    localStorage.setItem('Authorization', xhr.getResponseHeader('Authorization'))
    localStorage.setItem('Refresh_Token', xhr.getResponseHeader('Refresh_Token'))

    window.location = './main.html'
    
    

  }).fail(function(response){
    console.log(response.JSON)
    
    if(response.responseJSON.httpStatus === 'BAD_REQUEST'){
      alert("이메일과 비밀번호를 다시 확인 해주세요.")
    }else{
      alert("서버에 문제가 발생 했습니다. 잠시 후 재시도 해주세요.")
    }
  });
}

function checkAndSendCode(){
  const checkEmailDuplicationSettings = {
    "url": "http://localhost:8080/sign-up/email",
    "method": "POST",
    "timeout": 0,
    "headers": {
      "Content-Type": "application/json"
    },
    "data": JSON.stringify({
      "email": $('#signup-email').val()
    }),
  };
  
  $.ajax(checkEmailDuplicationSettings).done(function (response) {
    console.log(response);
    const sendAuthenticationCodesettings = {
      "url": "http://localhost:8080/email",
      "method": "POST",
      "timeout": 0,
      "headers": {
        "Content-Type": "application/json"
      },
      "data": JSON.stringify({
        "email": $('#signup-email').val()
      }),
    };
  
    $.ajax(sendAuthenticationCodesettings
      ).done(function (response) {
      alert("인증코드를 이메일을 발송 했습니다.")
      console.log(response);
    })

  }).fail(function(response){
    if(response.responseJSON.httpStatus === 'BAD_REQUEST'){
      alert("중복된 이메일 입니다.")   
    }else{
      alert("서버에 문제가 발생 했습니다. 잠시 후 다시 시도 해주세요.")
    }
  }); 
}