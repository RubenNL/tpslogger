package tpslogger.tpslogger.mixin;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tpslogger.tpslogger.client.TpsloggerClient;

@Mixin(ChatHud.class)
public class ReceiveMessageMixin {
	@Inject(method = "addMessage(Lnet/minecraft/text/Text;I)V", at = @At("HEAD"),cancellable = true)
	public void addMessage(Text text, int messageId, CallbackInfo info) {
		String message=text.getString();
		if(!message.contains("[⚡]")) return;
		if(TpsloggerClient.receivedLog(text.getString())) info.cancel();
	}
}