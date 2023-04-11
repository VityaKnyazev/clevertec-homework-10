package ru.clevertec.knyazev.filter;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = { "/*" }, filterName = "CharsetFilter")
public class CharsetFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String headerAccept = httpRequest.getHeader("accept");

		if (headerAccept != null && ((headerAccept.contains("text/plain") && headerAccept.contains("application/pdf")
				&& headerAccept.contains("application/json")) || headerAccept.contains("*/*"))) {
			chain.doFilter(request, response);
		} else {
			HttpServletResponse res = (HttpServletResponse) response;
			res.setCharacterEncoding("UTF-8");
			res.setStatus(415);
			PrintWriter pw = res.getWriter();
			pw.println("415 Unsupported Media Type. Accept header shoul contains Mime types:"
					+ "text/plain, application/json, application/pdf or */*");
		}
		
	}

}
