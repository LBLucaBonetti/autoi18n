package it.lbsoftware.autoi18n.translations;

import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParams;

/**
 * A translation API should perform an HTTP call to the API service provider to get the translations
 *
 * @param <O> The HTTP response object class
 * @param <I> The HTTP request object class
 */
public interface TranslationApi<O, I> {

  /**
   * Performs the HTTP call to the API service provider to get the translations
   *
   * @param translationEngineParams The required params, validated by a {@code
   *     TranslationEngineParamsValidator} and subsequently provided by a {@code
   *     TranslationEngineParamsProvider}
   * @param request The HTTP request object
   * @return The HTTP response object
   */
  O translate(TranslationEngineParams translationEngineParams, I request);
}
