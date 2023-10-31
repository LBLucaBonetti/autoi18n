package it.lbsoftware.autoi18n.validators.impl.googlecloudtranslationv3;

import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParams;
import it.lbsoftware.autoi18n.validators.TranslationEngineParamsValidator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class GoogleCloudTranslationV3ParamsValidator implements TranslationEngineParamsValidator {

  @Override
  public boolean validate(final Map<String, String> params) {
    var requiredParams =
        Set.of(
            TranslationEngineParams.API_KEY_PARAM,
            TranslationEngineParams.PROJECT_NUMBER_OR_ID_PARAM);
    return requiredParams.stream().map(params::get).allMatch(StringUtils::isNotBlank);
  }
}
