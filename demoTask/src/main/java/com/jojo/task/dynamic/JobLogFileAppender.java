package com.jojo.task.dynamic;

import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.spi.DeferredProcessingAware;
import ch.qos.logback.core.status.ErrorStatus;
import com.google.common.base.Charsets;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class JobLogFileAppender<E> extends OutputStreamAppender<E> {

	private static Logger logger = LoggerFactory.getLogger(JobLogFileAppender.class);

	public static ThreadLocal<Long> contextHolder = new ThreadLocal<Long>();

	protected boolean append = true;

	private static volatile String filePath;

	public void setFilePath(String filePath) {
		JobLogFileAppender.filePath = filePath;
	}

	public String getFilePath() {
		return JobLogFileAppender.filePath;
	}

	protected void subAppend(E event) {
		Long logId = contextHolder.get();
		if (logId == null) {
			return;
		}
		String logFilePath = getLogFilePath(new Date(), logId);
		lock.lock();
		try {
			subAppend0(event, logFilePath);
		} finally {
			this.lock.unlock();
		}
	}

	protected void subAppend0(E event, String logFilePath) {
		File logFile = new File(logFilePath);
		try {
			Files.createParentDirs(logFile);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
		} catch (IOException ioe) {
			addStatus(new ErrorStatus("IO failure in appender", this, ioe));
		}
		if (!isStarted()) {
			return;
		}
		try {
			if ((event instanceof DeferredProcessingAware)) {
				((DeferredProcessingAware) event).prepareForDeferredProcessing();
			}
			byte[] byteArray = this.encoder.encode(event);
			Files.asByteSink(logFile, FileWriteMode.APPEND).write(byteArray);
		} catch (IOException ioe) {
			this.started = false;
			addStatus(new ErrorStatus("IO failure in appender", this, ioe));
		}
	}

	public void start() {
		this.started = true;
	}

	private static String getLogFilePath(Date triggerDate, Long logId) {
		String nowFormat = DateFormatUtils.format(triggerDate, "yyyy-MM-dd");
		String logFileName = String.valueOf(logId).concat(".log");
		String fileName = new StringBuilder().append(filePath).append(File.separator).append(nowFormat)
				.append(File.separator).append(logFileName).toString();
		return fileName;
	}

	public static String readLog(Date triggerDate, Long logId) {
		String logFilePath = getLogFilePath(triggerDate, logId);
		File logFile = new File(logFilePath);
		if (!logFile.exists()) {
			return null;
		}
		String fileContent = null;
		try {
			fileContent = Files.asCharSource(logFile, Charsets.UTF_8).read();
		} catch (IOException e) {
			logger.info(e.getMessage(), e);
		}
		return fileContent;
	}

}