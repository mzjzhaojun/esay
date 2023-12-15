package com.yt.app.common.util;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * java8新特性
 * 
 * @author zj
 *
 */
public class StreamUtil {

	@SafeVarargs
	public static <T> Stream<T> of(T[]... tt) {
		return Stream.of(tt).flatMap(t -> Stream.of(t));
	}

	@SafeVarargs
	public static <T> Stream<T> of(Stream<T>... values) {
		return Stream.of(values).reduce((accu, newStream) -> Stream.concat(accu, newStream)).get();
	}

	public static <T> Stream<T> tail(Stream<T> stream) {
		return stream.skip(1);
	}

	public static <T> Optional<T> head(Stream<T> stream) {
		return stream.findFirst();
	}

	public static <T> Optional<T> head(Collection<T> collection) {
		return collection.stream().findFirst();
	}

	public static <T> Stream<T> merge(T value, Stream<T> stream) {
		return StreamUtil.of(Stream.of(value), stream);
	}

	public static <T> Stream<T> merge(Stream<T> stream, T value) {
		return StreamUtil.of(stream, Stream.of(value));
	}

	public static LongStream concat(LongStream... longStreams) {
		return Stream.<LongStream>of(longStreams).reduce((accu, newStream) -> LongStream.concat(accu, newStream)).get();
	}
}
