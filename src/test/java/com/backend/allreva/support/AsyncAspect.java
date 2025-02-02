package com.backend.allreva.support;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Aspect
@Component
public class AsyncAspect {

    private CountDownLatch countDownLatch;

    public AsyncAspect() {
        this(0);
    }

    public AsyncAspect(int count) {
        this.countDownLatch = new CountDownLatch(count);
    }

    public void init() {
        countDownLatch = new CountDownLatch(1);
    }

    public void init(int count) {
        countDownLatch = new CountDownLatch(count);
    }

    @After("execution(public * com.backend.allreva.chatting.chat.single.infra.SingleChatEventHandler.*(..)) || " +
            "execution(public * com.backend.allreva.chatting.chat.integration.infra.ParticipantEventHandler.*(..)) || " +
            "execution(public * com.backend.allreva.chatting.chat.group.infra.GroupChatEventHandler.*(..))")
    public void afterEventHandler() {
        countDownLatch.countDown();
    }

    public void await() throws InterruptedException {
        countDownLatch.await();
    }
}
