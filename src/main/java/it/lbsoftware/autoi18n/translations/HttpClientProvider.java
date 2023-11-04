package it.lbsoftware.autoi18n.translations;

import java.net.http.HttpClient;

public interface HttpClientProvider {

  HttpClient get();
}
