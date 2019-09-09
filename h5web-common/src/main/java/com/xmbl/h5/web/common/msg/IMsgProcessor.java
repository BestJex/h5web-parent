package com.xmbl.h5.web.common.msg;

import com.google.protobuf.Message;

public interface IMsgProcessor<T> {
	void processor(Message message, T ctx);
}
