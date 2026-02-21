package com.example.crypto;

import org.springframework.stereotype.Service;

@Service
public class CryptoService {

  public String process(String input, String key, String key2, String algo, String mode) {
    if (key == null || key.isBlank()) throw new IllegalArgumentException("Key is empty");
    boolean decrypt = "DEC".equalsIgnoreCase(mode);

    return switch (algo) {
      case "COLUMNAR_EN" -> {
        ColumnarCipher c = new ColumnarCipher();
        if (key2 != null && !key2.isBlank()) {
          yield decrypt
              ? c.decrypt(c.decrypt(input, key2), key)
              : c.encrypt(c.encrypt(input, key), key2);
        }
        yield decrypt ? c.decrypt(input, key) : c.encrypt(input, key);
      }
      case "VIGENERE_AUTOKEY_RU" -> {
        VigenereAutokeyRu v = new VigenereAutokeyRu();
        yield decrypt ? v.decrypt(input, key) : v.encrypt(input, key);
      }
      default -> throw new IllegalArgumentException("Unknown algo: " + algo);
    };
  }
}
