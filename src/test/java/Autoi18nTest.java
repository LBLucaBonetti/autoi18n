import static org.junit.jupiter.api.Assertions.assertEquals;

import it.lbsoftware.autoi18n.Autoi18n;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Autoi18nTest {

  private Autoi18n autoi18n;
  private static final ByteArrayOutputStream baos = new ByteArrayOutputStream();

  @BeforeEach
  void beforeEach() {
    autoi18n = new Autoi18n();
  }

  @BeforeAll
  static void beforeAll() {
    System.setOut(new PrintStream(baos));
  }

  @Test
  @DisplayName("Should print hello world")
  void test1() {
    // Given & when
    autoi18n.run();

    // Then
    assertEquals("Hello, world!" + System.lineSeparator(), baos.toString());
  }
}
