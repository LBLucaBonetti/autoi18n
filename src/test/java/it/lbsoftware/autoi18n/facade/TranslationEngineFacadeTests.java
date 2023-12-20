package it.lbsoftware.autoi18n.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.quarkus.test.junit.QuarkusTest;
import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParams;
import it.lbsoftware.autoi18n.translations.TranslationEngine;
import it.lbsoftware.autoi18n.utils.LanguageAndCountry;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import picocli.CommandLine.ExitCode;

@QuarkusTest
class TranslationEngineFacadeTests {

  @Test
  @DisplayName(
      "Should return usage exit code when translation engine params are invalid for GoogleCloudTranslationV3")
  void test1() {
    // Given
    var translationEngine = TranslationEngine.GOOGLE_CLOUD_TRANSLATION_V3;
    Map<String, String> params = Collections.emptyMap();
    Map<String, String> entries = Collections.emptyMap();
    var inputLanguageAndCountry = new LanguageAndCountry("it", null);
    var outputLanguageAndCountries = List.of(new LanguageAndCountry("en", null));
    var translationEngineFacade =
        new TranslationEngineFacade(
            translationEngine,
            params,
            entries,
            inputLanguageAndCountry,
            outputLanguageAndCountries,
            FileUtils.current());

    // When
    var res = translationEngineFacade.performTranslation();

    // Then
    assertNotNull(res);
    assertEquals(ExitCode.USAGE, res);
  }

  @Test
  @DisplayName("Should return usage exit code when there are no entries to translate")
  void test2() {
    // Given
    var translationEngine = TranslationEngine.GOOGLE_CLOUD_TRANSLATION_V3;
    Map<String, String> params =
        Map.of(
            TranslationEngineParams.API_KEY_PARAM,
            "fake-api-key",
            TranslationEngineParams.PROJECT_NUMBER_OR_ID_PARAM,
            "project-number-or-id");
    Map<String, String> entries = null;
    var inputLanguageAndCountry = new LanguageAndCountry("it", null);
    var outputLanguageAndCountries = List.of(new LanguageAndCountry("en", null));
    var translationEngineFacade =
        new TranslationEngineFacade(
            translationEngine,
            params,
            entries,
            inputLanguageAndCountry,
            outputLanguageAndCountries,
            FileUtils.current());

    // When
    var res = translationEngineFacade.performTranslation();

    // Then
    assertNotNull(res);
    assertEquals(ExitCode.USAGE, res);
  }

  @Test
  @DisplayName(
      "Should return usage exit code when there is no Property Resource Bundle file for the output languages")
  void test3() {
    // Given
    var translationEngine = TranslationEngine.GOOGLE_CLOUD_TRANSLATION_V3;
    Map<String, String> params =
        Map.of(
            TranslationEngineParams.API_KEY_PARAM,
            "fake-api-key",
            TranslationEngineParams.PROJECT_NUMBER_OR_ID_PARAM,
            "project-number-or-id");
    Map<String, String> entries = Map.of("key1", "value1");
    var inputLanguageAndCountry = new LanguageAndCountry("it", null);
    var outputLanguageAndCountries = List.of(new LanguageAndCountry("en", null));
    var translationEngineFacade =
        new TranslationEngineFacade(
            translationEngine,
            params,
            entries,
            inputLanguageAndCountry,
            outputLanguageAndCountries,
            FileUtils.current());

    // When
    var res = translationEngineFacade.performTranslation();

    // Then
    assertNotNull(res);
    assertEquals(ExitCode.USAGE, res);
  }
}
