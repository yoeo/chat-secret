/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chatserveur;
import java.net.*;
import java.io.*;

/**
 *
 * @author ydeo
 */
public class SocketReadWriteTest
{
	public static void main(String[] args)
	{
		Thread server = new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					ServerSocket socketServeur = new ServerSocket(9632);
					Socket s = socketServeur.accept();
					BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
					PrintStream out = new PrintStream(s.getOutputStream());

					System.out.println("s : " + in.readLine());
					out.println("server1");
					out.println("server2");
					System.out.println("s : " + in.readLine());
				}
				catch (Exception ex)
				{
					System.out.println(ex.getMessage());
				}
			}
		});

		Thread client = new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					Socket s = new Socket(InetAddress.getByName("127.0.0.1"), 9632);
					BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
					PrintStream out = new PrintStream(s.getOutputStream());

					out.println("client1");
					out.println("client2");
					System.out.println("c : " + in.readLine());
				}
				catch (Exception ex)
				{
					System.out.println(ex.getMessage());
				}
			}
		});

		server.start();
		client.start();
		try
		{
			server.join();
			client.join();
		}
		catch (InterruptedException ex)
		{
			System.out.println(ex.getMessage());
		}
	}
}
