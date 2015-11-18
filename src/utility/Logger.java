package utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger
{
	public enum Level
	{
		INFO,		// All levels
		WARNING,	// Incorrect behavior, but not a crash
		ERROR		// Errors, crashes, exceptions
	}
	
	private static Level _logLevel;
	private static String _logDirectory;
	
	
	public static void SetLogLevel(Level level)
	{
		_logLevel = level;
	}
	
	public static void Info(String message)
	{
		if (_logLevel.ordinal() <= Level.INFO.ordinal())
		{
			PrintToFile(Level.INFO.toString(), message);
		}
	}

	public static void Warn(String message)
	{
		if (_logLevel.ordinal() <= Level.WARNING.ordinal())
		{
			PrintToFile(Level.WARNING.toString(), message);
		}
	}
	
	public static void Error(String message)
	{
		if (_logLevel.ordinal() <= Level.ERROR.ordinal())
		{
			PrintToFile(Level.ERROR.toString(), message);
		}
	}

	
	private static void PrintToFile(String level, String message)
	{
		CreateLogDirectory();
		
		try
		{
			File logFile = new File(_logDirectory + File.separator + "log_" + GetCurrentDate() + ".txt");
			if (!logFile.exists())
			{
				logFile.createNewFile();
			}
			
			FileWriter fw = new FileWriter(logFile.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
			int lineNumber = ste.getLineNumber();
			String fileName = ste.getFileName().replace(".java", "");
			String currentTime = GetCurrentTime();

			bw.write(level + "::" + fileName + "::" + lineNumber + " - " + currentTime + " " + message + "\n");
			bw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static boolean CreateLogDirectory()
	{
		return new File(GetLogDirectory()).mkdirs();
	}
	
	private static String GetLogDirectory()
	{
		if (_logDirectory == "" || _logDirectory == null)
		{
			String fileSeparator = File.separator;
			_logDirectory = System.getProperty("user.home") + fileSeparator +
							"AppData" + fileSeparator +
							"Local" + fileSeparator +
							"Realm of Legends" + fileSeparator +
							"Logs" + fileSeparator;
		}
		
		return _logDirectory;
	}
	
	private static String GetCurrentDate()
	{
		String currentDate = new SimpleDateFormat("yyy/MM/dd").format(new Date());
		currentDate = currentDate.replace("/", "_");
		return currentDate;
	}
	
	private static String GetCurrentTime()
	{
		return new SimpleDateFormat("HH:mm:ss").format(new Date());
	}
	
}
