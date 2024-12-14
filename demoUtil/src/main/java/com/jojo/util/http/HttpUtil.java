package com.jojo.util.http;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * http工具类
 * @author chenyue
 */
public class HttpUtil {

    public static String getIp() {
        HttpServletRequest request = HttpUtil.getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取所有请求的值
     */
    public static Map<String, String> getRequestParameters() {
        Map<String, String> values = Maps.newLinkedHashMap();
        HttpServletRequest request = HttpUtil.getRequest();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (String paramName : parameterMap.keySet()) {
            String paramValue = request.getParameter(paramName);
            values.put(paramName, paramValue);
        }
        return values;
    }

    /**
     * 获取 HttpServletRequest
     */
    public static HttpServletResponse getResponse() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        return response;
    }

    /**
     * 获得当前的session, 如果没有返回null
     * @return
     */
    public static HttpSession getSession() {
        HttpSession res = null;
        HttpServletRequest request = HttpUtil.getRequest();
        if (request != null) {
            res = request.getSession();
        }
        return res;
    }

    /**
     * 获得当前登录用户
     * @return
     */
    public static Object getCurrentUser() {
//        Subject subject = SecurityUtils.getSubject();
//        if (subject == null) {
//            return null;
//        }
//        PrincipalCollection principals = subject.getPrincipals();
//        if (principals == null) {
//            return null;
//        }
//        Object primaryPrincipal = principals.getPrimaryPrincipal();
//        if (primaryPrincipal != null && primaryPrincipal instanceof User) {
//            return (User) primaryPrincipal;
//        }
        return null;
    }

    /**
     * 获取 包装防Xss Sql注入的 HttpServletRequest
     * @return request
     */
    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    /**
     * 判断是否ajax请求. 可以看到Ajax 请求多了个 x-requested-with ，可以利用它，<br>
     * request.getHeader("x-requested-with"); 为 null，则为传统同步请求，<br>
     * 为 XMLHttpRequest，则为Ajax 异步请求。<br>
     * @paramrequest HttpServletRequest
     * @return 是否ajax请求.
     */
    public static boolean isAjaxRequest() {
        HttpServletRequest request = getRequest();
        String xr = request.getHeader("X-Requested-With");
        if (StringUtils.isNotBlank(xr)) {
            return true;
        }
        return false;
    }


}