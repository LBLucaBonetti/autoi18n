package it.lbsoftware.autoi18n.translations;

import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParams;
import it.lbsoftware.autoi18n.utils.LanguageAndCountry;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

/**
 * A translation service wraps and handles the logic required to call the translation calls to the
 * service provider API, using a {@code TranslationApi}
 *
 * @param <O> The HTTP response object class for the {@code TranslationApi}
 * @param <I> The HTTP request object class for the {@code TranslationApi}
 */
public abstract class AbstractTranslationService<O, I> implements TranslationService {

  protected final TranslationApi<O, I> translationApi;

  protected AbstractTranslationService(final TranslationApi<O, I> translationApi) {
    this.translationApi = translationApi;
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

  protected abstract String translate(
      final String entry,
      final String inputLanguage,
      final String outputLanguage,
      final TranslationEngineParams translationEngineParams);
}
