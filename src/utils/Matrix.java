package utils;


public class Matrix {

  public static byte[] transparent(byte[][] array, int n, int k) {
    int pos = 0;
    byte[] revers = new byte[n * k];
    for (int i = 0; i < k; i++) {
      for (int j = 0; j < n; j++) {
        revers[pos] = array[j][i];
        pos++;
      }
    }
    return revers;
  }

  public static byte multiplication(byte[] array1, byte[] array2, int pos1, int size, int shift1,
      int shift2) {
    byte value = 0;
    size /= 7;
    for (int j = 0; j < size; ) {
      int bit = array1[pos1] & Bit.getBit(array2, j);
      bit ^= array1[pos1 + shift1] & Bit.getBit(array2, j + shift2);
      bit ^= array1[pos1 + 2 * shift1] & Bit.getBit(array2, j + 2 * shift2);
      bit ^= array1[pos1 + 3 * shift1] & Bit.getBit(array2, j + 3 * shift2);
      bit ^= array1[pos1 + 4 * shift1] & Bit.getBit(array2, j + 4 * shift2);
      bit ^= array1[pos1 + 5 * shift1] & Bit.getBit(array2, j + 5 * shift2);
      bit ^= array1[pos1 + 6 * shift1] & Bit.getBit(array2, j + 6 * shift2);
      value = Bit.setBit(value, bit, j);
      j += 1;
    }
    return value;
  }
}
