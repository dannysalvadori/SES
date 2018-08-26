<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<nav class="navbar navbar-expand-sm navbar-light" style="background-color: #d3e2ff;">
	
	<a class="navbar-brand" href="/" style="padding:0px">
		<img alt="stockSim Home" width="86px" src="<%=request.getContextPath()%>/resources/img/stockSimLogo-m.png"/>
	</a>

	<div id="navbarSupportedContent">
		
		<c:if test="${pageContext.request.userPrincipal.name==null}">
			<jsp:include page="nav-not-logged-in.jsp"></jsp:include>
		</c:if>
		
		<c:if test="${pageContext.request.userPrincipal.name!=null}">
			<jsp:include page="nav-logged-in.jsp"></jsp:include>
		</c:if>

	</div>
</nav>