package dev.ianaduarte.ghastly.network;

import dev.ianaduarte.ghastly.Ghastly;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record GhastDataPayload(int ghastId, int targetId, int chargeTime) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, GhastDataPayload> STREAM_CODEC = CustomPacketPayload.codec(GhastDataPayload::write, GhastDataPayload::new);
	public static final CustomPacketPayload.Type<GhastDataPayload> TYPE = new CustomPacketPayload.Type<>(Ghastly.getLocation("ghast_data"));
	
	public GhastDataPayload(FriendlyByteBuf buffer) {
		this(buffer.readInt(), buffer.readInt(), buffer.readInt());
	}
	
	private void write(FriendlyByteBuf buffer) {
		buffer.writeInt(this.ghastId);
		buffer.writeInt(this.targetId);
		buffer.writeInt(this.chargeTime);
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
