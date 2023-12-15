package com.yt.app.common.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.TypeUtils;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.yt.app.common.config.YtConfig;
import com.yt.app.common.util.SecurityUtil;

/**
 * 
 * @author zj AbstractJackson2HttpMessageConverter
 */
public class Yto extends MappingJackson2HttpMessageConverter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	private static final boolean jackson23Available = ClassUtils.hasMethod(ObjectMapper.class, "canDeserialize",
			JavaType.class, AtomicReference.class);

	private static final boolean jackson26Available = ClassUtils.hasMethod(ObjectMapper.class,
			"setDefaultPrettyPrinter", PrettyPrinter.class);

	private ObjectMapper objectMapper;

	private YtConfig ca;

	private Boolean prettyPrint;

	protected Yto(ObjectMapper objectMapper, YtConfig g) {
		this.objectMapper = objectMapper;
		this.ca = g;
	}

	/**
	 * Set the {@code ObjectMapper} for this view. If not set, a default
	 * {@link ObjectMapper#ObjectMapper() ObjectMapper} is used.
	 * <p>
	 * Setting a custom-configured {@code ObjectMapper} is one way to take further
	 * control of the JSON serialization process. For example, an extended
	 * {@link com.fasterxml.jackson.databind.ser.SerializerFactory} can be
	 * configured that provides custom serializers for specific types. The other
	 * option for refining the serialization process is to use Jackson's provided
	 * annotations on the types to be serialized, in which case a custom-configured
	 * ObjectMapper is unnecessary.
	 */
	public void setObjectMapper(ObjectMapper objectMapper) {
		Assert.notNull(objectMapper, "ObjectMapper must not be null");
		this.objectMapper = objectMapper;
		configurePrettyPrint();
	}

	/**
	 * Return the underlying {@code ObjectMapper} for this view.
	 */
	public ObjectMapper getObjectMapper() {
		return this.objectMapper;
	}

	/**
	 * Whether to use the {@link DefaultPrettyPrinter} when writing JSON. This is a
	 * shortcut for setting up an {@code ObjectMapper} as follows:
	 * 
	 * <pre class="code">
	 * ObjectMapper mapper = new ObjectMapper();
	 * mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	 * converter.setObjectMapper(mapper);
	 * </pre>
	 */
	public void setPrettyPrint(boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
		configurePrettyPrint();
	}

	private void configurePrettyPrint() {
		if (this.prettyPrint != null) {
			this.objectMapper.configure(SerializationFeature.INDENT_OUTPUT, this.prettyPrint);
		}
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return canRead(clazz, null, mediaType);
	}

	@Override
	public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
		if (!canRead(mediaType)) {
			return false;
		}
		JavaType javaType = getJavaType(type, contextClass);
		if (!jackson23Available || !logger.isWarnEnabled()) {
			return this.objectMapper.canDeserialize(javaType);
		}
		AtomicReference<Throwable> causeRef = new AtomicReference<Throwable>();
		if (this.objectMapper.canDeserialize(javaType, causeRef)) {
			return true;
		}
		Throwable cause = causeRef.get();
		if (cause != null) {
			String msg = "Failed to evaluate Jackson deserialization for type " + javaType;
			if (logger.isDebugEnabled()) {
				logger.warn(msg, cause);
			} else {
				logger.warn(msg + ": " + cause);
			}
		}
		return false;
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		if (!canWrite(mediaType)) {
			return false;
		}
		if (!jackson23Available || !logger.isWarnEnabled()) {
			return this.objectMapper.canSerialize(clazz);
		}
		AtomicReference<Throwable> causeRef = new AtomicReference<Throwable>();
		if (this.objectMapper.canSerialize(clazz, causeRef)) {
			return true;
		}
		Throwable cause = causeRef.get();
		if (cause != null) {
			String msg = "Failed to evaluate Jackson serialization for type [" + clazz + "]";
			if (logger.isDebugEnabled()) {
				logger.warn(msg, cause);
			} else {
				logger.warn(msg + ": " + cause);
			}
		}
		return false;
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {
		JavaType javaType = getJavaType(clazz, null);
		return readJavaType(javaType, inputMessage);
	}

	@Override
	public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {

		JavaType javaType = getJavaType(type, contextClass);
		return readJavaType(javaType, inputMessage);
	}

	@SuppressWarnings("deprecation")
	private Object readJavaType(JavaType javaType, HttpInputMessage inputMessage) {
		try {
			if (inputMessage instanceof MappingJacksonInputMessage) {
				Class<?> deserializationView = ((MappingJacksonInputMessage) inputMessage).getDeserializationView();
				if (deserializationView != null) {
					return this.objectMapper.readerWithView(deserializationView).withType(javaType)
							.readValue(inputMessage.getBody());
				}
			}
			if (!ca.isBuild()) {
				return this.objectMapper.readValue(inputMessage.getBody(), javaType);
			}
			InputStream inputStream = inputMessage.getBody();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, DEFAULT_CHARSET);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuffer buffer = new StringBuffer();
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			String s = SecurityUtil.decrypt(buffer.toString());
			return this.objectMapper.readValue(s, javaType);
		} catch (IOException ex) {
			throw new HttpMessageNotReadableException("Could not read document: " + ex.getMessage(), ex);
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
		JsonGenerator generator = this.objectMapper.getFactory().createGenerator(outputMessage.getBody(), encoding);
		try {
			writePrefix(generator, object);

			Class<?> serializationView = null;
			FilterProvider filters = null;
			Object value = object;
			JavaType javaType = null;
			if (object instanceof MappingJacksonValue) {
				MappingJacksonValue container = (MappingJacksonValue) object;
				value = container.getValue();
				serializationView = container.getSerializationView();
				filters = container.getFilters();
			}
			if (jackson26Available && type != null && value != null && TypeUtils.isAssignable(type, value.getClass())) {
				javaType = getJavaType(type, null);
			}
			ObjectWriter objectWriter;
			if (serializationView != null) {
				objectWriter = this.objectMapper.writerWithView(serializationView);
			} else if (filters != null) {
				objectWriter = this.objectMapper.writer(filters);
			} else {
				objectWriter = this.objectMapper.writer();
			}
			if (javaType != null && javaType.isContainerType()) {
				objectWriter = objectWriter.withType(javaType);
			}
			if (!ca.isBuild()) {
				objectWriter.writeValue(generator, value);
				writeSuffix(generator, object);
				generator.flush();
			} else {
				objectWriter.writeValue(generator, SecurityUtil.encrypt(value));
				writeSuffix(generator, object);
				generator.flush();
			}
		} catch (JsonProcessingException ex) {
			throw new HttpMessageNotWritableException("Could not write content: " + ex.getMessage(), ex);
		}
	}

	/**
	 * Write a prefix before the main content.
	 * 
	 * @param generator the generator to use for writing content.
	 * @param object    the object to write to the output message.
	 */
	protected void writePrefix(JsonGenerator generator, Object object) throws IOException {
		super.writePrefix(generator, object);
	}

	/**
	 * Write a suffix after the main content.
	 * 
	 * @param generator the generator to use for writing content.
	 * @param object    the object to write to the output message.
	 */
	protected void writeSuffix(JsonGenerator generator, Object object) throws IOException {
		super.writeSuffix(generator, object);
	}

	/**
	 * Return the Jackson {@link JavaType} for the specified type and context class.
	 * <p>
	 * The default implementation returns
	 * {@code typeFactory.constructType(type, contextClass)}, but this can be
	 * overridden in subclasses, to allow for custom generic collection handling.
	 * For instance:
	 * 
	 * <pre class="code">
	 * protected JavaType getJavaType(Type type) {
	 * 	if (type instanceof Class &amp;&amp; List.class.isAssignableFrom((Class) type)) {
	 * 		return TypeFactory.collectionType(ArrayList.class, MyBean.class);
	 * 	} else {
	 * 		return super.getJavaType(type);
	 * 	}
	 * }
	 * </pre>
	 * 
	 * @param type         the generic type to return the Jackson JavaType for
	 * @param contextClass a context class for the target type, for example a class
	 *                     in which the target type appears in a method signature
	 *                     (can be {@code null})
	 * @return the Jackson JavaType
	 */
	protected JavaType getJavaType(Type type, @Nullable Class<?> contextClass) {
		TypeFactory typeFactory = this.objectMapper.getTypeFactory();
		return typeFactory.constructType(GenericTypeResolver.resolveType(type, contextClass));
	}

	/**
	 * Determine the JSON encoding to use for the given content type.
	 * 
	 * @param contentType the media type as requested by the caller
	 * @return the JSON encoding to use (never {@code null})
	 */
	protected JsonEncoding getJsonEncoding(@Nullable MediaType contentType) {
		if (contentType != null && contentType.getCharset() != null) {
			Charset charset = contentType.getCharset();
			for (JsonEncoding encoding : JsonEncoding.values()) {
				if (charset.name().equals(encoding.getJavaName())) {
					return encoding;
				}
			}
		}
		return JsonEncoding.UTF8;
	}

	@Override
	protected MediaType getDefaultContentType(Object object) throws IOException {
		if (object instanceof MappingJacksonValue) {
			object = ((MappingJacksonValue) object).getValue();
		}
		return super.getDefaultContentType(object);
	}

	@Override
	protected Long getContentLength(Object object, MediaType contentType) throws IOException {
		if (object instanceof MappingJacksonValue) {
			object = ((MappingJacksonValue) object).getValue();
		}
		return super.getContentLength(object, contentType);
	}

}
