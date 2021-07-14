package vn.edu.hust.soict.thuanhq.digitalsignatureqrcode;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA
{

    private int keyLength = 1024;
    private SecureRandom     rand;
    private BigInteger p;
    private BigInteger q;
    private BigInteger n;
    private BigInteger lambda;
    private BigInteger e;
    private BigInteger d;
    private BigInteger[] publicKey = new BigInteger[2];
    private BigInteger[] privateKey = new BigInteger[2];
    private final BigInteger one = new BigInteger("1");

    public RSA()
    {
        rand = new SecureRandom();
        p = BigInteger.probablePrime(keyLength, rand);
        q = BigInteger.probablePrime(keyLength, rand);
        n = p.multiply(q);
        lambda = p.subtract(one).multiply(q.subtract(one));
        e = BigInteger.probablePrime(keyLength / 2, rand);
        while (e.intValue()<lambda.intValue() && (lambda.gcd(e).intValue()-one.intValue()) > 0){
            e.add(one);
        }
        d = e.modInverse(lambda);
        publicKey[0]=e;
        publicKey[1]=n;

        privateKey[0]=d;
        privateKey[1]=n;

    }
    public byte[] encrypt(BigInteger d, BigInteger n, byte[] message)
    {
        return (new BigInteger(message)).modPow(d, n).toByteArray();
    }

    public byte[] decrypt(BigInteger e, BigInteger n, byte[] message)
    {
        return (new BigInteger(message)).modPow(e, n).toByteArray();
    }

    public String toString(byte[] cipher)
    {
        String s = "";
        for (byte b : cipher)	{
            s += Byte.toString(b);
        }
        return s;
    }

    public BigInteger[] getPublicKey() {
        return publicKey;
    }

    public BigInteger[] getPrivateKey() {
        return privateKey;
    }

}