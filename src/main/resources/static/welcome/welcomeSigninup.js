let origin = `http://52.79.169.105:8080`

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

let isCheckUsernameDuplication = false;
let ischeckemailDuplication = false;
let ischeckAuthCode = false;
let ischeckpassword = false;

// 닉네임 중복 확인
function checkUsernameDuplication() {
    const settings = {
        "url": `${origin}/sign-up/username`,
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
        isCheckUsernameDuplication = true;
    }).fail(function (response) {
        if (response.responseJSON.httpStatus === 'BAD_REQUEST') {
            alert(response.responseJSON['message'])
            isCheckUsernameDuplication = false;
        } else {
            alert("서버에 문제가 발생 했습니다. 잠시 후 다시 시도 해주세요.")
            isCheckUsernameDuplication = false;
        }
    });

}

// 로그인
function login() {
    const settings = {
        "url": `${origin}/sign-in`,
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

        // alert("로그인 성공");

        localStorage.setItem('Authorization', xhr.getResponseHeader('Authorization'))
        localStorage.setItem('Refresh_Token', xhr.getResponseHeader('Refresh_Token'))

        location.replace('./main.html')
    }).fail(function (response) {
        alert(response.responseJSON['data'])
    });
}

// 이메일(Id) 중복 확인 빛 인증 메일 발송
function checkAndSendCode() {
    const checkEmailDuplicationSettings = {
        "url": `${origin}/sign-up/email`,
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
        const sendAuthenticationCodesettings = {
            "url": `${origin}/email`,
            "method": "POST",
            "timeout": 0,
            "headers": {
                "Content-Type": "application/json"
            },
            "data": JSON.stringify({
                "email": $('#signup-email').val()
            })

        }
        ischeckemailDuplication = true;

        $.ajax(sendAuthenticationCodesettings
        ).done(function (response) {
            alert("인증코드를 이메일로 발송 했습니다.\n제한 시간은 3분 입니다.")

        })
    }).fail(function (response) {
        if (response.responseJSON.httpStatus === 'BAD_REQUEST') {
            alert(response.responseJSON['message'])
            ischeckemailDuplication = false;
        } else {
            alert("서버에 문제가 발생 했습니다. 잠시 후 다시 시도 해주세요.")
            ischeckemailDuplication = false;
        }
    });
}

// 메일로 보낸 인증 코드 확인
function checkAuthCode() {
    var settings = {
        "url": `${origin}/email/auth-code`,
        "method": "POST",
        "timeout": 0,
        "headers": {
            "Content-Type": "application/json"
        },
        "data": JSON.stringify({email: $('#signup-email').val(), code: $('#email-authentication-code').val()}),
    };

    $.ajax(settings).done(function (response) {
        alert(response.data)
    }).fail(function (response) {
        alert(response.responseJSON['data'])
    });
}


$('.pwd-validation').focusout(function () {
    const pwd1 = $("#password").val();
    const pwd2 = $("#password-validation").val();

    if (pwd1 != '' && pwd2 == '') {
        null;
    } else if (pwd1 != "" || pwd2 != "") {
        if (pwd1 == pwd2) {
            $("#alert-success").css('display', 'inline-block');
            $("#alert-danger").css('display', 'none');
            ischeckpassword = true;
        } else {
            $("#alert-success").css('display', 'none');
            $("#alert-danger").css('display', 'inline-block');
            ischeckpassword = false;
        }
    }
});


function signup() {
    var settings = {
        "url": `${origin}/sign-up`,
        "method": "POST",
        "timeout": 0,
        "headers": {
            "Content-Type": "application/json"
        },
        "data": JSON.stringify({
            "email": $('#signup-email').val(),
            "password": $('#password').val(),
            "username": $('#username').val()
        }),
    }

    if (isCheckUsernameDuplication && ischeckAuthCode && ischeckemailDuplication && ischeckpassword) {
        $.ajax(settings).done(function (response) {
            alert("회원가입 완료")
            userForms.classList.remove('bounceLeft')
            userForms.classList.add('bounceRight')
        }).fail(function (response) {
            alert(response.responseJSON['message'])

        });
    } else {
        if (isCheckUsernameDuplication === false) {
            alert("닉네임을 다시 확인 해주세요")
        } else if (ischeckAuthCode === false) {
            alert("인증 코드를 다시 확인 해주세요")
        } else if (ischeckemailDuplication === false) {
            alert("이메일을 다시 확인 해주세요.")
        } else if (ischeckpassword === false) {
            alert("비밀번호를 다시 확인 해주세요.")
        }
    }
}

function sendPasswordToEmail() {
    var settings = {
        "url": `${origin}/email/password`,
        "method": "PUT",
        "timeout": 0,
        "headers": {
            "Content-Type": "application/json"
        },
        "data": JSON.stringify({
            "email": $('#findPwd-email').val()
        }),
    };

    $.ajax(settings).done(function (response) {
        alert("해당 이메일로 임시번호가 발송 되었습니다.")
    }).fail(function (response) {
        alert(response.responseJSON['message'])
    });
}

// 회원가입 창 입력값 변경 감지 시 회원가입 버튼을 비활성하기 위한 function
function changeUsernameDetection() {
    isCheckUsernameDuplication = false;
}

function changeEmailDetection() {
    ischeckemailDuplication = false;
}

function changePwdDetection() {
    ischeckpassword = false;
}

// 모달

const modal = document.getElementById("modal")
const btnModal = document.getElementById("btn-modal")
btnModal.addEventListener("click", e => {
    modal.style.display = "flex"
})

const closeBtn = modal.querySelector(".close-area")
closeBtn.addEventListener("click", e => {
    modal.style.display = "none"
})

modal.addEventListener("click", e => {
    const evTarget = e.target
    if (evTarget.classList.contains("modal-overlay")) {
        modal.style.display = "none"
    }
})

window.addEventListener("keyup", e => {
    if (modal.style.display === "flex" && e.key === "Escape") {
        modal.style.display = "none"
    }
})