package it.lbsoftware.autoi18n.translations.impl.libretranslate.api;

import it.lbsoftware.autoi18n.translations.HttpClientProvider;
import java.net.http.HttpClient;

public class LibreTranslateHttpClientProvider implements HttpClientProvider {

  @Override
  public HttpClient get() {
    return HttpClient.newBuilder().build();
  }
}
