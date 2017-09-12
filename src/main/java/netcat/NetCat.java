package netcat;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class NetCat {
	public static void main(String[] args) throws Exception {
		CommandLineParser parser = new PosixParser();

		Options options = new Options();
		options.addOption("l", "listen", false, "listen mode");
		options.addOption("p", "port", true, "port number");

		CommandLine line = parser.parse(options, args);

		if (line.hasOption('l') && line.hasOption('p')) {
			int port = Integer.parseInt(line.getOptionValue('p'));
			listen(port);
		} else {
			if (line.hasOption('p') && (line.getArgs().length > 0)) {
				int port = Integer.parseInt(line.getOptionValue('p'));
				connect(line.getArgs()[0], port);
			} else {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("netcat [OPTIONS] <HOST>", options);
			}
		}
	}

	private static void connect(String host, int port) throws Exception {
		System.err.println("Connecting to " + host + " port " + port);
		final Socket socket = new Socket(host, port);
		System.err.println("Connected");

		InputStream input1 = System.in;
		OutputStream output1 = socket.getOutputStream();
		Thread thread1 = new Thread(new Pipe(input1, output1));
		thread1.start();
		thread1.join();
		socket.shutdownOutput();
	}

	private static void listen(int port) throws Exception {
		System.err.println("Listening at port " + port);
		ServerSocket serverSocket = new ServerSocket(port);
		Socket socket = serverSocket.accept();
		System.err.println("Accepted");

		InputStream input2 = socket.getInputStream();
		PrintStream output2 = System.out;
		Thread thread2 = new Thread(new Pipe(input2, output2));
		thread2.start();
		thread2.join();
		socket.shutdownOutput();
	}

}
