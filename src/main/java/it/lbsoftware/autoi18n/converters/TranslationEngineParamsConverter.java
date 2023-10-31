package it.lbsoftware.autoi18n.converters;

import it.lbsoftware.autoi18n.utils.TranslationEngineParams;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine.ITypeConverter;

public class TranslationEngineParamsConverter implements ITypeConverter<TranslationEngineParams> {

  @Override
  public TranslationEngineParams convert(final String value) {
    var translationEngineParams = new TranslationEngineParams();
    String[] split = value.split(",");
    for (String pair : split) {
      String[] pairSplit = pair.split("=");
      String paramKey = pairSplit[0];
      String paramValue = pairSplit.length > 1 ? pairSplit[1] : StringUtils.EMPTY;
      if (TranslationEngineParams.API_KEY_PARAM.equals(paramKey)) {
        translationEngineParams.setApiKey(paramValue);
      }
      if (TranslationEngineParams.PROJECT_NUMBER_OR_ID_PARAM.equals(paramKey)) {
        translationEngineParams.setProjectNumberOrId(paramValue);
      }
    }
    return translationEngineParams;
  }
}
