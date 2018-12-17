package co.za.talkwood.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import co.za.talkwood.web.jdbc.to.StudentTO;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private StudentDbUtil studentDbUtil;

	@Resource(name = "jdbc/web_student_tracker")
	private DataSource dataSource;

	@Override
	public void init() throws ServletException {
		super.init();

		// create our student db util ... and pass in the conn pool / datasource
		try {
			studentDbUtil = new StudentDbUtil(dataSource);
		} catch (Exception exc) {
			throw new ServletException(exc);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// read the "command" parameter
			String theCommand = request.getParameter("command");

			// if the command is missing, then default to listing students
			if (theCommand == null) {
				theCommand = "LIST";
			}

			// route to the appropriate method
			switch (theCommand) {

			case "LIST":
				listStudents(request, response);
				break;
				
			case "LOAD":
				loadStudent(request, response);
				break;
				
			case "UPDATE":
				updateStudent(request, response);
				break;
			
			case "DELETE":
				deleteStudent(request, response);
				break;
				
			case "LOAD2":
				loadStudent2(request, response);
				break;

			default:
				listStudents(request, response);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// read the "command" parameter
			String theCommand = request.getParameter("command");

			// route to the appropriate method
			switch (theCommand) {

			case "ADD":
				addStudent(request, response);
				break;

			default:
				listStudents(request, response);
			}

		} catch (Exception exc) {
			throw new ServletException(exc);
		}

	}



	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student id from form data
		String theStudentId = request.getParameter("studentId");
		
		// delete student from database
		studentDbUtil.deleteStudent(theStudentId);
		
		// send them back to "list students" page
		listStudents(request, response);
		
	}

	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student info from form data
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		// create a new student object
		StudentTO theStudent = new StudentTO(id, firstName, lastName, email);
		
		// perform update on database
		studentDbUtil.updateStudent(theStudent);
		
		// send them back to the "list students" page
		listStudents(request, response);
		
	}

	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// read student id from form data
		String myStudentId = request.getParameter("studentId");
		
		// get student from database (db util)
		StudentTO theStudent = studentDbUtil.getStudent(myStudentId);
		
		// place student in the request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		
		// send to jsp page: update-student-form.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
		
	}

	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// read student info from form data
		String name = request.getParameter("firstName");
		String last = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		// create a new student object
		StudentTO newStudent = new StudentTO(name, last, email);
		
		// add the student to the database
		boolean isAdded = studentDbUtil.addStudent(newStudent);
		
		if(!isAdded) {
			System.out.println("Student: " + name + " is added successfully");
		} else {
			System.out.println("Student: " + name + " is not registered");
		}
				
		// send back to main page (the student list)
        // SEND AS REDIRECT to avoid multiple-browser reload issue
        response.sendRedirect(request.getContextPath() + "/StudentControllerServlet?command=LIST");		
		
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// get students from db util
		List<StudentTO> myStudents = studentDbUtil.getStudents();

		// add students to the request
		request.setAttribute("STUDENT_LIST", myStudents);

		// send to JSP page (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students_jstl_version.jsp");
		dispatcher.forward(request, response);
	}
	
	private void loadStudent2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// read student data from form data
		String myStudentId = request.getParameter("studentId");
		String myfirstName = request.getParameter("theFirstName");
		String myLastName = request.getParameter("theLastName");
		String myEmail = request.getParameter("theEmail");
		
		int myId = Integer.parseInt(myStudentId);
		
		StudentTO theStudent = new StudentTO(myId, myfirstName, myLastName, myEmail);
		
		// place student in the request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		
		// send to jsp page: update-student-form.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
		
	}

}
