package main;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.logging.Logger;

public class PluginMessageHandler implements PluginMessageListener {

    public Plugin plugin;

    public PluginMessageHandler(Plugin plugin){
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        if(s.equals("bungeecord:main") || s.equals("BungeeCord")){
            ByteArrayDataInput in = ByteStreams.newDataInput( bytes );
            String subChannel = in.readUTF();
            Logger.getLogger("").info("Received the plugin message from channel " + s + " with subchannel " + subChannel + "!");
        }
    }
}
