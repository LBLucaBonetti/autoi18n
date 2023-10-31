package it.lbsoftware.autoi18n;

import static it.lbsoftware.autoi18n.TestUtils.optionLongEntry;
import static it.lbsoftware.autoi18n.TestUtils.optionShortEntry;
import static it.lbsoftware.autoi18n.TestUtils.optionShortInputLanguage;
import static it.lbsoftware.autoi18n.TestUtils.optionShortOutputLanguages;
import static it.lbsoftware.autoi18n.TestUtils.optionShortTranslationEngine;
import static it.lbsoftware.autoi18n.TestUtils.requiredTranslationEngineParamsForDefaultTranslationEngine;
import static it.lbsoftware.autoi18n.constants.Constants.AUTOI18N_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.quarkus.test.junit.QuarkusTest;
import it.lbsoftware.autoi18n.translations.TranslationEngine;
import java.io.PrintWriter;
import java.io.StringWriter;
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
  void test3() {
    // Given
    String[] args = {
      optionShortInputLanguage("en"),
      optionShortOutputLanguages("it"),
      optionShortEntry("key1=value1"),
      optionShortEntry("key2"),
      optionShortEntry("key3="),
      optionLongEntry("key4=value4"),
      optionLongEntry("key5"),
      optionLongEntry("key6="),
      optionShortEntry("key7=value7,key8=value8"),
      requiredTranslationEngineParamsForDefaultTranslationEngine()
    };
    int entryArgsLength = args.length - 2;

    var exitCode = commandLine.execute(args);

    // Then
    assertEquals(ExitCode.OK, exitCode);
    var entries = autoi18n.getEntries();
    assertEquals(entryArgsLength, entries.keySet().size());
    assertEquals("value1", entries.get("key1"));
    assertEquals(StringUtils.EMPTY, entries.get("key2"));
    assertEquals(StringUtils.EMPTY, entries.get("key3"));
    assertEquals("value4", entries.get("key4"));
    assertEquals(StringUtils.EMPTY, entries.get("key5"));
    assertEquals(StringUtils.EMPTY, entries.get("key6"));
  }

  @Test
  @DisplayName("Should pick the latest key entry")
  void test4() {
    // Given
    String[] args = {
      optionShortInputLanguage("en"),
      optionShortOutputLanguages("it"),
      optionShortEntry("key=value1"),
      optionShortEntry("key=value2"),
      optionShortEntry("key=value3"),
      optionShortEntry("key="),
      optionShortEntry("key"),
      requiredTranslationEngineParamsForDefaultTranslationEngine()
    };

    // When
    var exitCode = commandLine.execute(args);

    // Then
    assertEquals(ExitCode.OK, exitCode);
    var entries = autoi18n.getEntries();
    assertEquals(1, entries.keySet().size());
    assertEquals(StringUtils.EMPTY, entries.get("key"));
  }

  @Test
  @DisplayName("Should not recognize an invalid translation engine and default it")
  void test5() {
    // Given
    String[] args = {
      optionShortInputLanguage("en"),
      optionShortEntry("key1=value1"),
      optionShortTranslationEngine("invalidTranslationEngine")
    };

    // When
    var exitCode = commandLine.execute(args);

    // Then
    assertNotEquals(ExitCode.OK, exitCode);
    assertEquals(TranslationEngine.GOOGLE_CLOUD_TRANSLATION_V3, autoi18n.getTranslationEngine());
  }

  @Test
  @DisplayName("Should pick multiple output languages")
  void test6() {
    // Given
    var lan1 = "en-US";
    var lan2 = "it";
    String[] args = {
      optionShortInputLanguage("en"),
      optionShortOutputLanguages(lan1 + "," + lan2),
      optionShortEntry("key1=value1"),
      requiredTranslationEngineParamsForDefaultTranslationEngine()
    };
    int outputLanguageArgsLength = 2;

    // When
    var exitCode = commandLine.execute(args);

    // Then
    assertEquals(ExitCode.OK, exitCode);
    var outputLocales = autoi18n.getOutputLocales();
    assertEquals(outputLanguageArgsLength, outputLocales.size());
    assertEquals(
        lan1, outputLocales.getFirst().getLanguage() + "-" + outputLocales.getFirst().getCountry());
    assertEquals(lan2, outputLocales.getLast().getLanguage());
  }
}
