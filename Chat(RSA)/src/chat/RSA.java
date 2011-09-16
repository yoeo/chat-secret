package chat;

/**
 * L' algorithme RSA un peu modifié.
 * la fonction de criptage à été modifiée pour cripter
 * un message avec une clée publique distante.
 * 
 * @author YDéo
 */
import java.math.BigInteger;
import java.security.SecureRandom;

class RSA
{
	private BigInteger modulo;
	private BigInteger privee;
	private BigInteger publique;

	public RSA(int bitlen)
	{
		SecureRandom aleatoire = new SecureRandom();
		BigInteger p = new BigInteger(bitlen / 2, 100, aleatoire);
		BigInteger q = new BigInteger(bitlen / 2, 100, aleatoire);
		modulo = p.multiply(q);
		BigInteger m = (p.subtract(BigInteger.ONE))
					   .multiply(q.subtract(BigInteger.ONE));
		publique = new BigInteger("3");
		while(m.gcd(publique).compareTo(BigInteger.ONE) > 0)
		{
			publique = publique.add(new BigInteger("2"));
		}
		privee = publique.modInverse(m);
	}

    public String crypteMessage (String s, String publiqueText, String moduloText)
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
	    BigInteger message = new BigInteger(bigint);
		BigInteger publiqueDistant = new BigInteger(publiqueText);
		BigInteger moduloDistant = new BigInteger(moduloText);
	    BigInteger encrypte = message.modPow(publiqueDistant, moduloDistant);

	    return "" + encrypte;
	}

	public String decrypteMessage (String encrypte)
	{
		// Décryptage RSA
	    BigInteger bigint = new BigInteger (encrypte);
	    BigInteger decrypte = bigint.modPow(privee, modulo);
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

	@Override
	public String toString()
    {
        String s = "";
        s += "clée publique = " + privee + "\n";
        s += "clée privée   = " + publique + "\n";
        s += "modulo        = " + modulo;
        return s;
    }

    public String getCleePublique ()
    {
        return "" + publique + " " + modulo;
    }

	public boolean estCleePrivee(String priveeCandidat, String moduloCandidat)
	{
		return (new BigInteger(priveeCandidat).compareTo(privee) == 0) &&
			   (new BigInteger(moduloCandidat).compareTo(modulo) == 0);
	}
}
