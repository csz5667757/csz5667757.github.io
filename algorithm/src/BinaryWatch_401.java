import java.util.ArrayList;
import java.util.List;

/**
 * 401. 二进制手表
 * <p>
 * description easy
 */
public class BinaryWatch_401 {
    public List<String> readBinaryWatch(int turnedOn) {
        List<String> times = new ArrayList<>();
        if (turnedOn > 8) return new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 60; j++) {
                if (Integer.bitCount(i) + Integer.bitCount(j) == turnedOn) {
                    String min;
                    if (j<10) {
                         min ="0"+j;
                    }else {
                        min = String.valueOf(j);
                    }
                    times.add(i + ":" + min);
                }
            }
        }
        return times;
    }

    public static void main(String[] args) {
        BinaryWatch_401 binaryWatch_401 = new BinaryWatch_401();
        System.out.println(binaryWatch_401.readBinaryWatch(8));
    }
}
