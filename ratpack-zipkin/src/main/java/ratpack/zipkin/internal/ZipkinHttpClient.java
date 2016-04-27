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
package ratpack.zipkin.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.exec.Promise;
import ratpack.func.Action;
import ratpack.http.client.HttpClient;
import ratpack.http.client.ReceivedResponse;
import ratpack.http.client.RequestSpec;
import ratpack.http.client.StreamedResponse;

import javax.inject.Inject;
import java.net.URI;

public class ZipkinHttpClient implements HttpClient {
  private final Logger logger = LoggerFactory.getLogger(ZipkinHttpClient.class);
  private final HttpClient delegate;

  @Inject
  public ZipkinHttpClient(final HttpClient delegate) {
    this.delegate = delegate;
  }

  @Override
  public Promise<ReceivedResponse> get(final URI uri, final Action<? super RequestSpec> requestConfigurer) {
    return request(uri, requestConfigurer);
  }

  @Override
  public Promise<ReceivedResponse> get(final URI uri) {
    return get(uri, Action.noop());
  }

  @Override
  public Promise<ReceivedResponse> post(final URI uri, final Action<? super RequestSpec> requestConfigurer) {
    return request(uri, requestConfigurer);
  }

  @Override
  public Promise<ReceivedResponse> request(final URI uri, final Action<? super RequestSpec>
      requestConfigurer) {
    Promise<ReceivedResponse> responsePromise = delegate
        .request(uri, requestConfigurer)
        .wiretap(receivedResponseResult -> logger
            .info("Wiretap: {}", receivedResponseResult.getValue().getStatus()));

    Promise<ReceivedResponse> result = Promise.<Void>of((f) -> {
      f.complete();
      logger.info("Starting request: {}", uri);
    }).flatMap(v ->  responsePromise);

    return result;
  }

  @Override
  public Promise<StreamedResponse> requestStream(final URI uri, final Action<? super RequestSpec>
      requestConfigurer) {
    return delegate.requestStream(uri, requestConfigurer);
  }
}
