package jedis;

import main.Config;
import main.Plugin;
import redis.clients.jedis.Jedis;

public class RedisThread implements Runnable{

    public Jedis jedis;
    public Plugin plugin;

    public RedisThread(Jedis jedis, Plugin plugin){
        this.plugin = plugin;
        this.jedis = jedis;
    }
    @Override
    public void run(){

        jedis.subscribe(new RedisSubscription(plugin), Config.getServerName());

    }
}
