package me.wini.redis.redisTest;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import static java.util.stream.Collectors.toList;
/**
 * Hello world!
 *
 */
@Slf4j
public class App_bulk_lambda 
{
	private static ObjectMapper mapper;
	
	static {
		mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}
	
    public static void main( String[] args )
    {
        @Cleanup Jedis jedis = new Jedis("192.168.0.139", 6379);
        jedis.auth("winitech");
        
        List<String> bulk = jedis.lrange("logstash", 0, -1);   
        
        List<Account> list = bulk.stream().map(App_bulk_lambda::string2Account).collect(toList());    
        
        
        //List<Account> list = new ArrayList<>();
        //bulk.forEach(data ->list.add(string2Account(data)));
        
        jedis.ltrim("logstash", list.size(), -1);          
    }
    
    public static Account string2Account(String data) {
    	 try {       	   
         	JsonNode node = mapper.readTree(data);
         	String msg = node.get("message").textValue();
         	
         	Account account = mapper.readValue(msg, Account.class);
         	return account;
         	
 		} catch (JsonParseException e) {
 			log.error("JsonParseException", e);
 		} catch (JsonMappingException e) {
 			log.error("JsonMappingException", e);
 		} catch (IOException e) {
 			log.error("IOException", e);
 		}
		return null;
    }
}
