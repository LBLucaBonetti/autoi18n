package it.lbsoftware.autoi18n;

import static it.lbsoftware.autoi18n.constants.Constants.AUTOI18N_NAME;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_LONG_BASE_DIRECTORY;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_LONG_OVERWRITE_ENTRIES;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_LONG_TRANSLATION_ENGINE;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_LONG_TRANSLATION_ENGINE_PARAMS;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_SHORT_BASE_DIRECTORY;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_SHORT_OVERWRITE_ENTRIES;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_SHORT_TRANSLATION_ENGINE;

import it.lbsoftware.autoi18n.converters.BaseDirectoryConverter;
import it.lbsoftware.autoi18n.converters.LanguageAndCountryTypeConverter;
import it.lbsoftware.autoi18n.facade.TranslationEngineFacade;
import it.lbsoftware.autoi18n.translations.TranslationEngine;
import it.lbsoftware.autoi18n.utils.LanguageAndCountry;
import it.lbsoftware.autoi18n.utils.VersionProvider;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

@Command(
    name = AUTOI18N_NAME,
    versionProvider = VersionProvider.class,
    mixinStandardHelpOptions = true)
public class Autoi18n implements Callable<Integer> {

  @Getter
  @Parameters(
      index = "1",
      description =
          "The target languages, according to the ISO 639 alpha-2 or alpha-3 standard, case insensitive; beware that no checks are carried out beforehand to verify that the chosen translation engine effectively supports this language.",
      paramLabel = "<output-language-and-country>",
      split = ",",
      splitSynopsisLabel = ",",
      converter = LanguageAndCountryTypeConverter.class)
  private List<LanguageAndCountry> outputLanguageAndCountries;

  @Getter
  @Parameters(
      index = "2..*",
      description =
          "Key-value item(s) to translate; if multiple key-value pairs are specified, they need to share a common source language (<input-language-and-country> option); an unspecified or blank value will not be translated nor inserted into language files; whenever an error occurs during the translation of one or multiple entries, the resulting output strings will be empty.",
      paramLabel = "<key>=<value>",
      mapFallbackValue = StringUtils.EMPTY)
  private Map<String, String> entries;

  @Getter
  @Parameters(
      index = "0",
      description =
          "The source language, according to the ISO 639 alpha-2 or alpha-3 standard, case insensitive; beware that no checks are carried out beforehand to verify that the chosen translation engine effectively supports this language.",
      paramLabel = "<input-language-and-country>",
      converter = LanguageAndCountryTypeConverter.class)
  private LanguageAndCountry inputLanguageAndCountry;

  @Getter
  @Option(
      names = {OPTION_SHORT_TRANSLATION_ENGINE, OPTION_LONG_TRANSLATION_ENGINE},
      required = false,
      description = {
        "The translation engine to use; the following engines are actually available:",
        "* GOOGLE_CLOUD_TRANSLATION_V3",
        "  Parameters via --translation-engine-params:",
        "  * api-key: the API key to use",
        "  * project-number-or-id: the Google Cloud project number or id to use",
        "* LIBRE_TRANSLATE",
        "  Parameters via --translation-engine-params:",
        "  <no parameters required>"
      },
      paramLabel = "<translationEngine>")
  private TranslationEngine translationEngine = TranslationEngine.GOOGLE_CLOUD_TRANSLATION_V3;

  @Getter
  @Option(
      names = {OPTION_LONG_TRANSLATION_ENGINE_PARAMS},
      required = false,
      description =
          "Additional key-value pairs to customize the translation engine; see <translationEngine> description for more details.",
      paramLabel = "<key>=<value>",
      mapFallbackValue = StringUtils.EMPTY,
      split = ",",
      splitSynopsisLabel = ",")
  private Map<String, String> params = new HashMap<>();

  @Getter
  @Option(
      names = {OPTION_SHORT_BASE_DIRECTORY, OPTION_LONG_BASE_DIRECTORY},
      required = false,
      description =
          "The base directory to search Property Resource Bundle files from, recursively; if the specified path is invalid, the current directory will be used instead, recursively.",
      paramLabel = "<baseDirectory>",
      converter = BaseDirectoryConverter.class)
  private File baseDirectory;

  @Getter
  @Option(
      names = {OPTION_SHORT_OVERWRITE_ENTRIES, OPTION_LONG_OVERWRITE_ENTRIES},
      required = false,
      description =
          "If set, the already-present entries in target Property Resource Bundle files will be silently overwritten; defaults to false",
      paramLabel = "<overwrite-entries>")
  private boolean overwriteEntries;

  @Spec private CommandSpec commandSpec;

  @Override
  public Integer call() {
    return new TranslationEngineFacade(
            translationEngine,
            params,
            entries,
            inputLanguageAndCountry,
            outputLanguageAndCountries,
            baseDirectory,
            overwriteEntries)
        .performTranslation();
  }
}
