package com.yt.app.common.common.yt;

/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;

/**
 * Represents an HTTP request or response entity, consisting of headers and
 * body.
 *
 * <p>
 * Typically used in combination with the
 * {@link org.springframework.web.client.RestTemplate}, like so:
 * 
 * <pre class="code">
 * HttpHeaders headers = new HttpHeaders();
 * headers.setContentType(MediaType.TEXT_PLAIN);
 * HttpEntity&lt;String&gt; entity = new HttpEntity&lt;String&gt;(helloWorld, headers);
 * URI location = template.postForLocation(&quot;http://example.com&quot;, entity);
 * </pre>
 * 
 * or
 * 
 * <pre class="code">
 * HttpEntity&lt;String&gt; entity = template.getForEntity(&quot;http://example.com&quot;, String.class);
 * String body = entity.getBody();
 * MediaType contentType = entity.getHeaders().getContentType();
 * </pre>
 * 
 * Can also be used in Spring MVC, as a return value from a @Controller method:
 * 
 * <pre class="code">
 * &#064;RequestMapping(&quot;/handle&quot;)
 * public HttpEntity&lt;String&gt; handle() {
 * 	HttpHeaders responseHeaders = new HttpHeaders();
 * 	responseHeaders.set(&quot;MyResponseHeader&quot;, &quot;MyValue&quot;);
 * 	return new HttpEntity&lt;String&gt;(&quot;Hello World&quot;, responseHeaders);
 * }
 * </pre>
 *
 * @author Arjen Poutsma
 * @author Juergen Hoeller
 * @since 3.0.2
 * @see org.springframework.web.client.RestTemplate
 * @see #getBody()
 * @see #getHeaders()
 */
public class YtHttpEntity<T> {

	/**
	 * The empty {@code HttpEntity}, with no body or headers.
	 */
	public static final YtHttpEntity<?> EMPTY = new YtHttpEntity<Object>();

	private final HttpHeaders headers;

	private final T body;

	/**
	 * Create a new, empty {@code HttpEntity}.
	 */
	protected YtHttpEntity() {
		this(null, null);
	}

	/**
	 * Create a new {@code HttpEntity} with the given body and no headers.
	 * 
	 * @param body the entity body
	 */
	public YtHttpEntity(T body) {
		this(body, null);
	}

	/**
	 * Create a new {@code HttpEntity} with the given headers and no body.
	 * 
	 * @param headers the entity headers
	 */
	public YtHttpEntity(MultiValueMap<String, String> headers) {
		this(null, headers);
	}

	/**
	 * Create a new {@code HttpEntity} with the given body and headers.
	 * 
	 * @param body    the entity body
	 * @param headers the entity headers
	 */
	public YtHttpEntity(T body, MultiValueMap<String, String> headers) {
		this.body = body;
		HttpHeaders tempHeaders = new HttpHeaders();
		if (headers != null) {
			tempHeaders.putAll(headers);
		}
		this.headers = HttpHeaders.readOnlyHttpHeaders(tempHeaders);
	}

	/**
	 * Returns the headers of this entity.
	 */
	public HttpHeaders getHeaders() {
		return this.headers;
	}

	/**
	 * Returns the body of this entity.
	 */
	public T getBody() {
		return this.body;
	}

	/**
	 * Indicates whether this entity has a body.
	 */
	public boolean hasBody() {
		return (this.body != null);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || other.getClass() != getClass()) {
			return false;
		}
		YtHttpEntity<?> otherEntity = (YtHttpEntity<?>) other;
		return (ObjectUtils.nullSafeEquals(this.headers, otherEntity.headers) && ObjectUtils.nullSafeEquals(this.body, otherEntity.body));
	}

	@Override
	public int hashCode() {
		return (ObjectUtils.nullSafeHashCode(this.headers) * 29 + ObjectUtils.nullSafeHashCode(this.body));
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("<");
		if (this.body != null) {
			builder.append(this.body);
			if (this.headers != null) {
				builder.append(',');
			}
		}
		if (this.headers != null) {
			builder.append(this.headers);
		}
		builder.append('>');
		return builder.toString();
	}

}
