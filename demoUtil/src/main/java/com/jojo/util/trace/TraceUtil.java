package com.jojo.util.trace;

import java.util.UUID;

import org.slf4j.MDC;

/**
 * trace工具
 *
 * @date 2017年3月10日
 * @since 1.0.0
 */
public class TraceUtil {

	public final static String TRACEID_TAG = "traceId";

	public static void traceStart() {
		String traceId = generateTraceId();
		ThreadContext.init();
		MDC.put(TRACEID_TAG, traceId);
		ThreadContext.putTraceId(traceId);
	}

	public static void traceStart(String traceId) {
		if (traceId == null) {
			traceId = generateTraceId();
		}
		ThreadContext.init();
		MDC.put(TRACEID_TAG, traceId);
		ThreadContext.putTraceId(traceId);
	}

	public static void traceEnd() {
		MDC.remove(TRACEID_TAG);
		ThreadContext.clean();
	}

	/**
	 * 生成跟踪ID
	 *
	 * @return
	 */
	private static String generateTraceId() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}