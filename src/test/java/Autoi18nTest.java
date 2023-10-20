import static it.lbsoftware.autoi18n.constants.Constants.AUTOI18N_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.quarkus.test.junit.QuarkusTest;
import it.lbsoftware.autoi18n.Autoi18n;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import picocli.CommandLine;

@QuarkusTest
class Autoi18nTest {

  private CommandLine commandLine;
  private StringWriter stringWriter;

  private void assertPrintsUsage() {
    assertTrue(stringWriter.toString().startsWith("Usage: " + AUTOI18N_NAME));
  }

  private void assertPrintsVersion() {
    assertTrue(stringWriter.toString().startsWith(AUTOI18N_NAME + " - version "));
  }

  @BeforeEach
  void beforeEach() {
    Autoi18n autoi18n = new Autoi18n();
    commandLine = new CommandLine(autoi18n);
    stringWriter = new StringWriter();
    commandLine.setOut(new PrintWriter(stringWriter));
  }

  @Test
  @DisplayName("Should print usage with no options nor parameters")
  void test1() {
    // Given and when
    var exitCode = commandLine.execute();

    // Then
    assertEquals(0, exitCode);
    assertPrintsUsage();
  }

  @ParameterizedTest
  @ValueSource(strings = {"-h", "--help"})
  @DisplayName("Should print usage with options")
  void test2(final String args) {
    // Given and when
    var exitCode = commandLine.execute(args);

    // Then
    assertEquals(0, exitCode);
    assertPrintsUsage();
  }

  @ParameterizedTest
  @ValueSource(strings = {"-V", "--version"})
  @DisplayName("Should print version")
  void test3(final String args) {
    // Given and when
    var exitCode = commandLine.execute(args);

    // Then
    assertEquals(0, exitCode);
    assertPrintsVersion();
  }
}
