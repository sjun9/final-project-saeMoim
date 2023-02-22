/**
 * 임시 정보
 */
const Authorization = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiTmFtZSI6ImJiYiIsImF1dGgiOiJVU0VSIiwiZXhwIjoxNjc3MTAwNTg2LCJpYXQiOjE2NzcwOTkzODZ9.BLR4u0wNfdNLBmZXuGoCzZ-YA0QWsKZBEv5yEno-s-g"
const Refresh_Token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiZXhwIjoxNjc3MTAyOTg2LCJpYXQiOjE2NzcwOTkzODZ9.wmhym_HsIKVXmOaRd92jVJGSZL3u0CPifC8dXeD10LY"

const tempGroupId = "1" // main -> board 전환시, 현재 어느 그룹의 게시판으로 넘어왔는지 localStorage에 저장후 getItem하여 활용
const tempUserId = "2" // 현재 로그인중인(나의) userId는 어떻게 들고오나요? (수정 삭제 시 본인확인용)
// 그리고 webconfig permitAll ???




/**
 * 우측 프로필 리스트
 */


// 해당 그룹 참여자 리스트 가져와서 localStorage에 저장
function getGroupProfileIdList() {
  var settings = {
    "url": "http://localhost:8080/group/" + tempGroupId + "/participant",
    "method": "GET",
    "timeout": 0,
    "headers": {
      "Authorization": Authorization,
      "Refresh_Token": Refresh_Token
    },
  };

  $.ajax(settings).done(function (response) {
    localStorage.setItem("profileIdList", JSON.stringify(response));
    const profileIdList = JSON.parse(localStorage.getItem("profileIdList"))
  });
}


// 프로필 리스트 전체 삭제
function deleteProfileList() {
  const userZone = document.querySelector('#user_zone');
  while (userZone.firstChild) {
    userZone.firstChild.remove()
  }
}


// localStorage에서 참여자 리스트 가져와서 페이지 우측에 표시
function renderProfileList() {
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

  $.ajax(settings).done(function (response) {
    const profile_modal_page = document.querySelector("#profile_modal_page")
    profile_modal_page.children[2].children[0].innerText = username
    profile_modal_page.children[3].innerText = response["content"]
  });
}

function profile() {
  getGroupProfileIdList()
  deleteProfileList()
  renderProfileList()
}
profile()







/**
 * 게시글
 */


// 게시글 생성 (토큰 필요)
function newPost() {
  const newPostTitle = document.querySelector("#newPost-title").value
  const newPostContent = document.querySelector("#newPost-content").value
  // const imageUrl  = 이미지 경로 추가

  var settings = {
    "url": "http://localhost:8080/posts/groups/" + tempGroupId,
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



function editPost(event) {
  const new_title = document.querySelector("#editPost-title").value
  const new_content = document.querySelector("#editPost-content").value
  const currentPostId = localStorage.getItem("current_post_id")
  
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
}



// 게시글 수정 모달창 열기
// 추후 게시글 이미지 url 추가 필요함
function editPostModal(event) {
  // 값 가져와서 수정 api
  
  const original_title = document.querySelector('#readPostModalLabel').innerText
  const original_content = document.querySelector('#readPostModalContent').innerText

  document.querySelector("#closeReadPostModal").click() // 모달창 닫고
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


// 게시글 삭제
function deletePost(event) {
  // 유저 확인하고 글 삭제
  alert("게시글 삭제")
}



/**
 * 게시글 렌더링
 */

// https://nohack.tistory.com/125

const contents = document.querySelector(".contents");
const buttons = document.querySelector(".buttons");

setPostsCount();
const numOfContent = localStorage.getItem("postsCount");// 전체 게시글 갯수
const maxContent = 10; // 표시할 게시글 갯수
const maxButton = 5; // 표시할 버튼 갯수
const maxPage = Math.ceil(numOfContent / maxContent);
let page = 1; // 새로고침 시 1페이지부터 시작


// 게시글 전체 길이 가져오기
function setPostsCount() {
  $.ajax({
    type: 'GET',
    url: "http://localhost:8080/posts/groups/1/count",
    data: {},
    async: false, // 비동기 해제
    success: function (response) {
      localStorage.setItem("postsCount", response)
    }
  });
}


// 해당 페이지 게시글 리스트 가져오기
function getPosts(pageNum, sizeNum) {
  $.ajax({
    type: 'GET',
    url: "http://localhost:8080/posts/groups/1?page=" + pageNum + "&size=" + sizeNum,
    data: {},
    async: false, // 비동기 해제
    success: function (data) {
      localStorage.setItem("pagedPostList", JSON.stringify(data));
      const postArray = JSON.parse(localStorage.getItem("pagedPostList"))
    }
  });
}


// 게시글 요소 생성
const makeContent = (i) => {
  const currentPost = JSON.parse(localStorage.getItem("pagedPostList"))[i];

  const contentwrap = document.createElement("li");
  contentwrap.classList.add("contentwrap");

  const content__header = document.createElement("div");
  content__header.classList.add("content__header");

  content__header.innerHTML = `
    <span class="content__header__id">${currentPost["id"]}</span>
    <span class="content__header__title" onclick="openBody(event)" data-bs-toggle="modal" data-bs-target="#readPostModal">${currentPost["title"]}</span>
    <span class="content__header__author">${currentPost["username"]}</span>
    <span class="content__header__date">${currentPost["createdAt"]}</span>
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
};


// 버튼 렌더링
const renderButton = (page) => {
  while (buttons.hasChildNodes()) {
    buttons.removeChild(buttons.lastChild);
  }
  for (let i = page; i < page + maxButton && i <= maxPage; i++) {
    buttons.appendChild(makeButton(i));
  }
  buttons.children[0].classList.add("active"); // 첫 로딩시 가장 왼쪽 페이지 선택

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
  renderContent(page);
  renderButton(page);
};


// 게시판 실행 (1 페이지)
render(page);








/**
* 댓글 관련 기능
*/


// 댓글 불러오기
function renderComments(currentPostId) {

  const commentList = document.querySelector('#commentList');
  while (commentList.firstChild) {
    commentList.firstChild.remove()
  }

  var settings = {
    "url": "http://localhost:8080/comments/" + currentPostId,
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

    if (cCnt > 0) {
      for (i = 0; i < cCnt; i++) {
        let temp_html = `
        <table class='table'>
          <h6><strong>${response[i]["username"]}</strong></h6>
          <p style="overflow: hidden; word-wrap: break-word;">
            ${response[i]["comment"]}
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
            <div style="display: none">${response[i]["id"]}</div>
            <div style="display: none">${response[i]["userId"]}</div>
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
  
  $.ajax(settings).done(function (response) {
    alert("댓글작성 완료")
    document.querySelector("#comment").value = ""
    renderComments(currentPostId) // 댓글목록 새로 불러오기
  });
  
}


// 댓글 수정
function editComment(event) {
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
 * 채팅
 */


// 프로필 DM
function gotochat() {
  alert('채팅 기록을 불러옵니다. 추후 구현 예정')
  window.open('./chattingPage.html');
}


