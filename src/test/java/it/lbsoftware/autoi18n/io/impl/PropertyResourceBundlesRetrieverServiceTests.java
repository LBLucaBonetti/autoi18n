package it.lbsoftware.autoi18n.io.impl;

import static it.lbsoftware.autoi18n.TestUtils.createPropertyResourceBundleFile;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.quarkus.test.junit.QuarkusTest;
import it.lbsoftware.autoi18n.utils.LanguageAndCountry;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
class PropertyResourceBundlesRetrieverServiceTests {

  @Test
  @DisplayName("Should only gather required Property Resource Bundle files")
  void test1() throws IOException {
    // Given
    var testDirectoryPath = Files.createTempDirectory("test");
    String file1Name = "language-IT.properties";
    String file2Name = "languagez_en.properties";
    String file3Name = "translATIONS_En-uS.properties";
    String file4Name = "LaBeL_iT.ProPerTIes";
    String file5Name = "labels_en.properties";
    String file6Name = "labbel.properties";
    createPropertyResourceBundleFile(testDirectoryPath, file1Name);
    createPropertyResourceBundleFile(testDirectoryPath, file2Name);
    createPropertyResourceBundleFile(testDirectoryPath, file3Name);
    createPropertyResourceBundleFile(testDirectoryPath, file4Name);
    createPropertyResourceBundleFile(testDirectoryPath, file5Name);
    createPropertyResourceBundleFile(testDirectoryPath, file6Name);
    var itLang = new LanguageAndCountry("it");
    var enUsLang = new LanguageAndCountry("en", "US");
    var esLang = new LanguageAndCountry("es");
    var propertyResourceBundlesRetrieverService = new PropertyResourceBundlesRetrieverService();

    // When
    var res =
        propertyResourceBundlesRetrieverService.retrieve(
            Set.of(itLang, enUsLang, esLang), testDirectoryPath.toFile());

    // Then
    assertFalse(res.containsKey(esLang));
    assertFalse(res.containsKey(itLang));
    assertTrue(res.containsKey(enUsLang));
    FileUtils.deleteDirectory(testDirectoryPath.toFile());
  }

  @Test
  @DisplayName("Should provide no output for non-readable file")
  void test2() throws IOException {
    // Given
    var testDirectoryPath = Files.createTempDirectory("test");
    String file1Name = "language_IT.properties";
    var file1Path = Files.createFile(Path.of(testDirectoryPath.toString(), file1Name));
    assertTrue(file1Path.toFile().setReadable(false));
    var itLang = new LanguageAndCountry("it");
    var propertyResourceBundlesRetrieverService = new PropertyResourceBundlesRetrieverService();

    // When
    var res =
        propertyResourceBundlesRetrieverService.retrieve(
            Set.of(itLang), testDirectoryPath.toFile());

    // Then
    assertFalse(res.containsKey(itLang));
    FileUtils.deleteDirectory(testDirectoryPath.toFile());
  }

  @Test
  @DisplayName("Should provide no output for non-writable file")
  void test3() throws IOException {
    // Given
    var testDirectoryPath = Files.createTempDirectory("test");
    String file1Name = "language_IT.properties";
    var file1Path = Files.createFile(Path.of(testDirectoryPath.toString(), file1Name));
    assertTrue(file1Path.toFile().setWritable(false));
    var itLang = new LanguageAndCountry("it");
    var propertyResourceBundlesRetrieverService = new PropertyResourceBundlesRetrieverService();

    // When
    var res =
        propertyResourceBundlesRetrieverService.retrieve(
            Set.of(itLang), testDirectoryPath.toFile());

    // Then
    assertFalse(res.containsKey(itLang));
    FileUtils.deleteDirectory(testDirectoryPath.toFile());
  }
}
