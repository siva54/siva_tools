package com.siva.tools.tool;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class FileReader {
	public static void main(String[] args) {
		try {
			File input = new File("src/main/resources/sample.txt");

			FileChannel fileChannel = new RandomAccessFile(input, "rw").getChannel();
			FileLock fileLock = fileChannel.lock();

			Thread.sleep(20000);

			fileLock.release();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
