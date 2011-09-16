package chatserveur;
import java.net.*;
import java.io.*;

/**
 *
 * @author YDéo
 */
public class Lecteur extends Thread
{
    Socket client;
    Serveur serveur;

    public Lecteur (Socket c, Serveur s)
    {
        client = c;
        serveur = s;
    }
    
    @Override
    public void run ()
    {
        String message;
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			while (true)
			{
                message = in.readLine();

                if (message == null)
                {
                    message = "Serveur déconnecté!";
                    System.out.println("Serveur déconnecté!");
                    break;
                }
				//System.out.println("Brute : " + message);
				//if (message.contains("PING"))
					serveur.publier (message);
            }
        }
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
    }
}
