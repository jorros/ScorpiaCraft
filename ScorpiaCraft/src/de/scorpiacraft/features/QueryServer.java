package de.scorpiacraft.features;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import org.bukkit.entity.Player;

import de.scorpiacraft.ScorpiaCraft;
import de.scorpiacraft.obj.SPlayer;

public class QueryServer extends Thread
{
	private final String host;
	private final int port;
	private final ServerSocket listener;

	public QueryServer(String host, int port) throws IOException
	{
		this.host = host;
		this.port = port;

		// Initialize the listener.
		InetSocketAddress address;
		if (host.equals(""))
		{
			address = new InetSocketAddress(port);
		}
		else
		{
			address = new InetSocketAddress(host, port);
		}
		listener = new ServerSocket();
		listener.bind(address);
	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				// Wait for and accept all incoming connections.
				Socket socket = getListener().accept();

				// Create a new thread to handle the request.
				(new Thread(new Request(socket))).start();
			}
		}
		catch (IOException ex)
		{
			ScorpiaCraft.log("Stoppe Statusabfrage");
		}
	}

	public String getHost()
	{
		return host;
	}

	public int getPort()
	{
		return port;
	}

	public ServerSocket getListener()
	{
		return listener;
	}
	
	public final class Request extends Thread
	{
		private final Socket socket;

		public Request(Socket socket)
		{
			this.socket = socket;
		}

		public void run()
		{
			try
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				// Read the request and handle it.
				handleRequest(socket, reader);

				// Finally close the socket.
				socket.close();
			}
			catch(IOException ex)
			{
				ScorpiaCraft.log("Request Probleme");
			}
		}

		private void handleRequest(Socket socket, BufferedReader reader) throws IOException
		{
			String request = reader.readLine();
			// Handle a query request.
			if (request == null)
				return;

			// Handle a standard Minequery request.
			if(request.equalsIgnoreCase("QUERY"))
			{
				ScorpiaCraft m = ScorpiaCraft.p;
				String[] playerList = new String[m.getServer().getOnlinePlayers().length];
				
				for(int i = 0; i < m.getServer().getOnlinePlayers().length; i++)
				{
					playerList[i] = m.getServer().getOnlinePlayers()[i].getName();
				}

				// Build the response.
				StringBuilder resp = new StringBuilder();
				resp.append("SERVERPORT " + m.getServer().getPort() + "\n");
				resp.append("PLAYERCOUNT " + m.getServer().getOnlinePlayers().length + "\n");
				resp.append("MAXPLAYERS " + m.getServer().getMaxPlayers() + "\n");
				resp.append("PLAYERLIST " + Arrays.toString(playerList) + "\n");

				// Send the response.
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				out.writeBytes(resp.toString());
			}
			else if(request.equalsIgnoreCase("QUERY_JSON"))
			{
				ScorpiaCraft m = ScorpiaCraft.p;

				// Build the JSON response.
				StringBuilder resp = new StringBuilder();
				resp.append("{");
				resp.append("\"serverPort\":").append(m.getServer().getPort()).append(",");
				resp.append("\"playerCount\":").append(m.getServer().getOnlinePlayers().length).append(",");
				resp.append("\"maxPlayers\":").append(m.getServer().getMaxPlayers()).append(",");
				resp.append("\"playerList\":");
				resp.append("[");

				// Iterate through the players.
				int count = 0;
				for(Player player : m.getServer().getOnlinePlayers())
				{
					resp.append("\"" + player.getName() + "\"");
					if (++count < m.getServer().getOnlinePlayers().length)
						resp.append(",");
				}

				resp.append("]");
				resp.append("}\n");

				// Send the JSON response.
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				out.writeBytes(resp.toString());
			}
			else if(request.equalsIgnoreCase("VFt8R7KyWejwyc"))
			{
				String name = reader.readLine();
				if(name == null)
					return;
				
				SPlayer sp = ScorpiaCraft.getPlayer(name);
				if(sp == null)
				{
					sp = new SPlayer(name);
				}
				
				sp.addVIPMonth();
				ScorpiaCraft.save("player");
				ScorpiaCraft.log(name + " hat 1 Monat VIP erhalten");
			}
		}
	}
}
