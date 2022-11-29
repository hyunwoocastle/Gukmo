// js파일에서 contextPath를 알아내는 함수
function getContextPath(){
  let hostIndex = location.href.indexOf(location.host) + location.host.length;
  let contextPath = location.href.substring(hostIndex, location.href.indexOf('/',hostIndex+1));
  return contextPath;
}

$(document).ready(function(){
  
    // select 값 잡기
//    const detail_category = requestScope.boardvo.detail_category;
//    $("#detail_category").val(detail_category).attr("selected","selected");

	//	==== 스마트 에디터 구현 시작 ==== //
	//전역변수
    var obj = [];
    
    //스마트에디터 프레임생성
    nhn.husky.EZCreator.createInIFrame({
        oAppRef: obj,
        elPlaceHolder: "content",
        sSkinURI: getContextPath()+"/resources/smarteditor/SmartEditor2Skin.html",
        htParams : {
            // 툴바 사용 여부 (true:사용/ false:사용하지 않음)
            bUseToolbar : true,            
            // 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
            bUseVerticalResizer : true,    
            // 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
            bUseModeChanger : true,
        }
    });
    // ==== 스마트 에디터 구현 끝 ==== //
	
    
    // ==== 해시태그 구현 시작 ==== //
    
      var hashtag = {};
      var counter = 0;

      // 태그를 추가한다.
      function addTag(value) {
    	hashtag[counter] = value; // 태그를 Object 안에 추가
        counter++; // counter 증가 삭제를 위한 del-btn 의 고유 id 가 된다.
      }

      // 최종적으로 서버에 넘길때 tag 안에 있는 값을 array type 으로 만들어서 넘긴다.
      function marginTag() {
        return Object.values(hashtag).filter(function (word) {
            return word !== "";
          });
      }

      
      $("#hashtag").on("keyup", function (e) {
          var self = $(this);

          // input 에 focus 되있을 때 엔터 및 스페이스바 입력시 구동
          if (e.key === "Enter" || e.keyCode == 32) {

            var tagValue = self.val().trim(); // 값 가져오기
            
            // 값이 없으면 동작 안합니다.
            if (tagValue !== "") {

              // 같은 태그가 있는지 검사한다. 있다면 해당값이 array 로 return 된다.
              var result = Object.values(hashtag).filter(function (word) {
                  return word === tagValue;
                })

             // 해시태그가 중복되었는지 확인
            if (result.length == 0) { 
                $("<li class='tag-item'>"+tagValue+"<span class='del-btn' idx='"+counter+"'>x</span></li>").insertBefore("input#hashtag");
                addTag(tagValue);
                self.val("");
            } else {
                alert("태그값이 중복됩니다.");
            }
            }
            e.preventDefault(); // SpaceBar 시 빈공간이 생기지 않도록 방지
          }
          
        });

      // 삭제 버튼
      // 삭제 버튼은 비동기적 생성이므로 document 최초 생성시가 아닌 검색을 통해 이벤트를 구현시킨다.
      $(document).on("click", ".del-btn", function (e) {
          var index = $(this).attr("idx");
          hashtag[index] = "";
          $(this).parent().remove();
        });
   
    // ==== 해시태그 구현 끝 ==== // 
    

  
	// 등록 버튼을 클릭했을시
    $("button#btn_write").click(function() {
    	
        var value = marginTag(); // return array
        value = value.join(',');
        $("#str_hashTag").val(value);
         //console.log(value);
         //console.log(typeof(value));
        
    	// ==== 스마트 에디터 구현 시작 ==== //
    	// id가 content인 textarea에 에디터에서 대입
    	obj.getById["content"].exec("UPDATE_CONTENTS_FIELD", []);
    	
    	// 카테고리 유효성 검사
    	const detail_category = $("select#detail_category").val();
    	if(detail_category == "") {
    		alert("카테고리를 선택하세요!!");
			return;
    	}
    	
    	// 글제목 유효성 검사
		const subject = $("input#subject").val().trim();
		if(subject == "") {
			alert("글제목을 입력하세요!!");
			return;
		}
		
		// 글내용 유효성 검사(스마트 에디터용)
		var contentval = $("textarea#content").val();
		contentval = contentval.replace(/&nbsp;/gi, "");
	
	    contentval = contentval.substring(contentval.indexOf("<p>")+3);   // "             </p>"
	    contentval = contentval.substring(0, contentval.indexOf("</p>")); // "             "
	            
	    if(contentval.trim().length == 0) {
	  	  alert("글내용을 입력하세요!!");
	      return;
	    }
	    

	    // 폼을 전송
	    const frm = document.writerFrm;
	    frm.method = "POST";
	    frm.action = getContextPath()+"/community/edit.do";
	    frm.submit();
	});
    
});// end of document