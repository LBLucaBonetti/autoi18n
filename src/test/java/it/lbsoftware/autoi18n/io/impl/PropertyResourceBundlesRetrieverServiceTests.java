package it.lbsoftware.autoi18n.io.impl;

import io.quarkus.test.junit.QuarkusTest;
import it.lbsoftware.autoi18n.utils.LanguageAndCountry;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import org.junit.jupiter.api.Test;

@QuarkusTest
class PropertyResourceBundlesRetrieverServiceTests {

  @Test
  void test1() throws IOException {
    // Given
    var testDirectory = Files.createTempDirectory("test");
    var propertyResourceBundlesRetrieverService =
        new PropertyResourceBundlesRetrieverService(
            Set.of(new LanguageAndCountry("it"), new LanguageAndCountry("en", "US")),
            testDirectory.toFile());
    String file1Name = "language-IT.properties";
    String file2Name = "languagez_en.properties";
    String file3Name = "translATIONS_En-uS.properties";
    String file4Name = "LaBeL_iT.ProPerTIes";
    var file1 = Files.createFile(Path.of(testDirectory.toString(), file1Name));
    var file2 = Files.createFile(Path.of(testDirectory.toString(), file2Name));
    var file3 = Files.createFile(Path.of(testDirectory.toString(), file3Name));
    var file4 = Files.createFile(Path.of(testDirectory.toString(), file4Name));

    // When
    propertyResourceBundlesRetrieverService.retrieve();

    // Then
    Files.deleteIfExists(file1);
    Files.deleteIfExists(file2);
    Files.deleteIfExists(file3);
    Files.deleteIfExists(file4);
    Files.deleteIfExists(testDirectory);
  }
}
