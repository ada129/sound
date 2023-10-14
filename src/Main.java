import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import utils.Matrix;
import utils.Utils;

public class Main {


  public static void main(String[] args) {
    byte[][] codeHamming = {{1, 0, 0, 0, 1, 1, 1},
        {0, 1, 0, 0, 1, 1, 0},
        {0, 0, 1, 0, 1, 0, 1},
        {0, 0, 0, 1, 0, 1, 1}};
    byte[] sindroms = {7, 6, 5, 3, 4, 2, 1};
    File file = new File(args[0]);
    File result = new File(args[1]);
    try (FileInputStream in = new FileInputStream(file);
        FileOutputStream out = new FileOutputStream(result)) {
      byte[] input = in.readAllBytes();
      byte[] descrambleBuffer = Utils.deScramble(input);
      byte[] demuxBuffer = Utils.deMux(descrambleBuffer,Integer.parseInt(args[2]));
      byte[] decode = Utils.decode(demuxBuffer, Matrix.transparent(codeHamming, 4, 7), sindroms);
      int position = Utils.findCombination(decode, new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
      System.out.println(position);
      out.write(Arrays.copyOf(decode, position));
      out.write(Arrays.copyOf(input, input.length - 10));
    } catch (IOException ignore) {
    }
  }
}
