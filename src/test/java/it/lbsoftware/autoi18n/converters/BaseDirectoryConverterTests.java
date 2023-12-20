package it.lbsoftware.autoi18n.converters;

import static it.lbsoftware.autoi18n.constants.Constants.DEFAULT_BASE_DIRECTORY;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.quarkus.test.junit.QuarkusTest;
import java.io.IOException;
import java.nio.file.Files;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@QuarkusTest
class BaseDirectoryConverterTests {

  private BaseDirectoryConverter baseDirectoryConverter;

  @BeforeEach
  public void beforeEach() {
    baseDirectoryConverter = new BaseDirectoryConverter();
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" "})
  @DisplayName("Should return default base directory with blank value")
  void test1(final String value) {
    // Given and when
    var res = assertDoesNotThrow(() -> baseDirectoryConverter.convert(value));

    // Then
    assertEquals(DEFAULT_BASE_DIRECTORY, res);
  }

  @Test
  @DisplayName("Should return default base directory with path that does not exits")
  void test2() {
    // Given
    var path = "test";

    // When
    var res = assertDoesNotThrow(() -> baseDirectoryConverter.convert(path));

    // Then
    assertEquals(DEFAULT_BASE_DIRECTORY, res);
  }

  @Test
  @DisplayName("Should return default base directory with path that is not a directory")
  void test3() throws IOException {
    // Given
    var file = Files.createTempFile("test", ".jpg");
    var filePath = file.toFile().getAbsolutePath();

    // When
    var res = assertDoesNotThrow(() -> baseDirectoryConverter.convert(filePath));

    // Then
    assertEquals(DEFAULT_BASE_DIRECTORY, res);
    Files.deleteIfExists(file);
  }

  @Test
  @DisplayName("Should return default base directory with path that is not readable")
  void test4() throws IOException {
    // Given
    var path = Files.createTempDirectory("test");
    assertTrue(path.toFile().setReadable(false));
    var pathAbsolutePath = path.toFile().getAbsolutePath();

    // When
    var res = assertDoesNotThrow(() -> baseDirectoryConverter.convert(pathAbsolutePath));

    // Then
    assertEquals(DEFAULT_BASE_DIRECTORY, res);
    assertTrue(path.toFile().setReadable(true));
    FileUtils.deleteDirectory(path.toFile());
  }

  @Test
  @DisplayName("Should return default base directory with path that is not writable")
  void test5() throws IOException {
    // Given
    var path = Files.createTempDirectory("test");
    assertTrue(path.toFile().setWritable(false));
    var pathAbsolutePath = path.toFile().getAbsolutePath();

    // When
    var res = assertDoesNotThrow(() -> baseDirectoryConverter.convert(pathAbsolutePath));

    // Then
    assertEquals(DEFAULT_BASE_DIRECTORY, res);
    FileUtils.deleteDirectory(path.toFile());
  }

  @Test
  @DisplayName("Should convert the value of a valid path to its File representation")
  void test6() throws IOException {
    // Given
    var path = Files.createTempDirectory("test");
    var pathAbsolutePath = path.toFile().getAbsolutePath();

    // When
    var res = assertDoesNotThrow(() -> baseDirectoryConverter.convert(pathAbsolutePath));

    // Then
    assertEquals(path.toFile(), res);
    FileUtils.deleteDirectory(path.toFile());
  }
}
