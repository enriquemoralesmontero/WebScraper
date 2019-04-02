package cryptography;

import java.security.Security;
import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.jcajce.provider.digest.SHA3.DigestSHA3;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

public class Algorithm {
	
	public static String generateSHA3_256(String url_ixbrl) {
		
		String hash_code = "";
		
		try {	

			// Calculate the SHA-3 256 hash code.
			
			Security.addProvider(new BouncyCastleProvider());
			final DigestSHA3 digestSHA3 = new SHA3.Digest256();
			digestSHA3.update(url_ixbrl.getBytes());
			hash_code = Hex.toHexString(digestSHA3.digest());

		} catch (Exception ex) {
			System.err.println(ex);
			return "";
		}

		return hash_code;
	}
}