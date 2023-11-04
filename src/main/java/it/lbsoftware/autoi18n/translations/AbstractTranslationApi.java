package it.lbsoftware.autoi18n.translations;

public abstract class AbstractTranslationApi<O, I> implements TranslationApi<O, I> {

  protected final HttpClientProvider httpClientProvider;

  protected AbstractTranslationApi(final HttpClientProvider httpClientProvider) {
    this.httpClientProvider = httpClientProvider;
  }
}
