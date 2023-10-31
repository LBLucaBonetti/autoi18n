package it.lbsoftware.autoi18n.paramsproviders;

public record TranslationEngineParams(String apiKey, String projectNumberOrId) {

  public static final String API_KEY_PARAM = "api-key";
  public static final String PROJECT_NUMBER_OR_ID_PARAM = "project-number-or-id";
}
