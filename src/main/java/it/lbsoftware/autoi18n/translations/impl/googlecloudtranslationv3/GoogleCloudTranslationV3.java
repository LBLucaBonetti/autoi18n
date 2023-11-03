package it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3;

import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParams;
import it.lbsoftware.autoi18n.translations.AbstractTranslationService;
import it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.api.GoogleCloudTranslationV3Api;
import it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.pojos.GoogleCloudTranslationV3Request;
import it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.pojos.GoogleCloudTranslationV3Response;
import it.lbsoftware.autoi18n.utils.LanguageAndCountry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

public final class GoogleCloudTranslationV3
    extends AbstractTranslationService<
        GoogleCloudTranslationV3Response, GoogleCloudTranslationV3Request> {

  public GoogleCloudTranslationV3() {
    super(new GoogleCloudTranslationV3Api());
  }

  @Override
  public Map<LanguageAndCountry, Map<String, String>> translate(
      @NonNull final Map<String, String> entries,
      @NonNull final LanguageAndCountry inputLanguageAndCountry,
      @NonNull final Set<LanguageAndCountry> outputLanguageAndCountries,
      @NonNull final TranslationEngineParams translationEngineParams) {
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
    String inputLanguage = inputLanguageAndCountry.toString();
    String outputLanguage = outputLanguageAndCountry.toString();
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
      var translateTextRequest =
          new GoogleCloudTranslationV3Request(List.of(entry), inputLanguage, outputLanguage);
      var translateTextResponse =
          translationApi.translate(translationEngineParams, translateTextRequest);
      return translateTextResponse.translations().get(0).translatedText();
    } catch (Exception e) {
      System.err.printf(
          "Error translating %s from %s to %s%n", entry, inputLanguage, outputLanguage);
    }
    return StringUtils.EMPTY;
  }
}
