package com.sidd.redis;

import redis.clients.jedis.Jedis;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Siddharth on 2/12/18.
 */
public class HelloRedis {

    public static void main(String[] args)
    {
        Jedis jedis = new Jedis();
        strings(jedis);
        lists(jedis);
        sets(jedis);
        sortedSets(jedis);
    }
    private static void strings(Jedis jedis)
    {
        jedis.set("key", "value");
        print(jedis.get("key"));
        jedis.del("key");
    }
    private static void lists(Jedis jedis)
    {
        jedis.lpush("key1", "item1");//Add element to head
        jedis.lpush("key1", "item2");//Add element to head
        jedis.rpush("key1", "item3");//Add element to tail
        //Pop element from head
        print(jedis.lpop("key1"));//Should print item2
        //Pop element from tail
        print(jedis.rpop("key1"));//Should print item3
        String[] numbers = {"3", "2", "1"};
        jedis.lpush("key2", numbers);
        System.out.println(jedis.lrange("key2", 0, 2));//Should print all the items from left to right
    }
    //Redis Sets are an unordered collection of Strings that come in handy when you want to exclude repeated members:
    private static void sets(Jedis jedis)
    {
        jedis.sadd("key3", "1");
        jedis.sadd("key3", "2");
        jedis.sadd("key3", "3");
        System.out.println(jedis.smembers("key3"));
    }
    private static void sortedSets(Jedis jedis)
    {
        //sort the set of 3 player's scores
        jedis.zadd("ranking", 2500.0, "player1");
        jedis.zadd("ranking", 3500.0, "player2");
        jedis.zadd("ranking", 1500.0, "player3");

        //Sorted ascending order
        System.out.println(jedis.zrange("ranking", 0, 2));//Prints [player3, player1, player2]
    }

    private static void print(String str)
    {
        System.out.println(str);
    }
}
