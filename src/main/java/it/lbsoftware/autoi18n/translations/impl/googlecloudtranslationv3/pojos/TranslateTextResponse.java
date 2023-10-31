package it.lbsoftware.autoi18n.translations.impl.googlecloudtranslationv3.pojos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.List;

@RegisterForReflection
public record TranslateTextResponse(List<Translation> translations) {}
