package de.tobias.logger;

import java.nio.file.Paths;

public class LoggerTest {

	public static void main(String[] args) {
		Logger.init(Paths.get("log"));
		Logger.setFileOutput(FileOutputOption.DISABLED);
		Logger.log(LogLevel.INFO, "Test");
		System.out.println("binopmü");
		System.out.println();
		System.out.println();
		System.out.print("ibnomp");
	}
}
