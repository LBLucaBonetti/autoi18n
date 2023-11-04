package it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.lbsoftware.autoi18n.config.JacksonConfig;
import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParams;
import it.lbsoftware.autoi18n.translations.AbstractTranslationApi;
import it.lbsoftware.autoi18n.translations.HttpClientProvider;
import it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.pojos.GoogleCloudTranslationV3Request;
import it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.pojos.GoogleCloudTranslationV3Response;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import org.apache.commons.lang3.StringUtils;

public class GoogleCloudTranslationV3Api
    extends AbstractTranslationApi<
        GoogleCloudTranslationV3Response, GoogleCloudTranslationV3Request> {

  private static final String BASE_URI = "https://translate.googleapis.com/v3/projects";

  public GoogleCloudTranslationV3Api(final HttpClientProvider httpClientProvider) {
    super(httpClientProvider);
  }

  @Override
  public GoogleCloudTranslationV3Response translate(
      final TranslationEngineParams translationEngineParams,
      final GoogleCloudTranslationV3Request googleCloudTranslationV3Request) {
    String uri = BASE_URI + "/" + translationEngineParams.projectNumberOrId() + ":translateText";
    String body = requestToBody(googleCloudTranslationV3Request);
    HttpRequest request =
        HttpRequest.newBuilder(URI.create(uri))
            .POST(BodyPublishers.ofString(body))
            .header("Authorization", "Bearer " + translationEngineParams.apiKey())
            .header("x-goog-user-project", translationEngineParams.projectNumberOrId())
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
    return GoogleCloudTranslationV3Response.EMPTY;
  }

  private String requestToBody(
      final GoogleCloudTranslationV3Request googleCloudTranslationV3Request) {
    try {
      return JacksonConfig.INSTANCE
          .getObjectMapper()
          .writeValueAsString(googleCloudTranslationV3Request);
    } catch (JsonProcessingException e) {
      System.err.println("Could not convert the request to a meaningful request body");
    }
    return StringUtils.EMPTY;
  }

  private GoogleCloudTranslationV3Response bodyToResponse(final String body) {
    try {
      return JacksonConfig.INSTANCE
          .getObjectMapper()
          .readValue(body, GoogleCloudTranslationV3Response.class);
    } catch (JsonProcessingException e) {
      System.err.println("Could not convert the response body to a meaningful response");
    }
    return GoogleCloudTranslationV3Response.EMPTY;
  }
}
