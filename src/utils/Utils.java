package utils;

import java.util.Arrays;

public class Utils {

  public static byte[] deScramble(byte[] array) {
    int register = 81;
    int size = array.length * 8;
    byte[] result = new byte[size];
    int resultPos = 0;
    for (int i = 0; i < size; i++) {
      byte bit = (byte) (Bit.getBit(register, 6) ^ Bit.getBit(register, 7));
      Bit.setBit(result, (byte) (Bit.getBit(array, i) ^ bit), resultPos);
      resultPos++;
      register = register << 1;
      register = Bit.setBit(register, bit, 0);
    }
    return result;
  }

  public static byte[] deMux(byte[] array, int n) {
    int size = array.length * 8;
    byte[] mix = new byte[n * n];
    byte[] result = new byte[2 * size];
    int muxPos = 0;
    int numberBit = 0;
    int resultPos = 0;
    while (numberBit < size) {
      mix[muxPos] = Bit.getBit(array, numberBit);
      muxPos++;
      if (muxPos >= n * n) {
        muxPos = 0;
        for (int i = 0; i < n * n; i++) {
          Bit.setBit(result, mix[muxPos], resultPos);
          muxPos = (muxPos + n) >= n * n ? muxPos % n + 1 : muxPos + n;
          resultPos++;
        }
        muxPos = 0;
      }
      numberBit++;
    }
    return Arrays.copyOf(result, (int) Math.ceil((double) (resultPos) / 8));
  }

  public static byte[] decode(byte[] g, byte[] h, byte[] syndromes) {
    byte[] vectors = getErrorVectors(syndromes);
    int size = g.length * 8 - ((g.length * 8) % 7);
    byte[] result = new byte[size];
    int pos = 0;
    for (int i = 0; i < size; i += 7) {
      for (int j = i; j < i + 7; j++) {
        Bit.setBit(result, Bit.getBit(g, j), j);
        pos++;
      }
      byte syndromeTemp = 0;
      int tempPos = pos - 7;
      for (int j = 0; j < 4; ) {
        int sum = Bit.getBit(result, tempPos) & h[j];
        sum ^= Bit.getBit(result, tempPos + 1) & h[j + 4];
        sum ^= Bit.getBit(result, tempPos + 2) & h[j + 8];
        sum ^= Bit.getBit(result, tempPos + 3) & h[j + 12];
        sum ^= Bit.getBit(result, tempPos + 4) & h[j + 16];
        sum ^= Bit.getBit(result, tempPos + 5) & h[j + 20];
        sum ^= Bit.getBit(result, tempPos + 6) & h[j + 24];
        syndromeTemp = Bit.setBit(syndromeTemp, sum, j);
        j += 1;
      }
      if (syndromeTemp != 0) {
        for (int j = 0; j < 7; j++) {
          if (syndromeTemp == vectors[j]) {
            Bit.setBit(result, (byte) (Bit.getBit(result, tempPos + j) ^ 1), tempPos + j);
            break;
          }
        }
      }
      pos -= 3;
    }
    System.out.println(Arrays.toString(Arrays.copyOf(result, pos / 8)));
    return Arrays.copyOf(result, pos / 8);
  }

  public static int findCombination(byte[] array1, byte[] array2) {
    int pos;
    for (int i = 0; i < array1.length; i++) {
      if (array1[i] == array2[0]) {
        pos = 1;
        for (int j = 1; j < array2.length; j++) {
          if (array1[j + i] != array2[j]) {
            break;
          }
          pos++;
        }
        if (pos == array2.length) {
          return i;
        }
      }
    }
    return -1;
  }

  private static byte[] getErrorVectors(byte[] syndromes) {
    byte[] error = new byte[]{1, 0, 0, 0, 0, 0, 0,
        0, 1, 0, 0, 0, 0, 0,
        0, 0, 1, 0, 0, 0, 0,
        0, 0, 0, 1, 0, 0, 0,
        0, 0, 0, 0, 1, 0, 0,
        0, 0, 0, 0, 0, 1, 0,
        0, 0, 0, 0, 0, 0, 1};
    byte[] vectors = new byte[7];
    for (int i = 0; i < error.length; ) {
      vectors[i / 7] = Matrix.multiplication(error,syndromes,i,syndromes.length,1,8);
      i += 7;
    }
    return vectors;
  }

}
