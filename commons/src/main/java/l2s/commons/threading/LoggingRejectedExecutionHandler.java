package l2s.commons.threading;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LoggingRejectedExecutionHandler implements RejectedExecutionHandler
{
	private static final Logger _log;

	@Override
	public void rejectedExecution(final Runnable r, final ThreadPoolExecutor executor)
	{
		if(executor.isShutdown())
			return;
		LoggingRejectedExecutionHandler._log.error(r + " from " + executor, new RejectedExecutionException());
	}

	static
	{
		_log = LoggerFactory.getLogger(LoggingRejectedExecutionHandler.class);
	}
}
