package it.lbsoftware.autoi18n.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.quarkus.test.junit.QuarkusTest;
import java.util.IllformedLocaleException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@QuarkusTest
class LocaleTypeConverterTests {

  private LocaleTypeConverter localeTypeConverter;

  @BeforeEach
  void beforeEach() {
    localeTypeConverter = new LocaleTypeConverter();
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {StringUtils.SPACE})
  @DisplayName("Should throw with invalid language string")
  void test1(final String value) {
    // Given, when and then
    assertThrows(IllegalArgumentException.class, () -> localeTypeConverter.convert(value));
  }

  @ParameterizedTest
  @ValueSource(strings = {"s", "tooLongString"})
  @DisplayName("Should throw with invalid Locale language string")
  void test2(final String value) {
    // Given, when and then
    assertThrows(IllformedLocaleException.class, () -> localeTypeConverter.convert(value));
  }

  @ParameterizedTest
  @ValueSource(strings = {"IT", "en"})
  @DisplayName("Should convert easy languages")
  void test3(final String value) throws Exception {
    // Given and when
    var res = localeTypeConverter.convert(value);

    // Then
    assertEquals(StringUtils.toRootLowerCase(value), res.getLanguage());
  }

  @ParameterizedTest
  @ValueSource(strings = {"NSO", "Mtei", "KRI"})
  @DisplayName("Should convert complex languages")
  void test4(final String value) throws Exception {
    // Given and when
    var res = localeTypeConverter.convert(value);

    // Then
    assertEquals(StringUtils.toRootLowerCase(value), res.getLanguage());
  }

  @Test
  @DisplayName("Should convert language and region formats")
  void test5() {
    // Given
    var language = "EN";
    var region = "us";
    var value = language + "-" + region;

    // When
    var res = localeTypeConverter.convert(value);

    // Then
    assertEquals(StringUtils.swapCase(value), res.getLanguage() + "-" + res.getCountry());
  }
}
