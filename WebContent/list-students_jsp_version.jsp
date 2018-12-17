<%@ page
	import="java.util.*, co.za.talkwood.web.jdbc.*, co.za.talkwood.web.jdbc.to.StudentTO"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
	<title>Student Tracker App</title>
	<link type="text/css" rel="stylesheet" href="css/style.css">	
</head>

<%
	// get the students from the request object (sent by servlet)
	List<StudentTO> theStudents = 
					(List<StudentTO>) request.getAttribute("STUDENT_LIST");
%>

<body>

	<div id="wrapper">
		<div id="header">
			<h2>FooBar University</h2>
		</div>
	</div>

<div id="container">
	
		<div id="content">
		
			<table>
			
				<tr>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Email</th>
				</tr>
				
				<% for (StudentTO tempStudent : theStudents) { %>
				
					<tr>
						<td> <%= tempStudent.getFirstName() %> </td>
						<td> <%= tempStudent.getLastName() %> </td>
						<td> <%= tempStudent.getEmail() %> </td>
					</tr>
				
				<% } %>
				
			</table>
		
		</div>
	
	</div>
</body>
</html>