package it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.quarkus.test.junit.QuarkusTest;
import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParams;
import it.lbsoftware.autoi18n.translations.TranslationApi;
import it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.api.GoogleCloudTranslationV3Api;
import it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.pojos.GoogleCloudTranslationV3Request;
import it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.pojos.GoogleCloudTranslationV3Response;
import it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.pojos.GoogleCloudTranslationV3Translation;
import it.lbsoftware.autoi18n.utils.LanguageAndCountry;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
class GoogleCloudTranslationV3ServiceTests {

  private GoogleCloudTranslationV3Service googleCloudTranslationV3Service;
  private TranslationApi<GoogleCloudTranslationV3Response, GoogleCloudTranslationV3Request>
      translationApi;

  @BeforeEach
  void beforeEach() {
    translationApi = mock(GoogleCloudTranslationV3Api.class);
    googleCloudTranslationV3Service = new GoogleCloudTranslationV3Service(translationApi);
  }

  @Test
  @DisplayName("Should translate single entry to single language")
  void test1() {
    // Given
    var key = "app.hello";
    var value = "ciao";
    var translatedValue = "hello";
    Map<String, String> entries = Map.of(key, value);
    var inputLanguageAndCountry = new LanguageAndCountry("it", null);
    var outputLanguageAndCountry = new LanguageAndCountry("en", null);
    var outputLanguageAndCountries = Set.of(outputLanguageAndCountry);
    var translationEngineParams = TranslationEngineParams.EMPTY;
    var translateResponse =
        new GoogleCloudTranslationV3Response(
            List.of(new GoogleCloudTranslationV3Translation(translatedValue)));
    when(translationApi.translate(
            any(TranslationEngineParams.class), any(GoogleCloudTranslationV3Request.class)))
        .thenReturn(translateResponse);

    // When
    var res =
        googleCloudTranslationV3Service.translate(
            entries, inputLanguageAndCountry, outputLanguageAndCountries, translationEngineParams);

    // Then
    assertNotNull(res);
    assertEquals(translatedValue, res.get(outputLanguageAndCountry).get(key));
    verify(translationApi, times(1))
        .translate(
            translationEngineParams,
            new GoogleCloudTranslationV3Request(
                List.of(value),
                inputLanguageAndCountry.getLanguage(),
                outputLanguageAndCountry.getLanguage()));
  }

  @Test
  @DisplayName("Should translate multiple entries to multiple languages")
  void test2() {
    // Given
    var key1 = "app.hello";
    var value1 = "ciao";
    var key2 = "app.world";
    var value2 = "mondo";
    var translatedValueLang11 = "hello";
    var translatedValueLang12 = "world";
    var translatedValueLang21 = "salut";
    var translatedValueLang22 = "monde";
    Map<String, String> entries = Map.ofEntries(Map.entry(key1, value1), Map.entry(key2, value2));
    var inputLanguageAndCountry = new LanguageAndCountry("it", null);
    var outputLanguageAndCountry1 = new LanguageAndCountry("en", null);
    var outputLanguageAndCountry2 = new LanguageAndCountry("fr", null);
    var outputLanguageAndCountries = Set.of(outputLanguageAndCountry1, outputLanguageAndCountry2);
    var translationEngineParams = TranslationEngineParams.EMPTY;
    var translateResponse11 =
        new GoogleCloudTranslationV3Response(
            List.of(new GoogleCloudTranslationV3Translation(translatedValueLang11)));
    var translateResponse12 =
        new GoogleCloudTranslationV3Response(
            List.of(new GoogleCloudTranslationV3Translation(translatedValueLang12)));
    var translateResponse21 =
        new GoogleCloudTranslationV3Response(
            List.of(new GoogleCloudTranslationV3Translation(translatedValueLang21)));
    var translateResponse22 =
        new GoogleCloudTranslationV3Response(
            List.of(new GoogleCloudTranslationV3Translation(translatedValueLang22)));
    when(translationApi.translate(
            translationEngineParams,
            new GoogleCloudTranslationV3Request(
                List.of(value1),
                inputLanguageAndCountry.getLanguage(),
                outputLanguageAndCountry1.getLanguage())))
        .thenReturn(translateResponse11);
    when(translationApi.translate(
            translationEngineParams,
            new GoogleCloudTranslationV3Request(
                List.of(value2),
                inputLanguageAndCountry.getLanguage(),
                outputLanguageAndCountry1.getLanguage())))
        .thenReturn(translateResponse12);
    when(translationApi.translate(
            translationEngineParams,
            new GoogleCloudTranslationV3Request(
                List.of(value1),
                inputLanguageAndCountry.getLanguage(),
                outputLanguageAndCountry2.getLanguage())))
        .thenReturn(translateResponse21);
    when(translationApi.translate(
            translationEngineParams,
            new GoogleCloudTranslationV3Request(
                List.of(value2),
                inputLanguageAndCountry.getLanguage(),
                outputLanguageAndCountry2.getLanguage())))
        .thenReturn(translateResponse22);

    // When
    var res =
        googleCloudTranslationV3Service.translate(
            entries, inputLanguageAndCountry, outputLanguageAndCountries, translationEngineParams);

    // Then
    assertNotNull(res);
    assertEquals(translatedValueLang11, res.get(outputLanguageAndCountry1).get(key1));
    assertEquals(translatedValueLang12, res.get(outputLanguageAndCountry1).get(key2));
    assertEquals(translatedValueLang21, res.get(outputLanguageAndCountry2).get(key1));
    assertEquals(translatedValueLang22, res.get(outputLanguageAndCountry2).get(key2));
    verify(translationApi, times(1))
        .translate(
            translationEngineParams,
            new GoogleCloudTranslationV3Request(
                List.of(value1),
                inputLanguageAndCountry.getLanguage(),
                outputLanguageAndCountry1.getLanguage()));
    verify(translationApi, times(1))
        .translate(
            translationEngineParams,
            new GoogleCloudTranslationV3Request(
                List.of(value2),
                inputLanguageAndCountry.getLanguage(),
                outputLanguageAndCountry1.getLanguage()));
    verify(translationApi, times(1))
        .translate(
            translationEngineParams,
            new GoogleCloudTranslationV3Request(
                List.of(value1),
                inputLanguageAndCountry.getLanguage(),
                outputLanguageAndCountry2.getLanguage()));
    verify(translationApi, times(1))
        .translate(
            translationEngineParams,
            new GoogleCloudTranslationV3Request(
                List.of(value2),
                inputLanguageAndCountry.getLanguage(),
                outputLanguageAndCountry2.getLanguage()));
  }

  @Test
  @DisplayName("Should return empty translations when entry value is blank")
  void test3() {
    // Given
    var key = "app.hello";
    var value = "";
    var translatedValue = "";
    Map<String, String> entries = Map.of(key, value);
    var inputLanguageAndCountry = new LanguageAndCountry("it", null);
    var outputLanguageAndCountry = new LanguageAndCountry("en", null);
    var outputLanguageAndCountries = Set.of(outputLanguageAndCountry);
    var translationEngineParams = TranslationEngineParams.EMPTY;

    // When
    var res =
        googleCloudTranslationV3Service.translate(
            entries, inputLanguageAndCountry, outputLanguageAndCountries, translationEngineParams);

    // Then
    assertNotNull(res);
    assertEquals(translatedValue, res.get(outputLanguageAndCountry).get(key));
    verify(translationApi, times(0))
        .translate(
            translationEngineParams,
            new GoogleCloudTranslationV3Request(
                List.of(value),
                inputLanguageAndCountry.getLanguage(),
                outputLanguageAndCountry.getLanguage()));
  }

  @Test
  @DisplayName("Should return no translations when there are no entries")
  void test4() {
    // Given
    Map<String, String> entries = Collections.emptyMap();
    var inputLanguageAndCountry = new LanguageAndCountry("it", null);
    var outputLanguageAndCountry = new LanguageAndCountry("en", null);
    var outputLanguageAndCountries = Set.of(outputLanguageAndCountry);
    var translationEngineParams = TranslationEngineParams.EMPTY;

    // When
    var res =
        googleCloudTranslationV3Service.translate(
            entries, inputLanguageAndCountry, outputLanguageAndCountries, translationEngineParams);

    // Then
    assertNotNull(res);
    assertTrue(res.get(outputLanguageAndCountry).isEmpty());
    verify(translationApi, times(0))
        .translate(
            translationEngineParams,
            new GoogleCloudTranslationV3Request(
                null,
                inputLanguageAndCountry.getLanguage(),
                outputLanguageAndCountry.getLanguage()));
  }
}
