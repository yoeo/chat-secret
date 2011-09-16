/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chat;

/**
 *
 * @author YDéo
 */
import java.math.BigInteger;
import java.security.SecureRandom;
public class RSA
{
    private final static BigInteger un = new BigInteger("1");
    private final static SecureRandom aleatoire = new SecureRandom();
    private BigInteger clePrivee;
    private BigInteger clePublique;
    private BigInteger modulo;


    public RSA()
    {
        // N représente le nombre de bit des clés publique et privée

        int N = 256;

        BigInteger p = BigInteger.probablePrime(N/2, aleatoire);
        BigInteger q = BigInteger.probablePrime(N/2, aleatoire);
        BigInteger phi = (p.subtract(un)).multiply(q.subtract(un));
        modulo = p.multiply(q);

        clePublique = new BigInteger("65537"); // common value in practice = 2^16 + 1
        clePrivee = clePublique.modInverse(phi);
    }

    public RSA(String cle)
    {
        // N représente le nombre de bit des clés publique et privée
        clePublique = new BigInteger("65537"); // common value in practice = 2^16 + 1
        clePrivee = new BigInteger (cle);
    }

    @Override
    public String toString()
    {
        String s = "";
        s += "clée publique = " + clePublique + "\n";
        s += "clée privée   = " + clePrivee + "\n";
        s += "modulo        = " + modulo;
        return s;
    }

    public String crypteMessage (String s)
	{
    	// Codage de la chaîne
	    String bigint = "";
	    for (int i = 0; i < s.length(); i++)
	    {
	        String ch = "" + s.codePointAt(i);
	        while (ch.length() < 3)
	        {
	            ch = "0" + ch;
	        }
	        bigint += ch;
	    }

	    // Cryptage RSA
	    BigInteger message = new BigInteger (bigint);
	    BigInteger encrypte = message.modPow(clePublique, modulo);

	    return "" + encrypte;
	}

	public String decrypteMessage (String bigint)
	{
		// Décryptage RSA
	    BigInteger encrypte = new BigInteger (bigint);
	    BigInteger decrypte = encrypte.modPow(clePrivee, modulo);
	    String s = "" + decrypte;

	    // Normalisation
	    while ((s.length() % 3) != 0)
	    {
	    	s = "0" + s;
	    }

	    // Décodage de la chaîne
	    String message = "";
	    for (int i = 0; i < (s.length() - 2); i+=3)
	    {
	        char ch = (char)Integer.parseInt(s.substring(i, i + 3));
	        message += ch;
	    }

	    return message;
	}
}