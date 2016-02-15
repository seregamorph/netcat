package netcat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Pipe implements Runnable {
	private static final int BUFFER_SIZE = 8192;

	private final InputStream input;
	private final OutputStream output;

	public Pipe(InputStream input, OutputStream output) {
		this.input = input;
		this.output = output;
	}

	@Override
	public void run() {
		try {
			byte[] bb = new byte[BUFFER_SIZE];
			int read;
			while ((read = input.read(bb)) > -1) {
				output.write(bb, 0, read);
				output.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
