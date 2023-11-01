package it.lbsoftware.autoi18n.facade;

import it.lbsoftware.autoi18n.translations.TranslationEngine;
import it.lbsoftware.autoi18n.utils.LanguageAndCountry;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import picocli.CommandLine.ExitCode;

@RequiredArgsConstructor
public final class TranslationEngineFacade {

  private final TranslationEngine translationEngine;
  private final Map<String, String> params;
  private final Map<String, String> entries;
  private final LanguageAndCountry inputLanguageAndCountry;
  private final List<LanguageAndCountry> outputLanguageAndCountries;

  /**
   * Performs a whole translation operation and returns the resulting exit code
   *
   * @return The exit code for the whole translation operation
   */
  public Integer performTranslation() {
    var translationEngineParamsValidator = translationEngine.getTranslationEngineParamsValidator();
    if (!translationEngineParamsValidator.validate(params)) {
      System.err.println(
          "Invalid set of parameters for the translation engine "
              + translationEngine.getName()
              + ". See <translationEngine> description for more details");
      return ExitCode.USAGE;
    }
    var translationEngineParamsProvider = translationEngine.getTranslationEngineParamsProvider();
    var translationEngineParams = translationEngineParamsProvider.provide(params);
    var translationService = translationEngine.getTranslationService();
    System.out.println(
        translationService.translate(
            entries,
            inputLanguageAndCountry,
            new HashSet<>(outputLanguageAndCountries),
            translationEngineParams));
    return ExitCode.OK;
  }
}
