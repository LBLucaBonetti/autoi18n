package it.lbsoftware.autoi18n.io.impl;

import static it.lbsoftware.autoi18n.TestUtils.createPropertyResourceBundleFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.quarkus.test.junit.QuarkusTest;
import it.lbsoftware.autoi18n.io.PropertyResourceBundleBackupWriter;
import it.lbsoftware.autoi18n.io.PropertyResourceBundleBackupWriterOptions;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
class PropertyResourceBundleBackupWriterServiceTests {

  private PropertyResourceBundleBackupWriter propertyResourceBundleBackupWriter;

  @BeforeEach
  public void beforeEach() {
    propertyResourceBundleBackupWriter = new PropertyResourceBundleBackupWriterService();
  }

  @Test
  @DisplayName("Should return false when resource bundle file is null")
  void test1() throws IOException {
    // Given
    File resourceBundle = null;
    var testDirectoryPath = Files.createTempDirectory("test");

    // When
    var res = propertyResourceBundleBackupWriter.backup(resourceBundle,
        new PropertyResourceBundleBackupWriterOptions(testDirectoryPath.toFile()));

    // Then
    assertFalse(res);
    FileUtils.deleteDirectory(testDirectoryPath.toFile());
  }

  @Test
  @DisplayName("Should return false when options are null")
  void test2() throws IOException {
    // Given
    var resourceBundle = Files.createTempFile("language_en", ".properties");
    PropertyResourceBundleBackupWriterOptions propertyResourceBundleBackupWriterOptions = null;

    // When
    var res = propertyResourceBundleBackupWriter.backup(resourceBundle.toFile(),
        propertyResourceBundleBackupWriterOptions);

    // Then
    assertFalse(res);
    Files.deleteIfExists(resourceBundle);
  }

  @Test
  @DisplayName("Should successfully back the resource bundle up")
  void test3() throws IOException {
    // Given
    var testDirectoryPath = Files.createTempDirectory("test");
    var resourceBundle = createPropertyResourceBundleFile(testDirectoryPath,
        "language_en.properties");
    var backupDirectoryPath = Path.of(testDirectoryPath.toFile().getAbsolutePath(), "backup");
    var resourceBundleFileContent = Files.readString(resourceBundle, StandardCharsets.ISO_8859_1);

    // When
    var res = propertyResourceBundleBackupWriter.backup(resourceBundle.toFile(),
        new PropertyResourceBundleBackupWriterOptions(
            backupDirectoryPath.toFile()));
    var backupFileNumber = backupDirectoryPath.toFile()
        .listFiles().length;
    var backupFile = backupDirectoryPath.toFile().listFiles()[0];
    var backupFileContent = Files.readString(backupFile.toPath(), StandardCharsets.ISO_8859_1);

    // Then
    assertTrue(res);
    assertEquals(1, backupFileNumber);
    assertEquals(resourceBundleFileContent, backupFileContent);
    assertEquals(-1L, Files.mismatch(resourceBundle, backupFile.toPath()));
    FileUtils.deleteDirectory(testDirectoryPath.toFile());
  }

}
