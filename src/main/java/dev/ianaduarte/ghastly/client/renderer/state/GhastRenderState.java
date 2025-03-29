package dev.ianaduarte.ghastly.client.renderer.state;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.joml.Vector3f;

public class GhastRenderState extends LivingEntityRenderState {
	public Vector3f targetDelta = new Vector3f();
	public float chargeProgress = 0;
	public AiState aiState;
	public int id;
	public Vector3f deltaMovement = new Vector3f();
	public Vector3f viewVector = new Vector3f();
	
	public enum AiState {
		IDLE,
		AGGRO,
		CHARGING
	}
}
