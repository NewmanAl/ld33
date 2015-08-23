package cosmitronique.ld33;

import java.awt.Cursor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import cosmitronique.ld33.Font.*;
import cosmitronique.ld33.Sprite.Suspect;
import cosmitronique.ld33.Sprite.Suspect.BODY_TYPE;
import cosmitronique.ld33.Sprite.Suspect.FACE;
import cosmitronique.ld33.Sprite.Suspect.HAIR;
import cosmitronique.ld33.Sprite.Suspect.SHIRT_COLOR;
import cosmitronique.ld33.Sprite.Suspect.SHIRT_TYPE;

public class Main extends ApplicationAdapter {
	private SpriteBatch batch;
	private FontManager font;
	private OrthographicCamera cam;
	private int mouseX;
	private int mouseY;
	private Texture cursorTex;
	private TextureRegion[][] cursor;
	
	private Texture background;
	private Texture backgroundWindow;
	private Suspect[] suspects;
	private Rectangle[] peopleRecs;
	private boolean[] hoveredOver;
	private boolean[] selected;
	private boolean prevClicked;
	private Rectangle mouseRec;
	private Pixmap dummyCursor;
	
	private Texture shuffleTex;
	private TextureRegion[][] shuffle;
	private Rectangle shuffleRec;
	
	private Texture confirmTex;
	private TextureRegion[][] confirm;
	private Rectangle confirmRec;
	
	private Texture deskFolder;
	private Rectangle deskFolderRec;
	
	
	private enum GAME_STATE{
		BEGIN_LEVEL,
		REGULAR,
		SHUFFLE,
		VIEW_DOC,
		AWAIT_RESULT,
		END_LEVEL,
		GAME_OVER
		
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new FontManager();
		cam = new OrthographicCamera(640,480 * (4/3));
		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();
        
        cursorTex = new Texture(Gdx.files.internal("Sprites/cursor.png"));
        cursor = TextureRegion.split(cursorTex, 122, 87);
        
        shuffleTex = new Texture(Gdx.files.internal("Sprites/shuffle.png"));
        shuffle = TextureRegion.split(shuffleTex, 76, 55);
        shuffleRec = new Rectangle(82, -395 - 55 + 480, 76, 55);
        
        confirmTex = new Texture(Gdx.files.internal("Sprites/confirm.png"));
        confirm = TextureRegion.split(confirmTex, 78, 66);
        confirmRec = new Rectangle(106, -317 - 66 + 480, 78, 66);
        
        deskFolder = new Texture(Gdx.files.internal("Sprites/folder.png"));
        deskFolderRec = new Rectangle(214, -373 - 110 + 480, 211, 110);
        
        background = new Texture(Gdx.files.internal("Sprites/Background.png"));
        backgroundWindow = new Texture(Gdx.files.internal("Sprites/BackgroundWindow.png"));
        
        suspects = new Suspect[5];
        suspects[0] = new Suspect(BODY_TYPE.LARGE, FACE.ONE, HAIR.SHORT, SHIRT_COLOR.PURPLE, SHIRT_TYPE.ONE, false, 100, -146 - 173 +480);
        suspects[1] = new Suspect(BODY_TYPE.LARGE, FACE.TWO, HAIR.BALD, SHIRT_COLOR.BROWN, SHIRT_TYPE.TWO, false, 197, -146 - 173 +480);
        suspects[2] = new Suspect(BODY_TYPE.LARGE, FACE.THREE, HAIR.LONG, SHIRT_COLOR.BLUE, SHIRT_TYPE.THREE, false, 293, -146 - 173 +480);
        suspects[3] = new Suspect(BODY_TYPE.LARGE, FACE.FOUR, HAIR.STYLE, SHIRT_COLOR.GREEN, SHIRT_TYPE.ONE, false, 389, -146 - 173 +480);
        suspects[4] = new Suspect(BODY_TYPE.LARGE, FACE.FOUR, HAIR.LONG_LONG, SHIRT_COLOR.RED, SHIRT_TYPE.FOUR, false, 485, -146 - 173 +480);
        
        peopleRecs = new Rectangle[5];
        peopleRecs[0] = new Rectangle(suspects[0].xPos, suspects[0].yPos,59,173);
        peopleRecs[1] = new Rectangle(suspects[1].xPos, suspects[1].yPos,59,173);
        peopleRecs[2] = new Rectangle(suspects[2].xPos, suspects[2].yPos, 59,173);
        peopleRecs[3] = new Rectangle(suspects[3].xPos, suspects[3].yPos,59,173);
        peopleRecs[4] = new Rectangle(suspects[4].xPos, suspects[4].yPos,59,173);
        hoveredOver = new boolean[5];
        selected = new boolean[5];
        mouseRec = new Rectangle();
        mouseRec.width = 1;
        mouseRec.height = 1;
        
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		
		mouseX = Gdx.input.getX();
		mouseY = Gdx.input.getY();
		mouseY = -mouseY + 600;
		
		mouseX = 640 * mouseX / 800;
		mouseY = 480 * mouseY / 600;
		mouseRec.x = mouseX;
		mouseRec.y = mouseY;
		
		batch.begin();
		batch.draw(backgroundWindow, 46,161);
		
		doPeople(batch);
		
		batch.draw(background, 0, 0);
		
		if(Gdx.input.isButtonPressed(0) && mouseRec.overlaps(shuffleRec))
			batch.draw(shuffle[0][1], 82, -413 -55 + 480);
		else
			batch.draw(shuffle[0][0], 82, -413 - 55 + 480);
		
		if(selected[0] || selected[1] || selected[2] || selected[3] || selected[4]){
			if(Gdx.input.isButtonPressed(0) && mouseRec.overlaps(confirmRec))
				batch.draw(confirm[0][2], 106, -335 - 66 + 480);
			else
				batch.draw(confirm[0][1], 106, -335 - 66 + 480);
		}
		else
			batch.draw(confirm[0][0], 106, -335 - 66 + 480);
		
		batch.draw(deskFolder, 214, -373 - 110 + 480);
		
		//font.drawMessage("X: " + mouseX + " - Y: " + mouseY, batch, 0, (float)(8 * 2), 2.0f, Color.WHITE);
		if(Gdx.input.isButtonPressed(0))
			batch.draw(cursor[0][1], mouseX, mouseY - cursor[0][1].getRegionHeight());
		else
			batch.draw(cursor[0][0], mouseX, mouseY - cursor[0][0].getRegionHeight());
		batch.end();
	}
	
	@Override
	public void dispose(){
		batch.dispose();
		font.dispose();
		cursorTex.dispose();
		for(int i = 0; i< cursor[0].length; i++)
			cursor[0][i].getTexture().dispose();
		background.dispose();
		backgroundWindow.dispose();
		
		shuffleTex.dispose();
		for(int i = 0; i< shuffle[0].length; i++)
			shuffle[0][i].getTexture().dispose();
		
		confirmTex.dispose();
		for(int i = 0; i<confirm[0].length; i++)
			confirm[0][i].getTexture().dispose();
		
		deskFolder.dispose();
		
		for(int i = 0; i<suspects.length; i++)
			suspects[i].dispose();
		Suspect.disposeAllTextures();
		
		//dummyCursor.dispose();
	}
	
	private void doPeople(SpriteBatch batch){//monstrous name... :D
		
		hoveredOver[0] = mouseRec.overlaps(peopleRecs[0]);
		hoveredOver[1] = mouseRec.overlaps(peopleRecs[1]);
		hoveredOver[2] = mouseRec.overlaps(peopleRecs[2]);
		hoveredOver[3] = mouseRec.overlaps(peopleRecs[3]);
		hoveredOver[4] = mouseRec.overlaps(peopleRecs[4]);
		
		if(hoveredOver[0] || selected[0]){
			batch.setColor(Color.RED);
			suspects[0].Draw(batch);
			batch.setColor(Color.WHITE);
		}else
			suspects[0].Draw(batch);
		
		if(hoveredOver[1] || selected[1]){
			batch.setColor(Color.RED);
			suspects[1].Draw(batch);
			batch.setColor(Color.WHITE);
		}else
			suspects[1].Draw(batch);
		
		if(hoveredOver[2] || selected[2]){
			batch.setColor(Color.RED);
			suspects[2].Draw(batch);
			batch.setColor(Color.WHITE);
		}else
			suspects[2].Draw(batch);

		if(hoveredOver[3] || selected[3]){
			batch.setColor(Color.RED);
			suspects[3].Draw(batch);
			batch.setColor(Color.WHITE);
		}else
			suspects[3].Draw(batch);
		
		if(hoveredOver[4] || selected[4]){
			batch.setColor(Color.RED);
			suspects[4].Draw(batch);
			batch.setColor(Color.WHITE);
		}else
			suspects[4].Draw(batch);
		
		//check input
		if(Gdx.input.isButtonPressed(0)){
			if( !prevClicked && (hoveredOver[0] || hoveredOver[1] || hoveredOver[2] || hoveredOver[3] || hoveredOver[4])){
				if(hoveredOver[0]){
					selected[1] = selected[2] = selected[3] = selected[4] =false;
					selected[0] = !selected[0];
				}
				else if(hoveredOver[1]){
					selected[0] = selected[2] = selected[3] = selected[4] =false;
					selected[1] = !selected[1];
				}
				else if(hoveredOver[2]){
					selected[0] = selected[1] = selected[3] = selected[4] = false;
					selected[2] = !selected[2];
				}
				else if(hoveredOver[3]){
					selected[0] = selected[1] = selected[2] = selected[4] = false;
					selected[3] = !selected[3];
				}
				else if(hoveredOver[4]){
					selected[0] = selected[1] = selected[2] = selected[3] = false;
					selected[4] = !selected[4];
				}
				
				prevClicked = true;
			}
		}else
			prevClicked = false;
	
	}
	
}
