package it.lbsoftware.autoi18n;

import static it.lbsoftware.autoi18n.constants.Constants.AUTOI18N_NAME;

import it.lbsoftware.autoi18n.utils.VersionProvider;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

@Command(
    name = AUTOI18N_NAME,
    versionProvider = VersionProvider.class,
    mixinStandardHelpOptions = true)
public class Autoi18n implements Callable<Integer> {

  @Spec CommandSpec commandSpec;

  @Override
  public Integer call() {
    commandSpec.commandLine().usage(commandSpec.commandLine().getOut());
    return 0;
  }
}
