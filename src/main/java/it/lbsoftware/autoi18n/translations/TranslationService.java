package it.lbsoftware.autoi18n.translations;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

public interface TranslationService {

  /**
   * Translates the given entries; if a single translation element fails, its value will be empty,
   * but the key will always be provided
   *
   * @param entries Key-value pairs to translate
   * @param inputLocale The source language of the entries
   * @param outputLocales The target languages for translations
   * @return A data structure associating each target language with its corresponding set of
   *     key-value pairs translated
   */
  Map<Locale, Map<String, String>> translate(
      Map<String, String> entries, Locale inputLocale, Set<Locale> outputLocales);
}