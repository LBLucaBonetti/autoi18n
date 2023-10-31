package it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3;

import it.lbsoftware.autoi18n.converters.LocaleTypeConverter;
import it.lbsoftware.autoi18n.translations.TranslationEngine;
import it.lbsoftware.autoi18n.translations.TranslationService;
import it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.api.GoogleCloudTranslationV3Api;
import it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.pojos.TranslateTextRequest;
import it.lbsoftware.autoi18n.utils.TranslationEngineParams;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

public class GoogleCloudTranslationV3 implements TranslationService {

  @Override
  public Map<Locale, Map<String, String>> translate(
      @NonNull final Map<String, String> entries,
      @NonNull final Locale inputLocale,
      @NonNull final Set<Locale> outputLocales,
      @NonNull final TranslationEngineParams translationEngineParams) {
    var translations = new HashMap<Locale, Map<String, String>>();
    outputLocales.forEach(
        (Locale outputLocale) ->
            translations.put(
                outputLocale,
                translate(entries, inputLocale, outputLocale, translationEngineParams)));
    return translations;
  }

  private Map<String, String> translate(
      final Map<String, String> entries,
      final Locale inputLocale,
      final Locale outputLocale,
      final TranslationEngineParams translationEngineParams) {
    var translations = new HashMap<String, String>();
    entries.forEach(
        (key, value) ->
            translations.put(
                key,
                StringUtils.isBlank(value)
                    ? StringUtils.EMPTY
                    : translate(value, inputLocale, outputLocale, translationEngineParams)));
    return translations;
  }

  private String translate(
      final String entry,
      final Locale inputLocale,
      final Locale outputLocale,
      final TranslationEngineParams translationEngineParams) {
    String inputLanguage = LocaleTypeConverter.getLanguageAndRegion(inputLocale);
    String outputLanguage = LocaleTypeConverter.getLanguageAndRegion(outputLocale);
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
        "Connecting to translate %s from %s to %s with %s%n",
        entry,
        inputLanguage,
        outputLanguage,
        TranslationEngine.GOOGLE_CLOUD_TRANSLATION_V3.getName());
    try {
      var translateTextRequest =
          new TranslateTextRequest(List.of(entry), inputLanguage, outputLanguage);
      var translateTextResponse =
          new GoogleCloudTranslationV3Api()
              .translate(
                  translationEngineParams.getApiKey(),
                  translationEngineParams.getProjectNumberOrId(),
                  translateTextRequest);
      return translateTextResponse.translations().get(0).translatedText();
    } catch (Exception e) {
      System.err.printf(
          "Error translating %s from %s to %s%n", entry, inputLanguage, outputLanguage);
    }
    return StringUtils.EMPTY;
  }
}
