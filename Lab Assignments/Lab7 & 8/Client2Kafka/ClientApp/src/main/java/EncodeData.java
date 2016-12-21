//import com.migcomponents.migbase64.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.codec.binary.Base64;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by harsha on 10/18/16.
 */
public class EncodeData {
    public static String EncodeToString(String filePath){
        String encodedString;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
        } catch (Exception e) {
            // TODO: handle exception
        }
        byte[] bytes = null;
//        byte[] buffer = new byte[8192];
//        int bytesRead;
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            bytes = IOUtils.toByteArray(inputStream);
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                output.write(buffer, 0, bytesRead);
//
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        bytes = output.toByteArray();
        encodedString = Base64.encodeBase64String(bytes);
        return encodedString;
    }
}
