package com.siva.tools.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.siva.tools.callable.MyCallable;

public class MainTool {

	public final static int THREAD_TOTAL = 10;

	public static void main(String[] args) {

		try {
			Long startTime = System.currentTimeMillis();

			File sourcePath = new File("C:\\Test\\input");
			File destinationPath = new File("src/main/resources/output");

			List<File> totalFiles = Arrays.asList(sourcePath.listFiles());

			int count = 0;
			int totalNoOfThreads = THREAD_TOTAL;

			int threshold = totalFiles.size() / THREAD_TOTAL;

			if (threshold == 0) {
				totalNoOfThreads = totalFiles.size();
				threshold = 1;
			}

			System.out.println("Threshold is : " + threshold);

			ExecutorService executorService = Executors.newFixedThreadPool(THREAD_TOTAL);
			List<Future<Boolean>> result = new ArrayList<Future<Boolean>>();

			for (int i = 0; i < totalNoOfThreads; i++) {

				MyCallable callable = new MyCallable(getFiles(totalFiles, count, threshold), destinationPath);
				String id = callable.getId();

				System.out.println("Create a thread: " + i + " with Id " + id);

				result.add(executorService.submit(callable));
				count = count + threshold;
			}

			executorService.shutdown();
			executorService.awaitTermination(1, TimeUnit.HOURS);

			Long endTime = System.currentTimeMillis();
			System.out.println("Total Time: " + (endTime - startTime) + " milliseconds");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static List<File> getFiles(List<File> totalFiles, int startIndex, int count) {
		List<File> resultList = new ArrayList<File>();

		for (int i = startIndex; i <= (startIndex + count); i++) {
			if (i >= totalFiles.size()) {
				break;
			}
			resultList.add(totalFiles.get(i));
		}

		return resultList;
	}
}
