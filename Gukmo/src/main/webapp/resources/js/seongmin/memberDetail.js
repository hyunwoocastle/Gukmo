
$(document).ready(function(){
	   var userid = $("input[name='userid']").attr('id');
	   var nickname = $("input[name='nickname']").attr('id');

	   $(document).on("click", ".memberBlock", function(){
		   block(userid, nickname);
	   });

}); //end of ready

//Function Declaration


function block(userid, nickname) {
  var bool = confirm("정지 등록 페이지로 이동하시겠습니까?");
  if( bool == true) {
    location.href="penaltyRegister.do?userid="+userid+"&nickname="+nickname;
  } //end of if

  else {
    return false;
  }
} //end of block()


function block_recovery() {
  var bool = confirm("정지 해제하시겠습니까?");
  if( bool == true) {
    alert("정지가 해제되었습니다.(ajax로 )");
  }//end of if

  else {
    return false;
  }
} //end of block_recovery()


function sleep_recovery() {
  var bool = confirm("휴면계정을 활성화하시겠습니까?");
  if(bool == true) {
    alert("휴면계정이 활성화 되었습니다.(ajax로 )");
  }

  else {
    return false;
  }
} //end of sleep_recovery()
