package co.za.talkwood.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import co.za.talkwood.web.jdbc.to.StudentTO;

public class StudentDbUtil {
	
	private DataSource datasource;

	public StudentDbUtil(DataSource datasource) {
		super();
		this.datasource = datasource;
	}
	
	Connection conn;
	Statement stmt;
	ResultSet rs;
	
	public List<StudentTO> getStudents() throws Exception{
		
		List<StudentTO> studentList = new ArrayList<>();
		try {
			// get database connnection
			conn = datasource.getConnection();
			
			// create sql statement
			stmt = conn.createStatement();
			
			// execute query
			String mySql = "select * from student order by first_name";
			rs = stmt.executeQuery(mySql);
			
			// process resultset
			while(rs.next()) {
				
				// retrieve data from result set row
				int id = rs.getInt("id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");
								
				StudentTO tempStudent = new StudentTO(id, firstName, lastName, email);
				
				studentList.add(tempStudent);
				
			}
			
			
			return studentList;
			
		} finally {
			// close JDBC objects
			close(conn, stmt, rs);
		}	
	}
	
	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {

		try {
			if (myRs != null) {
				myRs.close();
			}
			
			if (myStmt != null) {
				myStmt.close();
			}
			
			if (myConn != null) {
				myConn.close();   // doesn't really close it ... just puts back in connection pool
			}
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public boolean addStudent(StudentTO theStudent) throws SQLException {
		
		Connection myConn = null;
		PreparedStatement myPrepstmt = null;
		
		try {
			// get database connection
			myConn = datasource.getConnection();
			
			// create sql for insert
			String sql = "insert into student "
					   + "(first_name, last_name, email) "
					   + "values (?, ?, ?)";
			
			myPrepstmt = myConn.prepareStatement(sql);
			
			// set param values for the student
			myPrepstmt.setString(1, theStudent.getFirstName());
			myPrepstmt.setString(2, theStudent.getLastName());
			myPrepstmt.setString(3, theStudent.getEmail());
			
			// execute sql insert
			return myPrepstmt.execute();
			
		} finally {
			// clean up JDBC objects
			close(myConn, myPrepstmt, null);
		}
		
	}

	public StudentTO getStudent(String myStudentId) throws Exception {
		Connection myConn = null;
		PreparedStatement ptstmt = null;
		ResultSet rs = null;
		
		try {
			// convert student id to int
			int studentId = Integer.parseInt(myStudentId);
						
			// get database connection
			myConn = datasource.getConnection();
			
			// create sql for insert
			String mySql = "select * from student where id = ?";
			
			// create prepared statement
			ptstmt = myConn.prepareStatement(mySql);
			
			// set params
			ptstmt.setInt(1, studentId);
			
			// execute statement
			rs = ptstmt.executeQuery();
			
			// retrieve data from result set row
			if (rs.next()) {
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String email = rs.getString("email");
				
				// use the studentId during construction
				return new StudentTO(studentId, firstName, lastName, email);
			}
			else {
				throw new Exception("Could not find student id: " + studentId);
			}				
			
		} finally {
			// clean up JDBC objects
			close(myConn, ptstmt, rs);
		}
		
	}

	public void updateStudent(StudentTO theStudent) throws Exception{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			// get db connetion 
			myConn = datasource.getConnection();
			
			// create SQL update statement
			String sql = "update student "
						+ "set first_name=?, last_name=?, email=? "
						+ "where id=?";
			
			// prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			
			// execute SQL statement
			myStmt.execute();
		} finally {
			// clean up JDBC objects
			close(myConn, myStmt, null);
		}
		
	}

	public void deleteStudent(String theStudentId) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			// convert student id to int
			int studentId = Integer.parseInt(theStudentId);
			
			// get connection to database
			myConn = datasource.getConnection();
			
			// create sql to delete student
			String sql = "delete from student where id=?";
			
			// prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			// set params
			myStmt.setInt(1, studentId);
			
			// execute sql statement
			myStmt.execute();
		}
		finally {
			// clean up JDBC code
			close(myConn, myStmt, null);
		}	
		
	}

}
