package com.team1389.auto;

/**
 * This class selects, runs, and stops (if necessary) a specified autonomous
 * mode.
 */
public class AutoModeExecuter {
	private AutoModeBase m_auto_mode;
	private Thread m_thread = null;

	/**
	 * sets the auto mode to run
	 * 
	 * @param new_auto_mode
	 *            the auto mode to run
	 */
	public void setAutoMode(AutoModeBase new_auto_mode) {
		m_auto_mode = new_auto_mode;
	}

	/**
	 * runs the stored auto mode in a new thread
	 */
	public void start() {
		if (m_thread == null) {
			m_thread = new Thread(new Runnable() {
				@Override
				public void run() {
					if (m_auto_mode != null) {
						m_auto_mode.run();
					}
				}
			});
			m_thread.start();
		}

	}

	/**
	 * cancels the current auto mode
	 */
	public void stop() {
		if (m_auto_mode != null) {
			m_auto_mode.stop();
		}
		m_thread = null;
	}

}