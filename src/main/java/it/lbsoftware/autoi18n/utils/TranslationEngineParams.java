package it.lbsoftware.autoi18n.utils;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class TranslationEngineParams {
  public static final String API_KEY_PARAM = "api-key";
  public static final String PROJECT_NUMBER_OR_ID_PARAM = "project-number-or-id";

  private String apiKey = StringUtils.EMPTY;
  private String projectNumberOrId = StringUtils.EMPTY;
}
