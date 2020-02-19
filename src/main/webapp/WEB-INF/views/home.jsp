<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" />

<!doctype html>
<html>
<head>
	<meta charset="UTF-8" />
	<title>위메프 테스트 이재국</title>
	<link rel="stylesheet" href="${ctx }/resources/css/bootstrap.min.css" />
	<script src="${ctx }/resources/js/jquery-3.4.1.min.js"></script>
	<script src="${ctx }/resources/js/bootstrap.min.js"></script>
	<style>
		.mg20 {
			margin-top: 20px;
		}
	</style>
</head>
<body>
	<div class="mg20"></div>
	<div class="container">
		<h1 style="text-align: center;">위메프 테스트 이재국</h1>
		<form>
  			<div class="form-group">
    			<label for="">URL</label>
    			<input type="text" class="form-control" name="url" placeholder="URL을 입력하세요." value="${url }">
  			</div>
  			<div class="form-group">
    			<label for="">Type</label>
    			<select class="form-control" name="type" id="type">
      				<option value="rmtag">HTML 태그제외</option>
      				<option value="all">TEXT 전체</option>
    			</select>
  			</div>
  			<div class="form-group">
    			<label for="">출력묶음단위</label>
    			<input type="number" class="form-control" name="unit" placeholder="자연수를 입력하세요." value="${unit }">
  			</div>
  			<button type="submit" class="btn btn-primary">출력</button>
  			<div class="mg20"></div>
  			<div class="form-group">
    			<label for="">몫</label>
    			<textarea class="form-control" rows="10">${result }</textarea>
  			</div>
  			<div class="form-group">
    			<label for="exampleFormControlTextarea1">나머지</label>
    			<textarea class="form-control" rows="10">${remainder }</textarea>
  			</div>
		</form>
	</div>
	
	<script>
		$(function() {
			$("#type").val("${type}");
		})
	</script>
	
</body>
</html>