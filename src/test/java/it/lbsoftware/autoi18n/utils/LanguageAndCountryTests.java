package it.lbsoftware.autoi18n.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import io.quarkus.test.junit.QuarkusTest;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@QuarkusTest
class LanguageAndCountryTests {

  private static Stream<Arguments> test4() {
    // languageAndCountry1, languageAndCountry2
    return Stream.of(
        arguments(new LanguageAndCountry("it", null), new LanguageAndCountry("de", null)),
        arguments(new LanguageAndCountry("en", "US"), new LanguageAndCountry("it", "US")));
  }

  private static Stream<Arguments> test5() {
    // languageAndCountry1, languageAndCountry2
    return Stream.of(
        arguments(new LanguageAndCountry("it", null), new LanguageAndCountry("IT", null)),
        arguments(new LanguageAndCountry("IT", "it"), new LanguageAndCountry("iT", "En")),
        arguments(new LanguageAndCountry("eN", null), new LanguageAndCountry("En", "")));
  }

  @Test
  @DisplayName("Should equal to itself")
  void test1() {
    // Given
    var languageAndCountry = new LanguageAndCountry("it", null);

    // When
    var res = languageAndCountry.equals(languageAndCountry);

    // Then
    assertTrue(res);
  }

  @Test
  @DisplayName("Should not equal to null")
  void test2() {
    // Given
    var languageAndCountry = new LanguageAndCountry("en", "US");

    // When
    var res = languageAndCountry.equals(null);

    // Then
    assertFalse(res);
  }

  @Test
  @DisplayName("Should not equal to an object that is not instance of the same class")
  void test3() {
    // Given
    var languageAndCountry = new LanguageAndCountry("fr", "");

    // When
    var res = languageAndCountry.equals(new String());

    // Then
    assertFalse(res);
  }

  @ParameterizedTest
  @MethodSource
  @DisplayName("Should not equal when languages differ")
  void test4(LanguageAndCountry languageAndCountry1, LanguageAndCountry languageAndCountry2) {
    // Given and when
    var res = languageAndCountry1.equals(languageAndCountry2);

    // Then
    assertFalse(res);
  }

  @ParameterizedTest
  @MethodSource
  @DisplayName("Should equal when languages equal")
  void test5(LanguageAndCountry languageAndCountry1, LanguageAndCountry languageAndCountry2) {
    // Given and when
    var res = languageAndCountry1.equals(languageAndCountry2);

    // Then
    assertTrue(res);
  }
}
