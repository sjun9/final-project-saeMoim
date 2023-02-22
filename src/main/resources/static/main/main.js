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
    showCategory()
    showAllMoim()
    showPopularMoim()
})
document.querySelector("#side-mypage").addEventListener("click", () => {
    document.querySelector("#side-mypage-content").classList.add("active");
    showLeaderMoim()
    showParticipantMoim()
    showRequestedGroup()
    showAppliedGroup()
})
document.querySelector("#side-profile").addEventListener("click", () => {
    document.querySelector("#side-profile-content").classList.add("active");
    showMyProfile()
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

$(document).ready(function () {
    showUsername()
    showAllMoim()
    showPopularMoim()
    showCategory()
    //showLeaderMoim()
    //showParticipantMoim()
    //showRequestedGroup()
    //showAppliedGroup()
});

function changeNewValue(event) {
    document.querySelector('#newMoim-category').value = event.target.innerText
}

function changeModifyValue(event) {
    document.querySelector('#modifyMoim-category').value = event.target.innerText
}

function gotochat() {
    alert('채팅 기록을 불러옵니다. 추후 구현 예정')
    window.open('./chattingPage.html');
}

function logout() {
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/log-out",
        headers: {'Content-Type': 'application/json', 'Authorization': localStorage.getItem('Authorization')},
        success: function (response) {
            console.log(response)
        }
    }).done(function (response, status, xhr) {
        localStorage.setItem('Authorization', xhr.getResponseHeader('Authorization'))
        localStorage.setItem('Refresh_Token', xhr.getResponseHeader('Refresh_Token'))
        window.location = './main.html'
    }).fail(function (e) {
        alert(e.responseText)
    });
}

function showUsername() {
    $.ajax({
        type: "get",
        url: "http://localhost:8080/user",
        headers: {'Authorization': localStorage.getItem('Authorization')},
        success: function (data) {
            let username = data['username']
            $('#username').append(`${username}`)
        }, error: function (e) {
            $('#username').append(`"로그인 해주세요"`)
        }
    });
}

function showMyProfile() {
    $('#myProfile').empty()
    let temp_html = `<div>비밀번호를 입력하세요.</div>
                        <input type="text" id="checkPassword" rows="2" cols="20">
                        <input type="button" value="입력" onclick="getMyProfile()">`
    $('#myProfile').append(temp_html)
}


function showCategory() {
    $('#categoryFilter').empty().append(`<option value=0>전체</option>`)
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/category"
    }).done(function (response) {
        console.log(response)
        for (let i = 0; i < response.length; i++) {
            let categories = response[i]['categories']
            for (let i = 0; i < categories.length; i++) {
                let id = categories[i]['id']
                let name = categories[i]['name']
                let temp_html = `<option value=${id}>${name}</option>`
                let temp_html2 = `<li><a class="dropdown-item" href="#" onclick="changeNewValue(event)">${name}</a></li>`
                let temp_html3 = `<li><a class="dropdown-item" href="#" onclick="changeModifyValue(event)">${name}</a></li>`
                $('#categoryFilter').append(temp_html)
                $('#categoryMenu').append(temp_html2)
                $('#modifyCategoryMenu').append(temp_html3)
            }
        }
    }).fail(function (e) {
        console.log(e.responseText)
    });
}


function showSearch(name) {
    $('#find-content').empty()
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/group/name",
        data: {groupName: name}, //전송 데이터
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
    }).fail(function (e) {
        console.log(e.responseText)
    });
}

function showMoimAjax(url, contentId) {
    return {
        type: "GET",
        url: url,
        headers: {'Content-Type': 'application/json', 'Authorization': localStorage.getItem('Authorization')},
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
    };
}

function showAllMoim() {
    let contentId = '#find-content';
    let url = "http://localhost:8080/group";
    $(contentId).empty()
    $.ajax(showMoimAjax(url, contentId));
}

function showPopularMoim() {
    let contentId = '#popular-content';
    let url = "http://localhost:8080/group/popular";
    $(contentId).empty()
    $.ajax(showMoimAjax(url, contentId));
}

function showLeaderMoim() {
    let contentId = '#made-group';
    let url = "http://localhost:8080/leader/group";
    $(contentId).empty()
    $.ajax(showMoimAjax(url, contentId));
}

function showParticipantMoim() {
    let contentId = '#participant-group';
    let url = "http://localhost:8080/participant/group";
    $(contentId).empty()
    $.ajax(showMoimAjax(url, contentId));
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
    }).fail(function (e) {
        console.log(e.responseText)
    });
}


function showReview(id) {
    $('#moimDetail_reviews').empty().append(`<textarea style="width: 100%" rows="3" cols="30" id="reviewText" value=""> </textarea>
                                     <button type="button" class="btn btn-warning" onclick="addReviewMoim(document.getElementById('moimDetailId').value)">후기 등록</button>`);

    $.ajax({
        type: "GET",
        url: "http://localhost:8080/groups/" + id + "/review",
        success: function (response) {
            for (let i = 0; i < response.length; i++) {
                let id = response[i]['id']
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
                                          <textarea class="button_hide" style="width: 100%" rows="3" cols="30" id="review"
                                            name="review"></textarea>
                                          <div class="edit_delete">
                                            <button type="button" class="btn btn-danger" style="float: right;" onclick="deleteReview(${id})">삭제</button>
                                            <button type="button" onclick="gotoEditReview(event)" class="btn btn-secondary"
                                              style="float: right; margin-right: 8px;">수정</button>
                                          </div>
                                          <div class="cancel_submit button_hide">
                                            <button type="button" class="btn btn-success" style="float: right;" onclick="editReview(${id})">수정</button>
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
    }).fail(function (e) {
        console.log(e.responseText)
    });
}


function showRequestedGroup() {
    $('#requested-group').empty();
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/leader/application",
        headers: {'Content-Type': 'application/json', 'Authorization': localStorage.getItem('Authorization')},
        success: function (response) {
            for (let i = 0; i < response.length; i++) {
                let id = response[i]['id']
                let groupName = response[i]['groupName']
                let username = response[i]['username']
                let status = response[i]['status']

                let temp_html = `<tr>
                                    <td>${groupName}</td>
                                    <td>${username}</td>
                                    <td>${status}</td>
                                    <td><input type="button" onclick="permitApplication(${id})">승인</td>
                                    <td><input type="button" onclick="rejectApplication(${id})">거절</td>
                                  </tr>`
                $('#requested-group').append(temp_html)
                console.log(response)
            }
        }
    }).fail(function (e) {
        console.log(e.responseText)
    });
}

function showAppliedGroup() {
    $('#applied-group').empty()
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/participant/application",
        headers: {'Content-Type': 'application/json', 'Authorization': localStorage.getItem('Authorization')},
        success: function (response) {
            for (let i = 0; i < response.length; i++) {
                let id = response[i]['id']
                let groupName = response[i]['groupName']
                let leaderName = response[i]['leaderName']
                let status = response[i]['status']

                let temp_html = `<tr>
                                    <td>${groupName}</td>
                                    <td>${leaderName}</td>
                                    <td>${status}</td>
                                    <td><input type="button" onclick="cancelApplication(${id})">취소</td>
                                  </tr>`
                $('#applied-group').append(temp_html)
                console.log(response)
            }
        }
    }).fail(function (e) {
        console.log(e.responseText)
    });
}


function permitApplication(applicationId) {
    $.ajax({
        type: "PUT",
        url: "http://localhost:8080/applications/" + applicationId + "/permit",
        headers: {'Authorization': localStorage.getItem('Authorization')}
    }).done(function (data) {
        alert(data.responseJSON['message'])
        console.log(data);
        showRequestedGroup()
    }).fail(function (e) {
        alert(e.responseJSON['message'])
    })
}

function rejectApplication(applicationId) {
    $.ajax({
        type: "PUT",
        url: "http://localhost:8080/applications/" + applicationId + "/reject",
        headers: {'Authorization': localStorage.getItem('Authorization')}
    }).done(function (data) {
        alert(data.responseJSON['message'])
        console.log(data);
        showRequestedGroup()
    }).fail(function (e) {
        alert(e.responseJSON['message'])
    })
}

function cancelApplication(applicationId) {
    $.ajax({
        type: "DELETE",
        url: "http://localhost:8080/applications/" + applicationId,
        headers: {'Authorization': localStorage.getItem('Authorization')}
    }).done(function (data) {
        alert(data.responseJSON['message'])
        console.log(data);
        showAppliedGroup()
    }).fail(function (e) {
        alert(e.responseJSON['message'])
    })
}


function showMoimDetail(event, id) {
    let targetTitle = event.currentTarget.firstChild.nextSibling.children[1].innerText;
    let targetContent = event.currentTarget.firstChild.nextSibling.children[2].value;
    document.querySelector('#moimDetail_Title').innerText = targetTitle;
    document.querySelector('#moimDetail_introduce').innerText = targetContent;
    document.getElementById('moimDetailId').value = id;
    showReview(id);
}


//미완
function saveMoim() {
    let jsonData = { // Body에 첨부할 json 데이터
        "name": $('#newMoim-title').val(),
        "tagNames": [$('#newMoim-tag').val()],
        "categoryId": 6,
        "content": $('#newMoim-content').val(),
        "recruitNumber": $('#newMoim-recruit').val(),
        "address": "address",
        "firstRegion": "firstRegion",
        "secondRegion": "secondRegion",
        "latitude": "latitude",
        "longitude": "longitude"
    };
    $.ajax({
        type: "post",
        url: "http://localhost:8080/group",
        headers: {'Content-Type': 'application/json', 'Authorization': localStorage.getItem('Authorization')},
        data: JSON.stringify(jsonData), //전송 데이터
        dataType: "JSON", //응답받을 데이터 타입 (XML,JSON,TEXT,HTML,JSONP)
        contentType: "application/json; charset=utf-8" //헤더의 Content-Type을 설정
    }).done(function (data) {
        console.log(data);
        alert("작성 완료")
        location.reload()
    }).fail(function (e) {
        alert(e.responseText)
    })
}


function attendMoim(id) {
    $.ajax({
        type: "post",
        url: "http://localhost:8080/groups/" + id + "/application",
        headers: {'Authorization': localStorage.getItem('Authorization')}
    }).done(function (data) {
        console.log(data);
        alert(data.responseJSON['message'])
        location.reload()
    }).fail(function (e) {
        alert(e.responseJSON['message'])
    })
}

function withdrawMoim(id) {
    $.ajax({
        type: "delete",
        url: "http://localhost:8080/group/" + id + "/participant",
        headers: {'Authorization': localStorage.getItem('Authorization')}
    }).done(function (data) {
        console.log(data);
        alert(data.responseJSON['message'])
        showParticipantMoim()
        showAllMoim()
        showPopularMoim()
    }).fail(function (e) {
        alert(e.responseJSON['message'])
    })
}

function editMoim(id) {
    let jsonData = { // Body에 첨부할 json 데이터
        "name": $('#modifyMoim-title').val(),
        "tagNames": [$('#modifyMoim-tag').val()],
        "categoryId": 6,
        "content": $('#modifyMoim-content').val(),
        "recruitNumber": $('#modifyMoim-recruit').val(),
        "address": "address",
        "firstRegion": "firstRegion",
        "secondRegion": "secondRegion",
        "latitude": "latitude",
        "longitude": "longitude"
    };
    $.ajax({
        type: "put",
        url: "http://localhost:8080/groups/" + id,
        headers: {'Content-Type': 'application/json', 'Authorization': localStorage.getItem('Authorization')},
        data: JSON.stringify(jsonData), //전송 데이터
        dataType: "JSON", //응답받을 데이터 타입 (XML,JSON,TEXT,HTML,JSONP)
        contentType: "application/json; charset=utf-8", //헤더의 Content-Type을 설정
    }).done(function (data) {
        console.log(data);
        alert("작성 완료")
        location.reload()
    }).fail(function (e) {
        alert(e.responseText)
    })
}

function deleteMoim(id) {
    $.ajax({
        type: "delete",
        url: "http://localhost:8080/groups/" + id,
        headers: {'Authorization': localStorage.getItem('Authorization')},
    }).done(function (data) {
        console.log(data.responseJSON['message']);
        alert(data.responseJSON['message'])
        showAllMoim()
        showPopularMoim()
        showLeaderMoim()
    }).fail(function (e) {
        alert(e.responseJSON['message'])
    });
}

function wishMoim(id) {
    $.ajax({
        type: "post",
        url: "http://localhost:8080/groups/" + id + "/wish",
        headers: {'Authorization': localStorage.getItem('Authorization')},
        ///보낼 데이터를 JSON.stringify()로 감싸주어야 함
        success: function (data) {
            console.log(data);
            alert(data.responseJSON['message'])
        },
        error: function (e) {
            alert(e.responseJSON['message'])
            console.log("실패")
        }
    })
    //alert('찜 등록 후 페이지 새로고침\n마이페이지에 찜목록 보기 추가예정')
}


function addReviewMoim(id) {
    let jsonData = {
        "content": $('#reviewText').val()
    }
    $.ajax({
        type: "post",
        url: "http://localhost:8080/groups/" + id + "/review",
        headers: {'Content-Type': 'application/json', 'Authorization': localStorage.getItem('Authorization')},
        data: JSON.stringify(jsonData), //전송 데이터
        dataType: "JSON", //응답받을 데이터 타입 (XML,JSON,TEXT,HTML,JSONP)
        contentType: "application/json; charset=utf-8", //헤더의 Content-Type을 설정
        ///보낼 데이터를 JSON.stringify()로 감싸주어야 함
        success: function (data) {
            let id = data['id']
            console.log(data);
            alert(data.responseJSON['message'])
        },
        error: function (e) {
            alert(e.responseJSON['message'])
            console.log(e)
        }
    }).done(function () {
        showReview(id)
    })
    //alert('찜 등록 후 페이지 새로고침\n마이페이지에 찜목록 보기 추가예정')
}


function editReview(id) {
    let jsonData = {
        "content": $('#review').val()
    };
    $.ajax({
        type: "put",
        url: "http://localhost:8080/reviews/" + id,
        headers: {'Content-Type': 'application/json', 'Authorization': localStorage.getItem('Authorization')},
        data: JSON.stringify(jsonData), //전송 데이터
        dataType: "JSON", //응답받을 데이터 타입 (XML,JSON,TEXT,HTML,JSONP)
        contentType: "application/json; charset=utf-8", //헤더의 Content-Type을 설정
        ///보낼 데이터를 JSON.stringify()로 감싸주어야 함
        success: function (data) {
            console.log(data);
            alert(data.responseJSON['message'])
            window.location = './main.html'
        },
        error: function (e) {
            alert(e.responseJSON['message'])
            console.log(e)
        }
    })
}


function deleteReview(id) {
    $.ajax({
        type: "delete",
        url: "http://localhost:8080/reviews/" + id,
        headers: {'Authorization': localStorage.getItem('Authorization')},
        ///보낼 데이터를 JSON.stringify()로 감싸주어야 함
        success: function (data) {
            console.log(data);
            alert(data.responseJSON['message'])
            window.location = './main.html'
        },
        error: function (e) {
            alert(e.responseJSON['message'])
            console.log("실패")
        }
    })
}


function goToHome() {
    location.replace('./welcome.html')
}


function gotoBoard(id) {
    let myId
    let isParticipant = false
    $.ajax({
        type: "get",
        url: "http://localhost:8080/user",
        headers: {'Authorization': localStorage.getItem('Authorization')},
        success: function (data) {
            myId = data['id']
        }
    }).done(function () {
        $.ajax({
            type: "get",
            url: "http://localhost:8080/groups/" + id,
            headers: {'Authorization': localStorage.getItem('Authorization')},
            success: function (data) {
                if (myId === data['userId']) {
                    alert('게시판으로 이동합니다.')
                    window.location = './board.html'
                } else {
                    $.ajax({
                        type: "get",
                        url: "http://localhost:8080/group/" + id + "/participant",
                        headers: {'Authorization': localStorage.getItem('Authorization')},
                        success: function (data) {
                            for (let i = 0; i < data.length; i++) {
                                if (data[i]['userId'] === myId) {
                                    isParticipant = true
                                }
                            }
                            if (isParticipant) {
                                alert('게시판으로 이동합니다.')
                                window.location = './board.html'
                            } else {
                                alert('참가자만 입장 가능합니다.')
                            }
                        },
                        error: function (e) {
                            alert(e.responseJSON['message'])
                            console.log(e.responseJSON['message'])
                        }
                    });
                }
            },
            error: function (e) {
                alert(e.responseJSON['message'])
                console.log(e.responseJSON['message'])
            }
        });
    });
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


function getMyProfile() {
    let jsonData = {"password": $('#checkPassword').val()};
    $('#myProfile').empty();
    $.ajax({
        type: "post",
        url: "http://localhost:8080/profile",
        headers: {'Authorization': localStorage.getItem('Authorization')},
        data: JSON.stringify(jsonData), //전송 데이터
        dataType: "JSON", //응답받을 데이터 타입 (XML,JSON,TEXT,HTML,JSONP)
        contentType: "application/json; charset=utf-8", //헤더의 Content-Type을 설정
        success: function (response) {
            let username = response['username']
            let content = response['content']

            let temp_html = `<div>${username}</div>
                             <div>${content}</div>
                             
                             `
            $('#myProfile').append(temp_html)
            console.log(response)
        }, error: function (e) {
            console.log(e)
        }
    });
}