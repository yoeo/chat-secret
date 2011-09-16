package chat;

/**
 *
 * @author YDéo
 */
import java.net.*;
import java.io.*;
import javax.swing.JTextPane;
public class Client extends Thread
{
	final static int port = 9632;
    Socket socket;
    Lecteur lecteur;
    JTextPane pane;
	public Client(JTextPane jTextPane1)
	{
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
            lecteur.start();
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
        System.out.println("Le client écrit : "+ message);
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

    void publier(String message)
    {
        System.out.println("Le client reçoit : "+ message);
        pane.setText(message + '\n' + pane.getText());
    }
}
