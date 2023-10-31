package it.lbsoftware.autoi18n.paramsproviders.impl.googlecloudtranslationv3;

import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParams;
import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParamsProvider;
import java.util.Map;

public class GoogleCloudTranslationV3ParamsProvider implements TranslationEngineParamsProvider {

  @Override
  public TranslationEngineParams provide(final Map<String, String> params) {
    return new TranslationEngineParams(
        params.get(TranslationEngineParams.API_KEY_PARAM),
        params.get(TranslationEngineParams.PROJECT_NUMBER_OR_ID_PARAM));
  }
}
