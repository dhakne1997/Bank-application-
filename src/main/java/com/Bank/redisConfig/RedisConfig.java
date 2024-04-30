//package com.Bank.redisConfig;
//
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericToStringSerializer;
//import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import com.Bank.model.BankAccount;
//
//import redis.clients.jedis.Jedis;
//
//@Configuration
//public class RedisConfig {
//
//	
//	@Value("${spring.redis.host}")
//	String hostname;
//	
//	@Value("${spring.redis.port}")
//	int port;
//	
//
//
//    // Method to clear all data from Redis with specific host and port
//    public void clearRedis(String hostname, int port) {
//        try (Jedis jedis = new Jedis(hostname, port)) {
//            String flushDB = jedis.flushDB();
//            System.out.println(flushDB);
//        }
//    }
//    
//	@Bean
//	public JedisConnectionFactory connectionFactory() {
//		RedisStandaloneConfiguration configuration=new RedisStandaloneConfiguration();
//	configuration.setHostName(hostname);
//	configuration.setPort(port);
//	return new JedisConnectionFactory(configuration);
//	}
//	
//	
//	@Bean
//    public RedisTemplate<String, BankAccount> redisTemplate() {
//        RedisTemplate<String, BankAccount> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory());
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new JdkSerializationRedisSerializer());
//        template.setValueSerializer(new JdkSerializationRedisSerializer());
//        template.setEnableTransactionSupport(true);
//        template.afterPropertiesSet();
//        return template;
//    }
//	
//	
//	 
//	
//}
//	
//	
////	@Bean
////    public RedisTemplate<String, BankAccount> redisTemplate(RedisConnectionFactory connectionFactory) {
////        RedisTemplate<String, BankAccount> template = new RedisTemplate<>();
////        template.setConnectionFactory(connectionFactory);
////        template.setValueSerializer(new GenericToStringSerializer<>(BankAccount.class));
////        return template;
////    }
//	
//	
//	
//
