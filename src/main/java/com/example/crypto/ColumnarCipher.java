package com.example.crypto;

import java.util.Arrays;

public class ColumnarCipher {

  public String encrypt(String input, String keyRaw) {
    String text = filterEn(input);
    String key = filterEn(keyRaw);
    if (key.isEmpty()) throw new IllegalArgumentException("Key must contain A-Z");

    int m = key.length();
    int n = (int) Math.ceil((double) text.length() / m);

    char[][] grid = new char[n][m];
    int idx = 0;
    for (int r = 0; r < n; r++) {
      for (int c = 0; c < m; c++) {
        grid[r][c] = (idx < text.length()) ? text.charAt(idx++) : 0; // 0 = пусто
      }
    }

    int[] order = columnOrder(key);

    StringBuilder out = new StringBuilder(text.length());
    for (int k = 0; k < m; k++) {
      int col = order[k];
      for (int r = 0; r < n; r++) {
        char ch = grid[r][col];
        if (ch != 0) out.append(ch);
      }
    }
    return out.toString();
  }

  public String decrypt(String input, String keyRaw) {
    String cipher = filterEn(input);
    String key = filterEn(keyRaw);
    if (key.isEmpty()) throw new IllegalArgumentException("Key must contain A-Z");

    int m = key.length();
    int L = cipher.length();
    int n = (int) Math.ceil((double) L / m);

    int full = L / m;
    int rem = L % m;

    int[] colLen = new int[m];
    for (int c = 0; c < m; c++) {
      colLen[c] = full + (c < rem ? 1 : 0);
    }

    int[] order = columnOrder(key);

    char[][] cols = new char[m][];
    int pos = 0;
    for (int k = 0; k < m; k++) {
      int col = order[k];
      int len = colLen[col];
      cols[col] = cipher.substring(pos, pos + len).toCharArray();
      pos += len;
    }

    StringBuilder out = new StringBuilder(L);
    for (int r = 0; r < n; r++) {
      for (int c = 0; c < m; c++) {
        if (r < cols[c].length) out.append(cols[c][r]);
      }
    }
    return out.toString();
  }

  private static String filterEn(String s) {
    StringBuilder b = new StringBuilder(s.length());
    for (int i = 0; i < s.length(); i++) {
      char ch = s.charAt(i);
      if (ch >= 'a' && ch <= 'z') ch = (char) (ch - 32);
      if (ch >= 'A' && ch <= 'Z') b.append(ch);
    }
    return b.toString();
  }


  private static int[] columnOrder(String key) {
    int m = key.length();
    Integer[] idx = new Integer[m];
    for (int i = 0; i < m; i++) idx[i] = i;

    Arrays.sort(idx, (a, b) -> {
      char ca = key.charAt(a), cb = key.charAt(b);
      if (ca != cb) return Character.compare(ca, cb);
      return Integer.compare(a, b);
    });

    int[] order = new int[m];
    for (int k = 0; k < m; k++) order[k] = idx[k];
    return order;
  }
}
