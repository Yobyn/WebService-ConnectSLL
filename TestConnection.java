import java.io.*; 
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.codec.binary.Base64;


public class TestConnection {

    public static void main(String[] args) {
        try {
            new TestConnection().testConnection();
        } catch (IOException ex) {
            Logger.getLogger(TestConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private TrustManager[ ] get_trust_mgr() {
     TrustManager[ ] certs = new TrustManager[ ] {
        new X509TrustManager() {
           public X509Certificate[ ] getAcceptedIssuers() { return null; }
           public void checkClientTrusted(X509Certificate[ ] certs, String t) { }
           public void checkServerTrusted(X509Certificate[ ] certs, String t) { }
         }
      };
      return certs;
    }
    
    private void testConnection() throws MalformedURLException, IOException {
        
        String authStringEnc = "";
        TrustManager[] trustAllCerts = get_trust_mgr();
        
        try {
			String name = ".....";
			String password = "....";
			
			String authString = name + ":" + password;
			System.out.println("auth string: " + authString);
			byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
			
			authStringEnc = new String(authEncBytes);
			System.out.println("Base64 encoded auth string: " + authStringEnc);
			
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
        
        String webPage = "https://...../rest/....";
			
        URL url = new URL(webPage);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

        urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
        InputStream is = urlConnection.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);

        int numCharsRead;
        char[] charArray = new char[1024];
        StringBuilder sb = new StringBuilder();
        while ((numCharsRead = isr.read(charArray)) > 0) {
            sb.append(charArray, 0, numCharsRead);
        }
        //String result = sb.toString();

        System.out.println("*** BEGIN ***");
        //System.out.println(result);
        System.out.println("*** END ***");
        
    }
}
