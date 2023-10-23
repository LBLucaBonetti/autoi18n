package it.lbsoftware.autoi18n;

import static it.lbsoftware.autoi18n.constants.Constants.AUTOI18N_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.quarkus.test.junit.QuarkusTest;
import it.lbsoftware.autoi18n.constants.TranslationEngine;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import picocli.CommandLine;

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
    assertEquals(0, exitCode);
    assertPrintsUsage();
  }

  @ParameterizedTest
  @ValueSource(strings = {"-V", "--version"})
  @DisplayName("Should print version")
  void test2(final String args) {
    // Given and when
    var exitCode = commandLine.execute(args);

    // Then
    assertEquals(0, exitCode);
    assertPrintsVersion();
  }

  @Test
  @DisplayName("Should gather entries defaulting to empty")
  void test3() {
    // Given
    String[] args = {
      "-len",
      "-ekey1=value1",
      "-ekey2",
      "-ekey3=",
      "--entry=key4=value4",
      "--entry=key5",
      "--entry=key6=",
      "-ekey7=value7,key8=value8"
    };
    int argsLength = args.length;

    var exitCode = commandLine.execute(args);

    // Then
    assertEquals(0, exitCode);
    var entries = autoi18n.getEntries();
    assertEquals(argsLength, entries.keySet().size());
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
    String[] args = {"-len", "-ekey=value1", "-ekey=value2", "-ekey=value3", "-ekey=", "-ekey"};

    // When
    var exitCode = commandLine.execute(args);

    // Then
    assertEquals(0, exitCode);
    var entries = autoi18n.getEntries();
    assertEquals(1, entries.keySet().size());
    assertEquals(StringUtils.EMPTY, entries.get("key"));
  }

  @Test
  @DisplayName("Should not recognize an invalid translation engine and default it")
  void test5() {
    // Given
    String[] args = {"-len", "-ekey1=value1", "-tinvalidTranslationEngine"};

    // When
    var exitCode = commandLine.execute(args);

    // Then
    assertNotEquals(0, exitCode);
    assertEquals(TranslationEngine.GOOGLE_CLOUD_TRANSLATION_V3, autoi18n.getTranslationEngine());
  }
}
