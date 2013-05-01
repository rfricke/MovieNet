package testPackage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String birthday = request.getParameter("bday");
		String email = request.getParameter("email");
		String sex = request.getParameter("sex");
		String username = request.getParameter("user");
		String password1 = request.getParameter("pwd1");
		String password2 = request.getParameter("pwd2");
		if (!password1.equals(password2)) {
			request.getRequestDispatcher("unsuccessful_register_password.html").forward(request, response);
			return;
		}
		String splits[] = birthday.split("-");
		int year = Integer.parseInt(splits[0].trim());
		int age = 2013 - year;
		String ps = "SELECT * FROM MEMBER WHERE USERNAME = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null, pstmt2 = null, pstmt3 = null;
		ResultSet rset = null, rset2 = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@ginger.umd.edu:1521:dbclass1", "dbclass124", "O2Fjmit4");
			pstmt = conn.prepareStatement(ps);
		    pstmt.setString(1, username);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				rset.close();
				pstmt.close();
				conn.close();
				request.getRequestDispatcher("unsuccessful_register_username.html").forward(request, response);
				return;
			}
			pstmt2 = conn.prepareStatement("SELECT ACCT_NUMBER FROM (SELECT ACCT_NUMBER FROM MEMBER ORDER BY ACCT_NUMBER DESC) WHERE ROWNUM=1");
			rset2 = pstmt2.executeQuery();
			int acct_num = -1;
			if (rset2.next()) {
				acct_num = rset2.getInt("ACCT_NUMBER") + 1;
			} else {
				throw new Exception("wtf");
			}
			ps = "INSERT INTO MEMBER VALUES(?, ?, ?, ?, ?, ?, ?)";
			pstmt3 = conn.prepareStatement(ps);
			pstmt3.setString(1, name);
			pstmt3.setInt(2, acct_num);
			pstmt3.setString(3, email);
			pstmt3.setInt(4, age);
			pstmt3.setString(5, sex);
			pstmt3.setString(6, username);
			pstmt3.setString(7, password1);
			pstmt3.executeUpdate();
			conn.commit();		// this is important I think
			response.sendRedirect("register_successful.html");
		} catch (Exception e) {
			System.err.println("Oh snap! There's an exception: " + e);
		} finally {
			if (rset != null) try {rset.close();} catch (SQLException ignore) {}
		    if (pstmt != null) try {pstmt.close();} catch (SQLException ignore) {}
		    if (conn != null) try {conn.close();} catch (SQLException ignore) {}
		}
	}

}
