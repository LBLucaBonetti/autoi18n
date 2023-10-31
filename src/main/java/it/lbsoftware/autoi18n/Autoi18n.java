package it.lbsoftware.autoi18n;

import static it.lbsoftware.autoi18n.constants.Constants.AUTOI18N_NAME;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_LONG_ENTRY;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_LONG_INPUT_LANGUAGE;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_LONG_OUTPUT_LANGUAGES;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_LONG_TRANSLATION_ENGINE;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_LONG_TRANSLATION_ENGINE_PARAMS;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_SHORT_ENTRY;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_SHORT_INPUT_LANGUAGE;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_SHORT_OUTPUT_LANGUAGES;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_SHORT_TRANSLATION_ENGINE;

import it.lbsoftware.autoi18n.converters.LocaleTypeConverter;
import it.lbsoftware.autoi18n.facade.TranslationEngineFacade;
import it.lbsoftware.autoi18n.translations.TranslationEngine;
import it.lbsoftware.autoi18n.utils.VersionProvider;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@Command(
    name = AUTOI18N_NAME,
    versionProvider = VersionProvider.class,
    mixinStandardHelpOptions = true)
public class Autoi18n implements Callable<Integer> {

  @Getter
  @Option(
      names = {OPTION_SHORT_OUTPUT_LANGUAGES, OPTION_LONG_OUTPUT_LANGUAGES},
      required = true,
      description =
          "The target languages, according to the ISO 639 alpha-2 or alpha-3 standard, case insensitive.",
      paramLabel = "<output-language>",
      split = ",",
      splitSynopsisLabel = ",",
      converter = LocaleTypeConverter.class)
  private List<Locale> outputLocales;

  @Getter
  @Option(
      names = {OPTION_SHORT_ENTRY, OPTION_LONG_ENTRY},
      required = true,
      description =
          "Key-value item(s) to translate; if multiple key-value pairs are specified, they need to share a common source language (<input-language> option); an unspecified or blank value will not be translated nor inserted into language files; whenever an error occurs during the translation of one or multiple entries, the resulting output string will be empty",
      paramLabel = "<key>=<value>",
      mapFallbackValue = StringUtils.EMPTY,
      split = ",",
      splitSynopsisLabel = ",")
  private Map<String, String> entries;

  @Getter
  @Option(
      names = {OPTION_SHORT_INPUT_LANGUAGE, OPTION_LONG_INPUT_LANGUAGE},
      required = true,
      description =
          "The source language, according to the ISO 639 alpha-2 or alpha-3 standard, case insensitive.",
      paramLabel = "<input-language>",
      converter = LocaleTypeConverter.class)
  private Locale inputLocale;

  @Getter
  @Option(
      names = {OPTION_SHORT_TRANSLATION_ENGINE, OPTION_LONG_TRANSLATION_ENGINE},
      required = false,
      description = {
        "The translation engine to use; the following engines are actually available:",
        "* Google Cloud Translation v3",
        "  Parameters via --translation-engine-params:",
        "  * api-key: the API key to use",
        "  * project-number-or-id: the Google Cloud project or id to use"
      },
      paramLabel = "<translationEngine>")
  private TranslationEngine translationEngine = TranslationEngine.GOOGLE_CLOUD_TRANSLATION_V3;

  @Getter
  @Option(
      names = {OPTION_LONG_TRANSLATION_ENGINE_PARAMS},
      required = false,
      description =
          "Additional key-value pairs to customize the translation engine; see <translationEngine> description for more details",
      paramLabel = "<key>=<value>",
      mapFallbackValue = StringUtils.EMPTY,
      split = ",",
      splitSynopsisLabel = ",")
  private Map<String, String> params = new HashMap<>();

  @Spec private CommandSpec commandSpec;

  @Override
  public Integer call() {
    return new TranslationEngineFacade(
            translationEngine, params, entries, inputLocale, outputLocales)
        .performTranslation();
  }
}
