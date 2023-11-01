package it.lbsoftware.autoi18n.facade;

import it.lbsoftware.autoi18n.translations.TranslationEngine;
import it.lbsoftware.autoi18n.utils.LanguageAndCountry;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
    var outputLanguageAndCountriesNoDuplicates =
        outputLanguageAndCountries.stream()
            .filter((LanguageAndCountry lac) -> !inputLanguageAndCountry.equals(lac))
            .collect(Collectors.toSet());
    System.out.println("Detected input language: " + inputLanguageAndCountry.toString());
    System.out.println("Detected output languages: " + outputLanguageAndCountriesNoDuplicates);
    System.out.println("Translation engine: " + translationEngine.getName());
    System.out.println(
        translationService.translate(
            entries,
            inputLanguageAndCountry,
            outputLanguageAndCountriesNoDuplicates,
            translationEngineParams));
    return ExitCode.OK;
  }
}
