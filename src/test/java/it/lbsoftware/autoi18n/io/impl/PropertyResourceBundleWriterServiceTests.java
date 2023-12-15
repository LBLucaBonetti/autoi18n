package it.lbsoftware.autoi18n.io.impl;

import static it.lbsoftware.autoi18n.TestUtils.assertPropertyResourceBundleFileIsReadableAndContainsPairs;
import static it.lbsoftware.autoi18n.TestUtils.createPropertyResourceBundleFileWithContent;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.quarkus.test.junit.QuarkusTest;
import it.lbsoftware.autoi18n.io.PropertyResourceBundleWriterOptions;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
class PropertyResourceBundleWriterServiceTests {

  @Test
  @DisplayName(
      "Should write to Property Resource Bundle file, preserving other pairs that were there before")
  void test1() throws IOException {
    // Given
    var testDirectoryPath = Files.createTempDirectory("test");
    String fileName = "labels_en.properties";
    String key = "app.key1";
    String value = "value1";
    String key2 = "app.key2";
    String value2 = "value2";
    String examplePair = key + "=" + value + System.lineSeparator() + key2 + "=" + value2;
    String updatedValue = "updatedValue1";
    var updatedPairs = Map.of(key, updatedValue, key2, value2);
    var filePath =
        createPropertyResourceBundleFileWithContent(testDirectoryPath, fileName, examplePair);
    var propertyResourceBundleWriterService = new PropertyResourceBundleWriterService();

    // When
    var res =
        propertyResourceBundleWriterService.write(
            filePath.toFile(),
            Map.of(key, updatedValue),
            PropertyResourceBundleWriterOptions.DEFAULT);

    // Then
    assertTrue(res);
    assertPropertyResourceBundleFileIsReadableAndContainsPairs(
        Path.of(testDirectoryPath.toString(), fileName), updatedPairs);
    FileUtils.deleteDirectory(testDirectoryPath.toFile());
  }

  @Test
  @DisplayName("Should not read from non-readable file")
  void test2() throws IOException {
    // Given
    var testDirectoryPath = Files.createTempDirectory("test");
    String file1Name = "language_IT.properties";
    var file1Path = Files.createFile(Path.of(testDirectoryPath.toString(), file1Name));
    assertTrue(file1Path.toFile().setReadable(false));
    var propertyResourceBundleWriterService = new PropertyResourceBundleWriterService();

    // When
    var res =
        propertyResourceBundleWriterService.write(
            file1Path.toFile(),
            Map.of("key", "value"),
            PropertyResourceBundleWriterOptions.DEFAULT);

    // Then
    assertFalse(res);
  }

  @Test
  @DisplayName("Should not write on non-writable file")
  void test3() throws IOException {
    // Given
    var testDirectoryPath = Files.createTempDirectory("test");
    String file1Name = "language_IT.properties";
    var file1Path = Files.createFile(Path.of(testDirectoryPath.toString(), file1Name));
    assertTrue(file1Path.toFile().setWritable(false));
    var propertyResourceBundleWriterService = new PropertyResourceBundleWriterService();

    // When
    var res =
        propertyResourceBundleWriterService.write(
            file1Path.toFile(),
            Map.of("key", "value"),
            PropertyResourceBundleWriterOptions.DEFAULT);

    // Then
    assertFalse(res);
  }

  @Test
  @DisplayName("Should not overwrite existing mappings if instructed not to do so")
  void test4() throws IOException {
    // Given
    var testDirectoryPath = Files.createTempDirectory("test");
    String fileName = "labels_en.properties";
    String key = "app.key1";
    String value = "value1";
    String key2 = "app.key2";
    String value2 = "value2";
    String examplePair = key + "=" + value;
    String updatedValue = "updatedValue1";
    // Should not update key's value and keep the rest (key2=value2, ...)
    var updatedPairs = Map.of(key, value, key2, value2);
    var filePath =
        createPropertyResourceBundleFileWithContent(testDirectoryPath, fileName, examplePair);
    var propertyResourceBundleWriterService = new PropertyResourceBundleWriterService();

    // When
    var res =
        propertyResourceBundleWriterService.write(
            filePath.toFile(),
            Map.of(key, updatedValue, key2, value2),
            new PropertyResourceBundleWriterOptions(false, StandardCharsets.ISO_8859_1));

    // Then
    assertTrue(res);
    assertPropertyResourceBundleFileIsReadableAndContainsPairs(
        Path.of(testDirectoryPath.toString(), fileName), updatedPairs);
    FileUtils.deleteDirectory(testDirectoryPath.toFile());
  }
}
