package chat;

/**
 *
 * @author YDéo
 */
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JTextPane;
public class Client extends Thread
{
	final static int port = 9632;
    Socket socket;
    Lecteur lecteur;
    JTextPane pane;

	final static String PING = "[PING]";
    public boolean afficherCripte = false;
	RSA cryptage = null;
	ArrayList<String> cleListe = null;

	public Client(JTextPane jTextPane1)
	{
		cleListe = new ArrayList<String>();
        // System.out.println(cryptage.toString());
        pane = jTextPane1;
        lancer ();
	}

    public void lancer ()
    {
		try
		{
            System.out.println("Lancement du client!");

			InetAddress serveur = InetAddress.getByName("127.0.0.1");
			socket = new Socket(serveur, port);

            System.out.println("Socket : " + socket.toString());
            lecteur = new Lecteur (socket, this);
			cryptage = new RSA (512);
            lecteur.start();
			ping();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
    }
    public void arreter ()
    {
        lecteur.interrupt();
    }

    public void ecrire (String message)
    {
		String messageFinal = "";
        System.out.println("Le client écrit : " + message + " " + cleListe.size());
		int i = 0;
		try
		{
			PrintStream out = new PrintStream(socket.getOutputStream());
			for (String id : cleListe)
			{
				String[] lst = id.split(" ");
				String cle = lst[0];
				String mod = lst[1];
				messageFinal = cryptage.crypteMessage(message, cle, mod) +
						" " + cle + " " + mod;

				if (afficherCripte)
				{
					pane.setText("Message Crypté : " + messageFinal + '\n' + pane.getText());
				}

				// System.out.println("Publication  num : " + i + ", brute : " + messageFinal);
				i++;
				out.println(messageFinal);
				out.flush();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
    }

    synchronized void publier(String m)
    {
		String messageFinal = null;
        String message = "";
        String [] lst = m.split(" ");

		System.out.println("Brute : " + m);
		if (lst.length == 3)
        {
			message = lst[0];
			String cle = lst[1];
			String mod = lst[2];
			String id = cle + " " + mod;
            if (message.equals(PING))
			{
				pingBack(id);
			}
			else
			{
				try
				{
					if (cryptage.getCleePublique().equals(id))
					{
						messageFinal = cryptage.decrypteMessage(message);
					}
				}
				catch (Exception e)
				{
					messageFinal = m;
					System.out.println(e.getMessage());
				}
			}
		}
		else
		{
			messageFinal = m;
		}
		
		if (null != messageFinal)
		{
			System.out.println("Le client reçoit : " + messageFinal);
			pane.setText(messageFinal + '\n' + pane.getText());
		}

    }

	private void ping()
	{
        String message = PING + " " + cryptage.getCleePublique();
		System.out.println("Ping Clee = " + message);
		try
		{
            PrintStream out = new PrintStream(socket.getOutputStream());
            out.println(message);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void pingBack(String id)
	{
		if (!cleListe.contains(id))
		{
			System.out.println("Ping back : " + id);
			cleListe.add(id);
			System.out.println("Nbr cles : " + cleListe.size());
			ping();
		}
	}
}
