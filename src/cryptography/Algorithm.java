package cryptography;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
import java.security.Security;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.jcajce.provider.digest.SHA3.DigestSHA3;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

/**
 * Algorithm is a class  that offers a set of algorithms to obtain the hash code.
 * 
 * @author Enrique Morales Montero�
 * @since 2/4/2019
 * @version 3/4/2019 
 */
public class Algorithm {
	
	final static String tempFolder = "./"; 
	
	/**
	 * Function that generates a hash code.  This has code is needed to identify our documents 
	 * 
	 * @param url_ixbrl
	 * @return hash_code (String)
	 */
	public static String generateSHA3_256(String url_ixbrl) {
		
		String hash_code = "";
		
		try {
			
			URL url = new URL(url_ixbrl);					// Url.
			
			// Connection.

			URLConnection urlCon = url.openConnection();	// Connection: open.
			
			InputStream is = urlCon.getInputStream();		// Getting the InputStream.
			FileOutputStream fos = new FileOutputStream(tempFolder + "/temp.xml"); // Opening the file in the local system.
			
			// R/W in local.

			byte[] array = new byte[10000]; 				// Temporal buffer.

			int leido = is.read(array);

			while (leido > 0) {								// we go through the  file.
				fos.write(array, 0, leido);
				leido = is.read(array);
			}

			// it closes the file and the url conection since we finish the process.

			is.close();
			fos.close();

			// Using an external library, It Calculates the SHA-3 256 hash code 
			// 
			
			File file = new File(tempFolder + "/temp.xml");		// XBRL report file.

			// Getting bytes.

			Path filePath = Paths.get(file.getAbsolutePath());	// Path.
			byte[] fileBytes = Files.readAllBytes(filePath);	// Bytes.

			
			Security.addProvider(new BouncyCastleProvider());	// BouncyCastle hash generator.
			DigestSHA3 digestSHA3 = new SHA3.Digest256();		// SHA3 256.
			digestSHA3.update(fileBytes);						// Getting the hash code.
			hash_code = Hex.toHexString(digestSHA3.digest());	// To string...

			// We add an error control
			
			if (hash_code.equals("a7ffc6f8bf1ed76651c14756a061d662f580ff4de43b49fa82d80a4b80f8434a")) {
				System.out.print(" - Attention: Empty XML file! - ");
			}
			
		} catch (MalformedURLException e) {e.printStackTrace();
		} catch (UnknownHostException e) {e.printStackTrace();
		} catch (IOException e) {e.printStackTrace();
		//} catch (NoSuchAlgorithmException e) {e.printStackTrace();
		}

		return hash_code;
	}

	/**
	 * The Method functionality is to calculate the hexadecimal of an array of byte.
	 * We use a external library as a replacement for this method
	 * It should  be reused in the future.
	 * 
	 * @deprecated
	 * @param sha3Bytes (byte[])
	 * @return Hexadecimal (String)
	 */
	@SuppressWarnings("unused")
	private static String getHexadecimal(byte[] sha3Bytes) {
		
		String hex = "";
		
		for (byte b : sha3Bytes) {
			String h = Integer.toHexString(b & 0xFF);
			if (h.length() == 1) {hex += "0";}
			
			hex += h;
		}
		
		return hex.toUpperCase();
	}
}