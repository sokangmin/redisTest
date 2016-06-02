package me.wini.redis.redisTest;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * Hello world!
 *
 */
@Slf4j
public class App 
{
    public static void main( String[] args )
    {
        @Cleanup Jedis jedis = new Jedis("192.168.0.139", 6379);
        jedis.auth("winitech");
        
        String data = jedis.lpop("logstash");
        
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        
        try {       	   
        	//JsonNode node = mapper.readTree(data);
        	//String msg = node.get("message").textValue();
        	
        	Account account = mapper.readValue(data, Account.class);
        	
        	System.out.println(account);
        	
		} catch (JsonParseException e) {
			log.error("", e);
		} catch (JsonMappingException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		}
    }
}
