package chatserveur;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
/**
 *
 * @author YDéo
 */
public class Serveur  extends Thread
{
	final int port = 9632;
    public boolean actif = false;
    private ArrayList listeSocket = null;
    private ArrayList listeLecteur = null;

    public Serveur()
	{
        listeSocket = new ArrayList();
        listeLecteur = new ArrayList();
	}

    @Override
	public void run()
	{
		lancerServeur ();
	}

    @Override
    public void interrupt ()
    {
        arreterServeur ();
        super.interrupt ();
    }

    public void arreterServeur()
    {
        actif = false;
        for (int i = 0; i < listeLecteur.size(); i++)
        {
            Lecteur l = (Lecteur)listeLecteur.get(i);
            l.interrupt();
        }
        for (int i = 0; i < listeSocket.size(); i++)
        {
            Socket s = (Socket)listeSocket.get(i);
            try
            {
                s.close();
            }
            catch (Exception e)
            {
    			e.printStackTrace();
            }
        }
        listeLecteur.clear();
        listeSocket.clear();
        System.out.println("Serveur arreté");
    }

	public void lancerServeur ()
	{
        System.out.println("Serveur en Lancement...");
        actif = true;
		try
		{
			ServerSocket socketServeur = new ServerSocket(port);
			System.out.println("Serveur lancé");
			while (actif)
			{
				Socket socketClient = socketServeur.accept();
                ajouterClient (socketClient);
                System.out.println("Client connecté");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

    private void ajouterClient(Socket client)
    {
        Lecteur l = new Lecteur (client, this);
        listeSocket.add(client);
        listeLecteur.add(l);
        l.start();
    }

    synchronized void publier(String message)
    {
        System.out.println("Serveur publie le message : " + message);
        for (int i = 0; i < listeSocket.size(); i++)
        {
            Socket client = (Socket) listeSocket.get (i);
			//new PrintStream(client.getOutputStream()).
            System.out.println("Publie le message vers : " + client.toString());
            try
            {
                PrintStream out = new PrintStream(client.getOutputStream());
                out.println (message);
				out.flush();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("Message publié");
    }
}

