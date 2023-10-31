package it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.lbsoftware.autoi18n.converters.JacksonConfig;
import it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.pojos.TranslateTextRequest;
import it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.pojos.TranslateTextResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;

public class GoogleCloudTranslationV3Api {

  private static final String BASE_URI = "https://translate.googleapis.com/v3/projects";

  public TranslateTextResponse translate(
      final String apiKey,
      final String projectNumberOrId,
      final TranslateTextRequest translateTextRequest) {
    String uri = BASE_URI + "/" + projectNumberOrId + ":translateText";
    String body = requestToBody(translateTextRequest);
    HttpRequest request =
        HttpRequest.newBuilder(URI.create(uri))
            .POST(BodyPublishers.ofString(body))
            .header("Authorization", "Bearer " + apiKey)
            .header("x-goog-user-project", projectNumberOrId)
            .build();
    try (HttpClient client = HttpClient.newBuilder().build()) {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      return bodyToResponse(response.body());
    } catch (IOException ioException) {
      System.err.println("I/O exception");
    } catch (InterruptedException interruptedException) {
      System.err.println("Operation interrupted");
      Thread.currentThread().interrupt();
    }
    return new TranslateTextResponse(Collections.emptyList());
  }

  private String requestToBody(final TranslateTextRequest translateTextRequest) {
    try {
      return JacksonConfig.INSTANCE.getObjectMapper().writeValueAsString(translateTextRequest);
    } catch (JsonProcessingException e) {
      System.err.println("Could not convert the request to a meaningful request body");
    }
    return StringUtils.EMPTY;
  }

  private TranslateTextResponse bodyToResponse(final String body) {
    try {
      return JacksonConfig.INSTANCE.getObjectMapper().readValue(body, TranslateTextResponse.class);
    } catch (JsonProcessingException e) {
      System.err.println("Could not convert the response body to a meaningful response");
    }
    return new TranslateTextResponse(Collections.emptyList());
  }
}
