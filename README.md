# autoi18n

autoi18n is a CLI tool to automatically insert entries translated from a source to one or multiple
target
languages into [Property Resource Bundle](https://en.wikipedia.org/wiki/.properties) files.

The following assumptions should be kept in mind before using autoi18n:

- I/O operations with Property Resource Bundle files are performed with ISO-8859-1 character
  encoding
- Every comment and blank line of a Property Resource Bundle file will be stripped out while writing
  to it, as
  per [java.util.Properties](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Properties.html)
  standard JDK class
- Every character that needs to be escaped will be escaped, as
  per java.util.Properties standard JDK class
- The input order of keys will be preserved when writing to a Property Resource Bundle output file
- A backup for each output Property Resource Bundle file will be performed before writing to it, in
  a default autoi18n-backup folder in the base directory
- Property Resource Bundle files will have to be valid, have the ```.properties``` file extension,
  are matched to their respective language by their file name; valid file names are validated
  according to the
  regex ```(?i)(label|labels|language|languages|translation|translations)(_|-)languageAndCountry.properties```
  where ```languageAndCountry``` is the output language and country specified as the output language
  and country positional parameter
- Only ```.properties``` files are currently supported; the ```XML``` file support will be
  implemented in
  a
  future release

### Translation engines

The following translation engines are currently supported and tested:

- Google Cloud Translation V3 (the actual default, specified as ```GOOGLE_CLOUD_TRANSLATION_V3```)
    * Required parameters
        * ```api-key```: the API key to be used for translations
        * ```project-number-or-id```: the project number or id to be used for translations
- LibreTranslate, currently expected to run at ```localhost:5000``` (specified
  as ```LIBRE_TRANSLATE```)

### Parameters and options

#### Positional parameters

- ```Input language and country```: the source language, according to the ISO 639 alpha-2 or
  alpha-3 standard, case-insensitive; beware that no checks are carried out beforehand to verify
  that the chosen translation engine effectively supports this language
- ```Output language and country```: the target languages, according to the ISO 639 alpha-2 or
  alpha-3 standard, case-insensitive; beware that no checks are carried out beforehand to verify
  that the chosen translation engine effectively supports this language
- ```Entries```: key-value item(s) to translate; if multiple key-value pairs are specified, they
  need
  to share a common source language (```<input-language-and-country>``` option); an unspecified or
  blank value will not be translated nor inserted into language files; whenever an error occurs
  during the translation of one or multiple entries, the resulting output strings will be empty

#### Options

- ```-t```/```--translation-engine```: the translation engine to use; each translation engine needs
  its set of parameters to property work, as specified in the appropriate section of this
  documentation
- ```--translation-engine-params```: additional key-value pairs to customize the translation engine;
  see <translationEngine> description for more details
- ```-b```/```--base-directory```: the base directory to search Property Resource Bundle files from,
  recursively; if the specified path is invalid, the current directory will be used instead,
  recursively
- ```-o```/```--overwrite-entries```: if set, the already-present entries in target Property
  Resource Bundle files will be silently overwritten; defaults to false

### Usage example

To translate with Google Cloud Translation V3 (with an active project called ```autoi18n``` and the
gcloud CLI tool correctly installed and available to the terminal environment) the
sentence ```the book is on the table```
from ```en```
to ```it``` and ```fr``` and insert
the entry with a key of ```key1``` into ```en```, ```it``` and ```fr``` Property Resource Bundle
files in the ```/home/projects/project/languages``` base
directory,
use:

```autoi18n --base-directory=/home/projects/project/languages --translation-engine-params=api-key=$(gcloud auth print-access-token),project-number-or-id=autoi18n en en,it,fr "key1=the book is on the table"```

## Technologies

This is a Java 21, Quarkus and picocli application, tested and built natively with GraalVM for
Linux,
macOS and Windows operating systems.

## Authors

- [@LBLucaBonetti](https://www.github.com/LBLucaBonetti)

## Badges

[![GitHub license](https://img.shields.io/github/license/LBLucaBonetti/autoi18n)](https://github.com/LBLucaBonetti/autoi18n/blob/main/LICENSE)
[![Quality gate status (backend)](https://sonarcloud.io/api/project_badges/measure?project=LBLucaBonetti_autoi18n&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=LBLucaBonetti_autoi18n)