package it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.pojos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.Collections;
import java.util.List;
import lombok.NonNull;

@RegisterForReflection
public record GoogleCloudTranslationV3Response(
    @NonNull List<GoogleCloudTranslationV3Translation> translations) {
  public static GoogleCloudTranslationV3Response EMPTY =
      new GoogleCloudTranslationV3Response(Collections.emptyList());
}
