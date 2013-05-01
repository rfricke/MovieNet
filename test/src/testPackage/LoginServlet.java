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
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
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
		String username = request.getParameter("user");
		String password = request.getParameter("pwd");
		String ps = "SELECT * FROM MEMBER WHERE USERNAME = ? AND PASSWORD = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		boolean login = false;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@ginger.umd.edu:1521:dbclass1", "dbclass124", "O2Fjmit4");
			pstmt = conn.prepareStatement(ps);
		    pstmt.setString(1, username);
		    pstmt.setString(2, password);
			rset = pstmt.executeQuery();
			login = rset.next();
		} catch (Exception e) {
			System.err.println("Oh snap! There's an exception: " + e);
		} finally {
			if (rset != null) try {rset.close();} catch (SQLException ignore) {}
		    if (pstmt != null) try {pstmt.close();} catch (SQLException ignore) {}
		    if (conn != null) try {conn.close();} catch (SQLException ignore) {}
		}
		
		if (login) {
		    request.getSession().setAttribute("username", username);
		    response.sendRedirect("home.html"); // Redirect to home page.
		} else {
		    //request.setAttribute("message", "Unknown username/password, try again"); // This sets the ${message}
		    request.getRequestDispatcher("try_again.html").forward(request, response); // Redisplay JSP.
		}
	}

}
