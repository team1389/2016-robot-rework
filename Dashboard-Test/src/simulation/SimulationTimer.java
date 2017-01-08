package simulation;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Timer.Interface;

public class SimulationTimer implements Timer.StaticInterface{

	@Override
	public double getFPGATimestamp() {
		return System.currentTimeMillis()/1000;
	}

	@Override
	public double getMatchTime() {
		return System.currentTimeMillis()/1000;
	}

	@Override
	public void delay(double seconds) {
		double val=getFPGATimestamp();
		while(getFPGATimestamp()-val<seconds){
			
		}
	}

	@Override
	public Interface newTimer() {
		return new TimerImpl();
	}
	class TimerImpl implements Timer.Interface {
	    private double m_startTime;
	    private double m_accumulatedTime;
	    private boolean m_running;

	    /**
	     * Create a new timer object. Create a new timer object and reset the time
	     * to zero. The timer is initially not running and must be started.
	     */
	    public TimerImpl() {
	      reset();
	    }

	    private double getMsClock() {
	      return System.currentTimeMillis();
	    }

	    /**
	     * Get the current time from the timer. If the clock is running it is
	     * derived from the current system clock the start time stored in the timer
	     * class. If the clock is not running, then return the time when it was last
	     * stopped.
	     *
	     * @return Current time value for this timer in seconds
	     */
	    public synchronized double get() {
	      if (m_running) {
	        return ((double) ((getMsClock() - m_startTime) + m_accumulatedTime)) / 1000.0;
	      } else {
	        return m_accumulatedTime;
	      }
	    }

	    /**
	     * Reset the timer by setting the time to 0. Make the timer startTime the
	     * current time so new requests will be relative now
	     */
	    public synchronized void reset() {
	      m_accumulatedTime = 0;
	      m_startTime = getMsClock();
	    }

	    /**
	     * Start the timer running. Just set the running flag to true indicating
	     * that all time requests should be relative to the system clock.
	     */
	    public synchronized void start() {
	      m_startTime = getMsClock();
	      m_running = true;
	    }

	    /**
	     * Stop the timer. This computes the time as of now and clears the running
	     * flag, causing all subsequent time requests to be read from the
	     * accumulated time rather than looking at the system clock.
	     */
	    public synchronized void stop() {
	      final double temp = get();
	      m_accumulatedTime = temp;
	      m_running = false;
	    }

	    /**
	     * Check if the period specified has passed and if it has, advance the start
	     * time by that period. This is useful to decide if it's time to do periodic
	     * work without drifting later by the time it took to get around to
	     * checking.
	     *
	     * @param period The period to check for (in seconds).
	     * @return If the period has passed.
	     */
	    public synchronized boolean hasPeriodPassed(double period) {
	      if (get() > period) {
	        // Advance the start time by the period.
	        // Don't set it to the current time... we want to avoid drift.
	        m_startTime += period * 1000;
	        return true;
	      }
	      return false;
	    }
	  }

}
