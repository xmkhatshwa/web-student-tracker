<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
	<title>Student Tracker App</title>
	<link type="text/css" rel="stylesheet" href="css/style.css">	
</head>

<body>

	<div id="wrapper">
		<div id="header">
			<h2>FooBar University</h2>
		</div>
	</div>

	<div id="container">
	
		<div id="content">
		
			<!-- put new button: Add Student -->
			<input type="button" value="Register Student"
				onclick="window.location.href='add-student-form.jsp'; return false;"
				class="add-student-button" />
				
			<table>
			
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Email</th>
					<th>Action</th>
				</tr>
				
				<c:forEach var="tempStudent" items="${STUDENT_LIST}">
				
					<c:url var="tempLink" value="StudentControllerServlet">
						<c:param name="command" value="LOAD2" />
						<c:param name="studentId" value="${tempStudent.id}" />
						<c:param name="theFirstName" value="${tempStudent.firstName}" />
						<c:param name="theLastName" value="${tempStudent.lastName}" />
						<c:param name="theEmail" value="${tempStudent.email}" />
					</c:url>
					
					<tr>
						<td> ${tempStudent.firstName} </td>
						<td> ${tempStudent.lastName} </td>
						<td> ${tempStudent.email} </td>
						<td> 
							<a href="${tempLink}">Update</a> 
						</td>
					</tr>
				
				</c:forEach>
				
			</table>
		
		</div>
	
	</div>
</body>


</html>








