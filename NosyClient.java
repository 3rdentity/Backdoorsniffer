import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class NosyClient {

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

	public static void main(String[] args) throws UnknownHostException,
			IOException {
		if(args.length < 2){
			throw new IllegalArgumentException("Usage <program name> <host address> <port number>");
		}
		String command;
		String sniffer;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int port = Integer.parseInt(args[1]);
		Socket client = new Socket(args[0], port);
		PrintWriter commandToServer = new PrintWriter(client.getOutputStream(),
				true); 
		BufferedReader brServer = new BufferedReader(new InputStreamReader(
				client.getInputStream()));
		try {
			sniffer = brServer.readLine(); // gets the menu from the server
			for (; sniffer.length() != 0; sniffer = brServer.readLine()) {
				System.out.println(sniffer);
			}
		} catch (Exception ex) {
			System.out.println("SERVER ERROR . . . . . .");
			ex.printStackTrace();
			System.exit(port);
		}
		System.out.print("Enter command >");
		while ((command = br.readLine()) != null) {
			String format = String.format("%s", command);
			commandToServer.println(format);
			try {
				//output the server information
				sniffer = brServer.readLine();
				String sniff = String.format("%s\n",sniffer);
				System.out.print(sniff); 
			}catch (Exception ex) {
				System.out.println("Server response error....");
				ex.printStackTrace();
			}
			if (command.equalsIgnoreCase("exit")) {
				System.out.println("Client is shutting down . . . . . .");
				break;
			}
			System.out.print("Enter command >");
		}
		client.close();
	}
}
