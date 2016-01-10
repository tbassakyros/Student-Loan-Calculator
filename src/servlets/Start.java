package servlets;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Start
 */
@WebServlet({"/Start","/Startup/*"})
public class Start extends HttpServlet {
	private static final long serialVersionUID = 1L;

		/**
		* Default constructor.
		*/
		public Start() {
			// TODO Auto-generated constructor stub
		}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getServletPath().equals("/Startup") && request.getPathInfo().equals("/Yorkbank")) {
			response.sendRedirect(request.getContextPath() + "/Start");
		}

		response.setContentType("text/plain");
		Writer resOut = response.getWriter();

		ServletContext context = getServletContext();
		double principal = Double.parseDouble(context.getInitParameter("principal"));
		double interest = Double.parseDouble(context.getInitParameter("interest"));
		int period = Integer.parseInt(context.getInitParameter("period"));

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie: cookies) {
				if (cookie.getName().equals("principal")) {
					principal = Double.parseDouble(cookie.getValue());
				} else if (cookie.getName().equals("period")) {
					period = Integer.parseInt(cookie.getValue());
				}
			}
		}

		Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String param = params.nextElement();
			if (param.equals("principal")) {
				String value = request.getParameter("principal");
				principal = Double.parseDouble(value);
				response.addCookie(new Cookie("principal", value));
			} else if (param.equals("interest")) {
				interest = Double.parseDouble(request.getParameter("interest"));
			} else if (param.equals("period")) {
				String value = request.getParameter("period");
				period = Integer.parseInt(value);
				response.addCookie(new Cookie("period", value));
			}
		}

		double mnthIntrst = interest / 12;
		double payment = ( mnthIntrst * principal )/( 1 - Math.pow( 1 + mnthIntrst, -period ));

		String IPAddress = request.getRemoteAddr();
		String protocol = request.getProtocol();
		String method = request.getMethod();
		String query = request.getQueryString();
		int port = request.getRemotePort();
		resOut.write("IP Address: " + IPAddress
				+ "\nPort: " + port
				+ "\nProtocol: " + protocol
				+ "\nMethod: " + method
				+ "\nQuery String: " + query
				+ "\nParameter: " + request.getParameter("Test")
				+ "\nURI: " + request.getRequestURI()
				+ "\nContext: " + request.getContextPath()
				+ "\nServlet: " + request.getServletPath()
				+ "\nPath: " + request.getPathInfo()
				+ "\nContext Parameter Test: " + getServletContext().getInitParameter("appName")
				+ "\n--------------------------------------------------"
				+ String.format( "\nPrincipal amount of $%.2f. Interest rate of %.2f. Period of %d months."
					+ "\nYour monthly payment will be: $%.2f", principal, interest, period, payment )
				);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
