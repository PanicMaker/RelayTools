package pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
	// 线程池的最大数，默认是20
		private Integer threadCount = 20;
		// 单个任务的超时时间
		private Integer timeOut = 5000;
		// 是否根据任务总数计算超时平均时间
		private boolean isAutoTimeOut = false;
		// 任务总数
		private Integer sumCount = 0;
		// 返回结果集
		private List<Future<?>> results = new ArrayList<>();
		private ExecutorService pool;

		public ThreadPool(Integer threadCount, Integer timeOut, boolean isAutoTimeOut) {
			super();
			this.threadCount = threadCount;
			this.timeOut = timeOut;
			this.isAutoTimeOut = isAutoTimeOut;
			pool = Executors.newFixedThreadPool(threadCount);
		}

		public static int autoCalThreadCount(int taskCount) {

			if (taskCount == 1) {
				return 1;
			} else if (taskCount < 10) {
				return 5;
			} else if (taskCount >= 10 && taskCount < 20) {
				return 20;
			} else if (taskCount >= 20 && taskCount < 30) {
				return 30;
			} else if (taskCount >= 30 && taskCount < 40) {
				return 40;
			} else {
				return 50;

			}

		}

		public ThreadPool(Integer threadCount) {
			super();
			this.threadCount = threadCount;
			pool = Executors.newFixedThreadPool(threadCount);
		}

		public ThreadPool() {
			super();
			pool = Executors.newFixedThreadPool(threadCount);

		}

		public ThreadPool(Integer threadCount, Integer timeOut) {
			super();
			this.threadCount = threadCount;
			this.timeOut = timeOut;
			pool = Executors.newFixedThreadPool(threadCount);

		}

		public <T> ThreadPool addTask(Callable<T> o) {

			synchronized (this) {
				Future<T> future = pool.submit(o);
				results.add(future);
				sumCount++;
			}
			/*Future<T> future = pool.submit(o);
			results.add(future);
			sumCount++;*/
			return this;
		}

		public <T> List<T> getResult() {
			// 不再添加任务

			pool.shutdown();

			if (timeOut != null) {
				if (isAutoTimeOut) {
					// 开启自动计时，根据线程数来算出时间

					timeOut = (int) ((Math.ceil(sumCount.doubleValue() / threadCount.doubleValue())) * timeOut);
				}

				try {
					// 超过X秒没结束，不再等待
					boolean isFinish = pool.awaitTermination(timeOut, TimeUnit.SECONDS);
					if (!isFinish) {
						pool.shutdownNow();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// 开始接收结果
			List<T> beanList = new ArrayList<T>();
			try {
				for (int i = 0; i < results.size(); i++) {
					beanList.add((T) results.get(i).get());

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return beanList;
		}

}
