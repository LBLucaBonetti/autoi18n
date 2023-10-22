package it.lbsoftware.autoi18n;

import static it.lbsoftware.autoi18n.constants.Constants.AUTOI18N_NAME;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_LONG_ENTRY;
import static it.lbsoftware.autoi18n.constants.Constants.OPTION_SHORT_ENTRY;

import it.lbsoftware.autoi18n.utils.VersionProvider;
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
      names = {OPTION_SHORT_ENTRY, OPTION_LONG_ENTRY},
      mapFallbackValue = StringUtils.EMPTY,
      split = ",",
      splitSynopsisLabel = ",",
      required = true,
      description =
          "A key-value to translate; an unspecified or blank value will not be translated nor inserted into language files.")
  private Map<String, String> entries;

  @Spec private CommandSpec commandSpec;

  @Override
  public Integer call() {
    commandSpec.commandLine().usage(commandSpec.commandLine().getOut());
    return 0;
  }
}
