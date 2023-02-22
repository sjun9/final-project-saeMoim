const loginEmail = "aaa@naver.com"
const loginPassword = "aaaa1234!"

function login(email, password)  { // 임시로 토큰 받아오는 용

  var settings = {
    "url": "http://localhost:8080/sign-in",
    "method": "POST",
    "timeout": 0,
    "headers": {
      "Content-Type": "application/json"
    },
    "data": JSON.stringify({
      "email": "aaa@naver.com",
      "password": "aaaa1234!"
    }),
  };
  
  $.ajax(settings).done(function (response, status, xhr) {
    console.log(response);
    // console.log(xhr);
    console.log(xhr.getResponseHeader('Authorization'))
    // localStorage.setItem("Authorization", xhr.getResponseHeader("Authorization"))
  });


}
login(loginEmail, loginPassword)


function setPostsCount() {
  var settings = {
    "url": "http://localhost:8080/posts/groups/1/count",
    "method": "GET",
    "timeout": 0,
  };
  
  $.ajax(settings).done(function (response) {
    localStorage.setItem("postsCount", response)
  });
}


function getPosts(pageNum, sizeNum) {
  // 비동기
  // var settings = {
  //   "url": "http://localhost:8080/posts/groups/1?page=" + pageNum + "&size=" + sizeNum,
  //   "method": "GET",
  //   "timeout": 0
  // };
  
  // $.ajax(settings).done(function (response) {
  //   localStorage.setItem("pagedPostList", JSON.stringify(response));

  //   // 임시
  //   const postArray = JSON.parse(localStorage.getItem("pagedPostList"))
  //   console.log(postArray);
  // });

  // 동기
  $.ajax({
    type: 'GET',
    url: "http://localhost:8080/posts/groups/1?page=" + pageNum + "&size=" + sizeNum,
    data: {},
    async: false, // 비동기 해제
    success: function(data) {
      localStorage.setItem("pagedPostList", JSON.stringify(data));
      const postArray = JSON.parse(localStorage.getItem("pagedPostList"))
      // console.log(postArray);
    }
  });
}



// https://nohack.tistory.com/125

const contents = document.querySelector(".contents");
const buttons = document.querySelector(".buttons");

setPostsCount();
const numOfContent = localStorage.getItem("postsCount"); // 전체 게시글 갯수
const maxContent = 10; // 표시할 게시글 갯수
const maxButton = 5;
const maxPage = Math.ceil(numOfContent / maxContent);
let page = 1;





const makeContent = (i) => {
  const currentPost = JSON.parse(localStorage.getItem("pagedPostList"))[i];
  // console.log(currentPost["title"])

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
  // console.log("appended " + currentPost["title"])
  return contentwrap;
};



const makeButton = (i) => {
  const button = document.createElement("button");

  button.classList.add("button");
  button.innerText = i;
  return button;

  // const button = document.createElement("button");
  // button.classList.add("button");
  // button.dataset.num = i;
  // button.innerText = i;
  // button.addEventListener("click", (e) => {
  //   Array.prototype.forEach.call(buttons.children, (button) => {
  //     if (button.dataset.num) button.classList.remove("active");
  //   });
  //   e.target.classList.add("active");
  //   renderContent(parseInt(e.target.dataset.num));
  // });
  // return button;
};

// Prev, Next Button
const goPrevPage = () => {
  page -= maxButton;
  render(page);
};

const goNextPage = () => {
  page += maxButton;
  render(page);
};

const prev = document.createElement("button");
prev.classList.add("button", "prev");
prev.innerHTML = '<ion-icon name="chevron-back-outline"></ion-icon>';
prev.addEventListener("click", goPrevPage);

const next = document.createElement("button");
next.classList.add("button", "next");
next.innerHTML = '<ion-icon name="chevron-forward-outline"></ion-icon>';
next.addEventListener("click", goNextPage);



const renderContent = (page) => {
  // console.log("rendering page " + page + "...")
  
  while (contents.hasChildNodes()) {
    contents.removeChild(contents.lastChild);
  }
  
  getPosts(page - 1, 10); // 페이징 처리된 게시글 리스트 가져옴
  
  const postArray = JSON.parse(localStorage.getItem("pagedPostList")) // 가져온 게시글 리스트 갯수를 계산하기 위해
 

  for (let i = 0; i <= postArray.length - 1; i++) {
    contents.appendChild(makeContent(i));
  }
};

const renderButton = (page) => {
  while (buttons.hasChildNodes()) {
    buttons.removeChild(buttons.lastChild);
  }
  for (let i = page; i < page + maxButton && i <= maxPage; i++) {
    buttons.appendChild(makeButton(i));
  }
  buttons.children[0].classList.add("active"); // 첫 로딩시 가장 왼쪽 페이지 선택

  const createdButtons = document.querySelectorAll('.button');
  createdButtons.forEach( (createdButton) => {
    createdButton.addEventListener('click', gotoPageNum)
  })

  buttons.prepend(prev);
  buttons.append(next);

  if (page - maxButton < 1) buttons.removeChild(prev);
  if (page + maxButton > maxPage) buttons.removeChild(next);
};


function gotoPageNum(event) {
  document.querySelectorAll('.button').forEach ( (onebutton) => {
    onebutton.classList.remove('active')
  })
  event.target.classList.add("active");
  // console.log("you clicked " + event.target.innerText)
  renderContent(event.target.innerText)
  // console.log(event.target.innerText + " page done")
}

const render = (page) => {
  renderContent(page);
  renderButton(page);
};
render(page); // 게시판 실행 첫페이지 1







function openBody(event) {
  // 모달창에 해당 클릭이벤트 id 글 뿌려줌
  
  const postArray = JSON.parse(localStorage.getItem("pagedPostList")) 
  const currentPostId = event.target.previousSibling.previousSibling.innerText
  let currentPostId_inPostList = currentPostId % 10
  if (currentPostId_inPostList === 0) currentPostId_inPostList = 10
  currentPostId_inPostList = currentPostId_inPostList - 1

  console.log(currentPostId_inPostList)
  document.querySelector('#readPostModalLabel').innerText = postArray[currentPostId_inPostList]["title"]
  document.querySelector('#readPostModalContent').innerText = postArray[currentPostId_inPostList]["content"]

 }

 function openProfile(event) {
  // 모달창에 해당 클릭이벤트 id 프로필 뿌려줌
 }


 function newPost() {
  alert('작성 완료!');
  location.reload();
 }


/**
* 댓글 관련 기능
*/



 function fn_comment(code){
    
  $.ajax({
      type:'POST',
      url : "<c:url value='/board/addComment.do'/>",
      data:$("#commentForm").serialize(),
      success : function(data){
          if(data=="success")
          {
              getCommentList();
              $("#comment").val("");
          }
      },
      error:function(request,status,error){
          //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
     }
      
  });
}

/**
* 초기 페이지 로딩시 댓글 불러오기
*/
$(function(){
  
  // getCommentList();
  
});

/**
* 댓글 불러오기(Ajax)
*/
function getCommentList(){
  
  $.ajax({
      type:'GET',
      url : "<c:url value='./testCommentList.do'/>",
      dataType : "json", // 받는 타입 -> success 로 처리
      data:$("#commentForm").serialize(),
      contentType: "application/json; charset=UTF-8",  // 보내는 타입
      success : function(data){
          
          var html = "";
          var cCnt = data.length;
          
          if(data.length > 0){
              
              for(i=0; i<data.length; i++){
                  html += "<div>";
                  html += "<div><table class='table'><h6><strong>"+data[i].writer+"</strong></h6>";
                  html += data[i].comment + "<tr><td></td></tr>";
                  html += "</table></div>";
                  html += "</div>";
              }
              
          } else {
              
              html += "<div>";
              html += "<div><table class='table'><h6><strong>등록된 댓글이 없습니다.</strong></h6>";
              html += "</table></div>";
              html += "</div>";
              
          }
          
          $("#cCnt").html(cCnt);
          $("#commentList").html(html);
          
      },
      error:function(request,status,error){
          
     }
      
  });
}



// 댓글 수정
function gotoEditComment(event) {
  const original_comment = event.currentTarget.parentNode.previousSibling.previousSibling.innerText;
  console.log(event.currentTarget.parentNode.previousSibling)
  event.currentTarget.parentNode.previousSibling.value = original_comment;
  event.currentTarget.parentNode.classList.toggle('button_hide');
  event.currentTarget.parentNode.nextSibling.classList.toggle('button_hide');
  event.currentTarget.parentNode.previousSibling.classList.toggle('button_hide');
  event.currentTarget.parentNode.previousSibling.previousSibling.classList.toggle('button_hide');
}
function gotoDeleteComment(event) {
  event.currentTarget.parentNode.classList.toggle('button_hide');
  event.currentTarget.parentNode.previousSibling.classList.toggle('button_hide');
  event.currentTarget.parentNode.previousSibling.previousSibling.classList.toggle('button_hide');
  event.currentTarget.parentNode.previousSibling.previousSibling.previousSibling.classList.toggle('button_hide');
}

function gotochat() {
  alert('채팅 기록을 불러옵니다. 추후 구현 예정')
  window.open('./chattingPage.html');
}