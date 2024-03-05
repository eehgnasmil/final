var header = $("meta[name='_csrf_header']").attr('content');
var token = $("meta[name='_csrf']").attr('content');

function saveOrUpdate() {
    var idx = $("#idx").val(); // 게시물 ID
    var title = $("#title").val();
    var detail = window.editor.getHTML();

    var hashtags = window.tagify.value.map(function (tag) {
        var trimmedValue = tag.value.replace(/\s+/g, ''); // 문자열 내의 모든 공백을 제거
        return trimmedValue.startsWith('#') ? trimmedValue : '#' + trimmedValue.toLowerCase(); // #이 없다면 앞에 추가 있으면 그냥 넘김 공백제거해줌
    });

    // 콘텐츠 입력 유효성 검사
    if (window.editor.getMarkdown().length < 1) {
        alert('에디터 내용을 입력해 주세요.');
        throw new Error('editor content is required!');
    }

    if (title.length < 1) {
        alert('제목을 입력해 주세요.');
        throw new Error('editor content is required!');
    }

    var url = '/board/noticesaveorupdate'; // URL 변경
    var successMessage, redirectUrl;

    // API 호출을 위한 파라미터 세팅
    var params = {
        title: title,
        detail: detail,
        hashtags: hashtags
    }

    // idx 값이 존재하면 업데이트, 없으면 저장
    if (idx) {
        params['idx'] = idx;
        successMessage = '게시글이 수정되었습니다.';
        redirectUrl = '/board/board_detail/' + idx; // 수정한 게시물의 detail 페이지로 이동
    } else {
        params['isNotice'] = true;
        successMessage = '게시글이 저장되었습니다.';
        redirectUrl = '/board/noticelist';
    }

    // AJAX를 사용한 API 호출
    $.ajax({
        type: 'POST',
        url: url,
        data: JSON.stringify(params),
        contentType: "application/json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function (data) {
            alert(successMessage);
            location.href = redirectUrl;
        },
        error: function (error) {
            console.error(successMessage + ' 실패 : ', error);
        },
    });
}
