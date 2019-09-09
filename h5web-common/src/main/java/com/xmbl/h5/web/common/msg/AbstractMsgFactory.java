package com.xmbl.h5.web.common.msg;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Message;
import com.xmbl.h5.web.common.context.SpringContext;

public abstract class AbstractMsgFactory {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	protected final Map<Integer, Class<? extends Message>> maps = new HashMap<>();
	@SuppressWarnings("rawtypes")
	protected final Map<Integer, IMsgProcessor> processors = new HashMap<>();
	protected SpringContext context;
	protected abstract void setContext();
	protected abstract void init();
	
	public Message parseMessage(int msgId, byte[] bytes) {
		Class<? extends Message> clazz = getMsgClassById((short) msgId);
		if (Objects.isNull(clazz)) {
			log.info("尝试获取消息({})的类型为null", msgId);
			return null;
		}
		try {
			Method method = clazz.getMethod("parseFrom", byte[].class);
			return (Message) method.invoke(null, bytes);
		} catch (Exception e) {
			log.error("解析protobuf字节流时出错", e);
			return null;
		}
	}
	
	private Class<? extends Message> getMsgClassById(int msgId) {
		return maps.get(msgId);
	}

	@SuppressWarnings("unchecked")
	public void processor(Message message, int msgId, Object ctx) {
		@SuppressWarnings("rawtypes")
		IMsgProcessor processor = processors.get(msgId);
		if (Objects.isNull(processor)) {
			log.info("消息({})的处理器为null");
			return;
		}
		processor.processor(message, ctx);
	}
	
	protected void init(Class<?> clazz) {
		Field[] fields = clazz.getFields();
		for (Field field : fields) {
			int msgId = 0;
			try {
				msgId = field.getInt(null);
			} catch (Exception e) {
				log.error("", e);
			}

			AMsg amsg = field.getAnnotation(AMsg.class);
			Class<? extends Message> msgClass = amsg.message();
			try {
				maps.put(msgId, msgClass);
			} catch (Exception e) {
				log.error("解析消息配置文件失败", e);
			}

			AMsgProcessor aMsgProcessor = field.getAnnotation(AMsgProcessor.class);
			if (Objects.nonNull(aMsgProcessor)) {
				@SuppressWarnings("rawtypes")
				Class<? extends IMsgProcessor> processorClass = aMsgProcessor.processor();
				try {
					IMsgProcessor<?> processor = context.getBean(processorClass);
					processors.put(msgId, processor);
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}
	}
}
