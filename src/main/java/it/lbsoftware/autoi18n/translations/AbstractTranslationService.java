package it.lbsoftware.autoi18n.translations;

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
}
