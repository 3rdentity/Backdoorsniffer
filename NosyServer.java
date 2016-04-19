import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class NosyServer {
	public static void printSocketInformation(Socket socket) {
		try {
			System.out.format("Port:                 %s\n", socket.getPort());
			System.out.format("Canonical Host Name:  %s\n", socket
					.getInetAddress().getCanonicalHostName());
			System.out.format("Host Address:         %s\n\n", socket
					.getInetAddress().getHostAddress());
			System.out.format("Local Address:        %s\n",
					socket.getLocalAddress());
			System.out.format("Local Port:           %s\n",
					socket.getLocalPort());
			System.out.format("Local Socket Address: %s\n\n",
					socket.getLocalSocketAddress());
			System.out.format("Receive Buffer Size:  %s\n",
					socket.getReceiveBufferSize());
			System.out.format("Send Buffer Size:     %s\n\n",
					socket.getSendBufferSize());
			System.out.format("Keep-Alive:           %s\n",
					socket.getKeepAlive());
			System.out.format("SO Timeout:           %s\n",
					socket.getSoTimeout());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		//System.out.println(args.length +" "+ args[0]);
		if(args.length < 1){
			throw new IllegalArgumentException("Usage java <program name> <port number>");
		}
		int port =  Integer.parseInt(args[0]);
		ServerSocket server = new ServerSocket(port);
		String request = null;
		String menu = null;
		menu = "==================================== Menu =====================================\n";
		StringBuilder sb = new StringBuilder(menu);
		sb.append("date  - print the date and time of the server's system \n");
		sb.append("timezone  - print the time zone of the server's system \n");
		sb.append("Osname  - print the name of the server's operating system (OS) \n");
		sb.append("Osversion  - print the version number of server's (OS) \n");
		sb.append("user  - print the name of the user logged onto (i.e running) the server \n");
		sb.append("exit  - exit the program \n");
		sb.append("=============================================================================\n");

		BufferedReader fromClient = null;
		PrintWriter dataToClientFromServer = null;
		while (true) {
			Socket connection = server.accept();
			dataToClientFromServer = new PrintWriter(
					connection.getOutputStream(), true);
			dataToClientFromServer.println(sb.toString());
			dataToClientFromServer.flush();
			fromClient = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			try {
				while ((request =  fromClient.readLine()) != null) {
					String format = String.format("%s", request);
					//System.out.println("FROM TCP CLIENT: "
							//+ format);
					if (request.equalsIgnoreCase("exit")) {
						// cleaning and covering our tracks
						System.out.println("Server is shutting down . . . . . . ");
						server.close();
						System.exit(0);
					}else if (request.equalsIgnoreCase("user")) {
						dataToClientFromServer.println(System
								.getProperty("user.name"));
						dataToClientFromServer.flush();
					} 
					else if (request.toString()
							.equalsIgnoreCase("date")) {
						DateFormat dateFormat = new SimpleDateFormat(
								"yyyy/MM/dd");
						Calendar date = Calendar.getInstance();
						dataToClientFromServer.println(dateFormat.format(date
								.getTime()));
						dataToClientFromServer.flush();
					}else if (request.toString()
							.equalsIgnoreCase("timezone")) {
						dataToClientFromServer.println(TimeZone.getDefault()
								.getDisplayName());
						dataToClientFromServer.flush();
					} else if (request.toString()
							.equalsIgnoreCase("osname")) {
						dataToClientFromServer.println(System
								.getProperty("os.name"));
						dataToClientFromServer.flush();
					}else if (request.toString()
							.equalsIgnoreCase("osversion")) {
						dataToClientFromServer.println(System
								.getProperty("os.version"));
						dataToClientFromServer.flush();
					} else {
						dataToClientFromServer
								.println("Unknown Command try again . . . . ");
						dataToClientFromServer.flush();
					}
				}
			} catch (Exception ex) {
				server.close();
				dataToClientFromServer.println(" Error from server . . . \n");
				dataToClientFromServer.flush();
				ex.printStackTrace();
			}
		}
	}

}
