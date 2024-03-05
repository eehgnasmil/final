var header = $("meta[name='_csrf_header']").attr('content');
var token = $("meta[name='_csrf']").attr('content');

function writesave() {
    var title = $("#title").val();
    var detail = editor.getHTML();

    var hashtags = window.tagify.value.map(function (tag) {
        var trimmedValue = tag.value.replace(/\s+/g, ''); // 문자열 내의 모든 공백을 제거
        return trimmedValue.startsWith('#') ? trimmedValue : '#' + trimmedValue.toLowerCase(); // #이 없다면 앞에 추가 있으면 그냥 넘김 공백제거해줌
    });

    // 콘텐츠 입력 유효성 검사
    // 마크다운으로 가져온 이유는 비교가 더 단순함
    if (editor.getMarkdown().length < 1) {
        alert('에디터 내용을 입력해 주세요.');
        throw new Error('editor content is required!');
    }

    if (title.length < 1) {
        alert('제목을 입력해 주세요.');
        throw new Error('editor content is required!');
    }

    var postTagValue = JSON.stringify(hashtags);
    $("input[name='postTag']").val(postTagValue);

    // API 호출을 위한 파라미터 세팅
    var url = '/board/noticesave';
    var params = {
        title: title,
        detail: detail,
        isNotice: true,
        hashtags: hashtags
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
            alert('게시글이 저장되었습니다.');
            location.href = '/board/noticewrite';
        },
        error: function (error) {
            console.error('저장 실패 : ', error);
        },
    });
}

