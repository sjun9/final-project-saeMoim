
const sidebarListItems = document.querySelectorAll(".sidebar-list-item");
const appContents = document.querySelectorAll(".app-content");

let newMoimTemp = 
`
<div class="products-row">
  <div class="product-cell image">
    <img src="../static/images/main-english.jpg" alt="">
    <span>새 모임</span>
  </div>
  <div class="product-cell category"><span class="cell-label">카테고리:</span>Bathroom</div>
  <div class="product-cell status-cell">
    <span class="cell-label">모임상태:</span>
    <span class="status disabled">Disabled</span>
  </div>
  <div class="product-cell sales"><span class="cell-label">참가인원:</span>22</div>
  <div class="product-cell stock"><span class="cell-label">참여가능인원:</span>44</div>
  <div class="product-cell price"><span class="cell-label">후기수:</span>160</div>
</div>
`;

sidebarListItems.forEach((sidebarListItem) => {
  sidebarListItem.addEventListener('click',()=>{
    sidebarListItems.forEach( (selectedItem) => selectedItem.classList.remove('active'));
    appContents.forEach( (appContent) => appContent.classList.remove('active'));
    sidebarListItem.classList.add('active');
  })
})


document.querySelector("#side-find").addEventListener("click", () => {
  document.querySelector("#side-find-content").classList.add("active");
})
document.querySelector("#side-mypage").addEventListener("click", () => {
  document.querySelector("#side-mypage-content").classList.add("active");
})
document.querySelector("#side-profile").addEventListener("click", () => {
  document.querySelector("#side-profile-content").classList.add("active");
})
document.querySelector("#side-chat").addEventListener("click", () => {
  document.querySelector("#side-chat-content").classList.add("active");
})


document.querySelector(".jsFilter").addEventListener("click", function () {
  document.querySelector(".filter-menu").classList.toggle("active");
});

// modal
const body = document.querySelector('body');
const modal = document.querySelector('.modal');
const btnOpenPopup = document.querySelector('.btn-open-popup');

function showModal() {
  modal.classList.toggle('show');

  if (modal.classList.contains('show')) {
    body.style.overflow = 'hidden';
  }
}

function modalEscape(event) {
  if (event.target === modal) {
    modal.classList.toggle('show');

    if (!modal.classList.contains('show')) {
      body.style.overflow = 'auto';
    }
  }
}

btnOpenPopup.addEventListener('click', showModal);
modal.addEventListener('click', modalEscape);



// function newMoim() {
//   alert('새 모임 추가 완료!')
//   location.reload();
// }

const categories = document.querySelectorAll('.dropdown-item')
categories.forEach( (category) => {
  category.addEventListener('click', changeValue)
})

function changeValue(event) {
  document.querySelector('#newMoim-category').value = event.target.innerText
}

function gotochat() {
  alert('채팅 기록을 불러옵니다. 추후 구현 예정')
  window.open('./chattingPage.html');
}

function logout() {
  alert('로그아웃');
}
  

function showMoimDetail(event) {
    let targetTitle = event.currentTarget.firstChild.nextSibling.children[1].innerText;
    let targetImage = event.currentTarget.firstChild.nextSibling.children[0].currentSrc;

    document.querySelector('#moimDetail_Title').innerText = targetTitle;
    document.querySelector('#moimDetail_Image').src = targetImage;
}

function attendMoim() {
    alert('모임 참여 후 페이지 새로고침')

    // 페이지 새로고침
    // location.reload()
}

function editMoim() {
    alert('모임수정 페이지로 넘어갑니다.\n본인이 개설한 모임이 아니면 수정 못하게 막기\n모임수정페이지 만들어야하니 일단 이건 나중에 합시다')
}

function wishMoim() {
    alert('찜 등록 후 페이지 새로고침\n마이페이지에 찜목록 보기 추가예정')
}

function gotoBoard() {
    alert('[게시판으로 페이지 이동]\n본인이 개설 또는 참가한 모임이 아니면 입장 못하게 막기\nlocatStorage에 모임 정보를 넘겨준 뒤, board.html로 이동합니다\n그러면 board.html에서 localStorage의 정보로 게시글을 가져옴')
    
    alert('게시판으로 이동합니다.')
    // 게시판으로 이동
    location.href = './board.html'
}