package cosmitronique.ld33;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import cosmitronique.ld33.Font.*;

public class Main extends ApplicationAdapter {
	SpriteBatch batch;
	FontManager font;
	OrthographicCamera cam;
	int mouseX;
	int mouseY;
	Texture pixel;
	Texture testTex;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new FontManager();
		cam = new OrthographicCamera(640,480 * (4/3));
		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();
        pixel = new Texture(Gdx.files.internal("Debug/pixel.png"));
        testTex = new Texture(Gdx.files.internal("Sprites/easterEgg.png"));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		
		mouseX = Gdx.input.getX();
		mouseY = Gdx.input.getY();
		mouseY = -mouseY + 600;
		
		mouseX = 640 * mouseX / 800;
		mouseY = 480 * mouseY / 600;
		
		batch.begin();
		font.drawMessage("HELLO World. You are so lovely today :D LUDUM DARE 33", batch, 0, 0, 2f, Color.WHITE);
		font.drawMultilinedMessage("X: " + mouseX + "\nY: " + mouseY, batch, 100, 100, 2f, Color.GREEN);
		
		mouseInputTest(batch);
		
		batch.draw(pixel, mouseX, mouseY);
		batch.end();
	}
	
	private void mouseInputTest(SpriteBatch batch){
		Rectangle text = new Rectangle();
		text.width = 6 * 8 * 2;
		text.height = 2 * 8 * 2;
		text.x= 100;
		text.y = 100 - (text.height/2);
		
		Rectangle mouse = new Rectangle();
		mouse.height = 1;
		mouse.width = 1;
		mouse.x = mouseX;
		mouse.y = mouseY;
		
		if (mouse.overlaps(text)){
			if(Gdx.input.isButtonPressed(0))
				batch.draw(testTex,100,100 - (text.height/2));
		}
		
	}
	
}
