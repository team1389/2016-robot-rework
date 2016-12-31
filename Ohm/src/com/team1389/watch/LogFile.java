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

public class LogFile {
	private String filename;
	private Writer writer;
	private boolean inited;
	private Timer timer;

	public LogFile(String filename) {
		timer = new Timer();
		inited = false;
		this.filename = filename;
	}

	public void init() {
		inited = true;
		timer.zero();
		clearFile(filename);
	}

	public boolean isInited() {
		return inited;
	}

	public Writer getWriter() {
		return writer;
	}

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

	void open() {
		try {
			writer = new BufferedWriter(new FileWriter(filename, true), 32768);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void append(String s) {
		try {
			open();
			writer.append(s);
			close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void clearFile(String filename) {
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

	public static LogFile make(FileNameSupplier format, String path) {
		return new LogFile(path + format.get());
	}

	public static LogFile make(FileNameSupplier format) {
		return make(format, "");
	}

	public static LogFile make() {
		return make(LogFile::dateTime);
	}

	public static FileNameSupplier dateTimeFormatter() {
		return LogFile::dateTime;
	}

	public static String dateTime() {
		return dateTime(DateTimeFormatter.ofPattern("MM-dd-yy-HH-mm-s"));
	}

	private static String dateTime(DateTimeFormatter format) {
		return LocalDateTime.now().format(format);
	}

	public static FileNameSupplier getFormatter(DateTimeFormatter format) {
		return () -> dateTime(format);
	}

	@Override
	public String toString() {
		return filename;
	}

	public interface FileNameSupplier extends Supplier<String> {
		public default FileNameSupplier concat(String add) {
			return () -> {
				return add + get();
			};
		}
	}
}
