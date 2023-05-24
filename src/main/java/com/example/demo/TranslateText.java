 /*
* Copyright 2020 Google LLC
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.demo;

// [START translate_v3_translate_text]
// [START translate_v3_translate_text_0]
// Imports the Google Cloud Translation library.
import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.Translation;
import com.google.cloud.translate.v3.TranslationServiceClient;
import java.io.IOException;

// [END translate_v3_translate_text_0]

public class TranslateText {

 // [START translate_v3_translate_text_1]
 // Set and pass variables to overloaded translateText() method for translation.
 public static String translateText(String inp) throws IOException {
   // TODO(developer): Replace these variables before running the sample.
   String projectId = "abis-345004";
   // Supported Languages: https://cloud.google.com/translate/docs/languages
   String targetLanguage = "English";
   String text = "ஆயிரம் தோல்விகளை நீ ஒழ் இலப்புதில் அடைந்தாலும் சற்றும் அணைக்காத, போராட்ட குணம் ஒன்று உள்ளிடன் இருக்குமேயானால் இந்த உலகில் உன் போல வெற்றியாளன் இன்னும் உாைககில நன்றி ழங்தியர்";
   text = inp;
   return translateText(projectId, targetLanguage, text);
 }
 // [END translate_v3_translate_text_1]

 // [START translate_v3_translate_text_2]
 // Translate text to target language.
 public static String translateText(String projectId, String targetLanguage, String text)
     throws IOException {
String res = " ::: ";
   // Initialize client that will be used to send requests. This client only needs to be created
   // once, and can be reused for multiple requests. After completing all of your requests, call
   // the "close" method on the client to safely clean up any remaining background resources.
   try (TranslationServiceClient client = TranslationServiceClient.create()) {
     // Supported Locations: `global`, [glossary location], or [model location]
     // Glossaries must be hosted in `us-central1`
     // Custom Models must use the same location as your model. (us-central1)
     LocationName parent = LocationName.of(projectId, "global");

     // Supported Mime Types: https://cloud.google.com/translate/docs/supported-formats
     TranslateTextRequest request =
         TranslateTextRequest.newBuilder()
             .setParent(parent.toString())
             .setMimeType("text/plain")
             .setTargetLanguageCode("en")
             .addContents(text)
             .build();

     TranslateTextResponse response = client.translateText(request);
     // Display the translation for each input text provided
     for (Translation translation : response.getTranslationsList()) {
       res = res + " ::: " + translation.getTranslatedText();
        System.out.printf("Translated text : %s\n", res);
     }
   }
   return res;
 }
 // [END translate_v3_translate_text_2]
}

// [END translate_v3_translate_text] TranslateText {
