package com.xmbl.h5.web.game.event;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.xmbl.h5.web.common.event.Event;
import com.xmbl.h5.web.common.event.EventListener;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@Service
public class EventDispatcher implements ApplicationContextAware {

    private EventBus eventBus;
    private AsyncEventBus asyncEventBus;

    @Resource(name = "eventBusExecutor")
    private ExecutorService eventBusExecutor;

    private ApplicationContext ac;

    @PostConstruct
    public void init() {
        eventBus = new EventBus();
        asyncEventBus = new AsyncEventBus(eventBusExecutor);

        Map<String, Object> eventListeners = ac.getBeansWithAnnotation(EventListener.class);

        if (MapUtils.isEmpty(eventListeners)) {
            return;
        }
        IterableUtils.forEach(eventListeners.values(), eventListener -> eventBus.register(eventListener));
        IterableUtils.forEach(eventListeners.values(), eventListener -> asyncEventBus.register(eventListener));
    }

    public <T> void dispatch(T event) {
        eventBus.post(event);
    }

    public <T> void dispatchAync(T event) {
        asyncEventBus.post(event);
    }
    
    public <T extends Event> void post(T event) {
    	if (event.isAsync()) {
    		dispatchAync(event);
		}else {
			dispatch(event);
		}
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ac = applicationContext;
    }
}
