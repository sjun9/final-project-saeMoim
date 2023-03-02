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
    $('#passpass').show()
    $('#myProfile_2').hide()
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


// 모임 생성 지도
// 주소-좌표 변환 객체 생성
let geocoder = new kakao.maps.services.Geocoder();
// kakao map
let mapContainer = document.getElementById('saveMoimMap');
let mapOption = {
    center: new kakao.maps.LatLng(37.57205, 126.9615),	// 지도의 중심 좌표(임의 설정)
    level: 5					// 지도의 확대 레벨(임의 설정)
};
// 설정한 지도 생성
let map = new kakao.maps.Map(mapContainer, mapOption);
//마커 초기화(초기화 시 지도에 미리 지정 가능 : 카카오맵 API 문서 참조)
let marker = new kakao.maps.Marker({position: map.getCenter});

// 모임 생성,수정 시 넣을 변수
let address;
let firstRegion;
let secondRegion;
let latitude;
let longitude;

//모임 조회 지도
let detailMapContainer = document.getElementById('detailMoimMap');
let detailMap = new kakao.maps.Map(detailMapContainer, mapOption);
let detailMarker = new kakao.maps.Marker({position: detailMap.getCenter});

// 모임 수정 지도
// kakao map
let modifyMapContainer = document.getElementById('modifyMoimMap');
// 설정한 지도 생성
let modifyMap = new kakao.maps.Map(modifyMapContainer, mapOption);
//마커 초기화(초기화 시 지도에 미리 지정 가능 : 카카오맵 API 문서 참조)
let modifyMarker = new kakao.maps.Marker({position: modifyMap.getCenter});

// 생성하기 카카오맵 클릭 이벤트 추가
kakao.maps.event.addListener(map, 'click', (mouseEvent) => {
    searchDetailAddrFromCoords(mouseEvent.latLng, function (result, status) {
        if (status === kakao.maps.services.Status.OK) {
            address = result[0]['address']['address_name'];
            firstRegion = result[0]['address']['region_1depth_name']
            secondRegion = result[0]['address']['region_2depth_name']
            latitude = mouseEvent.latLng['Ma'];
            longitude = mouseEvent.latLng['La'];

            //마커 위치를 클릭한 위치로 이동
            map.setCenter(mouseEvent.latLng);
            marker.setPosition(mouseEvent.latLng);
            marker.setMap(map);

            document.getElementById("save_address").value = result[0]['address']['address_name'];
        }
    });
});

function searchDetailAddrFromCoords(coords, callback) {
    geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
}

// 생성하기 주소 검색
function save_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function (data) {
            let addr = data.address; // 최종 주소 변수
            // 주소 정보를 해당 필드에 넣는다.
            document.getElementById("save_address").value = addr;
            // 주소로 상세 정보를 검색
            geocoder.addressSearch(addr, function (results, status) {
                // 정상적으로 검색이 완료됐으면
                if (status === daum.maps.services.Status.OK) {
                    // 해당 주소에 대한 좌표를 받아서
                    let coords = new daum.maps.LatLng(results[0].y, results[0].x);

                    address = data['address']
                    firstRegion = data['sido']
                    secondRegion = data['sigungu']
                    latitude = coords['Ma'];
                    longitude = coords['La'];

                    // 지도 중심을 변경한다.
                    map.setCenter(coords);
                    // 마커를 결과값으로 받은 위치로 옮긴다.
                    marker.setPosition(coords)
                    marker.setMap(map);
                }
            });
        }
    }).open();
}

// 수정하기 카카오맵 클릭 이벤트 추가
kakao.maps.event.addListener(modifyMap, 'click', (mouseEvent) => {
    searchDetailAddrFromCoords(mouseEvent.latLng, function (result, status) {
        if (status === kakao.maps.services.Status.OK) {
            address = result[0]['address']['address_name'];
            firstRegion = result[0]['address']['region_1depth_name']
            secondRegion = result[0]['address']['region_2depth_name']
            latitude = mouseEvent.latLng['Ma'];
            longitude = mouseEvent.latLng['La'];

            //마커 위치를 클릭한 위치로 이동
            modifyMap.setCenter(mouseEvent.latLng);
            modifyMarker.setPosition(mouseEvent.latLng);
            modifyMarker.setMap(modifyMap);

            document.getElementById("modify_address").value = result[0]['address']['address_name'];
        }
    });
});

// 수정하기 주소 검색
function modify_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function (data) {
            let addr = data.address; // 최종 주소 변수
            // 주소 정보를 해당 필드에 넣는다.
            document.getElementById("modify_address").value = addr;
            // 주소로 상세 정보를 검색
            geocoder.addressSearch(addr, function (results, status) {
                // 정상적으로 검색이 완료됐으면
                if (status === daum.maps.services.Status.OK) {
                    // 해당 주소에 대한 좌표를 받아서
                    let coords = new daum.maps.LatLng(results[0].y, results[0].x);

                    address = data['address']
                    firstRegion = data['sido']
                    secondRegion = data['sigungu']
                    latitude = coords['Ma'];
                    longitude = coords['La'];

                    // 지도 중심을 변경한다.
                    modifyMap.setCenter(coords);
                    // 마커를 결과값으로 받은 위치로 옮긴다.
                    modifyMarker.setPosition(coords)
                    modifyMarker.setMap(modifyMap);
                }
            });
        }
    }).open();
}


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

function relayoutMap() {
    setTimeout(function () {
        map.relayout();
        modifyMap.relayout();
    }, 300);
}

$(document).ready(function () {
    showUsername()
    showAllMoim()
    showPopularMoim()
    showCategory()
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
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(logout, 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }
    });
}

function showUsername() {
    $('#username').empty()
    $.ajax({
        type: "get",
        url: "http://localhost:8080/user",
        headers: {'Authorization': localStorage.getItem('Authorization')},
        success: function (data) {
            let username = data['username']
            $('#username').append(`${username}`)
        }, error: function (e) {
            $('#username').append(`로그인이 필요합니다`)
        }
    });
}


function showCategory() {
    $('#categoryFilter').empty().append(`<option value="0">전체</option>`)
    $('#categoryMenu').empty()
    $('#modifyCategoryMenu').empty()
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/category"
    }).done(function (response) {
        for (let i = 0; i < response['data'].length; i++) {
            let categories = response['data'][i]
            for (let i = 0; i < categories['categories'].length; i++) {
                let id = categories['categories'][i]['id']
                let name = categories['categories'][i]['name']
                let temp_html = `<option value=${id}>${name}</option>`
                let temp_html2 = `<li><a class="dropdown-item" href="#" onclick="changeNewValue(event)">${name}</a></li>`
                let temp_html3 = `<li><a class="dropdown-item" href="#" onclick="changeModifyValue(event)">${name}</a></li>`
                $('#categoryFilter').append(temp_html)
                $('#categoryMenu').append(temp_html2)
                $('#modifyCategoryMenu').append(temp_html3)
            }
        }
    }).fail(function (e) {
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(showCategory, 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }

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
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(showSearch(name), 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }

    });
}

function reissue() {
    var settings = {
        "url": "http://localhost:8080/reissue",
        "method": "POST",
        "timeout": 0,
        "headers": {
            "Refresh_Token": localStorage.getItem('Refresh_Token'),
            "Authorization": localStorage.getItem('Authorization')
        },
    };
    $.ajax(settings).done(function (response, status, xhr) {
        console.log("성공성공")
        localStorage.setItem('Authorization', xhr.getResponseHeader('Authorization'))
        localStorage.setItem('Refresh_Token', xhr.getResponseHeader('Refresh_Token'))
    }).fail(function (e) {
        console.log("실패실패")
        console.log(e)
        alert("다시 로그인 해주세요.")
        localStorage.removeItem('Authorization')
        localStorage.removeItem('Refresh_Token')
        window.location.replace('./welcome.html');
    });
}

function showAllMoim() {
    let contentId = '#find-content';
    let url = "http://localhost:8080/group";
    $(contentId).empty()
    $.ajax({
        type: "GET",
        url: url,
        headers: {'Content-Type': 'application/json', 'Authorization': localStorage.getItem('Authorization')},
        success: function (response) {
            console.log(response)
            response = response['data']['content']
            for (let i = 0; i < response.length; i++) {
                let id = response[i]['id']
                let groupName = response[i]['groupName']
                let content = response[i]['content']
                let categoryName = response[i]['categoryName']
                let participantCount = response[i]['participantCount']
                let recruitNumber = response[i]['recruitNumber']
                let wishCount = response[i]['wishCount']
                let status = response[i]['status']
                let tags = response[i]['tags']
                let leaderId = response[i]['userId']
                let leaderName = response[i]['username']

                let temp_html = `<div class="products-row" data-bs-toggle="modal" data-bs-target="#moimDetailModal" 
                                    onClick="showMoimDetail(event, ${id})">
                                    <div class="product-cell image">
                                        <img src="../static/images/main-running.jpg" alt="">
                                            <span>${groupName}</span>
                                            <input type="hidden" value=${content}>
                                            <input type="hidden" value=${tags}>
                                            <input type="hidden" value=${leaderName}>
                                            <input type="hidden" value=${leaderId}>
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
    }).fail(function (e) {
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(showAllMoim, 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }
    });
}

function showPopularMoim() {
    let contentId = '#popular-content';
    let url = "http://localhost:8080/group/popular";
    $(contentId).empty()
    $.ajax({
            type: "GET",
            url: url,
            headers: {'Content-Type': 'application/json', 'Authorization': localStorage.getItem('Authorization')},
            success: function (response) {
                console.log(response)
                response = response['data']
                for (let i = 0; i < response.length; i++) {
                    let id = response[i]['id']
                    let groupName = response[i]['groupName']
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
        }
    ).fail(function (e) {
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(showPopularMoim, 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }
    });
}

function showLeaderMoim() {
    let contentId = '#made-group';
    let url = "http://localhost:8080/leader/group";
    $(contentId).empty()
    $.ajax({
        type: "GET",
        url: url,
        headers: {'Content-Type': 'application/json', 'Authorization': localStorage.getItem('Authorization')},
        success: function (response) {
            console.log(response)
            response = response['data']
            for (let i = 0; i < response.length; i++) {
                let id = response[i]['id']
                let groupName = response[i]['groupName']
                let content = response[i]['content']
                let categoryName = response[i]['categoryName']
                let participantCount = response[i]['participantCount']
                let recruitNumber = response[i]['recruitNumber']
                let wishCount = response[i]['wishCount']
                let status = response[i]['status']
                let tags = response[i]['tags']
                let leaderId = response[i]['userId']
                let leaderName = response[i]['username']

                let temp_html = `<div class="products-row" data-bs-toggle="modal" data-bs-target="#moimDetailModal" 
                                    onClick="showMoimDetail(event, ${id})">
                                    <div class="product-cell image">
                                        <img src="../static/images/main-running.jpg" alt="">
                                            <span>${groupName}</span>
                                            <input type="hidden" value=${content}>
                                            <input type="hidden" value=${tags}>
                                            <input type="hidden" value=${leaderName}>
                                            <input type="hidden" value=${leaderId}>
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
    }).fail(function (e) {
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(showLeaderMoim, 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }
    });
}

function showParticipantMoim() { // 참여중인 모임 조회
    let contentId = '#participant-group';
    let url = "http://localhost:8080/participant/group";
    $(contentId).empty()
    $.ajax({
        type: "GET",
        url: url,
        headers: {'Content-Type': 'application/json', 'Authorization': localStorage.getItem('Authorization')},
        success: function (response) {
            console.log(response)
            response = response['data']
            for (let i = 0; i < response.length; i++) {
                let id = response[i]['id']
                let groupName = response[i]['groupName']
                let content = response[i]['content']
                let categoryName = response[i]['categoryName']
                let participantCount = response[i]['participantCount']
                let recruitNumber = response[i]['recruitNumber']
                let wishCount = response[i]['wishCount']
                let status = response[i]['status']
                let tags = response[i]['tags']
                let leaderId = response[i]['userId']
                let leaderName = response[i]['username']

                let temp_html = `<div class="products-row" data-bs-toggle="modal" data-bs-target="#moimDetailModal" 
                                    onClick="showMoimDetail(event, ${id})">
                                    <div class="product-cell image">
                                        <img src="../static/images/main-running.jpg" alt="">
                                            <span>${groupName}</span>
                                            <input type="hidden" value=${content}>
                                            <input type="hidden" value=${tags}>
                                            <input type="hidden" value=${leaderName}>
                                            <input type="hidden" value=${leaderId}>
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
    }).fail(function (e) {
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(showParticipantMoim, 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
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
            response = response['data']
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
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(showFilter(categoryId, status), 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }

    });
}


function showReview(id) {
    $('#moimDetail_reviews').empty().append(`<textarea style="width:150%" rows="3" cols="30" id="reviewText" value=""> </textarea>
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
                                          <textarea class="button_hide" style="width:150
                                         %" rows="3" cols="30" id="review"
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
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(showReview(id), 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }

    });
}


function showRequestedGroup() {
    $('#requested-group').empty();
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/leader/application",
        headers: {'Content-Type': 'application/json', 'Authorization': localStorage.getItem('Authorization')},
        success: function (response) {
            response = response['data']
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
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(showRequestedGroup, 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }

    });
}

function showAppliedGroup() {
    $('#applied-group').empty()
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/participant/application",
        headers: {'Content-Type': 'application/json', 'Authorization': localStorage.getItem('Authorization')},
        success: function (response) {
            response = response['data']
            for (let i = 0; i < response.length; i++) {
                let id = response[i]['id']
                let groupName = response[i]['groupName']
                let leaderName = response[i]['leaderName']
                let status = response[i]['status']

                let temp_html = `<tr>
                                    <td>${groupName}</td>
                                    <td>${leaderName}</td>
                                    <td>${status}</td>
                                    <td><input type="button" onclick="cancelApplication(${id})">삭제</td>
                                  </tr>`
                $('#applied-group').append(temp_html)
                console.log(response)
            }
        }
    }).fail(function (e) {
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(showAppliedGroup, 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }

    });
}


function permitApplication(applicationId) {
    $.ajax({
        type: "PUT",
        url: "http://localhost:8080/applications/" + applicationId + "/permit",
        headers: {'Authorization': localStorage.getItem('Authorization')}
    }).done(function (data) {
        alert(data['message'])
        console.log(data);
        showRequestedGroup()
    }).fail(function (e) {
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(permitApplication(applicationId), 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }

    })
}

function rejectApplication(applicationId) {
    $.ajax({
        type: "PUT",
        url: "http://localhost:8080/applications/" + applicationId + "/reject",
        headers: {'Authorization': localStorage.getItem('Authorization')}
    }).done(function (data) {
        alert(data['message'])
        console.log(data);
        showRequestedGroup()
    }).fail(function (e) {
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(rejectApplication(applicationId), 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }

    })
}

function cancelApplication(applicationId) {
    $.ajax({
        type: "DELETE",
        url: "http://localhost:8080/applications/" + applicationId,
        headers: {'Authorization': localStorage.getItem('Authorization')}
    }).done(function (data) {
        alert(data['message'])
        console.log(data);
        showAppliedGroup()
    }).fail(function (e) {
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(cancelApplication(applicationId), 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }

    })
}


function showMoimDetail(event, id) {
    $.ajax({
        type: "get",
        url: "http://localhost:8080/groups/" + id
    }).done(function (data) {
        document.querySelector('#moimDetail_Title').innerText = data.groupName;
        // document.querySelector('#moimLeader').innerText = leaderName;
        document.querySelector('#moimTag').innerText = data.tags;
        document.querySelector('#moimDetail_introduce').innerText = data.content;
        document.getElementById('moimDetailId').value = data.id;
        $('#detailAddress').empty().append(`<h5>모임장소</h5>
                                    <h6>${data.address}</h6>`)
        console.log(data.address)
        let detailLatLng = new kakao.maps.LatLng(data.latitude, data.longitude);
        // detailMarker.setPosition(detailLatLng);
        // detailMarker.setMap(detailMap);
        // detailMap.setCenter(detailLatLng);

        kakao.maps.event.addListener(detailMap, 'tilesloaded', function () {
            detailMap.setCenter(detailLatLng);
            detailMarker.setPosition(detailLatLng);
            detailMarker.setMap(detailMap);
        }, {once: true});

        setTimeout(function () {
            detailMap.relayout();
        }, 200);
    }).fail(function (e) {
        if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }
    });
    showReview(id);
}


//미완
function saveMoim() {
    let tags = []
    for (let i = 0; i < $('[name="tagsA"]').length; i++) {
        tags.push($('[name="tagsA"]')[i].value)
    }
    if (address === undefined) {
        alert("지도에서 주소를 체크 해주세요.")
    }
    let jsonData = { // Body에 첨부할 json 데이터
        "name": $('#newMoim-title').val(),
        "tagNames": tags,
        "categoryName": $('#newMoim-category').val(),
        "content": $('#newMoim-content').val(),
        "recruitNumber": $('#newMoim-recruit').val(),
        "address": address,
        "firstRegion": firstRegion,
        "secondRegion": secondRegion,
        "latitude": latitude,
        "longitude": longitude
    };
    address = undefined;
    firstRegion = undefined;
    secondRegion = undefined;
    latitude = undefined;
    longitude = undefined;

    $.ajax({
        type: "post",
        url: "http://localhost:8080/group",
        headers: {'Content-Type': 'application/json', 'Authorization': localStorage.getItem('Authorization')},
        data: JSON.stringify(jsonData), //전송 데이터
        dataType: "JSON", //응답받을 데이터 타입 (XML,JSON,TEXT,HTML,JSONP)
        contentType: "application/json; charset=utf-8" //헤더의 Content-Type을 설정
    }).done(function (data) {
        alert("작성 완료")
        location.reload()
    }).fail(function (e) {
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(saveMoim, 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }
    })
}


function attendMoim(id) {
    $.ajax({
        type: "post",
        url: "http://localhost:8080/groups/" + id + "/application",
        headers: {'Authorization': localStorage.getItem('Authorization')}
    }).done(function (data) {
        console.log(data);
        alert(data['message'])
        location.reload()
    }).fail(function (e) {
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(attendMoim(id), 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }
    });
}

function withdrawMoim(id) {
    $.ajax({
        type: "delete",
        url: "http://localhost:8080/participant/groups/" + id,
        headers: {'Authorization': localStorage.getItem('Authorization')}
    }).done(function (data) {
        console.log(data);
        alert(data['message'])
        showParticipantMoim()
        showAllMoim()
        showPopularMoim()
    }).fail(function (e) {
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(withdrawMoim(id), 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }
    });
}

function editMoim(id) {
    if (address === undefined) {
        alert("지도에서 주소를 체크 해주세요.")
    }
    let jsonData = { // Body에 첨부할 json 데이터
        "name": $('#modifyMoim-title').val(),
        "tagNames": [$('#modifyMoim-tag').val()],
        "categoryName": $('#modifyMoim-category').val(),
        "content": $('#modifyMoim-content').val(),
        "recruitNumber": $('#modifyMoim-recruit').val(),
        "address": address,
        "firstRegion": firstRegion,
        "secondRegion": secondRegion,
        "latitude": latitude,
        "longitude": longitude
    };

    address = undefined;
    firstRegion = undefined;
    secondRegion = undefined;
    latitude = undefined;
    longitude = undefined;

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
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(editMoim(id), 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }
    });

}

function deleteMoim(id) {
    $.ajax({
        type: "delete",
        url: "http://localhost:8080/groups/" + id,
        headers: {'Authorization': localStorage.getItem('Authorization')},
    }).done(function (data) {
        console.log(data)
        console.log(data['message']);
        alert(data['message'])
        showAllMoim()
        showPopularMoim()
        showLeaderMoim()
    }).fail(function (e) {
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(deleteMoim(id), 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }
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
            alert(data['message'])
        }

    }).fail(function (e) {
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(wishMoim(id), 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }
    });

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
            console.log(data);
            alert(data['message'])
        },
        error: function (e) {
            alert(e.responseJSON['message'])
            console.log(e)
        }
    }).done(function () {
        showReview(id)
    }).fail(function (e) {
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(addReviewMoim(id), 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }
    });
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
            alert(data['message'])
            window.location = './main.html'
        }
    }).fail(function (e) {
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(editReview(id), 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }
    });
}


function deleteReview(id) {
    $.ajax({
        type: "delete",
        url: "http://localhost:8080/reviews/" + id,
        headers: {'Authorization': localStorage.getItem('Authorization')},
        ///보낼 데이터를 JSON.stringify()로 감싸주어야 함
        success: function (data) {
            console.log(data);
            alert(data['message'])
            window.location = './main.html'
        }
    }).fail(function (e) {
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(deleteReview(id), 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }
    });
}


function goToHome() {
    location.replace('./welcome.html')
}


function gotoBoard(id) {
    let myId
    let isParticipant = false
    $.ajax({ // 유저 정보 가져오기
        type: "get",
        url: "http://localhost:8080/user",
        headers: {'Authorization': localStorage.getItem('Authorization')},
        async: false,
        success: function (data) {
            myId = data['id']
        }
    }).done(function () {
        $.ajax({ // 그룹 정보 가져오기
            type: "get",
            url: "http://localhost:8080/groups/" + id,
            headers: {'Authorization': localStorage.getItem('Authorization')},
            async: false,
            success: function (data) {
                if (myId === data['userId']) { // 페이지 이동 조건 (done)

                    // data에서 리더 정보 가져와서 localStorage에 넣어준다 -> 프로필 최상단 리더프로필 고정용
                    localStorage.setItem("current_group_id", id)
                    localStorage.setItem("current_user_id", myId)
                    alert('게시판으로 이동합니다.')
                    window.location = './board.html'
                } else {
                    $.ajax({ // 특정 모임 참여자 조회
                        type: "get",
                        url: "http://localhost:8080/participant/groups/" + id,
                        headers: {'Authorization': localStorage.getItem('Authorization')},
                        async: false,
                        success: function (data) {
                            data = data['data']
                            for (let i = 0; i < data.length; i++) {
                                if (data[i]['userId'] === myId) {
                                    isParticipant = true
                                }
                            }
                            if (isParticipant) {
                                alert(id)
                                alert('게시판으로 이동합니다.')
                                localStorage.setItem("current_group_id", id)
                                localStorage.setItem("current_user_id", myId)
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
        })
    }).fail(function (e) {
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(gotoBoard(id), 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }
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
    $.ajax({
        type: "post",
        url: "http://localhost:8080/profile",
        headers: {'Authorization': localStorage.getItem('Authorization')},
        data: JSON.stringify(jsonData), //전송 데이터
        dataType: "JSON", //응답받을 데이터 타입 (XML,JSON,TEXT,HTML,JSONP)
        contentType: "application/json; charset=utf-8", //헤더의 Content-Type을 설정
        success:
            function (response) {
                let username = response['username']
                let content = response['content']
                $('#profileName').append(username)
                $('#floatingInput').append(content)
                console.log(response)
                $('#passpass').hide()
                $('#myProfile_2').show()
            }, error: function (e) {
            console.log(e)
        }
    }).fail(function (e) {
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(getMyProfile, 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }
    });
}

$('.pw').focusout(function () {
    var pwd1 = $("#password_1").val();
    var pwd2 = $("#password_2").val();

    if (pwd1 != '' && pwd2 == '') {
        null;
    } else if (pwd1 != "" || pwd2 != "") {
        if (pwd1 == pwd2) {
            $("#alert-success").css('display', 'inline-block');
            $("#alert-danger").css('display', 'none');
        } else {
            alert("비밀번호가 일치하지 않습니다. 비밀번호를 재확인해주세요.");
            $("#alert-success").css('display', 'none');
            $("#alert-danger").css('display', 'inline-block');
        }
    }
});

function updateProfile(content, pass) {
    var settings = {
        "url": "http://localhost:8080/profile",
        "method": "PUT",
        "timeout": 0,
        "headers": {
            "Authorization": localStorage.getItem('Authorization'),
            "Content-Type": "application/json"
        },
        "data": JSON.stringify({
            "password": pass,
            "content": content
        }),
    };

    $.ajax(settings).done(function (response) {
        console.log(response);
        alert("정보가 수정되었습니다.")
        $('#passpass').show()
        $('#myProfile_2').hide()
        window.location.reload()
    }).fail(function (e) {
        console.log(e.status)
        if (e.status === 401) {
            reissue()
            setTimeout(updateProfile(content, pass), 150)
            setTimeout(showUsername, 150)
        } else if (e.responseJSON['httpStatus'] === "BAD_REQUEST") {
            alert(e.responseJSON['message'])
        } else {
            alert(e.responseText['message'])
        }
    });
}


$.expr[":"].contains = $.expr.createPseudo(function (arg) {
    return function (elem) {
        return $(elem).text().toUpperCase().indexOf(arg.toUpperCase()) >= 0;
    };
});
$(document).ready(function () {
    $('#addTagBtn').click(function () {
        $('#tags option:selected').each(function () {
            $(this).appendTo($('#selectedTags'));
        });
    });
    $('#removeTagBtn').click(function () {
        $('#selectedTags option:selected').each(function (el) {
            $(this).appendTo($('#tags'));
        });
    });
    $('.tagRemove').click(function (event) {
        event.preventDefault();
        $(this).parent().remove();
    });
    $('ul.tags').click(function () {
        $('#search-field').focus();
    });
    $('#search-field').keypress(function (event) {
        if (event.which == '13') {
            if (($(this).val() != '') && ($(".tags .addedTag:contains('" + $(this).val() + "') ").length == 0)) {
                temp_html = `<li class="addedTag" id="added">` + $(this).val() + `<span class="tagRemove" onclick="$(this).parent().remove();">x</span>
               <input type="hidden" value="` + $(this).val() + `" name="tagsA">
             </li>`
                $('#tatag').append(temp_html)

                console.log($('[name="tagsA"]').val())

                $(this).val('');
            } else {
                $(this).val('');

            }
        }
    });

});

// $('#btn-save').on('click', uploadImage);

function uploadImage() {
    var file = $('#img')[0].files[0];
    console.log(file);
    var form = new FormData();
    form.append("img", file);

    var settings = {
        "url": "http://localhost:8080/profile/image",
        "method": "POST",
        "timeout": 0,
        "headers": {
            "Authorization": localStorage.getItem("Authorization")
        },
        "processData": false,
        "mimeType": "multipart/form-data",
        "contentType": false,
        "data": form
    };

    $.ajax(settings).done(function (response) {
        console.log(response);
    });
}