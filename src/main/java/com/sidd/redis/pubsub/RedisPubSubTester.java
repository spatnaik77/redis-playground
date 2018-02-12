package com.sidd.redis.pubsub;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.io.IOException;

/**
 * Created by Siddharth on 2/12/18.
 */
public class RedisPubSubTester extends JedisPubSub {

    public static void main(String[] args) throws Exception
    {
        final String channelName = "mychannel";
        //Initialize a pool of jedis instances
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        final JedisPool jedisPool = new JedisPool(poolConfig, "127.0.0.1", 6379, 0);
        final Jedis subscriberJedis = jedisPool.getResource();
        final Jedis publisherJedis  = jedisPool.getResource();

        //Subscribe to the channel
        Thread subscriber = new Thread(new Subscriber(subscriberJedis, channelName));
        subscriber.start();

        Thread.sleep(1000);

        for(int c = 0; c < 5; c++)
        {
            publisherJedis.publish(channelName, "Hello"+c);
        }
    }

}
class Subscriber extends JedisPubSub implements Runnable
{
    Jedis jedis;
    String channelName;

    public Subscriber(Jedis jedis, String channelName)
    {
        this.jedis = jedis;
        this.channelName = channelName;
    }
    public void run()
    {
        jedis.subscribe(this, channelName);
        System.out.println("Subscription done...");
    }

    @Override
    public void onMessage(String channel, String message) {
        super.onMessage(channel, message);
        System.out.println("Received: " + message);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        super.onSubscribe(channel, subscribedChannels);
        System.out.println("Subscribed to channel : " + channel);
    }
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        super.onUnsubscribe(channel, subscribedChannels);
    }

}