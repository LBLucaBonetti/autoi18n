package it.lbsoftware.autoi18n.converters;

import static it.lbsoftware.autoi18n.constants.Constants.DEFAULT_BASE_DIRECTORY;

import java.io.File;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine.ITypeConverter;

public class BaseDirectoryConverter implements ITypeConverter<File> {

  @Override
  public File convert(final String value) {
    if (StringUtils.isBlank(value)) {
      return DEFAULT_BASE_DIRECTORY;
    }
    return Optional.of(new File(value)).filter(this::isValid).orElse(DEFAULT_BASE_DIRECTORY);
  }

  private boolean isValid(final File baseDirectory) {
    return baseDirectory.exists()
        && baseDirectory.isDirectory()
        && baseDirectory.canRead()
        && baseDirectory.canWrite();
  }
}
