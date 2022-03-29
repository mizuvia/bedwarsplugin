package jedis;

import com.hoshion.library.MizuviaLibrary.RedisSubscriber;
import com.hoshion.mongoapi.MongoService;
import com.hoshion.mongoapi.docs.Party;
import game.Team;
import main.Config;
import main.Plugin;
import redis.clients.jedis.JedisPubSub;

import java.util.Arrays;
import java.util.List;

public class RedisSubscription extends RedisSubscriber {

    public static List<Integer> slotsList = Arrays.asList(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43);

    public Plugin plugin;

    public RedisSubscription(Plugin plugin, String channelName){
        super(channelName);
        this.plugin = plugin;
    }

    public void onMessage(String channel, String uuid){
        if(!plugin.isEnabled()) return;

        Party party = MongoService.findOneParty("id", MongoService.findOnePlayer("uuid", uuid).gen$party_id);
        if(party == null) plugin.getJedis().publish("pending", uuid + " true");
        else {
            int amount = party.members.size();

            for(Team team : this.getPlugin().getTeams()){
                if(Config.getPlayersPerTeam() - team.getTeammatesAmount() >= amount){
                    plugin.getJedis().publish("pending", uuid + " true");
                    return;
                }
            }

            plugin.getJedis().publish("pending", uuid + " false");
        }
    }

    public Plugin getPlugin(){return this.plugin;}
}
