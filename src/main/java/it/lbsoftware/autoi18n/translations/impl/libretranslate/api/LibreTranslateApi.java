package it.lbsoftware.autoi18n.translations.impl.libretranslate.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.lbsoftware.autoi18n.config.JacksonConfig;
import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParams;
import it.lbsoftware.autoi18n.translations.AbstractTranslationApi;
import it.lbsoftware.autoi18n.translations.HttpClientProvider;
import it.lbsoftware.autoi18n.translations.impl.libretranslate.pojos.LibreTranslateRequest;
import it.lbsoftware.autoi18n.translations.impl.libretranslate.pojos.LibreTranslateResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import org.apache.commons.lang3.StringUtils;

public class LibreTranslateApi
    extends AbstractTranslationApi<LibreTranslateResponse, LibreTranslateRequest> {

  private static final String BASE_URI = "http://localhost:5000/translate";

  public LibreTranslateApi(final HttpClientProvider httpClientProvider) {
    super(httpClientProvider);
  }

  @Override
  public LibreTranslateResponse translate(
      TranslationEngineParams translationEngineParams,
      LibreTranslateRequest libreTranslateRequest) {
    String body = requestToBody(libreTranslateRequest);
    HttpRequest request =
        HttpRequest.newBuilder(URI.create(BASE_URI))
            .POST(BodyPublishers.ofString(body))
            .header("Content-Type", "application/json")
            .build();
    try (HttpClient client = httpClientProvider.get()) {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      return bodyToResponse(response.body());
    } catch (IOException ioException) {
      System.err.println("I/O exception");
    } catch (InterruptedException interruptedException) {
      System.err.println("Operation interrupted");
      Thread.currentThread().interrupt();
    }
    return LibreTranslateResponse.EMPTY;
  }

  private String requestToBody(final LibreTranslateRequest libreTranslateRequest) {
    try {
      return JacksonConfig.INSTANCE.getObjectMapper().writeValueAsString(libreTranslateRequest);
    } catch (JsonProcessingException e) {
      System.err.println("Could not convert the request to a meaningful request body");
    }
    return StringUtils.EMPTY;
  }

  private LibreTranslateResponse bodyToResponse(final String body) {
    try {
      return JacksonConfig.INSTANCE.getObjectMapper().readValue(body, LibreTranslateResponse.class);
    } catch (JsonProcessingException e) {
      System.err.println("Could not convert the response body to a meaningful response");
    }
    return LibreTranslateResponse.EMPTY;
  }
}
