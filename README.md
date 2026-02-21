# crypto-web-spring

Spring Boot web app: 
- Columnar transposition (EN), improved = double transposition with K1 + optional K2
- Vigenere autokey (RU)

## Run
```bash
./gradlew bootRun
# or
gradle bootRun
```

Open: http://localhost:8080

## Notes
- EN: only A-Z are processed, everything else ignored.
- RU: only Russian letters are processed; Ё is mapped to Е.
