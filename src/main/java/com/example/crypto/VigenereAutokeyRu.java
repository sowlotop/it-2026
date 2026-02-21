package com.example.crypto;

public class VigenereAutokeyRu {

    private static final String ALPH = "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    //АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ
    //АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ
    private static final int N = ALPH.length();

    public String encrypt(String input, String keyRaw) {
        String plain = filterRu(input);
        String key0 = filterRu(keyRaw);
        if (key0.isEmpty()) throw new IllegalArgumentException("Key must contain RU letters");

        StringBuilder out = new StringBuilder(plain.length());

        for (int i = 0; i < plain.length(); i++) {
            char p = plain.charAt(i);
            char k = (i < key0.length()) ? key0.charAt(i) : plain.charAt(i - key0.length());
            int ci = (idx(p) + idx(k)) % N;
            out.append(ALPH.charAt(ci));
        }
        return out.toString();
    }

    public String decrypt(String input, String keyRaw) {
        String cipher = filterRu(input);
        String key0 = filterRu(keyRaw);
        if (key0.isEmpty()) throw new IllegalArgumentException("Key must contain RU letters");

        StringBuilder plain = new StringBuilder(cipher.length());
        for (int i = 0; i < cipher.length(); i++) {
            char k = (i < key0.length()) ? key0.charAt(i) : plain.charAt(i - key0.length());
            int pi = (idx(cipher.charAt(i)) - idx(k)) % N;
            if (pi < 0) pi += N;
            plain.append(ALPH.charAt(pi));
        }
        return plain.toString();
    }

    private static int idx(char ch) {
        int i = ALPH.indexOf(ch);
        if (i < 0) throw new IllegalArgumentException("Non-RU letter in filtered text");
        return i;
    }

    private static String filterRu(String s) {
        StringBuilder b = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);


            if (ch == 'ё') ch = 'е';
            if (ch == 'Ё') ch = 'Е';


            if (ch >= 'а' && ch <= 'я') ch = (char) (ch - 32);

            if (ch >= 'А' && ch <= 'Я') {
                if (ch == 'Ё') ch = 'Е';
                if (ALPH.indexOf(ch) >= 0) b.append(ch);
            }
        }
        return b.toString();
    }
}