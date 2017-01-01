package com.team1389.watch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

import com.team1389.hardware.inputs.hardware.Timer;

/**
 * handles logging to a specific given file
 * 
 * @author amind
 */
public class LogFile {
	private final String filename;
	private Writer writer;
	private boolean inited;
	private Timer timer;

	/**
	 * @param filename the file to write to
	 */
	public LogFile(String filename) {
		timer = new Timer();
		inited = false;
		this.filename = filename;
	}

	/**
	 * initializes the file
	 */
	public void init() {
		clearFile(filename);
		open();
		inited = true;
		timer.zero();
	}

	/**
	 * @return whether this file has been initialized
	 */
	public boolean isInited() {
		return inited;
	}

	/**
	 * LogFile must be initialized before calling this
	 * 
	 * @return the writer for this log file
	 */
	public Writer getWriter() {
		if (!inited) {
			init();
		}
		return writer;
	}

	/**
	 * @return the time since this file was initialized
	 */
	public double getTimeStamp() {
		return timer.get();
	}

	void close() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void open() {
		try {
			writer = new BufferedWriter(new FileWriter(filename, true), 32768);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * deletes all existing text in the file
	 * 
	 * @param filename the file to clear
	 */
	public static void clearFile(String filename) {
		if (new File(filename).exists()) {
			PrintWriter w;
			try {
				w = new PrintWriter(filename);
				w.print("");
				w.close();
			} catch (FileNotFoundException e) {
			}
		}
	}
	// Log File Naming util

	/**
	 * generates a LogFile with the given naming convention and folder path
	 * 
	 * @param format the naming convention, a String supplier
	 * @param path the string path to the log folder - should end with a '/'
	 * @see FileNameSupplier
	 * @return a fresh LogFile
	 * @see LogFile#dateTimeFormatter
	 * @see LogFile#getFormatter(DateTimeFormatter)
	 */
	public static LogFile make(FileNameSupplier format, String path) {
		return new LogFile(path + format.get());
	}

	/**
	 * generates a LogFile with the given naming convention <br>
	 * uses the default folder path
	 * 
	 * @param format the naming convetion, a String supplier
	 * @return a fresh LogFile
	 * @see LogFile#dateTimeFormatter
	 * @see LogFile#getFormatter(DateTimeFormatter)
	 */
	public static LogFile make(FileNameSupplier format) {
		return make(format, "");
	}

	/**
	 * generates the default LogFile
	 * 
	 * @return a fresh LogFile
	 */
	public static LogFile make() {
		return make(LogFile::dateTime);
	}

	/**
	 * @return a {@link FileNameSupplier} that supplies the a string representation of the date and time in the default format
	 */
	public static FileNameSupplier dateTimeFormatter() {
		return LogFile::dateTime;
	}

	/**
	 * 
	 * @param format the date/time format to use
	 * @return a {@link FileNameSupplier} that supplies the a string representation of the date and time in the given format
	 */
	public static FileNameSupplier getFormatter(DateTimeFormatter format) {
		return () -> dateTime(format);
	}

	private static String dateTime() {
		return dateTime(DateTimeFormatter.ofPattern("MM-dd-yy-HH-mm-s"));
	}

	private static String dateTime(DateTimeFormatter format) {
		return LocalDateTime.now().format(format);
	}

	@Override
	public String toString() {
		return filename;
	}

	/**
	 * a wrapper around the {@link java.util.function.Supplier} for Strings specifically
	 * 
	 * @author amind
	 */
	public interface FileNameSupplier extends Supplier<String> {

		/**
		 * generates a new supplier with the string concatenated to the result of the old supplier <br>
		 * <em>NOTE</em>: no spaces or seperators are added between the {@code add} and the result by default
		 * 
		 * @param add the String
		 * @return a new FileNameSupplier with the modified get method
		 */
		public default FileNameSupplier concat(String add) {
			return () -> {
				return add + get();
			};
		}
	}
}
