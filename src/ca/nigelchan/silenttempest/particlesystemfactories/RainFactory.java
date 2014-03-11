package ca.nigelchan.silenttempest.particlesystemfactories;

import org.andengine.entity.Entity;
import org.andengine.entity.particle.BatchedPseudoSpriteParticleSystem;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.emitter.RectangleParticleEmitter;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;

import android.opengl.GLES20;
import ca.nigelchan.silenttempest.resources.CommonResource;

public class RainFactory {
	
	public static ParticleSystem<Entity> create(CommonResource resource, int minRate, int maxRate, int maxParticles) {
		BatchedPseudoSpriteParticleSystem ps = new BatchedPseudoSpriteParticleSystem(
			new RectangleParticleEmitter(
				resource.getScreenWidth() / 2 + resource.getDPI(),
				0,
				resource.getScreenWidth() + resource.getDPI() * 2,
				1
			),
			minRate,
			maxRate,
			maxParticles,
			resource.getRain(),
			resource.getVertexBufferObjectManager()
		);
		ps.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
		ps.addParticleInitializer(new VelocityParticleInitializer<Entity>(-resource.getDPI() * 2, resource.getScreenHeight() * 2));
		ps.addParticleInitializer(new RotationParticleInitializer<Entity>(10, 20));
		ps.addParticleInitializer(new ExpireParticleInitializer<Entity>(1));
		ps.addParticleInitializer(new AlphaParticleInitializer<Entity>(0.8f));
		
		return ps;
	}
	
	private RainFactory() {}

}
