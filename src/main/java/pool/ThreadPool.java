package pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
	// �̳߳ص��������Ĭ����20
		private Integer threadCount = 20;
		// ��������ĳ�ʱʱ��
		private Integer timeOut = 5000;
		// �Ƿ���������������㳬ʱƽ��ʱ��
		private boolean isAutoTimeOut = false;
		// ��������
		private Integer sumCount = 0;
		// ���ؽ����
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
			// �����������

			pool.shutdown();

			if (timeOut != null) {
				if (isAutoTimeOut) {
					// �����Զ���ʱ�������߳��������ʱ��

					timeOut = (int) ((Math.ceil(sumCount.doubleValue() / threadCount.doubleValue())) * timeOut);
				}

				try {
					// ����X��û���������ٵȴ�
					boolean isFinish = pool.awaitTermination(timeOut, TimeUnit.SECONDS);
					if (!isFinish) {
						pool.shutdownNow();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// ��ʼ���ս��
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
