package com.xmbl.h5.web.rank.config.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
//import com.xmbl.h5.web.game.interceptor.JsonParamArgumentResolver;
import com.xmbl.h5.web.rank.interceptor.ExceptionHandlerInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//	@Override
//	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//		resolvers.add(jsonArgumentResolver());
//	}
//
//	@Bean
//	public JsonParamArgumentResolver jsonArgumentResolver() {
//		return new JsonParamArgumentResolver();
//	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(jsonHttpMessageConverter());
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(exceptionHandlerInterceptor());
	}

	@Bean
	public HttpMessageConverter<?> jsonHttpMessageConverter() {
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
		List<SerializerFeature> features = new ArrayList<>();
		features.add(SerializerFeature.QuoteFieldNames);
		// features.add(SerializerFeature.PrettyFormat);
		features.add(SerializerFeature.WriteMapNullValue);
		features.add(SerializerFeature.WriteNullListAsEmpty);
		features.add(SerializerFeature.WriteNullBooleanAsFalse);
		features.add(SerializerFeature.WriteNullNumberAsZero);
		features.add(SerializerFeature.WriteNullStringAsEmpty);
		features.add(SerializerFeature.BrowserCompatible);
		features.add(SerializerFeature.DisableCircularReferenceDetect);
		features.add(SerializerFeature.SkipTransientField);
		
		converter.setFeatures(features.toArray(new SerializerFeature[0]));
		return converter;
	}

	@Bean
	public ExceptionHandlerInterceptor exceptionHandlerInterceptor() {
		return new ExceptionHandlerInterceptor();
	}
}
