/*
 * Copyright 2019 Google Inc.
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

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;
/**
 * Code sample demonstrating Cloud Vision usage within the context of Spring Framework using Spring
 * Cloud GCP libraries. The sample is written as a Spring Boot application to demonstrate a
 * practical application of this usage.
 */
@RestController
public class VisionController {

  @Autowired private ResourceLoader resourceLoader;

  // [START vision_spring_autowire]
  @Autowired private CloudVisionTemplate cloudVisionTemplate;
  // [END vision_spring_autowire]

  /**
   * This method downloads an image from a URL and sends its contents to the Vision API for label
   * detection.
   *
   * @param imageUrl the URL of the image
   * @param map the model map to use
   * @return a string with the list of labels and percentage of certainty
   */
  @GetMapping("/extractLabels")
  public ModelAndView extractLabels(String imageUrl, ModelMap map) {
    // [START vision_spring_image_labelling]
    AnnotateImageResponse response =
        this.cloudVisionTemplate.analyzeImage(
            this.resourceLoader.getResource(imageUrl), Type.LABEL_DETECTION);

    Map<String, Float> imageLabels =
        response.getLabelAnnotationsList().stream()
            .collect(
                Collectors.toMap(
                    EntityAnnotation::getDescription,
                    EntityAnnotation::getScore,
                    (u, v) -> {
                      throw new IllegalStateException(String.format("Duplicate key %s", u));
                    },
                    LinkedHashMap::new));
    // [END vision_spring_image_labelling]

    map.addAttribute("annotations", imageLabels);
    map.addAttribute("imageUrl", imageUrl);

    return new ModelAndView("result", map);
  }

  @GetMapping("/extractText")
  public String extractText(String imageUrl) throws IOException {
    // [START vision_spring_text_extraction]
    String textFromImage =
        this.cloudVisionTemplate.extractTextFromImage(this.resourceLoader.getResource(imageUrl));

    TranslateText translateText = new TranslateText();
    String result = translateText.translateText(textFromImage);
    return "Text from image translated: " + result;
    // [END vision_spring_text_extraction]
  }
}