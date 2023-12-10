package it.lbsoftware.autoi18n.io.impl;

import it.lbsoftware.autoi18n.io.PropertyResourceBundleWriter;
import it.lbsoftware.autoi18n.io.PropertyResourceBundleWriterOptions;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Map;
import java.util.Properties;

public class PropertyResourceBundleWriterService implements PropertyResourceBundleWriter {

  @Override
  public boolean write(
      File resourceBundle,
      Map<String, String> translations,
      PropertyResourceBundleWriterOptions options) {
    final var existingProperties = new Properties();
    try (final InputStream fileInputStream = Files.newInputStream(resourceBundle.toPath())) {
      existingProperties.load(fileInputStream);
      if (options.overwriteExistingMappings()) {
        translations.forEach(existingProperties::setProperty);
      } else {
        translations.forEach(
            (String key, String value) -> {
              if (existingProperties.containsKey(key)) {
                return;
              }
              existingProperties.setProperty(key, value);
            });
      }
    } catch (IOException e) {
      System.err.println("I/O error reading from file {}" + resourceBundle.getAbsolutePath());
      return false;
    }
    try (final OutputStream fileOutputStream = Files.newOutputStream(resourceBundle.toPath())) {
      existingProperties.store(fileOutputStream, null);
    } catch (IOException e) {
      System.err.println("I/O error writing to file {}" + resourceBundle.getAbsolutePath());
      return false;
    }
    return true;
  }
}
