package com.lgu.common.xssFilter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;



public class CrossScriptingFilter implements Filter {

	public FilterConfig filterConfig;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	public void destroy() {
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		String path = ((HttpServletRequest) request).getServletPath();
		if (	path.equals("/admin/cctv/cctvLogFileDown.do") 
			/*	|| path.equals("/inf/survey/surveyInfoList.json")
				|| path.equals("/inf/faqInfo/faqList.json")
				|| path.equals("/inf/faqInfo/faqDetail.json")
				|| path.equals("/inf/videoGuide/videoGuideInfoList.json")
				|| path.equals("/inf/noti/notiInfoList.json")
				|| path.equals("/inf/banner/bannerInfoList.json")*/
			){
			// 필터기능 제외
			 chain.doFilter(request, response);
			    return;
		}else{
			// 필터
			chain.doFilter(new RequestWrapper((HttpServletRequest) request),
					response);
		}
		
	}

}