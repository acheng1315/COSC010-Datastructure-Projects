import java.io.*;
import java.net.Socket;
import java.util.Arrays;

/**
 * Handles communication to/from the server for the editor
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author Chris Bailey-Kellogg; overall structure substantially revised Winter 2014
 * @author Travis Peters, Dartmouth CS 10, Winter 2015; remove EditorCommunicatorStandalone (use echo server for testing)
 * @author Andrew Cheng
 */
public class EditorCommunicator extends Thread {
	private PrintWriter out;		// to server
	private BufferedReader in;		// from server
	protected Editor editor;		// handling communication for

	/**
	 * Establishes connection and in/out pair
	 * @param serverIP
	 * @param editor from editor object
	 */
	public EditorCommunicator(String serverIP, Editor editor) {
		this.editor = editor;
		System.out.println("connecting to " + serverIP + "...");
		try {
			Socket sock = new Socket(serverIP, 4242);
			out = new PrintWriter(sock.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			System.out.println("...connected");
		}
		catch (IOException e) {
			System.err.println("couldn't connect");
			System.exit(-1);
		}
	}

	/**
	 * Sends message to the server
	 * @param msg message to send
	 */
	public void send(String msg) {
		out.println(msg);
	}

	/**
	 * Keeps listening for and handling (your code) messages from the server
	 */
	public void run() {
		try {
			// Handle messages
			// TODO: YOUR CODE HERE
			String line;
			while (true) {

				if ((line = in.readLine()) != null) {

					if (line.equals("Shapes are:")) {
						System.out.println("Incoming sketch...");
						Sketch sketch = new Sketch();
						line=in.readLine();

						while (!line.equals("end")) {
							String[] code = line.split("\t");
							System.out.println(code);
							// parses for the id and other info
							sketch.add(Integer.parseInt(code[0]), code[1]);
							line=in.readLine();
						}

						editor.setSketch(sketch);
						editor.repaint();
						System.out.println(editor.getSketch());
					}
				}
			}
		}

		catch (IOException e) {
			e.printStackTrace();
		}

		finally {
			System.out.println("server hung up");
		}
	}	

	// Send editor requests to the server
	// TODO: YOUR CODE HERE
	
}
