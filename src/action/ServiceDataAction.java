/*
 * Create date 2014-1-6
 *
 * @author YangTing
 *
 */


package action;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/service"})
public class ServiceDataAction  extends HttpServlet {
	
	private static final long serialVersionUID = -3696540644331738921L;

	@Override
	protected synchronized void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String service = req.getParameter("service");
		if (service == null || service.length() == 0 ) service = "www.aappcc.com";
		BufferedReader bin =  new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(service)));
		StringBuffer sb =  new StringBuffer();
		String str =  bin.readLine();
		while (str != null){
			sb.append(str);
			str =  bin.readLine();
		}
		bin.close();
		resp.getWriter().write(sb.toString());
		resp.getWriter().close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req,resp);
	}
}
