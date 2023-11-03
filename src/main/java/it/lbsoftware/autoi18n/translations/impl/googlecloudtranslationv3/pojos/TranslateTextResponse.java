package it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.pojos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.Collections;
import java.util.List;

@RegisterForReflection
public record TranslateTextResponse(List<Translation> translations) {
  public static TranslateTextResponse EMPTY = new TranslateTextResponse(Collections.emptyList());
}
