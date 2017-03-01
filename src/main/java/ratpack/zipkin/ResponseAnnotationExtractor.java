/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ratpack.zipkin;

import com.github.kristofa.brave.KeyValueAnnotation;
import java.util.LinkedList;
import java.util.List;
import ratpack.http.Response;

import java.util.Collection;
import java.util.Collections;
import zipkin.Constants;
import zipkin.TraceKeys;

@FunctionalInterface
public interface ResponseAnnotationExtractor {
  Collection<KeyValueAnnotation> annotationsForResponse(Response response);

  ResponseAnnotationExtractor DEFAULT = (response -> {
    int httpStatus = response.getStatus().getCode();

    List<KeyValueAnnotation> annotations = new LinkedList<>();

    if ((httpStatus < 200) || (httpStatus > 299)) {
      annotations.add(KeyValueAnnotation
          .create(TraceKeys.HTTP_STATUS_CODE, String.valueOf(httpStatus)));
    }

    if (httpStatus > 500) {
      annotations.add(KeyValueAnnotation.create(Constants.ERROR, "server error"));
    }

    return annotations;
  });
}
