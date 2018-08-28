package com.siva.helper;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Optional;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;

import lombok.Getter;
import lombok.Setter;

public class WebHelper {
	public Optional<WebResponse> postMessage(WebRequest request)
			throws UnknownHostException, IOException, HttpException {

		HttpRequestExecutor httpexecutor;
		HttpProcessor httpproc;

		httpproc = HttpProcessorBuilder.create().add(new RequestContent()).add(new RequestTargetHost())
				.add(new RequestConnControl()).add(new RequestUserAgent("Test/1.1"))
				.add(new RequestExpectContinue(true)).build();

		httpexecutor = new HttpRequestExecutor();

		DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(8 * 1024);
		ConnectionReuseStrategy connStrategy = DefaultConnectionReuseStrategy.INSTANCE;

		HttpCoreContext coreContext = HttpCoreContext.create();
		HttpHost host = new HttpHost("localhost", 8080);
		coreContext.setTargetHost(host);
		HttpEntity requestBody = new StringEntity(request.messageBody, ContentType.create("text/plain", Consts.UTF_8));

		if (!conn.isOpen()) {
			Socket socket = new Socket(host.getHostName(), host.getPort());
			conn.bind(socket);
		}

		BasicHttpEntityEnclosingRequest httpRequest = new BasicHttpEntityEnclosingRequest("POST", request.getPath());
		httpRequest.setEntity(requestBody);
		System.out.println(">> Request URI: " + httpRequest.getRequestLine().getUri());

		httpexecutor.preProcess(httpRequest, httpproc, coreContext);
		HttpResponse response = httpexecutor.execute(httpRequest, conn, coreContext);
		httpexecutor.postProcess(response, httpproc, coreContext);

		System.out.println("<< Response: " + response.getStatusLine());
		System.out.println(EntityUtils.toString(response.getEntity()));
		System.out.println("==============");
		if (!connStrategy.keepAlive(response, coreContext)) {
			conn.close();
		} else {
			System.out.println("Connection kept alive...");
		}

		return Optional.empty();
	}

	public Optional<WebResponse> getMessage(WebRequest request) {
		return Optional.empty();
	}

	@Getter
	@Setter
	class WebRequest {
		String host;
		String path;
		String messageBody;
		String contentType;
		Map<String, Object> queryParams;
		Map<String, Object> headerParams;
	}

	@Getter
	@Setter
	class WebResponse {
		int statusCode;
		String responseBody;
		InputStream responseStream;
	}
}