/*
 * Copyright 2016 Rodrigo Agerri

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package eus.ixa.ixa.pipe.ml.features;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.featuregen.ArtifactToSerializerMapper;
import opennlp.tools.util.featuregen.CustomFeatureGenerator;
import opennlp.tools.util.featuregen.FeatureGeneratorResourceProvider;
import opennlp.tools.util.model.ArtifactSerializer;
import eus.ixa.ixa.pipe.ml.resources.POSDictionary;

public class POSDictionaryFeatureGenerator extends CustomFeatureGenerator implements ArtifactToSerializerMapper {

  private POSDictionary posDictionary;
  private Map<String, String> attributes;
  
  public POSDictionaryFeatureGenerator() {
  }
  
  public void createFeatures(List<String> features, String[] tokens, int index,
      String[] preds) {

    String posTag = posDictionary.getMostFrequentTag(tokens[index].toLowerCase());
    features.add(attributes.get("dict") + "=" + posTag);
  }
  
  @Override
  public void updateAdaptiveData(String[] tokens, String[] outcomes) {
    
  }

  @Override
  public void clearAdaptiveData() {
    
  }

  @Override
  public void init(Map<String, String> properties,
      FeatureGeneratorResourceProvider resourceProvider)
      throws InvalidFormatException {
    Object dictResource = resourceProvider.getResource(properties.get("dict"));
    if (!(dictResource instanceof POSDictionary)) {
      throw new InvalidFormatException("Not a POSDictionary resource for key: " + properties.get("dict"));
    }
    this.posDictionary = (POSDictionary) dictResource;
    this.attributes = properties;
  }

  @Override
  public Map<String, ArtifactSerializer<?>> getArtifactSerializerMapping() {
    Map<String, ArtifactSerializer<?>> mapping = new HashMap<>();
    mapping.put("posdictserializer", new POSDictionary.POSDictionarySerializer());
    return Collections.unmodifiableMap(mapping);
  }
  
}

