/**
 * 임시 정보
 */
// const Authorization = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiTmFtZSI6ImJiYiIsImF1dGgiOiJVU0VSIiwiZXhwIjoxNjc3MTY3NDg4LCJpYXQiOjE2NzcxNjYyODh9.sMYFSF56Hobd0qsCvuENY6cxlGIGylIU1dfj5aOT_ns"
// const Refresh_Token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiZXhwIjoxNjc3MTY5ODg4LCJpYXQiOjE2NzcxNjYyODh9.OxXhznYD-J6_VcEAIW3WROaitYp59bWOJopLB7jB2Ms"

// const tempGroupId = "1"dw
// const tempUserId = "2"

let Authorization = localStorage.getItem("Authorization")
let Refresh_Token = localStorage.getItem("Refresh_Token")

let tempGroupId = localStorage.getItem("current_group_id")
let tempUserId = localStorage.getItem("current_user_id")

/**
 * 우측 프로필 리스트
 */


// 현재 그룹 참여자 리스트 가져와서 localStorage에 저장
function getGroupProfileIdList() {
<<<<<<< HEAD
  $.ajax({
    type: "GET",
    url: `http://localhost:8080/participant/groups/${tempGroupId}`,
    async: false,
    data: {},
    beforeSend: function (xhr) {
      xhr.setRequestHeader("Authorization", Authorization);
      xhr.setRequestHeader("Refresh_Token", Refresh_Token);
    },
    success: function (response) {
      localStorage.setItem("profileIdList", JSON.stringify(response["data"]));
      // const profileIdList = JSON.parse(localStorage.getItem("profileIdList"))
    }
  });
=======
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/participant/groups/" + tempGroupId,
        async: false,
        data: {},
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", Authorization);
            xhr.setRequestHeader("Refresh_Token", Refresh_Token);
        },
        success: function (response) {
            localStorage.setItem("profileIdList", JSON.stringify(response));
            // const profileIdList = JSON.parse(localStorage.getItem("profileIdList"))
        }
    });
>>>>>>> d314029eff17ddf05ce5e31e6686da9fc4ce9479
}


// 기존 프로필 리스트 전체 삭제
function deleteProfileList() {
    const userZone = document.querySelector('#user_zone');
    while (userZone.firstChild) {
        userZone.firstChild.remove()
    }
}


// localStorage에서 참여자 리스트 가져와서 페이지 우측에 표시
// 참여자 리스트 로컬스토리지에 저장해두고 모달창 열 때 활용해도 될 듯
// -> openprofile 시 api 요청 줄이기
function renderProfileList() {
<<<<<<< HEAD
  const profileIdList = JSON.parse(localStorage.getItem("profileIdList"))
  profileIdList.forEach((user) => {
    var settings = {
      "url": `http://localhost:8080/profile/users/${user["userId"]}`,
      "method": "GET",
      "timeout": 0,
      "headers": {
        "Authorization": Authorization,
        "Refresh_Token": Refresh_Token
      },
    };
=======
    const profileIdList = JSON.parse(localStorage.getItem("profileIdList"))
    profileIdList.forEach((user) => {
        var settings = {
            "url": "http://localhost:8080/profile/users/" + String(user["userId"]),
            "method": "GET",
            "timeout": 0,
            "headers": {
                "Authorization": Authorization,
                "Refresh_Token": Refresh_Token
            },
        };
>>>>>>> d314029eff17ddf05ce5e31e6686da9fc4ce9479

        $.ajax(settings).done(function (response) {
            appendProfileButton(response)
        });
    })
}


// 프로필 버튼 요소 추가
function appendProfileButton(response) {
    let temp_html = `
    <div class="userzone__user" onclick="openProfile(event)" data-bs-toggle="modal" data-bs-target="#profileModal">
      <div class="userzone__user__img">
      </div>
      <div class="userzone__user__name">${response["username"]}</div>
    </div>
  `
    $('#user_zone').append(temp_html)
}


// 클릭 시 해당 유저 프로필 모달창 열기
function openProfile(event) {
<<<<<<< HEAD
  let username = event.currentTarget.children[1].innerText
  let userId;
  const profileIdList = JSON.parse(localStorage.getItem("profileIdList"))
  profileIdList.forEach((user) => {
    if (user["username"] === username) {
      userId = user["userId"]
    }
  })
  var settings = {
    "url": `http://localhost:8080/profile/users/${userId}`,
    "method": "GET",
    "timeout": 0,
    "headers": {
      "Authorization": Authorization,
      "Refresh_Token": Refresh_Token
    },
  };
=======
    let username = event.currentTarget.children[1].innerText
    let userId;
    const profileIdList = JSON.parse(localStorage.getItem("profileIdList"))
    profileIdList.forEach((user) => {
        if (user["username"] === username) {
            userId = user["userId"]
        }
    })
    var settings = {
        "url": "http://localhost:8080/profile/users/" + String(userId),
        "method": "GET",
        "timeout": 0,
        "headers": {
            "Authorization": Authorization,
            "Refresh_Token": Refresh_Token
        },
    };
>>>>>>> d314029eff17ddf05ce5e31e6686da9fc4ce9479

    $.ajax(settings).done(function (response) {
        const profile_modal_page = document.querySelector("#profile_modal_page")
        profile_modal_page.children[2].children[0].innerText = username
        profile_modal_page.children[3].innerText = response["content"]
    });
}

function profile() {
    // 리더프로필정보 가져오기
    getGroupProfileIdList()
    deleteProfileList()
    // 리더프로필 추가
    renderProfileList()
}

profile()


/**
 * 게시글
 */


// 게시글 생성
function newPost() {
    const newPostTitle = document.querySelector("#newPost-title").value
    const newPostContent = document.querySelector("#newPost-content").value
    // const imageUrl  = 이미지 경로 추가

<<<<<<< HEAD
  var settings = {
    "url": `http://localhost:8080/groups/${tempGroupId}/post`,
    "method": "POST",
    "timeout": 0,
    "headers": {
      "Authorization": Authorization,
      "Refresh_Token": Refresh_Token,
      "Content-Type": "application/json"
    },
    "data": JSON.stringify({
      "title": newPostTitle,
      "content": newPostContent
    }),
  };
=======
    var settings = {
        "url": "http://localhost:8080/groups/" + tempGroupId + "/post",
        "method": "POST",
        "timeout": 0,
        "headers": {
            "Authorization": Authorization,
            "Refresh_Token": Refresh_Token,
            "Content-Type": "application/json"
        },
        "data": JSON.stringify({
            "title": newPostTitle,
            "content": newPostContent
        }),
    };
>>>>>>> d314029eff17ddf05ce5e31e6686da9fc4ce9479

    $.ajax(settings).done(function (response) {
        alert('작성 완료!');
        location.reload();
    });
}


// 게시글 불러오기

// 클릭된 target의 id 값을 가져온 후
// localStorage에 저장된 게시글 리스트 중 id가 일치하는 게시글을 가져와서
// 모달창에 표시
// 추후 게시글 이미지 url 추가 필요함
function openBody(event) {

    const postArray = JSON.parse(localStorage.getItem("pagedPostList"))
    const nodes = [...event.target.parentElement.parentElement.parentElement.children];
    let index = nodes.indexOf(event.target.parentElement.parentElement);

    document.querySelector('#readPostModalLabel').innerText = postArray[index]["title"]
    document.querySelector('#readPostModalContent').innerText = postArray[index]["content"]

    let currentPostId = postArray[index]["id"]
    let currentPostUserId = postArray[index]["userId"]
    localStorage.setItem("current_post_id", currentPostId)
    localStorage.setItem("current_post_user_id", currentPostUserId)

    renderComments(currentPostId)
}


// 게시글 수정
function editPost(event) {
    const new_title = document.querySelector("#editPost-title").value
    const new_content = document.querySelector("#editPost-content").value
    const currentPostId = localStorage.getItem("current_post_id")

<<<<<<< HEAD
  var settings = {
    "url": `http://localhost:8080/posts/${currentPostId}`,
    "method": "PUT",
    "timeout": 0,
    "headers": {
      "Authorization": Authorization,
      "Refresh_Token": Refresh_Token,
      "Content-Type": "application/json"
    },
    "data": JSON.stringify({
      "title": new_title,
      "content": new_content
    }),
  };

  $.ajax(settings).done(function (response) {
    console.log(response);
    alert("수정 완료")
    location.reload()

    // 게시글 최신화 (임시로 겉보기만...)
    // document.querySelector('#readPostModalLabel').innerText = new_title
    // document.querySelector('#readPostModalContent').innerText = new_content

    // document.querySelector("#closeEditPostModal").click() // 수정 모달창 닫고
    // document.querySelector("#openRead").click() // 읽기 모달창 열고
  });
=======
    var settings = {
        "url": "http://localhost:8080/posts/" + currentPostId,
        "method": "PUT",
        "timeout": 0,
        "headers": {
            "Authorization": Authorization,
            "Refresh_Token": Refresh_Token,
            "Content-Type": "application/json"
        },
        "data": JSON.stringify({
            "title": new_title,
            "content": new_content
        }),
    };

    $.ajax(settings).done(function (response) {
        console.log(response);
        alert("수정 완료")

        // 게시글 최신화 (임시로 겉보기만...)
        document.querySelector('#readPostModalLabel').innerText = new_title
        document.querySelector('#readPostModalContent').innerText = new_content

        document.querySelector("#closeEditPostModal").click() // 수정 모달창 닫고
        document.querySelector("#openRead").click() // 읽기 모달창 열고
    });
>>>>>>> d314029eff17ddf05ce5e31e6686da9fc4ce9479
}


// 게시글 수정 모달창 열기
// 추후 게시글 이미지 url 추가 필요함
function editPostModal(event) {
    // 값 가져와서 수정 api

    const original_title = document.querySelector('#readPostModalLabel').innerText
    const original_content = document.querySelector('#readPostModalContent').innerText

    document.querySelector("#closeReadPostModal").click() // 게시글 모달창 닫고
    document.querySelector("#openEdit").click() // 수정 모달창 열기

    document.querySelector("#editPost-title").value = original_title
    document.querySelector("#editPost-content").value = original_content
}


// 게시글 수정모드 전환 전 유저검증
function gotoEditPost(event) {
    const currentPostUserId = localStorage.getItem("current_post_user_id")
    if (tempUserId !== currentPostUserId) {
        alert('작성자만 수정 가능합니다');
        return;
    }
    editPostModal(event) // 댓글 수정모드 전환
}


// 게시글 수정 취소
function cancleEdit(event) {
    document.querySelector("#closeReadPostModal").click() // 수정 모달창 닫고
    document.querySelector("#openRead").click() // 게시글 모달창 열기
}


// 게시글 삭제
function deletePost(event) {
    const currentPostId = localStorage.getItem("current_post_id")

<<<<<<< HEAD
  var settings = {
    "url": `http://localhost:8080/posts/${currentPostId}`,
    "method": "DELETE",
    "timeout": 0,
    "headers": {
      "Authorization": Authorization,
      "Refresh_Token": Refresh_Token
    },
  };
=======
    var settings = {
        "url": "http://localhost:8080/posts/" + currentPostId,
        "method": "DELETE",
        "timeout": 0,
        "headers": {
            "Authorization": Authorization,
            "Refresh_Token": Refresh_Token
        },
    };
>>>>>>> d314029eff17ddf05ce5e31e6686da9fc4ce9479

    $.ajax(settings).done(function (response) {
        alert("삭제 되었습니다.");
        location.reload()
    });
}


// 게시글 삭제 확인 메세지
function AskDeletePost(event) {
    // 유저 확인하고 글 삭제
    if (confirm("게시글을 삭제하시겠습니까?")) {
        deletePost(event)
    }
}


// 게시글 삭제 전 유저검증
function gotoDeletePost(event) {
    const currentPostUserId = localStorage.getItem("current_post_user_id")
    if (tempUserId !== currentPostUserId) {
        alert('작성자만 삭제 가능합니다');
        return;
    }
    AskDeletePost(event)// 댓글 수정모드 전환
}


/**
 * 게시글 렌더링
 */

// https://nohack.tistory.com/125

const contents = document.querySelector(".contents");
const buttons = document.querySelector(".buttons");

// setPostsCount(); 지움
let numOfContent = localStorage.getItem("postsCount");// 전체 게시글 갯수
const maxContent = 10; // 표시할 게시글 갯수
const maxButton = 5; // 표시할 버튼 갯수
let maxPage = Math.ceil(numOfContent / maxContent);
let page = 1; // 새로고침 시 1페이지부터 시작


<<<<<<< HEAD
// 게시글 전체 갯수 가져오기 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// function setPostsCount() {
//   $.ajax({
//     type: 'GET',
//     url: `http://localhost:8080/posts/groups/${tempGroupId}/count`,
//     data: {},
//     async: false, // 비동기 해제
//     success: function (response) {
//       localStorage.setItem("postsCount", response)
//     }
//   });
// }
=======
// 게시글 전체 길이 가져오기
function setPostsCount() {
    $.ajax({
        type: 'GET',
        url: "http://localhost:8080/posts/groups/" + tempGroupId + "/count",
        data: {},
        async: false, // 비동기 해제
        success: function (response) {
            localStorage.setItem("postsCount", response)
        }
    });
}

>>>>>>> d314029eff17ddf05ce5e31e6686da9fc4ce9479

// pageable pageable pageable pageable pageable pageable pageable pageable pageable pageable pageable pageable pageable pageable pageable
// 해당 페이지 게시글 리스트 가져오기 
function getPosts(pageNum, sizeNum) {
<<<<<<< HEAD
  $.ajax({
    type: 'GET',
    url: `http://localhost:8080/groups/${tempGroupId}/post?page=${pageNum}&size=${sizeNum}`,
    // url: `http://localhost:8080/groups/${tempGroupId}/post?page=0&size=10`,
    data: {},
    beforeSend: function (xhr) {
      xhr.setRequestHeader("Authorization", Authorization);
      xhr.setRequestHeader("Refresh_Token", Refresh_Token);
    },
    async: false, // 비동기 해제
    success: function (data) {
      console.log(data)
      localStorage.setItem("postsCount", data["totalElements"])

      localStorage.setItem("pagedPostList", JSON.stringify(data["content"])); // response에서 게시글 리스트 부분
      // const postArray = JSON.parse(localStorage.getItem("pagedPostList"))
    }
  });
=======
    $.ajax({
        type: 'GET',
        url: "http://localhost:8080/posts/groups/" + tempGroupId + "?page=" + pageNum + "&size=" + sizeNum,
        data: {},
        async: false, // 비동기 해제
        success: function (data) {
            localStorage.setItem("pagedPostList", JSON.stringify(data));
            const postArray = JSON.parse(localStorage.getItem("pagedPostList"))
        }
    });
>>>>>>> d314029eff17ddf05ce5e31e6686da9fc4ce9479
}


// 게시글 요소 생성
const makeContent = (i) => {
    const currentPost = JSON.parse(localStorage.getItem("pagedPostList"))[i];

    const contentwrap = document.createElement("li");
    contentwrap.classList.add("contentwrap");

    const content__header = document.createElement("div");
    content__header.classList.add("content__header");

<<<<<<< HEAD
  let simpleCreatedAt = "null"
  let currentLikeCount = currentPost["likeCount"]
  let likeChecked = ""
  if (currentPost["likeChecked"] == true) { likeChecked += "active"}
=======
    let simpleCreatedAt = "null"
    let likeChecked = ""
    let currentLikeCount = currentPost["likeCount"]

    // 좋아요 체크여부 가져오기
    checkLike(currentPost["id"]) // localStorage에 현재 게시글 좋아요 체크 여부를 저장
    if (localStorage.getItem("currentLikeChecked") === true) {
        likeChecked += "active"
    }
>>>>>>> d314029eff17ddf05ce5e31e6686da9fc4ce9479

    if (currentPost["createdAt"]) { // createdAt이 null일 경우 페이지 로딩 안됨... 임시로 if문 적용
        const createdAt = String(currentPost["createdAt"])
        const simpleCreatedAtDate = createdAt.split('.')[0].split('T')[0]
        const simpleCreatedAtHour = createdAt.split('.')[0].split('T')[1].split(':')[0]
        const simpleCreatedAtMin = createdAt.split('.')[0].split('T')[1].split(':')[1]
        simpleCreatedAt = ""
        simpleCreatedAt = simpleCreatedAtDate + " " + simpleCreatedAtHour + ":" + simpleCreatedAtMin
    }
    // console.log(currentPost)
    content__header.innerHTML = `
    <span class="content__header__id">${currentPost["id"]}</span>
    <a href="#" class="like-button content__header__likeButton ${likeChecked}">
      <div class="content__header__likeNumber">${currentLikeCount}</div>
      <?xml version="1.0" encoding="utf-8"?>
      <svg width="20" height="20" viewBox="0 0 1792 1792" xmlns="http://www.w3.org/2000/svg"><path d="M320 1344q0-26-19-45t-45-19q-27 0-45.5 19t-18.5 45q0 27 18.5 45.5t45.5 18.5q26 0 45-18.5t19-45.5zm160-512v640q0 26-19 45t-45 19h-288q-26 0-45-19t-19-45v-640q0-26 19-45t45-19h288q26 0 45 19t19 45zm1184 0q0 86-55 149 15 44 15 76 3 76-43 137 17 56 0 117-15 57-54 94 9 112-49 181-64 76-197 78h-129q-66 0-144-15.5t-121.5-29-120.5-39.5q-123-43-158-44-26-1-45-19.5t-19-44.5v-641q0-25 18-43.5t43-20.5q24-2 76-59t101-121q68-87 101-120 18-18 31-48t17.5-48.5 13.5-60.5q7-39 12.5-61t19.5-52 34-50q19-19 45-19 46 0 82.5 10.5t60 26 40 40.5 24 45 12 50 5 45 .5 39q0 38-9.5 76t-19 60-27.5 56q-3 6-10 18t-11 22-8 24h277q78 0 135 57t57 135z"/></svg>
    </a>
    <span class="content__header__title" onclick="openBody(event)" data-bs-toggle="modal" data-bs-target="#readPostModal">${currentPost["title"]}</span>
    <span class="content__header__author">${currentPost["username"]}</span>
    <span class="content__header__date">${simpleCreatedAt}</span>
  `;

    contentwrap.appendChild(content__header);
    return contentwrap;
};


// 버튼 요소 생성
const makeButton = (i) => {
    const button = document.createElement("button");
    button.classList.add("button");
    button.innerText = i;
    return button;
};


// 이전 페이지 목록으로 이동
const goPrevPage = () => {
    page -= maxButton;
    render(page);
};


// 다음 페이지 목록으로 이동
const goNextPage = () => {
    page += maxButton;
    render(page);
};


// 이전 페이지 목록 이동버튼 요소 생성
const prev = document.createElement("button");
prev.classList.add("button", "prev");
prev.innerHTML = '<ion-icon name="chevron-back-outline"></ion-icon>';
prev.addEventListener("click", goPrevPage);


// 다음 페이지 목록 이동버튼 요소 생성
const next = document.createElement("button");
next.classList.add("button", "next");
next.innerHTML = '<ion-icon name="chevron-forward-outline"></ion-icon>';
next.addEventListener("click", goNextPage);


// 게시글 렌더링 (10개씩)
const renderContent = (page) => {
    while (contents.hasChildNodes()) {
        contents.removeChild(contents.lastChild);
    }

    getPosts(page - 1, 10); // 페이징 처리된 게시글 리스트 가져옴

    const postArray = JSON.parse(localStorage.getItem("pagedPostList")) // 가져온 게시글 리스트 갯수를 계산하기 위해

    for (let i = 0; i <= postArray.length - 1; i++) {
        contents.appendChild(makeContent(i));
    }
    addLikeFeatureToButtons()
};


// 버튼 렌더링
const renderButton = (page) => {
<<<<<<< HEAD
  while (buttons.hasChildNodes()) {
    buttons.removeChild(buttons.lastChild);
  }
  for (let i = page; i < page + maxButton && i <= maxPage; i++) {
    buttons.appendChild(makeButton(i));
  }
  // console.log(page)
  buttons.children[0].classList.add("active"); // 첫 로딩시 가장 왼쪽 페이지 선택
=======
    while (buttons.hasChildNodes()) {
        buttons.removeChild(buttons.lastChild);
    }
    for (let i = page; i < page + maxButton && i <= maxPage; i++) {
        buttons.appendChild(makeButton(i));
    }
    console.log(page)
    buttons.children[0].classList.add("active"); // 첫 로딩시 가장 왼쪽 페이지 선택
>>>>>>> d314029eff17ddf05ce5e31e6686da9fc4ce9479

    const createdButtons = document.querySelectorAll('.button');
    createdButtons.forEach((createdButton) => {
        createdButton.addEventListener('click', gotoPageNum)
    })

    buttons.prepend(prev);
    buttons.append(next);

    if (page - maxButton < 1) buttons.removeChild(prev);
    if (page + maxButton > maxPage) buttons.removeChild(next);
};


// 버튼 클릭시 해당 페이지로 이동
function gotoPageNum(event) {
    document.querySelectorAll('.button').forEach((onebutton) => {
        onebutton.classList.remove('active')
    })
    event.target.classList.add("active");
    renderContent(event.target.innerText)
}


// 해당 페이지 게시글 및 버튼 렌더링
const render = (page) => {
    document.querySelector('#moim_title').innerText = tempGroupId + "번 게시판"
    renderContent(page);
    renderButton(page);
};


// 게시판 실행 (1 페이지)
render(page);


<<<<<<< HEAD




=======
>>>>>>> d314029eff17ddf05ce5e31e6686da9fc4ce9479
/**
 * 댓글 관련 기능
 */


// 댓글 불러오기
function renderComments(currentPostId) {

    const commentList = document.querySelector('#commentList');
    while (commentList.firstChild) {
        commentList.firstChild.remove()
    }

<<<<<<< HEAD
  var settings = {
    "url": `http://localhost:8080/posts/${currentPostId}/comment`,
    "method": "GET",
    "timeout": 0,
    "headers": {
      "Authorization": Authorization,
      "Refresh_Token": Refresh_Token
    },
  };

  $.ajax(settings).done(function (response) {
    let commentList = response["data"]
    // console.log(response)
    var cCnt = commentList.length;
    document.querySelector("#cCnt").innerText = cCnt;
=======
    var settings = {
        "url": "http://localhost:8080/posts/" + currentPostId + "/comment",
        "method": "GET",
        "timeout": 0,
        "headers": {
            "Authorization": Authorization,
            "Refresh_Token": Refresh_Token
        },
    };

    $.ajax(settings).done(function (response) {
        var cCnt = response.length;
        document.querySelector("#cCnt").innerText = cCnt;
>>>>>>> d314029eff17ddf05ce5e31e6686da9fc4ce9479

        if (cCnt > 0) {
            for (i = 0; i < cCnt; i++) {
                let temp_html = `
        <table class='table'>
          <h6><strong>${commentList[i]["username"]}</strong></h6>
          <p style="overflow: hidden; word-wrap: break-word;">
            ${commentList[i]["comment"]}
          </p>
          <tr>
            <td></td>
          </tr>
          <textarea class="button_hide" style="width: 100%" rows="3" cols="30" id="comment"
            name="comment"></textarea>
          <div class="edit_delete">
            <button type="button" onclick="deleteComment(event)" class="btn btn-danger" style="float: right;">삭제</button>
            <button type="button" onclick="gotoEditComment(event)" class="btn btn-secondary"
              style="float: right; margin-right: 8px;">수정</button>
            <div style="display: none">${commentList[i]["id"]}</div>
            <div style="display: none">${commentList[i]["userId"]}</div>
          </div>
          <div class="cancel_submit button_hide">
            <button type="button" onclick="editComment(event)" class="btn btn-success" style="float: right;">수정</button>
            <button type="button" onclick="gotoDeleteComment(event)" class="btn btn-secondary"
              style="float: right; margin-right: 8px;">취소</button>
          </div>
        </table>
        `
                $('#commentList').append(temp_html)
            }
        } else {
            let temp_html = `
      <table class='table'>
        <h6><strong>등록된 댓글이 없습니다.</strong></h6>
      </table>
      `
            $('#commentList').append(temp_html)
        }
    });
}


// 댓글 작성
function writeComment() {
<<<<<<< HEAD
  const commentStr = document.querySelector("#comment").value
  const currentPostId = localStorage.getItem("current_post_id")
  var settings = {
    "url": `http://localhost:8080/posts/${currentPostId}/comment`,
    "method": "POST",
    "timeout": 0,
    "headers": {
      "Authorization": Authorization,
      "Refresh_Token": Refresh_Token,
      "Content-Type": "application/json"
    },
    "data": JSON.stringify({
      "comment": commentStr
    }),
  };
=======
    const commentStr = document.querySelector("#comment").value
    const currentPostId = localStorage.getItem("current_post_id")
    var settings = {
        "url": "http://localhost:8080/posts/" + currentPostId + "/comment",
        "method": "POST",
        "timeout": 0,
        "headers": {
            "Authorization": Authorization,
            "Refresh_Token": Refresh_Token,
            "Content-Type": "application/json"
        },
        "data": JSON.stringify({
            "comment": commentStr
        }),
    };
>>>>>>> d314029eff17ddf05ce5e31e6686da9fc4ce9479

    $.ajax(settings).done(function (response) {
        alert("댓글작성 완료")
        document.querySelector("#comment").value = ""
        renderComments(currentPostId) // 댓글목록 새로 불러오기
    });

}


// 댓글 수정
function editComment(event) {
<<<<<<< HEAD
  const currentPostId = localStorage.getItem("current_post_id")
  const currentCommentId = event.currentTarget.parentElement.previousElementSibling.children[2].innerText
  const newCommentStr = event.currentTarget.parentElement.previousElementSibling.previousElementSibling.value
  var settings = {
    "url": `http://localhost:8080/comments/${currentCommentId}`,
    "method": "PUT",
    "timeout": 0,
    "headers": {
      "Authorization": Authorization,
      "Refresh_Token": Refresh_Token,
      "Content-Type": "application/json"
    },
    "data": JSON.stringify({
      "comment": newCommentStr
    }),
  };
=======
    const currentPostId = localStorage.getItem("current_post_id")
    const currentCommentId = event.currentTarget.parentElement.previousElementSibling.children[2].innerText
    const newCommentStr = event.currentTarget.parentElement.previousElementSibling.previousElementSibling.value
    var settings = {
        "url": "http://localhost:8080/comments/" + currentCommentId,
        "method": "PUT",
        "timeout": 0,
        "headers": {
            "Authorization": Authorization,
            "Refresh_Token": Refresh_Token,
            "Content-Type": "application/json"
        },
        "data": JSON.stringify({
            "comment": newCommentStr
        }),
    };
>>>>>>> d314029eff17ddf05ce5e31e6686da9fc4ce9479

    $.ajax(settings).done(function (response) {
        alert("수정 완료")
        renderComments(currentPostId)
    });
}


// 댓글 수정모드 전환
function convert_edit_comment(event) {
    const original_comment = event.currentTarget.parentNode.previousSibling.previousSibling.innerText;
    event.currentTarget.parentNode.previousSibling.value = original_comment;
    event.currentTarget.parentNode.classList.toggle('button_hide');
    event.currentTarget.parentNode.nextSibling.classList.toggle('button_hide');
    event.currentTarget.parentNode.previousSibling.classList.toggle('button_hide');
    event.currentTarget.parentNode.previousSibling.previousSibling.classList.toggle('button_hide');
}


// 댓글 수정모드 전환 전 유저검증
function gotoEditComment(event) {
    const currentCommentId = event.currentTarget.nextSibling.nextSibling.innerText
    const currentCommentUserId = event.currentTarget.nextSibling.nextSibling.nextSibling.nextSibling.innerText
    if (tempUserId !== currentCommentUserId) {
        alert('작성자만 수정 가능합니다');
        return;
    }
    convert_edit_comment(event) // 댓글 수정모드 전환
}


// 댓글 삭제
function deleteComment(event) {
    const currentPostId = localStorage.getItem("current_post_id")
    const currentCommentId = event.currentTarget.nextElementSibling.nextElementSibling.innerText

<<<<<<< HEAD
  var settings = {
    "url": `http://localhost:8080/comments/${currentCommentId}`,
    "method": "DELETE",
    "timeout": 0,
    "headers": {
      "Authorization": Authorization,
      "Refresh_Token": Refresh_Token,
      "Content-Type": "application/json"
    },
  };
=======
    var settings = {
        "url": "http://localhost:8080/comments/" + currentCommentId,
        "method": "DELETE",
        "timeout": 0,
        "headers": {
            "Authorization": Authorization,
            "Refresh_Token": Refresh_Token,
            "Content-Type": "application/json"
        },
    };
>>>>>>> d314029eff17ddf05ce5e31e6686da9fc4ce9479

    $.ajax(settings).done(function (response) {
        alert("삭제 완료")
        renderComments(currentPostId)
    });
}


// 수정모드 취소 (삭제모드로 전환)
function gotoDeleteComment(event) {
    event.currentTarget.parentNode.classList.toggle('button_hide');
    event.currentTarget.parentNode.previousSibling.classList.toggle('button_hide');
    event.currentTarget.parentNode.previousSibling.previousSibling.classList.toggle('button_hide');
    event.currentTarget.parentNode.previousSibling.previousSibling.previousSibling.classList.toggle('button_hide');
}


/**
 * 좋아요
 */

// 좋아요 버튼
function addLikeFeatureToButtons() {
    const like_buttons = document.querySelectorAll(".like-button");

    like_buttons.forEach((button) => {
        button.addEventListener("click", function (e) {
            console.log('hi')
            e.preventDefault();
            this.classList.toggle("active"); // 좋아요 상태를 변경

            let currentLikeCount = parseInt(this.firstElementChild.innerText)

            const nodes = [...e.currentTarget.parentElement.parentElement.parentElement.children];
            let index = nodes.indexOf(e.currentTarget.parentElement.parentElement);

            const postArray = JSON.parse(localStorage.getItem("pagedPostList")) // to get postId
            let currentPostId = postArray[index]["id"]

            // 변경된 상태에 따라 동작
            if (this.classList.contains('active')) {
                doLike(currentPostId)
                e.currentTarget.classList.add("animated");
                currentLikeCount = currentLikeCount + 1
                this.firstElementChild.innerText = String(currentLikeCount)
            } else {
                unLike(currentPostId)
                e.currentTarget.classList.remove("animated");
                currentLikeCount = currentLikeCount - 1
                this.firstElementChild.innerText = String(currentLikeCount)
            }
        });
    })

}


function plusOrMinus() {
    return Math.random() < 0.5 ? -1 : 1;
}

function randomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1) + min);
}


<<<<<<< HEAD
// 좋아요 누르기
function doLike(postId) {
  var settings = {
    "url": `http://localhost:8080/posts/${postId}/like`,
    "method": "POST",
    "timeout": 0,
    "headers": {
      "Authorization": Authorization,
      "Refresh_Token": Refresh_Token
    },
  };
  
  $.ajax(settings).done(function (response) {
    console.log(response);
  }).fail(function () {
    alert("like failed")
    location.reload()
  });
=======
// 좋아요 여부 확인 -> 좋아요 선택되어있게 표시
function checkLike(postId) {
    // console.log(String(postId))
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/posts/" + String(postId) + "/likeCheck",
        async: false,
        data: {},
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", Authorization);
            xhr.setRequestHeader("Refresh_Token", Refresh_Token);
        },
        success: function (response) {
            if (response["message"] === "true") {
                localStorage.setItem("currentLikeChecked", true)
            } else {
                localStorage.setItem("currentLikeChecked", false)
            }
        }
    });
}


// 좋아요 누르기
function doLike(postId) {
    var settings = {
        "url": "http://localhost:8080/posts/" + String(postId) + "/like",
        "method": "POST",
        "timeout": 0,
        "headers": {
            "Authorization": Authorization,
            "Refresh_Token": Refresh_Token
        },
    };

    $.ajax(settings).done(function (response) {
        console.log(response);
    }).fail(function () {
        alert("like failed")
        location.reload()
    });
>>>>>>> d314029eff17ddf05ce5e31e6686da9fc4ce9479
}


// 좋아요 취소
function unLike(postId) {
<<<<<<< HEAD
  var settings = {
    "url": `http://localhost:8080/posts/${postId}/like`,
    "method": "DELETE",
    "timeout": 0,
    "headers": {
      "Authorization": Authorization,
      "Refresh_Token": Refresh_Token
    },
  };
  
  $.ajax(settings).done(function (response) {
    console.log(response);
  }).fail(function () {
    alert("unlike failed")
    location.reload()
  });
}
=======
    var settings = {
        "url": "http://localhost:8080/posts/" + String(postId) + "/like",
        "method": "DELETE",
        "timeout": 0,
        "headers": {
            "Authorization": Authorization,
            "Refresh_Token": Refresh_Token
        },
    };
>>>>>>> d314029eff17ddf05ce5e31e6686da9fc4ce9479

    $.ajax(settings).done(function (response) {
        console.log(response);
    }).fail(function () {
        alert("unlike failed")
        location.reload()
    });
}


/**
 * 채팅
 */


// 프로필 DM
function gotochat() {
    alert('채팅 기록을 불러옵니다. 추후 구현 예정')
    window.open('./chattingPage.html');
}


