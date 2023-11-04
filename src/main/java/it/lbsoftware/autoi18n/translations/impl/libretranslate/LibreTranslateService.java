package it.lbsoftware.autoi18n.translations.impl.libretranslate;

import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParams;
import it.lbsoftware.autoi18n.translations.AbstractTranslationService;
import it.lbsoftware.autoi18n.translations.impl.libretranslate.api.LibreTranslateApi;
import it.lbsoftware.autoi18n.translations.impl.libretranslate.pojos.LibreTranslateRequest;
import it.lbsoftware.autoi18n.translations.impl.libretranslate.pojos.LibreTranslateResponse;
import it.lbsoftware.autoi18n.utils.LanguageAndCountry;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

public final class LibreTranslateService
    extends AbstractTranslationService<LibreTranslateResponse, LibreTranslateRequest> {

  public LibreTranslateService() {
    super(new LibreTranslateApi());
  }

  @Override
  public Map<LanguageAndCountry, Map<String, String>> translate(
      Map<String, String> entries,
      LanguageAndCountry inputLanguageAndCountry,
      Set<LanguageAndCountry> outputLanguageAndCountries,
      TranslationEngineParams translationEngineParams) {
    var translations = new HashMap<LanguageAndCountry, Map<String, String>>();
    outputLanguageAndCountries.forEach(
        (LanguageAndCountry outputLanguageAndCountry) ->
            translations.put(
                outputLanguageAndCountry,
                translate(
                    entries,
                    inputLanguageAndCountry,
                    outputLanguageAndCountry,
                    translationEngineParams)));
    return translations;
  }

  private Map<String, String> translate(
      final Map<String, String> entries,
      final LanguageAndCountry inputLanguageAndCountry,
      final LanguageAndCountry outputLanguageAndCountry,
      final TranslationEngineParams translationEngineParams) {
    var translations = new HashMap<String, String>();
    entries.forEach(
        (key, value) ->
            translations.put(
                key,
                StringUtils.isBlank(value)
                    ? StringUtils.EMPTY
                    : translate(
                        value,
                        inputLanguageAndCountry,
                        outputLanguageAndCountry,
                        translationEngineParams)));
    return translations;
  }

  private String translate(
      final String entry,
      final LanguageAndCountry inputLanguageAndCountry,
      final LanguageAndCountry outputLanguageAndCountry,
      final TranslationEngineParams translationEngineParams) {
    String inputLanguage = inputLanguageAndCountry.getLanguage();
    String outputLanguage = outputLanguageAndCountry.getLanguage();
    if (Stream.of(inputLanguage, outputLanguage).allMatch(StringUtils::isNotBlank)) {
      return translate(entry, inputLanguage, outputLanguage, translationEngineParams);
    }
    System.out.printf(
        "Both input and output languages should not be blank; inputLanguage: %s, outputLanguage: %s%n",
        inputLanguage, outputLanguage);
    return StringUtils.EMPTY;
  }

  private String translate(
      final String entry,
      final String inputLanguage,
      final String outputLanguage,
      final TranslationEngineParams translationEngineParams) {
    System.out.printf(
        "Connecting to translate %s from %s to %s%n", entry, inputLanguage, outputLanguage);
    try {
      var translateTextRequest = new LibreTranslateRequest(entry, inputLanguage, outputLanguage);
      var translateTextResponse =
          translationApi.translate(translationEngineParams, translateTextRequest);
      return translateTextResponse.translatedText();
    } catch (Exception e) {
      System.err.printf(
          "Error translating %s from %s to %s%n", entry, inputLanguage, outputLanguage);
    }
    return StringUtils.EMPTY;
  }
}
