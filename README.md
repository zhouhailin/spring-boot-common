# spring boot common

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/bc80abd17a444f0ba0d94ec807e07843)](https://app.codacy.com/manual/zhouhailin/spring-boot-common?utm_source=github.com&utm_medium=referral&utm_content=zhouhailin/spring-boot-common&utm_campaign=Badge_Grade_Settings)
[![Jdk Version](https://img.shields.io/badge/JDK-1.8-green.svg)](https://img.shields.io/badge/JDK-1.8-green.svg)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/link.thingscloud/spring-boot-common/badge.svg)](https://maven-badges.herokuapp.com/maven-central/link.thingscloud/spring-boot-common/)

## Using

### common-data-redis

#### 分布式锁

    // 极端异常，如有严格要求，建议使用redisson
    // 当主从同步时，锁成功，主挂了，从升为主时，锁key未同步过来，会导致第二次锁也会成功
    @Autowired
    private RedisTemplate0 redisTemplate0;
    
    redisTemplate0.lock("xxx", 2000, new RedisResponseCallback() {
       
        @Override
        public void onSucceed() {
            // 锁成功
        }

        @Override
        public void onFailure() {
            // 锁失败
        }

        @Override
        public void onException(Throwable cause) {
            // 异常
        }
    });
        
#### 消息队列

    

    
## License

[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) Copyright (C) Apache Software Foundation
