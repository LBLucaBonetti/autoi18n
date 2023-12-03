package it.lbsoftware.autoi18n;

import static it.lbsoftware.autoi18n.TestUtils.createPropertyResourceBundleFile;
import static it.lbsoftware.autoi18n.TestUtils.optionShortTranslationEngine;
import static it.lbsoftware.autoi18n.TestUtils.parameterEntry;
import static it.lbsoftware.autoi18n.TestUtils.parameterInputLanguage;
import static it.lbsoftware.autoi18n.TestUtils.parameterOutputLanguages;
import static it.lbsoftware.autoi18n.TestUtils.requiredTranslationEngineParamsForDefaultTranslationEngine;
import static it.lbsoftware.autoi18n.constants.Constants.AUTOI18N_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.quarkus.test.junit.QuarkusTest;
import it.lbsoftware.autoi18n.translations.TranslationEngine;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import picocli.CommandLine;
import picocli.CommandLine.ExitCode;

@QuarkusTest
class Autoi18nTests {

  private Autoi18n autoi18n;
  private CommandLine commandLine;
  private StringWriter outStringWriter;
  private StringWriter errorStringWriter;

  private void assertPrintsUsage() {
    assertTrue(outStringWriter.toString().startsWith("Usage: " + AUTOI18N_NAME));
  }

  private void assertPrintsVersion() {
    assertTrue(outStringWriter.toString().startsWith(AUTOI18N_NAME + " - version "));
  }

  @BeforeEach
  void beforeEach() {
    autoi18n = new Autoi18n();
    commandLine = new CommandLine(autoi18n);
    outStringWriter = new StringWriter();
    errorStringWriter = new StringWriter();
    commandLine.setOut(new PrintWriter(outStringWriter));
    commandLine.setErr(new PrintWriter(errorStringWriter));
  }

  @ParameterizedTest
  @ValueSource(strings = {"-h", "--help"})
  @DisplayName("Should print usage with options")
  void test1(final String args) {
    // Given and when
    var exitCode = commandLine.execute(args);

    // Then
    assertEquals(ExitCode.OK, exitCode);
    assertPrintsUsage();
  }

  @ParameterizedTest
  @ValueSource(strings = {"-V", "--version"})
  @DisplayName("Should print version")
  void test2(final String args) {
    // Given and when
    var exitCode = commandLine.execute(args);

    // Then
    assertEquals(ExitCode.OK, exitCode);
    assertPrintsVersion();
  }

  @Test
  @DisplayName("Should gather entries defaulting to empty")
  void test3() throws IOException {
    // Given
    String[] args = {
      parameterInputLanguage("en"),
      parameterOutputLanguages("it"),
      parameterEntry("key1=value1"),
      parameterEntry("key2"),
      parameterEntry("key3="),
      parameterEntry("key4=value4"),
      parameterEntry("key5"),
      parameterEntry("key6="),
      parameterEntry("key4=value7"),
      requiredTranslationEngineParamsForDefaultTranslationEngine()
    };
    int entryArgsLength = args.length - 4;
    var testDirectoryPath = FileUtils.current().toPath();
    var itPropertiesFile =
        createPropertyResourceBundleFile(testDirectoryPath, "labels_it.properties");

    var exitCode = commandLine.execute(args);

    // Then
    assertEquals(ExitCode.OK, exitCode);
    var entries = autoi18n.getEntries();
    assertEquals(entryArgsLength, entries.keySet().size());
    assertEquals("value1", entries.get("key1"));
    assertEquals(StringUtils.EMPTY, entries.get("key2"));
    assertEquals(StringUtils.EMPTY, entries.get("key3"));
    assertEquals("value7", entries.get("key4"));
    assertEquals(StringUtils.EMPTY, entries.get("key5"));
    assertEquals(StringUtils.EMPTY, entries.get("key6"));
    Files.deleteIfExists(itPropertiesFile);
  }

  @Test
  @DisplayName("Should pick the latest key entry")
  void test4() throws IOException {
    // Given
    String[] args = {
      parameterInputLanguage("en"),
      parameterOutputLanguages("it"),
      parameterEntry("key=value1"),
      parameterEntry("key=value2"),
      parameterEntry("key=value3"),
      parameterEntry("key="),
      parameterEntry("key"),
      requiredTranslationEngineParamsForDefaultTranslationEngine()
    };
    var testDirectoryPath = FileUtils.current().toPath();
    var itPropertiesFile =
        createPropertyResourceBundleFile(testDirectoryPath, "labels_it.properties");

    // When
    var exitCode = commandLine.execute(args);

    // Then
    assertEquals(ExitCode.OK, exitCode);
    var entries = autoi18n.getEntries();
    assertEquals(1, entries.keySet().size());
    assertEquals(StringUtils.EMPTY, entries.get("key"));
    Files.deleteIfExists(itPropertiesFile);
  }

  @Test
  @DisplayName("Should not recognize an invalid translation engine and default it")
  void test5() {
    // Given
    String[] args = {
      parameterInputLanguage("en"),
      parameterOutputLanguages("it"),
      parameterEntry("key1=value1"),
      optionShortTranslationEngine("invalidTranslationEngine")
    };

    // When
    var exitCode = commandLine.execute(args);

    // Then
    assertNotEquals(ExitCode.OK, exitCode);
    assertEquals(TranslationEngine.GOOGLE_CLOUD_TRANSLATION_V3, autoi18n.getTranslationEngine());
  }

  @Test
  @DisplayName("Should pick multiple output language and countries")
  void test6() throws IOException {
    // Given
    var lan1 = "en-US";
    var lan2 = "it";
    String[] args = {
      parameterInputLanguage("en"),
      parameterOutputLanguages(lan1 + "," + lan2),
      parameterEntry("key1=value1"),
      requiredTranslationEngineParamsForDefaultTranslationEngine()
    };
    int outputLanguageArgsLength = 2;
    var testDirectoryPath = FileUtils.current().toPath();
    var lan1PropertiesFile =
        createPropertyResourceBundleFile(testDirectoryPath, "labels_" + lan1 + ".properties");
    var lan2PropertiesFile =
        createPropertyResourceBundleFile(testDirectoryPath, "labels_" + lan2 + ".properties");

    // When
    var exitCode = commandLine.execute(args);

    // Then
    assertEquals(ExitCode.OK, exitCode);
    var outputLanguageAndCountries = autoi18n.getOutputLanguageAndCountries();
    assertEquals(outputLanguageArgsLength, outputLanguageAndCountries.size());
    assertEquals(
        lan1,
        outputLanguageAndCountries.getFirst().getLanguage()
            + "-"
            + outputLanguageAndCountries.getFirst().getCountry());
    assertEquals(lan2, outputLanguageAndCountries.getLast().getLanguage());
    Files.deleteIfExists(lan1PropertiesFile);
    Files.deleteIfExists(lan2PropertiesFile);
  }
}
