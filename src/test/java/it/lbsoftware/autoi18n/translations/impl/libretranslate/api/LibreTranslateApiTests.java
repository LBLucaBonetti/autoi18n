package it.lbsoftware.autoi18n.translations.impl.libretranslate.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.quarkus.test.junit.QuarkusTest;
import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParams;
import it.lbsoftware.autoi18n.translations.impl.libretranslate.pojos.LibreTranslateRequest;
import it.lbsoftware.autoi18n.translations.impl.libretranslate.pojos.LibreTranslateResponse;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
@SuppressWarnings("unchecked")
class LibreTranslateApiTests {

  @Test
  @DisplayName("Should return translated text")
  void test1() throws IOException, InterruptedException {
    // Given
    var httpClientProvider = Mockito.mock(LibreTranslateHttpClientProvider.class);
    HttpClient httpClient = Mockito.mock(HttpClient.class);
    HttpResponse<String> httpResponse = (HttpResponse<String>) Mockito.mock(HttpResponse.class);
    var entry = "ciao";
    var body = """
{
  "translatedText": "%s"
}
""".formatted(entry);
    when(httpResponse.body()).thenReturn(body);
    when(httpClientProvider.get()).thenReturn(httpClient);
    when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(httpResponse);
    var translationApi = new LibreTranslateApi(httpClientProvider);

    // When
    var res =
        translationApi.translate(
            TranslationEngineParams.EMPTY, new LibreTranslateRequest("hello", "en", "it"));

    // Then
    assertNotNull(res);
    assertEquals(entry, res.translatedText());
  }

  @Test
  @DisplayName("Could throw I/O exception")
  void test2() throws IOException, InterruptedException {
    // Given
    var httpClientProvider = Mockito.mock(LibreTranslateHttpClientProvider.class);
    HttpClient httpClient = Mockito.mock(HttpClient.class);
    when(httpClientProvider.get()).thenReturn(httpClient);
    when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenThrow(IOException.class);
    var translationApi = new LibreTranslateApi(httpClientProvider);

    // When
    var res =
        translationApi.translate(
            TranslationEngineParams.EMPTY, new LibreTranslateRequest("hello", "en", "it"));

    // Then
    assertNotNull(res);
    assertEquals(LibreTranslateResponse.EMPTY, res);
  }

  @Test
  @DisplayName("Could throw JsonProcessingException when reading response")
  void test3() throws IOException, InterruptedException {
    // Given
    var httpClientProvider = Mockito.mock(LibreTranslateHttpClientProvider.class);
    HttpClient httpClient = Mockito.mock(HttpClient.class);
    HttpResponse<String> httpResponse = (HttpResponse<String>) Mockito.mock(HttpResponse.class);
    var body = "invalid-JSON";
    when(httpResponse.body()).thenReturn(body);
    when(httpClientProvider.get()).thenReturn(httpClient);
    when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(httpResponse);
    var translationApi = new LibreTranslateApi(httpClientProvider);

    // When
    var res =
        translationApi.translate(
            TranslationEngineParams.EMPTY, new LibreTranslateRequest("hello", "en", "it"));

    // Then
    assertNotNull(res);
    assertEquals(LibreTranslateResponse.EMPTY, res);
  }
}
