package com.jojo.util.trace;

import java.util.HashMap;
import java.util.Map;

/**
 * 线程上下文
 *
 * @since 1.0.0
 */
public class ThreadContext {
	/**
	 * 线程上下文变量的持有者
	 */
	private final static ThreadLocal<Map<String, Object>> CTX_HOLDER = new ThreadLocal<Map<String, Object>>();

	static {
		CTX_HOLDER.set(new HashMap<String, Object>());
	}

	/**
	 * traceID
	 */
	private final static String TRACE_ID_KEY = "traceId";

	/**
	 * 会话ID
	 */
	private final static String SESSION_KEY = "token";

	/**
	 * 操作用户ID
	 */
	private final static String VISITOR_ID_KEY = "userId";

	/**
	 * 客户端IP
	 */
	private static final String CLIENT_IP_KEY = "clientIp";

	/**
	 * 添加内容到线程上下文中
	 *
	 * @param key
	 * @param value
	 */
	public final static void putContext(String key, Object value) {
		Map<String, Object> ctx = CTX_HOLDER.get();
		if (ctx == null) {
			return;
		}
		ctx.put(key, value);
	}

	/**
	 * 从线程上下文中获取内容
	 *
	 * @param key
	 */
	@SuppressWarnings("unchecked")
	public final static <T extends Object> T getContext(String key) {
		Map<String, Object> ctx = CTX_HOLDER.get();
		if (ctx == null) {
			return null;
		}
		return (T) ctx.get(key);
	}

	/**
	 * 获取线程上下文
	 */
	public final static Map<String, Object> getContext() {
		Map<String, Object> ctx = CTX_HOLDER.get();
		if (ctx == null) {
			return null;
		}
		return ctx;
	}

	/**
	 * 删除上下文中的key
	 *
	 * @param key
	 */
	public final static void remove(String key) {
		Map<String, Object> ctx = CTX_HOLDER.get();
		if (ctx != null) {
			ctx.remove(key);
		}
	}

	/**
	 * 上下文中是否包含此key
	 *
	 * @param key
	 * @return
	 */
	public final static boolean contains(String key) {
		Map<String, Object> ctx = CTX_HOLDER.get();
		if (ctx != null) {
			return ctx.containsKey(key);
		}
		return false;
	}

	/**
	 * 清空线程上下文
	 */
	public final static void clean() {
		CTX_HOLDER.remove();
	}

	/**
	 * 初始化线程上下文
	 */
	public final static void init() {
		CTX_HOLDER.set(new HashMap<String, Object>());
	}

	/**
	 * 设置traceID数据
	 */
	public final static void putTraceId(String traceId) {
		putContext(TRACE_ID_KEY, traceId);
	}

	/**
	 * 获取traceID数据
	 */
	public final static String getTraceId() {
		return getContext(TRACE_ID_KEY);
	}

	/**
	 * 设置会话的用户ID
	 */
	public final static void putUserId(Integer userId) {
		putContext(VISITOR_ID_KEY, userId);
	}

	/**
	 * 设置会话的用户ID
	 */
	public final static int getUserId() {
		Integer val = getContext(VISITOR_ID_KEY);
		return val == null ? 0 : val;
	}


	/**
	 * 取出IP
	 *
	 * @return
	 */
	public static final String getClientIp() {
		return getContext(CLIENT_IP_KEY);
	}

	/**
	 * 设置IP
	 *
	 * @param ip
	 */
	public static final void putClientIp(String ip) {
		putContext(CLIENT_IP_KEY, ip);
	}

	/**
	 * 设置会话ID
	 *
	 * @param token
	 */
	public static void putSessionId(String token) {
		putContext(SESSION_KEY, token);
	}

	/**
	 * 获取会话ID
	 *
	 * @param token
	 */
	public static String getSessionId(String token) {
		return getContext(SESSION_KEY);
	}

	/**
	 * 清空会话数据
	 */
	public final static void removeSessionId() {
		remove(SESSION_KEY);
	}
}
