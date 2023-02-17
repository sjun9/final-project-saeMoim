// https://nohack.tistory.com/125

const contents = document.querySelector(".contents");
const buttons = document.querySelector(".buttons");

const numOfContent = 178;
const maxContent = 10;
const maxButton = 5;
const maxPage = Math.ceil(numOfContent / maxContent);
let page = 1;

const makeContent = (id) => {
  const contentwrap = document.createElement("li");
  contentwrap.classList.add("contentwrap");

  const content__header = document.createElement("div");
  content__header.classList.add("content__header");

  content__header.innerHTML = `
    <span class="content__header__id">${id}</span>
    <span class="content__header__title" onclick="openBody(event)" data-bs-toggle="modal" data-bs-target="#readPostModal">게시물 제목</span>
    <span class="content__header__author">작성자</span>
    <span class="content__header__date">2022.01.01</span>
  `;

  contentwrap.appendChild(content__header);

  return contentwrap;
};

const makeButton = (id) => {
  const button = document.createElement("button");
  button.classList.add("button");
  button.dataset.num = id;
  button.innerText = id;
  button.addEventListener("click", (e) => {
    Array.prototype.forEach.call(buttons.children, (button) => {
      if (button.dataset.num) button.classList.remove("active");
    });
    e.target.classList.add("active");
    renderContent(parseInt(e.target.dataset.num));
  });
  return button;
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
  while (contents.hasChildNodes()) {
    contents.removeChild(contents.lastChild);
  }
  for (let id = (page - 1) * maxContent + 1; id <= page * maxContent && id <= numOfContent; id++) {
    contents.appendChild(makeContent(id));
  }
};

const renderButton = (page) => {
  while (buttons.hasChildNodes()) {
    buttons.removeChild(buttons.lastChild);
  }
  for (let id = page; id < page + maxButton && id <= maxPage; id++) {
    buttons.appendChild(makeButton(id));
  }
  buttons.children[0].classList.add("active");

  buttons.prepend(prev);
  buttons.append(next);

  if (page - maxButton < 1) buttons.removeChild(prev);
  if (page + maxButton > maxPage) buttons.removeChild(next);
};

const render = (page) => {
  renderContent(page);
  renderButton(page);
};
render(page);





function openBody(event) {
  // 모달창에 해당 클릭이벤트 id 글 뿌려줌
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
  
  getCommentList();
  
});

/**
* 댓글 불러오기(Ajax)
*/
function getCommentList(){
  
  $.ajax({
      type:'GET',
      url : "<c:url value='/board/commentList.do'/>",
      dataType : "json",
      data:$("#commentForm").serialize(),
      contentType: "application/x-www-form-urlencoded; charset=UTF-8", 
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