package my.app.helpers;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

public class RedisKeyInvalidate {
    private final Jedis jedis;

    public RedisKeyInvalidate(Jedis jedis) {
        this.jedis = jedis;
    }


    public void invalidateKeys(String substring) throws JedisException {
        String cursor = "0";
        ScanParams scanParams = new ScanParams().match("*" + substring + "*").count(100);
        do {
            ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
            List<String> keys = scanResult.getResult();
            for (String key : keys) {
                jedis.del(key);
            }
            cursor = scanResult.getCursor();
        } while (!cursor.equals("0"));
    }
}
