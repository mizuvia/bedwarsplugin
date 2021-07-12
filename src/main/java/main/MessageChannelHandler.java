package main;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class MessageChannelHandler implements PluginMessageListener {

    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] bytes){

        if(channel.equals("bungeecord")){
            ByteArrayDataInput in = ByteStreams.newDataInput( bytes );
            String subChannel = in.readUTF();

            if(subChannel.equals("Connect")){
                //String servername = in.readUTF();
            }

        }

    }

}
