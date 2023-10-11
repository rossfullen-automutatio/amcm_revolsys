package com.revolsys.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.Configurable;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.HeaderGroup;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.jeometry.common.collection.list.ListEx;
import org.jeometry.common.json.JsonList;
import org.jeometry.common.json.JsonObject;

import com.revolsys.net.http.ApacheHttp;
import com.revolsys.net.http.ApacheHttpException;
import com.revolsys.util.UriBuilder;

public class HttpRequestBuilder {

  static class InternalEntityEclosingRequest extends HttpEntityEnclosingRequestBase {

    private final String method;

    InternalEntityEclosingRequest(final String method) {
      super();
      this.method = method;
    }

    @Override
    public String getMethod() {
      return this.method;
    }

  }

  static class InternalRequest extends HttpRequestBase {

    private final String method;

    InternalRequest(final String method) {
      super();
      this.method = method;
    }

    @Override
    public String getMethod() {
      return this.method;
    }

  }

  public static final StringEntity EMPTY_ENTITY = new StringEntity("", ContentType.TEXT_PLAIN);

  public static HttpRequestBuilder copy(final HttpRequest request) {
    Args.notNull(request, "HTTP request");
    return new HttpRequestBuilder().setRequest(request);
  }

  public static HttpRequestBuilder create(final String method) {
    Args.notBlank(method, "HTTP method");
    return new HttpRequestBuilder().setMethod(method);
  }

  public static HttpRequestBuilder delete() {
    return new HttpRequestBuilder().setMethod(HttpDelete.METHOD_NAME);
  }

  public static HttpRequestBuilder delete(final String uri) {
    return new HttpRequestBuilder().setMethod(HttpDelete.METHOD_NAME).setUri(uri);
  }

  public static HttpRequestBuilder delete(final URI uri) {
    return new HttpRequestBuilder().setMethod(HttpDelete.METHOD_NAME).setUri(uri);
  }

  public static HttpRequestBuilder get() {
    return new HttpRequestBuilder().setMethod(HttpGet.METHOD_NAME);
  }

  public static HttpRequestBuilder get(final String uri) {
    return new HttpRequestBuilder().setMethod(HttpGet.METHOD_NAME).setUri(uri);
  }

  public static HttpRequestBuilder get(final URI uri) {
    return new HttpRequestBuilder().setMethod(HttpGet.METHOD_NAME).setUri(uri);
  }

  public static HttpRequestBuilder head() {
    return new HttpRequestBuilder().setMethod(HttpHead.METHOD_NAME);
  }

  public static HttpRequestBuilder head(final String uri) {
    return new HttpRequestBuilder().setMethod(HttpHead.METHOD_NAME).setUri(uri);
  }

  public static HttpRequestBuilder head(final URI uri) {
    return new HttpRequestBuilder().setMethod(HttpHead.METHOD_NAME).setUri(uri);
  }

  public static HttpRequestBuilder options() {
    return new HttpRequestBuilder().setMethod(HttpOptions.METHOD_NAME);
  }

  public static HttpRequestBuilder options(final String uri) {
    return new HttpRequestBuilder().setMethod(HttpOptions.METHOD_NAME).setUri(uri);
  }

  public static HttpRequestBuilder options(final URI uri) {
    return new HttpRequestBuilder().setMethod(HttpOptions.METHOD_NAME).setUri(uri);
  }

  public static HttpRequestBuilder patch() {
    return new HttpRequestBuilder().setMethod(HttpPatch.METHOD_NAME);
  }

  public static HttpRequestBuilder patch(final String uri) {
    return new HttpRequestBuilder().setMethod(HttpPatch.METHOD_NAME).setUri(uri);
  }

  public static HttpRequestBuilder patch(final URI uri) {
    return new HttpRequestBuilder().setMethod(HttpPatch.METHOD_NAME).setUri(uri);
  }

  public static HttpRequestBuilder post() {
    return new HttpRequestBuilder().setMethod(HttpPost.METHOD_NAME);
  }

  public static HttpRequestBuilder post(final String uri) {
    return new HttpRequestBuilder().setMethod(HttpPost.METHOD_NAME).setUri(uri);
  }

  public static HttpRequestBuilder post(final URI uri) {
    return new HttpRequestBuilder().setMethod(HttpPost.METHOD_NAME).setUri(uri);
  }

  public static HttpRequestBuilder put() {
    return new HttpRequestBuilder().setMethod(HttpPut.METHOD_NAME);
  }

  public static HttpRequestBuilder put(final String uri) {
    return new HttpRequestBuilder().setMethod(HttpPut.METHOD_NAME).setUri(uri);
  }

  public static HttpRequestBuilder put(final URI uri) {
    return new HttpRequestBuilder().setMethod(HttpPut.METHOD_NAME).setUri(uri);
  }

  public static HttpRequestBuilder trace() {
    return new HttpRequestBuilder().setMethod(HttpTrace.METHOD_NAME);
  }

  public static HttpRequestBuilder trace(final String uri) {
    return new HttpRequestBuilder().setMethod(HttpTrace.METHOD_NAME).setUri(uri);
  }

  public static HttpRequestBuilder trace(final URI uri) {
    return new HttpRequestBuilder().setMethod(HttpTrace.METHOD_NAME).setUri(uri);
  }

  private String method;

  private Charset charset = Consts.UTF_8;

  private ProtocolVersion version;

  private URI uri;

  private HeaderGroup headerGroup;

  private HttpEntity entity;

  private List<NameValuePair> parameters;

  private RequestConfig config;

  private final Set<String> headerNames = new TreeSet<>();

  private HttpRequestBuilderFactory factory;

  HttpRequestBuilder() {
  }

  public HttpRequestBuilder(final HttpRequestBuilderFactory factory) {
    this.factory = factory;
  }

  public HttpRequestBuilder addHeader(final Header header) {
    this.headerNames.add(header.getName());
    if (this.headerGroup == null) {
      this.headerGroup = new HeaderGroup();
    }
    this.headerGroup.addHeader(header);
    return this;
  }

  public HttpRequestBuilder addHeader(final String name, final String value) {
    final BasicHeader header = new BasicHeader(name, value);
    return addHeader(header);
  }

  public HttpRequestBuilder addHeaders(final Header[] headers) {
    for (final Header header : headers) {
      addHeader(header);
    }
    return this;
  }

  public HttpRequestBuilder addParameter(final NameValuePair parameter) {
    if (parameter != null) {
      if (this.parameters == null) {
        this.parameters = new LinkedList<>();
      }
      this.parameters.add(parameter);
    }
    return this;
  }

  public HttpRequestBuilder addParameter(final String name, final Object value) {
    String string;
    if (value == null) {
      string = null;
    } else {
      string = value.toString();
    }
    final BasicNameValuePair parameter = new BasicNameValuePair(name, string);
    return addParameter(parameter);
  }

  public HttpRequestBuilder addParameters(final Iterable<NameValuePair> parameters) {
    for (final NameValuePair parameter : parameters) {
      addParameter(parameter);
    }
    return this;
  }

  public HttpRequestBuilder addParameters(final NameValuePair... nvps) {
    for (final NameValuePair nvp : nvps) {
      addParameter(nvp);
    }
    return this;
  }

  public HttpRequestBuilder apply(final Consumer<HttpRequestBuilder> action) {
    action.accept(this);
    return this;
  }

  public HttpUriRequest build() {
    final HttpRequestBase result;
    URI uri = this.uri;
    if (uri == null) {
      uri = URI.create("/");
    }
    HttpEntity entityCopy = this.entity;
    if (this.parameters != null && !this.parameters.isEmpty()) {
      if (entityCopy == null && (HttpPost.METHOD_NAME.equalsIgnoreCase(this.method)
        || HttpPut.METHOD_NAME.equalsIgnoreCase(this.method))) {
        entityCopy = new UrlEncodedFormEntity(this.parameters,
          this.charset != null ? this.charset : HTTP.DEF_CONTENT_CHARSET);
      } else {
        uri = new UriBuilder(uri).setCharset(this.charset).addParameters(this.parameters).build();
      }
    }
    if (entityCopy == null) {
      result = new InternalRequest(this.method);
    } else {
      final InternalEntityEclosingRequest request = new InternalEntityEclosingRequest(this.method);
      request.setEntity(entityCopy);
      result = request;
    }
    result.setProtocolVersion(this.version);
    result.setURI(uri);
    if (this.headerGroup != null) {
      result.setHeaders(this.headerGroup.getAllHeaders());
    }
    result.setConfig(this.config);
    return result;
  }

  public void execute() {
    final Consumer<HttpResponse> noop = r -> {
    };
    execute(noop);
  }

  public void execute(final Consumer<HttpResponse> action) {
    final HttpUriRequest request = build();
    ApacheHttp.execute(request, action);
  }

  public <V> V execute(final Function<HttpResponse, V> action) {
    final HttpUriRequest request = build();
    return ApacheHttp.execute(request, action);
  }

  /**
   * @since 4.4
   */
  public Charset getCharset() {
    return this.charset;
  }

  public RequestConfig getConfig() {
    return this.config;
  }

  public HttpEntity getEntity() {
    return this.entity;
  }

  public HttpRequestBuilderFactory getFactory() {
    return this.factory;
  }

  public Header getFirstHeader(final String name) {
    return this.headerGroup != null ? this.headerGroup.getFirstHeader(name) : null;
  }

  public Set<String> getHeaderNames() {
    return Collections.unmodifiableSet(this.headerNames);
  }

  public Header[] getHeaders(final String name) {
    return this.headerGroup != null ? this.headerGroup.getHeaders(name) : null;
  }

  public JsonObject getJson() {
    setHeader("Accept", "application/json");
    final Function<HttpResponse, JsonObject> function = ApacheHttp::getJson;
    return execute(function);
  }

  public JsonList getJsonList() {
    setHeader("Accept", "application/json");
    final Function<HttpResponse, JsonList> function = ApacheHttp::getJsonList;
    return execute(function);
  }

  public Optional<JsonObject> getJsonObjectOptional() {
    try {
      final JsonObject json = getJson();
      return Optional.ofNullable(json);
    } catch (final ApacheHttpException e) {
      final int code = e.getStatusCode();
      if (code == HttpStatus.SC_NOT_FOUND || code == HttpStatus.SC_GONE) {
        return Optional.empty();
      } else {
        throw e;
      }
    }
  }

  public Header getLastHeader(final String name) {
    return this.headerGroup != null ? this.headerGroup.getLastHeader(name) : null;
  }

  @SuppressWarnings({
    "unchecked", "rawtypes"
  })
  public <V> ListEx<V> getList() {
    return (ListEx)getJsonList();
  }

  public String getMethod() {
    return this.method;
  }

  public List<NameValuePair> getParameters() {
    return this.parameters != null ? new ArrayList<>(this.parameters) : new ArrayList<>();
  }

  public String getString() {
    final Function<HttpResponse, String> function = ApacheHttp::getString;
    return execute(function);
  }

  public URI getUri() {
    return this.uri;
  }

  public ProtocolVersion getVersion() {
    return this.version;
  }

  public InputStream newInputStream() {
    final HttpUriRequest request = build();
    return ApacheHttp.getInputStream(request);
  }

  public HttpRequestBuilder removeHeader(final Header header) {
    if (this.headerGroup != null) {
      this.headerGroup.removeHeader(header);
    }
    return this;
  }

  public HttpRequestBuilder removeHeaders(final String name) {
    if (name != null && this.headerGroup != null) {
      this.headerNames.remove(name);
      for (final HeaderIterator i = this.headerGroup.iterator(); i.hasNext();) {
        final Header header = i.nextHeader();
        if (name.equalsIgnoreCase(header.getName())) {
          i.remove();
        }
      }
    }
    return this;
  }

  public HttpRequestBuilder removeParameters(final String name) {
    if (name != null && this.parameters != null) {
      for (final Iterator<NameValuePair> i = this.parameters.iterator(); i.hasNext();) {
        final NameValuePair parameter = i.next();
        if (name.equalsIgnoreCase(parameter.getName())) {
          i.remove();
        }
      }
    }
    return this;
  }

  public HttpRequestBuilder setCharset(final Charset charset) {
    this.charset = charset;
    return this;
  }

  public HttpRequestBuilder setConfig(final RequestConfig config) {
    this.config = config;
    return this;
  }

  public HttpRequestBuilder setEmptyEntity() {
    setEntity(EMPTY_ENTITY);
    return this;
  }

  public HttpRequestBuilder setEntity(final HttpEntity entity) {
    this.entity = entity;
    return this;
  }

  public HttpRequestBuilder setHeader(final Header header) {
    if (this.headerGroup == null) {
      this.headerGroup = new HeaderGroup();
    }
    this.headerGroup.updateHeader(header);
    return this;
  }

  public HttpRequestBuilder setHeader(final String name, final String value) {
    final BasicHeader header = new BasicHeader(name, value);
    return setHeader(header);
  }

  public HttpRequestBuilder setJsonEntity(final JsonObject value) {
    final String jsonString = value.toJsonString();
    final StringEntity entity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);
    setEntity(entity);
    return this;
  }

  HttpRequestBuilder setMethod(final HttpMethod method) {
    this.method = method.name();
    return this;
  }

  HttpRequestBuilder setMethod(final String method) {
    this.method = method;
    return this;
  }

  public HttpRequestBuilder setParameter(final NameValuePair parameter) {
    removeParameters(parameter.getName());
    return addParameter(parameter);
  }

  public HttpRequestBuilder setParameter(final String name, final Object value) {
    removeParameters(name);
    return addParameter(name, value);
  }

  HttpRequestBuilder setRequest(final HttpRequest request) {
    this.method = request.getRequestLine().getMethod();
    this.version = request.getRequestLine().getProtocolVersion();

    if (this.headerGroup == null) {
      this.headerGroup = new HeaderGroup();
    }
    this.headerGroup.clear();
    this.headerGroup.setHeaders(request.getAllHeaders());

    this.parameters = null;
    this.entity = null;

    if (request instanceof HttpEntityEnclosingRequest) {
      final HttpEntity originalEntity = ((HttpEntityEnclosingRequest)request).getEntity();
      final ContentType contentType = ContentType.get(originalEntity);
      if (contentType != null && contentType.getMimeType()
        .equals(ContentType.APPLICATION_FORM_URLENCODED.getMimeType())) {
        try {
          final List<NameValuePair> formParams = URLEncodedUtils.parse(originalEntity);
          if (!formParams.isEmpty()) {
            this.parameters = formParams;
          }
        } catch (final IOException ignore) {
        }
      } else {
        this.entity = originalEntity;
      }
    }

    if (request instanceof HttpUriRequest) {
      this.uri = ((HttpUriRequest)request).getURI();
    } else {
      this.uri = URI.create(request.getRequestLine().getUri());
    }

    if (request instanceof Configurable) {
      this.config = ((Configurable)request).getConfig();
    } else {
      this.config = null;
    }
    return this;
  }

  HttpRequestBuilder setUri(final String uri) {
    if (uri == null) {
      this.uri = null;
    } else {
      this.uri = URI.create(uri);
    }
    return this;
  }

  HttpRequestBuilder setUri(final URI uri) {
    this.uri = uri;
    return this;
  }

  public HttpRequestBuilder setVersion(final ProtocolVersion version) {
    this.version = version;
    return this;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append(this.method);
    builder.append(' ');
    builder.append(this.uri);
    builder.append(' ');
    builder.append(this.parameters);

    builder.append(", charset=");
    builder.append(this.charset);
    builder.append(", version=");
    builder.append(this.version);
    builder.append(", headerGroup=");
    builder.append(this.headerGroup);
    builder.append(", entity=");
    builder.append(this.entity);
    builder.append(", config=");
    builder.append(this.config);
    return builder.toString();
  }

}
