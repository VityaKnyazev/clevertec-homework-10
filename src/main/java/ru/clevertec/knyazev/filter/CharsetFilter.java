package ru.clevertec.knyazev.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebFilter(urlPatterns = { "/services/*" }, filterName = "AcceptHeaderFilter")
public class CharsetFilter implements Filter {
	private static final String ACCEPT_HEADER = "accept";
	private static final String PDF_TYPE = "application/pdf";

	private static final String ACCEPT_HEADER_ERROR = "{\"error\":\"415 Unsupported Media Type. " +
			"Accept access header should contains only application/json or application/pdf\"}";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String headerAccept = httpRequest.getHeader(ACCEPT_HEADER);

		if (headerAccept != null && (headerAccept.contains(PDF_TYPE)
				|| headerAccept.contains(MimeTypeUtils.APPLICATION_JSON_VALUE))) {
			chain.doFilter(request, response);
		} else {
			HttpServletResponse resp = (HttpServletResponse) response;
			resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
			resp.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
			resp.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
			resp.getWriter().println(ACCEPT_HEADER_ERROR);
		}
		
	}

}
