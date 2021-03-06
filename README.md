# spring boot common

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/bc80abd17a444f0ba0d94ec807e07843)](https://app.codacy.com/manual/zhouhailin/spring-boot-common?utm_source=github.com&utm_medium=referral&utm_content=zhouhailin/spring-boot-common&utm_campaign=Badge_Grade_Settings)
[![Jdk Version](https://img.shields.io/badge/JDK-1.8-green.svg)](https://img.shields.io/badge/JDK-1.8-green.svg)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/link.thingscloud/spring-boot-common/badge.svg)](https://maven-badges.herokuapp.com/maven-central/link.thingscloud/spring-boot-common/)

## Using

### common-all

    <dependency>
      <groupId>link.thingscloud</groupId>
      <artifactId>spring-boot-common-all</artifactId>
      <version>${spring-boot-common.version}</version>
    </dependency>
    
    implementation 'link.thingscloud:spring-boot-common-all:${spring-boot-common.version}'

### common-core

    <dependency>
      <groupId>link.thingscloud</groupId>
      <artifactId>spring-boot-common-core</artifactId>
      <version>${spring-boot-common.version}</version>
    </dependency>
    
    implementation 'link.thingscloud:spring-boot-common-core:${spring-boot-common.version}'

### common-aop

    <dependency>
      <groupId>link.thingscloud</groupId>
      <artifactId>spring-boot-common-aop</artifactId>
      <version>${spring-boot-common.version}</version>
    </dependency>
    
    implementation 'link.thingscloud:spring-boot-common-aop:${spring-boot-common.version}'

    @Logging
    public String echo(String now) {
        return now;
    }
        
    @Logging(level = LoggingLevel.DEBUG, result = false)
    public String echo(String now) {
        return now;
    }

### common-data-redis

    <dependency>
      <groupId>link.thingscloud</groupId>
      <artifactId>spring-boot-common-data-redis</artifactId>
      <version>${spring-boot-common.version}</version>
    </dependency>

    implementation 'link.thingscloud:spring-boot-common-data-redis:${spring-boot-common.version}'

#### 分布式锁

    // 极端异常，如有严格要求，建议使用redisson
    // 当主从同步时，锁成功，主挂了，从升为主时，锁key未同步过来，会导致第二次锁也会成功
    @Autowired
    private SimpleDistributedLocker simpleDistributedLocker;
    
    private RedisResponseCallback callback = new RedisResponseCallback() {
        @Override
        public void onSucceed() {
            log.info("lock {} onSucceed", key);
        }

        @Override
        public void onFailure() {
            log.info("lock {} onFailure", key);
        }

        @Override
        public void onException(Throwable cause) {
            log.error("lock {} onException", key, cause);
        }
    };

    simpleDistributedLocker.tryLock(key, callback);
    simpleDistributedLocker.lock(key, callback);

#### 分布式并发控制

    @Autowired
    private SimpleDistributedLimiter simpleDistributedLimiter;
    // 是否达到并发最大值
    boolean result = simpleDistributedLimiter.permit(100);
    // 清楚过期数据，过期时间 60秒
    simpleDistributedLimiter.clear(60000);

#### 消息队列

    基于redis消息队列实现

    实现 RedisMessageListenerAdapter, 通过RedisTopic主叫或者实现其getTopic()方法

##### 1.使用list数据结构

    rpush/lpush : 元素入队列右侧/左侧
    
    lpop/rpop : 元素入队列左侧/右侧
    blpop/brpop : 元素入队列左侧/右侧 - 没有元素则阻塞

#### 动态配置

    基于redis发布订阅实现配置动态加载

## License

[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) Copyright (C) Apache Software Foundation
