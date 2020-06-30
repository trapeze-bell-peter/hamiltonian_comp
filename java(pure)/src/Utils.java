import java.io.IOException;

public class Utils {

    public static String waitForInput(String prompt) {
        try {
            System.out.println(prompt);
            while (System.in.available() <= 0) {
                Thread.sleep(10);
            }
            byte[] buf = new byte[System.in.available()];
            System.in.read(buf);
            return new String(buf);
        } catch (IOException e) {
        } catch (InterruptedException e) {
        }
        return "";
    }

}
