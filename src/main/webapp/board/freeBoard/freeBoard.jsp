<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/pages/common/header.jsp"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>게시판</title>

<style type="text/css">

body {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
}

.container {
  width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.content {
  background-color: #fff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.title {
  font-size: 36px;
  font-weight: bold;
  color: #000;
  margin-bottom: 10px;
}

.category {
  font-size: 18px;
  color: #909090;
  margin-bottom: 20px;
}

.nick_name {
  font-size: 18px;
  color: #909090;
}

.info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.date {
  font-size: 18px;
  color: #909090;
}

.comment {
  font-size: 18px;
  color: #909090;
}

.content-text {
  font-size: 16px;
  color: #000;
  margin-bottom: 20px;
}

.buttons {
  text-align: right;
}

.btn {
  display: inline-block;
  padding: 10px 20px;
  background-color: #007bff;
  color: #fff;
  text-decoration: none;
  border-radius: 5px;
  margin-left: 10px;
}

.follow {
  border-radius: 1em; 
  font-size: 14px; 
  color: white; 
  background-color: #717171;
  border: none;
  padding-left: 20px;
  padding-right: 20px;
  padding-top: 4px;
}

</style>
<script type="text/javascript">

	function checkValid(){
		var f = window.document.commentForm;
		
		if ( f.commentContent.value == "" ) {
	        alert( "댓글을 입력해 주세요." );
	        f.commentContent.focus();
	        return false;
	    }
		
		return true;
	}

</script>

</head>
<body>
	<div class="container">
		<div class="content">
			<p class="category">커뮤니티 게시판 > 자유게시판</p>
			<h1 class="title">${board.boardName}</h1>

			
			<h3 class="info" style="font-size: 20px">
				${board.regDate}&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;댓글
			</h3>

			<div class="nick_name" style="padding-bottom: 30px; font-size: 20px;">
				${board.userDTO.userId} &nbsp;&nbsp;
				<input type="button" id="follow" class="follow" value="팔로우">
			</div>

			<div class="content" style="height: auto; min-height: 100px; overflow: auto;">
			
				<%--다운로드 확인--%><div>    
				<a href="${path}/front?fname=cmd 명령어.txt">cmd 명령어.txt</a>    
				<a href="${path}/front?fname=file.txt">file.txt</a></div>
				${board.boardContent}
				
			</div>

			<div class="buttons">

				<c:choose>
					<c:when
						test="${user_seq == board.userSeq && not empty loginUser && UserStatus == 0}">
						<a href="${path}/front?key=board&methodName=selectByFreeBoardSeq&boardSeq=${board.boardSeq}" class="btn">목록</a>
						<a href="${path}/front?key=board&methodName=freeboardUpdateForm&boardSeq=${board.boardSeq}"
							class="btn">수정</a>
						<a href="${path}/front?key=board&methodName=deleteFreeBoard&boardSeq=${board.boardSeq}"
							class="btn">삭제</a>
					</c:when>
					<c:otherwise>
						<a href="${path}/front?key=board&methodName=selectByFreeBoardSeq&boardSeq=${board.boardSeq}" class="btn">목록</a>
					</c:otherwise>
				</c:choose>
			</div>
			
			<hr>
			<h4 style="color: black; font-weight: bold;">댓글</h4>
			<c:forEach items="${commentList}" var="comment">
				<div><b style="color: black; font-weight: bold; font-size: 17px">
					${comment.userDTO.userId}&nbsp;&nbsp;&nbsp;</b>
					<b style="color: black; font-size: 17px">
					${comment.commentContent}</b></div>


			</c:forEach>

			<hr>
			<c:if test="${not empty loginUser}">
				<p><b style="color: black;">댓글입력</b></p>
				<form name="commentForm" method="post"
					action="${path}/front?key=board&methodName=insertComment&boardSeq=${board.boardSeq}"
					onSubmit='return checkValid()' >
					<input type="hidden" name="category" value="FREE">
                      <input type="hidden" name="parent_comment" value="0">
                      <input type="hidden" name="userSeq" value="${user_seq}">
					<textarea name="commentContent" cols="145" rows="3"></textarea>
					<input type="submit" value="댓글 입력" id="commentSubmit" class="btn"
						style="float: right;"> <br>
					<br>
				</form>
			</c:if>
			
		</div>
	</div>

	<div class="container">
	</div>


	<jsp:include page="../../pages/common/footer.jsp"/>


</body>
</html>