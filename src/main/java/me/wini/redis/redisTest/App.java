package me.wini.redis.redisTest;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Cleanup;
import redis.clients.jedis.Jedis;

/**
 * Hello world!
 *
 */
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
        	JsonNode node = mapper.readTree(data);
        	String msg = node.get("message").textValue();
        	
        	Account account = mapper.readValue(msg, Account.class);
        	
        	System.out.println(account);
        	
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
