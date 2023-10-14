package utils;

public class Bit {

  static byte getBit(int value, int pos) {
    return (byte) ((value >> pos) & 1);
  }

  static byte setBit(int oldValue, int number, int pos) {
    return (byte) ((number == 0 && getBit(oldValue, pos) == 1) ? oldValue ^ (1 << pos)
        : oldValue | (number << pos));
  }

  static byte getBit(byte[] array, int pos) {
    if (pos >= array.length * 8) {
      return 0;
    }
    if (pos > 7) {
      return getBit(array[pos / 8], pos % 8);
    }
    return getBit(array[0], pos);
  }

  static void setBit(byte[] array, byte value, int pos) {
    if (pos < array.length * 8) {
      if (pos > 7) {
        array[pos / 8] = setBit(array[pos / 8], value, pos % 8);
      } else {
        array[0] = setBit(array[0], value, pos);
      }
    }
  }

}
