/*
 * Create date 2014-1-6
 *
 * @author YangTing
 *
 */


package action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/combo"})
public class ComboAction  extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7464362770462972302L;

	/**
	 * 
	 */


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.getWriter().write("[{'name' : 'www.aappcc.com', 'value' : 'www.aappcc.com'},{'name' : 'pvapi.ceeg.cn', 'value' : 'pvapi.ceeg.cn'}]");
		resp.getWriter().close();
		
	}
}
