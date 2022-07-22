import java.awt.*;
import java.io.*;
import java.net.Socket;

/**
 * Handles communication between the server and one client, for SketchServer
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised Winter 2014 to separate SketchServerCommunicator
 * @author Andrew Cheng
 */
public class SketchServerCommunicator extends Thread {
	private Socket sock;					// to talk with client
	private BufferedReader in;				// from client
	private PrintWriter out;				// to client
	private SketchServer server;			// handling communication for
	private Sketch sketch;
	private boolean running;

	public SketchServerCommunicator(Socket sock, SketchServer server) {
		this.sock = sock;
		this.server = server;
		sketch = new Sketch();
		running = true;
	}

	/**
	 * Sends a message to the client
	 * @param msg to send
	 */
	public void send(String msg) {
		out.println(msg);
	}
	
	/**
	 * Keeps listening for and handling (your code) messages from the client
	 */
	public void run() {
		try {
			System.out.println("someone connected");
			
			// Communication channel
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);

			// Tell the client the current state of the world
			// TODO: YOUR CODE HERE
			String line;
            send(server.getSketch().toString());

            while (running) {
				// Keep getting and handling messages from the client
				// TODO: YOUR CODE HERE
				if ((line = in.readLine()) != null) {

					// for drawing
					if (line.equals("DRAW")) {
						line = in.readLine();
						sketch.add(server.getCounter(), line);
						server.updateSketch(sketch);
					}

					// for moving
					if (line.equals("MOVE")) {
						System.out.println("Moving:");
						line=in.readLine();
						String[] command = line.split(" ");
						String shape = "";

						for(int i = 1; i< command.length; i++) {
							shape += command[i] + " ";
						}

						Shape s = Sketch.defineShape(shape);
						System.out.println(s);
						server.moveShape(Integer.parseInt(command[0]), s);
					}

					// for recoloring
					if (line.equals("RECOLOR")) {
						System.out.println("Recoloring:");
						line=in.readLine();
						String[] command = line.split(" ");
						int id = Integer.parseInt(command[0]);
						Color color = new Color(Integer.parseInt(command[1]));
						server.colorShape(id, color);
					}

					// for deleting
					if (line.equals("DELETE")) {
						line=in.readLine();
						int id = Integer.parseInt(line);
						System.out.println("Deleting " + id);
						server.deleteShape(id);
					}

					server.broadcast(sketch.toString());
				}

				else {
					running = false;
				}
			}

			// Clean up -- note that also remove self from server's list so it doesn't broadcast here
			server.removeCommunicator(this);
			out.close();
			in.close();
			sock.close();
		}

		catch (IOException e) {
			e.printStackTrace();
		}

	}
}
