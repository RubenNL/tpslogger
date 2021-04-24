package tpslogger.tpslogger.client;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ServerInfo;
import tpslogger.tpslogger.mixin.ReceiveMessageMixin;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class TpsloggerClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		class SayHello extends TimerTask {
			public void run() {
				ServerInfo serverInfo=MinecraftClient.getInstance().getCurrentServerEntry();
				if(serverInfo==null) return;
				if(!serverInfo.address.contains("cubekrowd.net")) return;
				ClientPlayerEntity thisPlayer=MinecraftClient.getInstance().player;
				if(thisPlayer==null) return;
				List<PlayerInfo> playerList=thisPlayer.networkHandler.getPlayerList().stream().filter(player->{
					if(player.getDisplayName()==null) return false;
					return player.getDisplayName().getString().contains("[su1]");
				}).map(player->player.getDisplayName().getString()).map(PlayerInfo::new).collect(Collectors.toList());
				System.out.println(playerList);
				askTPS();
			}
		}
		Timer timer = new Timer();
		timer.schedule(new SayHello(), 0, 60000);
	}
	public static void askTPS() {
		messagesToGo=9;
		sparkInfo="";
		assert MinecraftClient.getInstance().player != null;
		MinecraftClient.getInstance().player.sendChatMessage("/tps");
	}
	private static int messagesToGo=0;
	private static String sparkInfo="";
	public static boolean receivedLog(String message) {
		if(messagesToGo==0) return false;
		messagesToGo--;
		sparkInfo+=message+"\n";
		if(messagesToGo==0) parseSpark(sparkInfo);
		return true;
	}
	public static void parseSpark(String sparkInfo) {
		String[] lines=sparkInfo.split("\\n");
		String tpsString=lines[1].split(", ")[1];
		if(tpsString.contains("*")) tpsString=tpsString.split("\\*")[1];
		double tps=Double.valueOf(tpsString);
		System.out.println("tps:"+tps);
		double mspt=Double.valueOf(lines[4].split(", ")[1].split("/")[1]);
		System.out.println("mspt:"+mspt);
		int cpuSystem=Integer.valueOf(lines[7].split("%, ")[1]);
		System.out.println("cpuSystem:"+cpuSystem);
		int cpuProcess=Integer.valueOf(lines[8].split("%, ")[1]);
		System.out.println("cpuProcess:"+cpuProcess);
	}
}
