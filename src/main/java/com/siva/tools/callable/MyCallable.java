package com.siva.tools.callable;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.apache.commons.io.FileUtils;

public class MyCallable implements Callable<Boolean> {

	private List<File> inputFileList;
	private File destination;
	private String id = null;

	public MyCallable(List<File> inputFileList, File destination) {
		this.inputFileList = inputFileList;
		this.destination = destination;

		initId();
	}

	public Boolean call() throws Exception {
		for (File inputFile : inputFileList) {

			System.out.println("Result for Thread : " + id + " is " + FileUtils.readFileToString(inputFile));

			// }
			// FileChannel fileChannel = new RandomAccessFile(inputFile,
			// "rw").getChannel();
			// FileLock lock = fileChannel.lock();
			//
			// ByteBuffer byteBuffer = ByteBuffer.allocate(48);
			// int bytesRead = fileChannel.read(byteBuffer);
			//
			// while (bytesRead != -1) {
			//
			// byteBuffer.flip(); // make buffer ready for read
			//
			// while (byteBuffer.hasRemaining()) {
			// System.out.print((char) byteBuffer.get()); // read 1 byte at
			// // a time
			// }
			//
			// byteBuffer.clear(); // make buffer ready for writing
			// bytesRead = fileChannel.read(byteBuffer);
			// }
			//
			// fileChannel.close();
			// lock.release();
			// }
		}
		return true;
	}

	private void initId() {
		id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}
}
