package my.app.redis;

import com.google.inject.AbstractModule;

import io.github.cdimascio.dotenv.Dotenv;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisModule extends AbstractModule {
    @Override
    protected void configure() {
        Dotenv dotenv = Dotenv.load();

        String redisHost = dotenv.get("REDIS_HOST");
        int redisPort = Integer.parseInt(dotenv.get("REDIS_PORT"));

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(poolConfig, redisHost, redisPort);
        bind(JedisPool.class).toInstance(jedisPool);
    }
}
