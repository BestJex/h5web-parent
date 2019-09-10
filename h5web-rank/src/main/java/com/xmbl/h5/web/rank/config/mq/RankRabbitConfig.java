package com.xmbl.h5.web.rank.config.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xmbl.h5.web.common.consts.RabbitConst;
import com.xmbl.h5.web.rank.mq.NoticeMsgReceiver;
import com.xmbl.h5.web.rank.mq.RankRpcServer;

@Configuration
public class RankRabbitConfig {

	@Bean(RabbitConst.h5_web_rank_notice_queue)
	public Queue playChangeQueue() {
		return new Queue(RabbitConst.h5_web_rank_notice_queue);
	}

	@Bean(RabbitConst.h5_web_rank_query_rpc_queue)
	public Queue queue() {
		return new Queue(RabbitConst.h5_web_rank_query_rpc_queue);
	}

	@Bean(RabbitConst.h5_web_rank_query_rpc_exchange)
	public DirectExchange exchange() {
		return new DirectExchange(RabbitConst.h5_web_rank_query_rpc_exchange);
	}

	@Bean
	public NoticeMsgReceiver receiver() {
		return new NoticeMsgReceiver();
	}
	
	@Bean
	public RankRpcServer server() {
		return new RankRpcServer();
	}

	@Bean
	public Binding binding(@Qualifier(RabbitConst.h5_web_rank_query_rpc_exchange) DirectExchange exchange, @Qualifier(RabbitConst.h5_web_rank_query_rpc_queue) Queue queue) {
		return BindingBuilder.bind(queue).to(exchange).with(RabbitConst.h5_web_rank_query_rpc_route_key);
	}
}