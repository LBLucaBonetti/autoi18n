package it.lbsoftware.autoi18n.constants;

import java.io.File;
import org.apache.commons.io.FileUtils;

public final class Constants {

  public static final String AUTOI18N_NAME = "autoi18n";
  public static final String OPTION_SHORT_TRANSLATION_ENGINE = "-t";
  public static final String OPTION_LONG_TRANSLATION_ENGINE = "--translation-engine";
  public static final String OPTION_LONG_TRANSLATION_ENGINE_PARAMS = "--translation-engine-params";
  public static final String OPTION_SHORT_BASE_DIRECTORY = "-b";
  public static final String OPTION_LONG_BASE_DIRECTORY = "--base-directory";
  public static final File DEFAULT_BASE_DIRECTORY = FileUtils.current();
  public static final String DEFAULT_BACKUP_DIRECTORY_NAME = AUTOI18N_NAME + "-backup";
  public static final String OPTION_SHORT_OVERWRITE_ENTRIES = "-o";
  public static final String OPTION_LONG_OVERWRITE_ENTRIES = "--overwrite-entries";

  private Constants() {
    throw new UnsupportedOperationException("This class cannot be instantiated");
  }
}
