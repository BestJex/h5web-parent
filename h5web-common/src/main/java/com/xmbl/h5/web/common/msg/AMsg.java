package com.xmbl.h5.web.common.msg;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.protobuf.Message;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AMsg {
	
    Class<? extends Message> message() default Message.class;
}
