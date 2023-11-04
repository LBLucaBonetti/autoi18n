package it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.api;

import it.lbsoftware.autoi18n.translations.HttpClientProvider;
import java.net.http.HttpClient;

public class GoogleCloudTranslationV3HttpClientProvider implements HttpClientProvider {

  @Override
  public HttpClient get() {
    return HttpClient.newBuilder().build();
  }
}
