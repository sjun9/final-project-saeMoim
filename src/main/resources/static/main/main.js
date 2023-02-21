const sidebarListItems = document.querySelectorAll(".sidebar-list-item");
const appContents = document.querySelectorAll(".app-content");


sidebarListItems.forEach((sidebarListItem) => {
    sidebarListItem.addEventListener('click', () => {
        sidebarListItems.forEach((selectedItem) => selectedItem.classList.remove('active'));
        appContents.forEach((appContent) => appContent.classList.remove('active'));
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


const categories = document.querySelectorAll('.dropdown-item')
categories.forEach((category) => {
    category.addEventListener('click', changeValue)
})

$(document).ready(function () {
    showMoim('all')
    showMoim('popular')
    showMoim('made')
    showMoim('participant')
    showRequestedGroup()
    showAplliedGroup()
    showCategory()
});

function changeValue(event) {
    document.querySelector('#newMoim-category').value = event.target.innerText
}

function gotochat() {
    alert('채팅 기록을 불러옵니다. 추후 구현 예정')
    window.open('./chattingPage.html');
}

function logout() {
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/log-out",
        success: function (response) {
            console.log(response)
        }
    });
}

function showCategory() {
    $('#categoryFilter').empty().append(`<option value=0>전체</option>`)
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/category",
        success: function (response) {
            console.log(response)
            for (let i = 0; i < response.length; i++) {
                let categories = response[i]['categories']
                for (let i = 0; i < categories.length; i++) {
                    let id = categories[i]['id']
                    let name = categories[i]['name']
                    let temp_html = `<option value=${id}>${name}</option>`
                    $('#categoryFilter').append(temp_html)
                }
            }
        }
    });
}

function showMoim(type) {
    let contentId;
    let url;
    if (type === "all") {
        contentId = '#find-content';
        url = "http://localhost:8080/group";
    } else if (type === "popular") {
        contentId = '#popular-content';
        url = "http://localhost:8080/group/popular";
    } else if (type === "made") {
        contentId = '#made-group';
        url = "http://localhost:8080/leader/group";
    } else if (type === "participant") {
        contentId = '#participant-group';
        url = "http://localhost:8080/participant/group";
    }

    $(contentId).empty()
    $.ajax({
        type: "GET",
        url: url,
        success: function (response) {
            for (let i = 0; i < response.length; i++) {
                let id = response[i]['id']
                let groupName = response[i]['groupName']
                let content = response[i]['content']
                let categoryName = response[i]['categoryName']
                let participantCount = response[i]['participantCount']
                let recruitNumber = response[i]['recruitNumber']
                let wishCount = response[i]['wishCount']
                let status = response[i]['status']

                let temp_html = `<div class="products-row" data-bs-toggle="modal" data-bs-target="#moimDetailModal" 
                                    onClick="showMoimDetail(event, ${id})">
                                    <div class="product-cell image">
                                        <img src="../static/images/main-running.jpg" alt="">
                                            <span>${groupName}</span>
                                            <input type="hidden" value=${content}>
                                    </div>
                                    <div class="product-cell category"><span class="cell-label">카테고리:</span>${categoryName}</div>
                                    <div class="product-cell status-cell">
                                        <span class="cell-label">모임상태:</span>
                                        <span class="status active">${status}</span>
                                    </div>
                                    <div class="product-cell sales"><span class="cell-label">참가인원:</span>${participantCount}</div>
                                    <div class="product-cell stock"><span class="cell-label">모집인원:</span>${recruitNumber}</div>
                                    <div class="product-cell price"><span class="cell-label">관심 등록 수:</span>${wishCount}</div>
                                </div>`
                $(contentId).append(temp_html)
                console.log(response)
            }
        }
    });
}


function showFilter(categoryId, status) {
    $('#find-content').empty()
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/group/categories/" + categoryId,
        data: {status: status}, //전송 데이터
        success: function (response) {
            for (let i = 0; i < response.length; i++) {
                let id = response[i]['id']
                let groupName = response[i]['groupName']
                let content = response[i]['content']
                let categoryName = response[i]['categoryName']
                let participantCount = response[i]['participantCount']
                let recruitNumber = response[i]['recruitNumber']
                let wishCount = response[i]['wishCount']
                let status = response[i]['status']

                let temp_html = `<div class="products-row" data-bs-toggle="modal" data-bs-target="#moimDetailModal" 
                                    onClick="showMoimDetail(event, ${id})">
                                    <div class="product-cell image">
                                        <img src="../static/images/main-running.jpg" alt="">
                                            <span>${groupName}</span>
                                            <input type="hidden" value=${content}>
                                    </div>
                                    <div class="product-cell category"><span class="cell-label">카테고리:</span>${categoryName}</div>
                                    <div class="product-cell status-cell">
                                        <span class="cell-label">모임상태:</span>
                                        <span class="status active">${status}</span>
                                    </div>
                                    <div class="product-cell sales"><span class="cell-label">참가인원:</span>${participantCount}</div>
                                    <div class="product-cell stock"><span class="cell-label">모집인원:</span>${recruitNumber}</div>
                                    <div class="product-cell price"><span class="cell-label">관심 등록 수:</span>${wishCount}</div>
                                </div>`
                $('#find-content').append(temp_html)
                console.log(response)
            }
        }
    });
}


function showReview(id) {
    $('#moimDetail_reviews').empty();
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/groups/" + id + "/review",
        success: function (response) {
            for (let i = 0; i < response.length; i++) {
                let content = response[i]['content']
                let username = response[i]['username']

                let temp_html = `<div class="container">
                                    <form id="reivewListForm" name="reivewListForm" method="post">
                                      <div id="reivewList">
                                        <table class='table'>
                                          <h6><strong>작성자 : ${username}</strong></h6>
                                          <p style="overflow: hidden; word-wrap: break-word;">
                                            ${content}
                                          </p>
                                          <tr>
                                            <td></td>
                                          </tr>
                                          <textarea class="button_hide" style="width: 100%" rows="3" cols="30" id="reivew"
                                            name="reivew"></textarea>
                                          <div class="edit_delete">
                                            <button type="button" class="btn btn-danger" style="float: right;">삭제</button>
                                            <button type="button" onclick="gotoEditReview(event)" class="btn btn-secondary"
                                              style="float: right; margin-right: 8px;">수정</button>
                                          </div>
                                          <div class="cancel_submit button_hide">
                                            <button type="button" class="btn btn-success" style="float: right;">수정</button>
                                            <button type="button" onclick="gotoDeleteReview(event)" class="btn btn-secondary"
                                              style="float: right; margin-right: 8px;">취소</button>
                                          </div>
                                        </table>
                                      </div>
                                    </form>
                                  </div>`
                $('#moimDetail_reviews').append(temp_html)
                console.log(response)
            }
        }
    });
}


function showRequestedGroup() {
    $('#requested-group').empty();
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/leader/application",
        success: function (response) {
            for (let i = 0; i < response.length; i++) {
                let groupName = response[i]['groupName']
                let username = response[i]['username']
                let status = response[i]['status']

                let temp_html = `<tr>
                                    <td>${groupName}</td>
                                    <td>${username}</td>
                                    <td>${status}</td>
                                  </tr>`
                $('#requested-group').append(temp_html)
                console.log(response)
            }
        }
    });
}

function showAplliedGroup() {
    $('#applied-group').empty()
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/participant/application",
        success: function (response) {
            for (let i = 0; i < response.length; i++) {
                let groupName = response[i]['groupName']
                let username = response[i]['username']
                let status = response[i]['status']

                let temp_html = `<tr>
                                    <td>${groupName}</td>
                                    <td>${username}</td>
                                    <td>${status}</td>
                                  </tr>`
                $('#applied-group').append(temp_html)
                console.log(response)
            }
        }
    });
}


function showMoimDetail(event, id) {
    let targetTitle = event.currentTarget.firstChild.nextSibling.children[1].innerText;
    let targetContent = event.currentTarget.firstChild.nextSibling.children[2].value;
    document.querySelector('#moimDetail_Title').innerText = targetTitle;
    document.querySelector('#moimDetail_introduce').innerText = targetContent;
    document.getElementById('moimDetailId').value = id;
    showReview(id);
}

function saveMoim() {
    let jsonData = { // Body에 첨부할 json 데이터
        "name": $('#newMoim-title').val(),
        "tag": $('#newMoim-tag').val(),
        "category": $('#newMoim-category').val(),
        "content": $('#newMoim-content').val(),
        "recruitNumber": $('#newMoim-recruit').val()
    };

    $.ajax({
        type: "post",
        url: "http://localhost:8080/group",
        headers: {'Content-Type': 'application/json'},
        data: JSON.stringify(jsonData), //전송 데이터
        dataType: "JSON", //응답받을 데이터 타입 (XML,JSON,TEXT,HTML,JSONP)
        contentType: "application/json; charset=utf-8", //헤더의 Content-Type을 설정
        ///보낼 데이터를 JSON.stringify()로 감싸주어야 함
        success: function (data) {
            console.log(data);
        },
        error: function (e) {
        }
    })

    // alert('새 모임 추가 완료!')
    // location.reload();
}


function modifyMoim() {

    // alert('새 모임 수정 완료!')
    // location.reload();
}


function attendMoim(id) {
    console.log(id)
    $.ajax({
        type: "post",
        url: "http://localhost:8080/groups/" + id + "/application",
        headers: {'Content-Type': 'application/json'},
        ///보낼 데이터를 JSON.stringify()로 감싸주어야 함
        success: function (data) {
            console.log(data);
        },
        error: function (e) {
        }
    })

    // 페이지 새로고침
    // location.reload()
}

function withdrawMoim(id) {
    $.ajax({
        type: "post",
        url: "http://localhost:8080/group/" + id + "/participant",
        headers: {'Content-Type': 'application/json'},
        ///보낼 데이터를 JSON.stringify()로 감싸주어야 함
        success: function (data) {
            console.log(data);
        },
        error: function (e) {
        }
    })

    // 페이지 새로고침
    // location.reload()
}

function editMoim(id) {
    alert('모임수정 페이지로 넘어갑니다.\n본인이 개설한 모임이 아니면 수정 못하게 막기\n모임수정페이지 만들어야하니 일단 이건 나중에 합시다')
}

function deleteMoim(id) {
    $.ajax({
        type: "delete",
        url: "http://localhost:8080/groups/" + id,
        headers: {'Content-Type': 'application/json'},
        ///보낼 데이터를 JSON.stringify()로 감싸주어야 함
        success: function (data) {
            console.log(data);
        },
        error: function (e) {
        }
    })
    alert('모임삭제 페이지로 넘어갑니다.\n본인이 개설한 모임이 아니면 삭제 못하게 막기\n모임삭제페이지 만들어야하니 일단 이건 나중에 합시다')
}

function wishMoim(id) {
    $.ajax({
        type: "post",
        url: "http://localhost:8080/groups/" + id + "/wish",
        headers: {'Content-Type': 'application/json'},
        ///보낼 데이터를 JSON.stringify()로 감싸주어야 함
        success: function (data) {
            console.log(data);
        },
        error: function (e) {
        }
    })
    alert('찜 등록 후 페이지 새로고침\n마이페이지에 찜목록 보기 추가예정')
}

function gotoBoard() {
    alert('[게시판으로 페이지 이동]\n본인이 개설 또는 참가한 모임이 아니면 입장 못하게 막기\nlocatStorage에 모임 정보를 넘겨준 뒤, board.html로 이동합니다\n그러면 board.html에서 localStorage의 정보로 게시글을 가져옴')

    alert('게시판으로 이동합니다.')
    // 게시판으로 이동
    location.href = './board.html'
}

function gotoEditReview(event) {
    const original_review = event.currentTarget.parentNode.previousSibling.previousSibling.innerText;
    console.log(event.currentTarget.parentNode.previousSibling)
    event.currentTarget.parentNode.previousSibling.value = original_review;
    event.currentTarget.parentNode.classList.toggle('button_hide');
    event.currentTarget.parentNode.nextSibling.classList.toggle('button_hide');
    event.currentTarget.parentNode.previousSibling.classList.toggle('button_hide');
    event.currentTarget.parentNode.previousSibling.previousSibling.classList.toggle('button_hide');
}

function gotoDeleteReview(event) {
    event.currentTarget.parentNode.classList.toggle('button_hide');
    event.currentTarget.parentNode.previousSibling.classList.toggle('button_hide');
    event.currentTarget.parentNode.previousSibling.previousSibling.classList.toggle('button_hide');
    event.currentTarget.parentNode.previousSibling.previousSibling.previousSibling.classList.toggle('button_hide');
}