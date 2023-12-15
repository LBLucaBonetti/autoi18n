package it.lbsoftware.autoi18n.io;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public record PropertyResourceBundleWriterOptions(
    boolean overwriteExistingMappings, Charset charset) {
  public static PropertyResourceBundleWriterOptions DEFAULT =
      new PropertyResourceBundleWriterOptions(true, StandardCharsets.ISO_8859_1);
}
