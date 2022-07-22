import java.awt.*;
import java.net.*;
import java.util.*;
import java.io.*;

/**
 * A server to handle sketches: getting requests from the clients,
 * updating the overall state, and passing them on to the clients
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised Winter 2014 to separate SketchServerCommunicator
 */
public class SketchServer {
	private ServerSocket listen;						// for accepting connections
	private ArrayList<SketchServerCommunicator> comms;	// all the connections with clients
	private Sketch sketch;								// the state of the world
	private int counter;
	
	public SketchServer(ServerSocket listen) {
		this.listen = listen;
		sketch = new Sketch();
		comms = new ArrayList<SketchServerCommunicator>();
		counter = 0;
	}

	//gets number of shapes so IDs can be given according to newest shape
	public int getCounter(){
		int num = counter;
		counter++;
		return num;
	}


	public Sketch getSketch() {
		return sketch;
	}

	public void moveShape(int id, Shape shape){
		sketch.shapes.put(id,shape);
	}

	public void colorShape(int id, Color color){
		sketch.shapes.get(id).setColor(color);
	}

	public void deleteShape(int id){ sketch.shapes.remove(id); }

	public void updateSketch(Sketch sketch){
		this.sketch.combineSketch(sketch);
	}
	
	/**
	 * The usual loop of accepting connections and firing off new threads to handle them
	 */
	public void getConnections() throws IOException {
		System.out.println("server ready for connections");
		while (true) {
			SketchServerCommunicator comm = new SketchServerCommunicator(listen.accept(), this);
			comm.setDaemon(true);
			comm.start();
			addCommunicator(comm);
		}
	}

	/**
	 * Adds the communicator to the list of current communicators
	 */
	public synchronized void addCommunicator(SketchServerCommunicator comm) {
		comms.add(comm);
	}

	/**
	 * Removes the communicator from the list of current communicators
	 */
	public synchronized void removeCommunicator(SketchServerCommunicator comm) {
		comms.remove(comm);
	}

	/**
	 * Sends the message from the one communicator to all (including the originator)
	 */
	public synchronized void broadcast(String msg) {
		for (SketchServerCommunicator comm : comms) {
			comm.send(msg);
		}
	}
	
	public static void main(String[] args) throws Exception {
		new SketchServer(new ServerSocket(4242)).getConnections();
	}
}
