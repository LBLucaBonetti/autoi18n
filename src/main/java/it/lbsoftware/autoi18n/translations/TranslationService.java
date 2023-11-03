package it.lbsoftware.autoi18n.translations;

import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParams;
import it.lbsoftware.autoi18n.utils.LanguageAndCountry;
import java.util.Map;
import java.util.Set;

public interface TranslationService {

  /**
   * Translates the given entries; if a single translation element fails, its value will be empty,
   * but the key will always be provided
   *
   * @param entries Key-value pairs to translate
   * @param inputLanguageAndCountry The source language (and country) of the entries
   * @param outputLanguageAndCountries The target languages (and countries) for
   *     googleCloudTranslationV3Translations
   * @param translationEngineParams The additional parameters for the translation engine; it should
   *     contain at least the required params, or the calls may fail
   * @return A data structure associating each target language (and country) with its corresponding
   *     set of key-value pairs translated
   */
  Map<LanguageAndCountry, Map<String, String>> translate(
      Map<String, String> entries,
      LanguageAndCountry inputLanguageAndCountry,
      Set<LanguageAndCountry> outputLanguageAndCountries,
      TranslationEngineParams translationEngineParams);
}
