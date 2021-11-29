
import java.io.*;

public class ioTest {
    public static void main(String[] args) throws IOException {
        File a = new File("/Users/assertor/Desktop/dj.xml");
        FileInputStream fis = new FileInputStream(a);
        InputStreamReader reader = new InputStreamReader(fis,"utf-8");
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line =null;
        StringBuilder sb  = new StringBuilder();
        while ((line=bufferedReader.readLine())!=null){
            sb.append(line).append("\n");
        }
        System.out.println(sb);
    }
}
