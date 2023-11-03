package it.lbsoftware.autoi18n.paramsproviders.impl.libretranslate;

import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParams;
import it.lbsoftware.autoi18n.paramsproviders.TranslationEngineParamsProvider;
import java.util.Map;

public class LibreTranslateParamsProvider implements TranslationEngineParamsProvider {

  @Override
  public TranslationEngineParams provide(Map<String, String> params) {
    return TranslationEngineParams.EMPTY;
  }
}
