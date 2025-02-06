package com.backend.allreva.support;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Aspect
@Component
public class AsyncAspect {

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public void init() {
        init(1);
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
