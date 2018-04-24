<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<ul class="navbar-nav mr-auto">
	<li class="nav-item">
		<a class="nav-link disabled">Welcome ${pageContext.request.userPrincipal.name}</a>
	</li>
	<li class="nav-item">
		<a class="nav-link" href="/user/myAccount">My Account</a>
	</li>
	<li class="nav-item">
		<a class="nav-link" href="/user/stockExchange">Stock Exchange</a>
	</li>
	<li class="nav-item">
		<a class="nav-link" href="/user/reports">Reports</a>
	</li>
	<c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')==true}">
		<li class="nav-item">
			<a class="nav-link" href="/admin/adminHome">Admin</a>
		</li>
	</c:if>
	<li class="nav-item">
		<a class="nav-link" href="/logout">Logout</a>
	</li>
</ul>