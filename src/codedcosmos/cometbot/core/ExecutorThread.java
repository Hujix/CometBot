/*
 * Discord CometBot by codedcosmos
 *
 * CometBot is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License 3 as published by
 * the Free Software Foundation.
 * CometBot is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License 3 for more details.
 * You should have received a copy of the GNU General Public License 3
 * along with CometBot.  If not, see <https://www.gnu.org/licenses/>.
 */

package codedcosmos.cometbot.core;

import codedcosmos.hyperdiscord.utils.debug.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public abstract class ExecutorThread implements Runnable {
	private ScheduledExecutorService scheduler;
	private long tickRate = 0;

	private String name;
	private boolean running = false;

	public ExecutorThread(String name, int spt) {
		this.name = name;

		tickRate = (long)(1000f*(float)spt);
	}

	public void start() {
		try {
			scheduler = Executors.newScheduledThreadPool(1, new NamedThreadFactory());

			onStart();

			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					internalRun();
				}
			};

			running = true;
			scheduler.scheduleAtFixedRate(runnable, 0, tickRate, TimeUnit.MILLISECONDS);
		} catch (Throwable e) {
			Log.printErr(e);
			running = false;
			System.exit(1);
		}
	}

	private void internalRun() {
		try {
			run();
		} catch (Throwable e) {
			Log.printErr(e);
			running = false;
			System.exit(1);
		}
	}

	public void stop() {
		try {
			scheduler.shutdown();
			onStop();

			System.exit(0);
		} catch (Throwable e) {
			Log.printErr(e);
		}
	}

	public boolean isRunning() {
		return running;
	}


	public abstract void onStart();
	public abstract void onStop();

	public class NamedThreadFactory implements ThreadFactory {
		public Thread newThread(Runnable r) {
			return new Thread(r, name);
		}
	}
}