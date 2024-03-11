let page = 0;
const size = 8;
let urlParams = new URLSearchParams(window.location.search);
let hashtag = window.location.pathname.split('/').pop();
let swiper;

$(document).ready(function () {
    swiper = new Swiper(".mySwiper2", {
        pagination: {
            el: ".swiper-pagination",
            type: "progressbar",
        },
        navigation: {
            nextEl: ".swiper-button-next",
            prevEl: ".swiper-button-prev",
        },
    });

    $('#sortList a').each(function () {
        let href = $(this).attr('href');
        href = href.replace('category=all', 'category=' + category);
        $(this).attr('href', href);
    });

    loadMore();
    page++;
});

$(window).scroll(function () {
    if ($(window).scrollTop() + $(window).height() > $(document).height() - 100) {
        loadMore();
        page++;
    }
});

function loadMore() {
    $.ajax({
        url: `/board/hashtag_ajax/${hashtag}?page=${page}&size=${size}`,
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            $.each(data.content, function (i, board) {
                // 게시물 HTML 생성
                let boardHtml = `
                   <li style="width: 25%; padding:0 0 60px 0; display:inline-block; ">
                       <div style="position:relative; height: 0; padding-bottom:120%; overflow:hidden;" class="mySwiper2 swiper">
                         <ul class="swiper-wrapper">
                              ${board.imageUrls.map(imageUrl =>
                    `<li class="swiper-slide" style="padding-bottom:130%;">
                              <a href="/board/board_detail/${board.idx}"><img src="${imageUrl}" alt="Board image"></a>
                          </li>`)}
                          </ul>
                       </div>`;
                boardHtml += `<ul class="bUl" style="width:90%; margin:0 auto; position:relative;">               
                              <li style="text-align:left; position:absolute; top:-75px; z-index:99; left:20px; ">
                                  <span style="display:inline-block; width:50px; height:50px;  border-radius:100px; overflow:hidden;">
                                  <a href="/member/userPage/${board.memberId}"><img src="${board.memeberImg}" alt="이미지" style="width:100%;"></a>
                                  </span>
                                  <a href="/member/userPage/${board.memberId}"><span style="line-height: 50px; display: inline-block; height: 50px; vertical-align: bottom; margin-left:10px; color:white; letter-spacing:1px;  text-shadow: 4px 2px 2px gray; font-weight:600;">
                                      ${board.nickname}
                                  </span></a>                                	
                              </li>
                              <li style="text-align:right;">
                                  <span  style="letter-spacing:1px;">
                                      👀 ${board.viewcount}
                                  </span>
                                  <span  style="margin:0 10px; letter-spacing:1px;">
                                      ♡ ${board.likesCount}												
                                  </span>											
                              </li>	
                              <li><a href="/board/board_detail/${board.idx}">${board.title}</a></li>
                              <li>${board.category}</li>`
                if (board.hashtags && board.hashtags.length > 0) {
                    let hashtags = board.hashtags.map(hashtag => `<a href="/board/hashtag/${encodeURIComponent(hashtag)}">${hashtag}</a>`);
                    boardHtml += `<li>${hashtags.join(' ')}</li>`;
                }
                boardHtml += `</ul></li>`
                $('#boardList').append(boardHtml);
            });
        },
        error: function (xhr, status, error) {
            console.error(xhr.responseText);
        }
    });
}
