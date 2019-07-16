package com.jojo.web.filter;

import com.jojo.util.trace.TraceUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@WebFilter(filterName = "TraceFilter", urlPatterns = "/*")
public class TraceFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			/**
			 * 以上方式在多级服务调用中每个服务都会生成新的traceId，导致无法衔接跟踪。
			 * 这时就需要对http调用工具进行相应的改造了，在发送http请求时自动将traceId添加到header中
			 * 如果上游系统通过http传过来traceId的时候，直接从头里拿。如果没有的话直接生成一个
			 */
			String traceId = request.getHeader("traceId");
			TraceUtil.traceStart(traceId);
			filterChain.doFilter(request, response);
		} finally {
			TraceUtil.traceEnd();
		}

	}

}
