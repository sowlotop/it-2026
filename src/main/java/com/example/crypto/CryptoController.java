package com.example.crypto;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@Controller
public class CryptoController {
  private final CryptoService cryptoService;

  public CryptoController(CryptoService cryptoService) {
    this.cryptoService = cryptoService;
  }

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("text", "");
    model.addAttribute("key", "");
    model.addAttribute("key2", "");
    model.addAttribute("algo", "COLUMNAR_EN");
    model.addAttribute("mode", "ENC");
    return "index";
  }

  @PostMapping("/process-text")
  public String processText(
      @RequestParam String text,
      @RequestParam String key,
      @RequestParam(required = false, defaultValue = "") String key2,
      @RequestParam String algo,
      @RequestParam String mode,
      Model model
  ) {
    String out = cryptoService.process(text, key, key2, algo, mode);
    model.addAttribute("text", text);
    model.addAttribute("key", key);
    model.addAttribute("key2", key2);
    model.addAttribute("algo", algo);
    model.addAttribute("mode", mode);
    model.addAttribute("result", out);
    return "index";
  }

  @PostMapping("/process-file")
  public ResponseEntity<ByteArrayResource> processFile(
      @RequestParam MultipartFile file,
      @RequestParam String key,
      @RequestParam(required = false, defaultValue = "") String key2,
      @RequestParam String algo,
      @RequestParam String mode
  ) throws Exception {
    String in = new String(file.getBytes(), StandardCharsets.UTF_8);
    String out = cryptoService.process(in, key, key2, algo, mode);

    byte[] bytes = out.getBytes(StandardCharsets.UTF_8);
    String suffix = mode.equalsIgnoreCase("DEC") ? "_decrypted" : "_encrypted";

    String outName = (file.getOriginalFilename() == null || file.getOriginalFilename().isBlank())
        ? "result.txt"
        : file.getOriginalFilename();

      outName = outName.replaceAll("(\\.[^.]+)?$", suffix + ".txt");


      ByteArrayResource res = new ByteArrayResource(bytes);
    return ResponseEntity.ok()
        .contentType(MediaType.TEXT_PLAIN)
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + outName + "\"")
        .contentLength(bytes.length)
        .body(res);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public String handleBadRequest(IllegalArgumentException e, Model model) {
    model.addAttribute("text", model.getAttribute("text"));
    model.addAttribute("key", model.getAttribute("key"));
    model.addAttribute("key2", model.getAttribute("key2"));
    model.addAttribute("algo", model.getAttribute("algo"));
    model.addAttribute("mode", model.getAttribute("mode"));
    model.addAttribute("error", e.getMessage());
    return "index";
  }
}
