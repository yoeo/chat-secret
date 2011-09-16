package chat;
import java.net.*;
import java.io.*;

/**
 *
 * @author YDéo
 */

public class Lecteur extends Thread
{
    Client client;
    Socket socket;

    public Lecteur (Socket s, Client c)
    {
        client = c;
        socket = s;
    }

    @Override
    public void run ()
    {
        String message;
        boolean serveurActif = true;
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (serveurActif)
			{
                message = in.readLine();
                if (message == null)
                {
                    serveurActif = false;
                    message = "Serveur déconnecté!";
                    System.out.println("Serveur déconnecté!");
                }
                client.publier (message);
			}
		}
		catch (Exception e)
		{
			serveurActif = false;
			System.out.println(e.getMessage());
		}
    }
}
