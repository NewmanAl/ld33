package cosmitronique.ld33;

import java.awt.Cursor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
	
	private Texture openingTex;
	private float elapsedTime;
	private boolean flashText;
	private float fade = 1;
	
	private Texture shuffleTex;
	private TextureRegion[][] shuffle;
	private Rectangle shuffleRec;
	
	private Texture confirmTex;
	private TextureRegion[][] confirm;
	private Rectangle confirmRec;
	
	private Texture deskFolder;
	private Rectangle deskFolderRec;
	
	private Texture folderClosed;
	private Texture folderOpen;
	float folderY;
	
	private Texture clockTex;
	private TextureRegion[][] clock;
	private int clockState;
	private boolean timeRunout;
	
	private Music backgroundMusic;
	
	private int level;
	private int numCorrect;
	private boolean started;
	private boolean shuffleState;
	
	
	private enum GAME_STATE{
		OPENING,
		OPENING_FADE,
		BEGIN_FADE,
		BEGIN_LEVEL,
		BEGIN_REGULAR,
		REGULAR,
		FOLDER_UP,
		SHUFFLE_START,
		SHUFFLE_END,
		AWAIT_RESULT,
		RESULT,
		END_LEVEL,
		GAME_OVER
	}
	private GAME_STATE gameState;
	
	private Sound yay;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new FontManager();
		cam = new OrthographicCamera(640,480 * (4/3));
		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();
        
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/You are the Monster.mp3"));
        backgroundMusic.setVolume(0);
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
        
        yay = Gdx.audio.newSound(Gdx.files.internal("Sound/yay.mp3"));
        
        gameState = GAME_STATE.OPENING;
        openingTex = new Texture(Gdx.files.internal("Sprites/NEwspaper_opening.png"));
        elapsedTime = 0;
        
        
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
        
        folderClosed = new Texture(Gdx.files.internal("Sprites/Large_Folder_Closed.png"));
        folderOpen = new Texture(Gdx.files.internal("Sprites/Large_Folder_Open.png"));
        
        clockTex = new Texture(Gdx.files.internal("Sprites/clocks.png"));
        clock = TextureRegion.split(clockTex, 56, 56);
        
        suspects = new Suspect[5];
        suspects[0] = new Suspect(BODY_TYPE.LARGE, FACE.ONE, HAIR.SHORT, SHIRT_COLOR.PURPLE, SHIRT_TYPE.ONE, false, 100, -146 - 173 +480, font);
        suspects[0].suspectNumber = 1;
        suspects[1] = new Suspect(BODY_TYPE.LARGE, FACE.TWO, HAIR.BALD, SHIRT_COLOR.BROWN, SHIRT_TYPE.TWO, false, 197, -146 - 173 +480, font);
        suspects[1].suspectNumber = 2;
        suspects[2] = new Suspect(BODY_TYPE.LARGE, FACE.THREE, HAIR.LONG, SHIRT_COLOR.BLUE, SHIRT_TYPE.THREE, false, 293, -146 - 173 +480, font);
        suspects[2].suspectNumber = 3;
        suspects[3] = new Suspect(BODY_TYPE.LARGE, FACE.FOUR, HAIR.STYLE, SHIRT_COLOR.GREEN, SHIRT_TYPE.ONE, false, 389, -146 - 173 +480, font);
        suspects[3].suspectNumber = 4;
        suspects[4] = new Suspect(BODY_TYPE.LARGE, FACE.FOUR, HAIR.LONG_LONG, SHIRT_COLOR.RED, SHIRT_TYPE.FOUR, false, 485, -146 - 173 +480, font);
        suspects[4].suspectNumber = 5;
        
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
        
        //gameState = GAME_STATE.BEGIN_FADE;
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
		mouseRec.x = mouseX;
		mouseRec.y = mouseY;
		
		batch.begin();
		switch(gameState){
		case OPENING:
			batch.draw(openingTex,0,0);

			if(backgroundMusic.getVolume() < 1)
				backgroundMusic.setVolume(Math.min(backgroundMusic.getVolume() + Gdx.graphics.getDeltaTime(), 1));
			else{
			
				elapsedTime += Gdx.graphics.getDeltaTime();
				
				if(elapsedTime >= 1){
					//reset flashtime
					flashText = !flashText;
					elapsedTime = 0;
				}
				if(flashText)
					font.drawMessage("CLICK ANYWHERE TO START", batch, 264, 10, 2, Color.WHITE);
				
				if(Gdx.input.isButtonPressed(0)){
					elapsedTime = 0;
					flashText = false;
					gameState = GAME_STATE.OPENING_FADE;
				}
			}
			break;
		case OPENING_FADE:
			fade -= Gdx.graphics.getDeltaTime();
			if (fade < 0)
				fade = 0;
			
			batch.setColor(1,1,1,fade);
			batch.draw(openingTex, 0 ,0);
			batch.setColor(Color.WHITE);
			
			if(fade == 0)
				gameState = GAME_STATE.BEGIN_FADE;
			
			break;
		case BEGIN_FADE:
			fade += Gdx.graphics.getDeltaTime();
			if(fade > 1){
				fade = 1;
			}
			
			batch.setColor(1,1,1,fade);
			batch.draw(background, 0,0);
			batch.draw(folderOpen,-94,0);
			font.drawMultilinedMessage("You are an agent of the P.A.I.N agency \n(Protection Against Intelligent \nNon-Humans), charged with ridding the \ncity of its shape-shifting monsters.\n\nAs an expert monster spotter, you \nmust find the monster hidden among the \nsuspects brought before you. Use the \nfile provided for clues.\n\nGood luck!",
					batch, 290, 300, 1, Color.BLACK);
			batch.setColor(Color.WHITE);
			
			if(fade == 1){
				font.drawMessage("Click anywhere to begin!", batch, 350, 25, 1, Color.BLACK);
				
				if(Gdx.input.isButtonPressed(0)){
					elapsedTime = 0;
					gameState = GAME_STATE.BEGIN_LEVEL;
					prevClicked = true;
				}
			}
				
			break;
		case BEGIN_LEVEL:
			switch(level){
			case 0:
				batch.draw(background,0,0);
				batch.draw(folderOpen, -94,0);
				font.drawMultilinedMessage("Case 001\n\n\n- The monster is tall.\n- The monster likes green.", batch, 290, 300, 1, Color.BLACK);
				
				if(Gdx.input.isButtonPressed(0) && !prevClicked && !started){
					//set up "suspsects
					suspects[0].bodyType = BODY_TYPE.SMALL;
					suspects[0].shirtType = SHIRT_TYPE.TWO;
					suspects[0].shirtColor = SHIRT_COLOR.PURPLE;
					suspects[0].faceType = FACE.FOUR;
					suspects[0].hairType = HAIR.LONG_LONG;
					suspects[0].isMonster = false;
					suspects[0].initBodyParts();
					
					suspects[1].bodyType = BODY_TYPE.MED;
					suspects[1].shirtType = SHIRT_TYPE.ONE;
					suspects[1].shirtColor = SHIRT_COLOR.GREEN;
					suspects[1].faceType = FACE.THREE;
					suspects[1].hairType = HAIR.BALD;
					suspects[1].isMonster = false;
					suspects[1].initBodyParts();
					
					suspects[2].bodyType = BODY_TYPE.MED;
					suspects[2].shirtType = SHIRT_TYPE.THREE;
					suspects[2].shirtColor = SHIRT_COLOR.BLUE;
					suspects[2].faceType = FACE.TWO;
					suspects[2].hairType = HAIR.SHORT;
					suspects[2].isMonster = false;
					suspects[2].initBodyParts();
					
					suspects[3].bodyType = BODY_TYPE.LARGE;
					suspects[3].shirtType = SHIRT_TYPE.THREE;
					suspects[3].shirtColor = SHIRT_COLOR.GREEN;
					suspects[3].faceType = FACE.ONE;
					suspects[3].hairType = HAIR.STYLE;
					suspects[3].isMonster = true;
					suspects[3].initBodyParts();
					
					suspects[4].bodyType = BODY_TYPE.SMALL;
					suspects[4].shirtType = SHIRT_TYPE.TWO;
					suspects[4].shirtColor = SHIRT_COLOR.BROWN;
					suspects[4].faceType = FACE.FOUR;
					suspects[4].hairType = HAIR.LONG;
					suspects[4].isMonster = false;
					suspects[4].initBodyParts();
					
					gameState = GAME_STATE.BEGIN_REGULAR;
				}
				else
					if(Gdx.input.isButtonPressed(0) && !prevClicked)
						gameState = GAME_STATE.BEGIN_REGULAR;
					else
					if(!Gdx.input.isButtonPressed(0) &&  prevClicked)
						prevClicked = false;
				
				break;
			case 1:
				batch.draw(background,0,0);
				batch.draw(folderOpen, -94,0);
				font.drawMultilinedMessage("\n\n\n- The monster can’t see very well.\n- The monster is fashionable in purple.\n- The monster is not the tallest.", batch, 290, 300, 1, Color.BLACK);
				
				if(Gdx.input.isButtonPressed(0) && !prevClicked && !started){
					//set up "suspsects
					suspects[0].bodyType = BODY_TYPE.SMALL;
					suspects[0].shirtType = SHIRT_TYPE.THREE;
					suspects[0].shirtColor = SHIRT_COLOR.GREEN;
					suspects[0].faceType = FACE.ONE;
					suspects[0].hairType = HAIR.SHORT;
					suspects[0].isMonster = false;
					suspects[0].suspectNumber = 1;
					suspects[0].initBodyParts();
					
					suspects[1].bodyType = BODY_TYPE.MED;
					suspects[1].shirtType = SHIRT_TYPE.FOUR;
					suspects[1].shirtColor = SHIRT_COLOR.PURPLE;
					suspects[1].faceType = FACE.THREE;
					suspects[1].hairType = HAIR.LONG_LONG;
					suspects[1].isMonster = true;
					suspects[1].suspectNumber = 2;
					suspects[1].initBodyParts();
					
					suspects[2].bodyType = BODY_TYPE.LARGE;
					suspects[2].shirtType = SHIRT_TYPE.TWO;
					suspects[2].shirtColor = SHIRT_COLOR.PURPLE;
					suspects[2].faceType = FACE.THREE;
					suspects[2].hairType = HAIR.STYLE;
					suspects[2].isMonster = false;
					suspects[2].suspectNumber = 3;
					suspects[2].initBodyParts();
					
					suspects[3].bodyType = BODY_TYPE.SMALL;
					suspects[3].shirtType = SHIRT_TYPE.ONE;
					suspects[3].shirtColor = SHIRT_COLOR.RED;
					suspects[3].faceType = FACE.TWO;
					suspects[3].hairType = HAIR.BALD;
					suspects[3].isMonster = false;
					suspects[3].suspectNumber = 4;
					suspects[3].initBodyParts();
					
					suspects[4].bodyType = BODY_TYPE.SMALL;
					suspects[4].shirtType = SHIRT_TYPE.ONE;
					suspects[4].shirtColor = SHIRT_COLOR.PURPLE;
					suspects[4].faceType = FACE.FOUR;
					suspects[4].hairType = HAIR.SHORT;
					suspects[4].isMonster = false;
					suspects[4].suspectNumber = 5;
					suspects[4].initBodyParts();
					
					gameState = GAME_STATE.BEGIN_REGULAR;
				}
				else
					if(Gdx.input.isButtonPressed(0) && !prevClicked)
						gameState = GAME_STATE.BEGIN_REGULAR;
					else
					if(!Gdx.input.isButtonPressed(0) &&  prevClicked)
						prevClicked = false;
				
				break;
			case 2:
				batch.draw(background,0,0);
				batch.draw(folderOpen, -94,0);
				//font.drawMultilinedMessage("\n\n\n- The monster can’t see very well.\n- The monster is fashionable in purple.\n- The monster is not the tallest.", batch, 290, 300, 1, Color.BLACK);
				
				if(Gdx.input.isButtonPressed(0) && !prevClicked && !started){
					//set up "suspsects
					suspects[0].bodyType = BODY_TYPE.MED;
					suspects[0].shirtType = SHIRT_TYPE.ONE;
					suspects[0].shirtColor = SHIRT_COLOR.GREEN;
					suspects[0].faceType = FACE.FOUR;
					suspects[0].hairType = HAIR.LONG_LONG;
					suspects[0].isMonster = false;
					suspects[0].suspectNumber = 1;
					suspects[0].initBodyParts();
					
					suspects[1].bodyType = BODY_TYPE.LARGE;
					suspects[1].shirtType = SHIRT_TYPE.FOUR;
					suspects[1].shirtColor = SHIRT_COLOR.RED;
					suspects[1].faceType = FACE.TWO;
					suspects[1].hairType = HAIR.SHORT;
					suspects[1].isMonster = false;
					suspects[1].suspectNumber = 2;
					suspects[1].initBodyParts();
					
					suspects[2].bodyType = BODY_TYPE.MED;
					suspects[2].shirtType = SHIRT_TYPE.ONE;
					suspects[2].shirtColor = SHIRT_COLOR.RED;
					suspects[2].faceType = FACE.ONE;
					suspects[2].hairType = HAIR.SHORT;
					suspects[2].isMonster = false;
					suspects[2].suspectNumber = 3;
					suspects[2].initBodyParts();
					
					suspects[3].bodyType = BODY_TYPE.LARGE;
					suspects[3].shirtType = SHIRT_TYPE.THREE;
					suspects[3].shirtColor = SHIRT_COLOR.GREEN;
					suspects[3].faceType = FACE.ONE;
					suspects[3].hairType = HAIR.STYLE;
					suspects[3].isMonster = true;
					suspects[3].suspectNumber = 4;
					suspects[3].initBodyParts();
					
					suspects[4].bodyType = BODY_TYPE.SMALL;
					suspects[4].shirtType = SHIRT_TYPE.ONE;
					suspects[4].shirtColor = SHIRT_COLOR.GREEN;
					suspects[4].faceType = FACE.FOUR;
					suspects[4].hairType = HAIR.LONG;
					suspects[4].isMonster = false;
					suspects[4].suspectNumber = 5;
					suspects[4].initBodyParts();
					
					gameState = GAME_STATE.BEGIN_REGULAR;
				}
				else
					if(Gdx.input.isButtonPressed(0) && !prevClicked)
						gameState = GAME_STATE.BEGIN_REGULAR;
					else
					if(!Gdx.input.isButtonPressed(0) &&  prevClicked)
						prevClicked = false;
				break;
			case 3:
				batch.draw(background,0,0);
				batch.draw(folderOpen, -94,0);
				//font.drawMultilinedMessage("\n\n\n- The monster can’t see very well.\n- The monster is fashionable in purple.\n- The monster is not the tallest.", batch, 290, 300, 1, Color.BLACK);
				
				if(Gdx.input.isButtonPressed(0) && !prevClicked && !started){
					//set up "suspsects
					suspects[0].bodyType = BODY_TYPE.MED;
					suspects[0].shirtType = SHIRT_TYPE.TWO;
					suspects[0].shirtColor = SHIRT_COLOR.PURPLE;
					suspects[0].faceType = FACE.THREE;
					suspects[0].hairType = HAIR.LONG;
					suspects[0].isMonster = false;
					suspects[0].suspectNumber = 1;
					suspects[0].initBodyParts();
					
					suspects[1].bodyType = BODY_TYPE.LARGE;
					suspects[1].shirtType = SHIRT_TYPE.FOUR;
					suspects[1].shirtColor = SHIRT_COLOR.BLUE;
					suspects[1].faceType = FACE.FOUR;
					suspects[1].hairType = HAIR.STYLE;
					suspects[1].isMonster = false;
					suspects[1].suspectNumber = 2;
					suspects[1].initBodyParts();
					
					suspects[2].bodyType = BODY_TYPE.MED;
					suspects[2].shirtType = SHIRT_TYPE.ONE;
					suspects[2].shirtColor = SHIRT_COLOR.RED;
					suspects[2].faceType = FACE.ONE;
					suspects[2].hairType = HAIR.LONG_LONG;
					suspects[2].isMonster = false;
					suspects[2].suspectNumber = 3;
					suspects[2].initBodyParts();
					
					suspects[3].bodyType = BODY_TYPE.LARGE;
					suspects[3].shirtType = SHIRT_TYPE.THREE;
					suspects[3].shirtColor = SHIRT_COLOR.BROWN;
					suspects[3].faceType = FACE.THREE;
					suspects[3].hairType = HAIR.BALD;
					suspects[3].isMonster = false;
					suspects[3].suspectNumber = 4;
					suspects[3].initBodyParts();
					
					suspects[4].bodyType = BODY_TYPE.MED;
					suspects[4].shirtType = SHIRT_TYPE.FOUR;
					suspects[4].shirtColor = SHIRT_COLOR.PURPLE;
					suspects[4].faceType = FACE.TWO;
					suspects[4].hairType = HAIR.SHORT;
					suspects[4].isMonster = true;
					suspects[4].suspectNumber = 5;
					suspects[4].initBodyParts();
					
					gameState = GAME_STATE.BEGIN_REGULAR;
				}
				else
					if(Gdx.input.isButtonPressed(0) && !prevClicked)
						gameState = GAME_STATE.BEGIN_REGULAR;
					else
					if(!Gdx.input.isButtonPressed(0) &&  prevClicked)
						prevClicked = false;
				break;
			case 4:
				batch.draw(background,0,0);
				batch.draw(folderOpen, -94,0);
				//font.drawMultilinedMessage("\n\n\n- The monster can’t see very well.\n- The monster is fashionable in purple.\n- The monster is not the tallest.", batch, 290, 300, 1, Color.BLACK);
				
				if(Gdx.input.isButtonPressed(0) && !prevClicked && !started){
					//set up "suspsects
					suspects[0].bodyType = BODY_TYPE.MED;
					suspects[0].shirtType = SHIRT_TYPE.TWO;
					suspects[0].shirtColor = SHIRT_COLOR.PURPLE;
					suspects[0].faceType = FACE.TWO;
					suspects[0].hairType = HAIR.STYLE;
					suspects[0].isMonster = false;
					suspects[0].suspectNumber = 1;
					suspects[0].initBodyParts();
					
					suspects[1].bodyType = BODY_TYPE.SMALL;
					suspects[1].shirtType = SHIRT_TYPE.FOUR;
					suspects[1].shirtColor = SHIRT_COLOR.RED;
					suspects[1].faceType = FACE.ONE;
					suspects[1].hairType = HAIR.LONG;
					suspects[1].isMonster = false;
					suspects[1].suspectNumber = 2;
					suspects[1].initBodyParts();
					
					suspects[2].bodyType = BODY_TYPE.LARGE;
					suspects[2].shirtType = SHIRT_TYPE.TWO;
					suspects[2].shirtColor = SHIRT_COLOR.BLUE;
					suspects[2].faceType = FACE.THREE;
					suspects[2].hairType = HAIR.SHORT;
					suspects[2].isMonster = false;
					suspects[2].suspectNumber = 3;
					suspects[2].initBodyParts();
					
					suspects[3].bodyType = BODY_TYPE.SMALL;
					suspects[3].shirtType = SHIRT_TYPE.ONE;
					suspects[3].shirtColor = SHIRT_COLOR.GREEN;
					suspects[3].faceType = FACE.FOUR;
					suspects[3].hairType = HAIR.STYLE;
					suspects[3].isMonster = true;
					suspects[3].suspectNumber = 4;
					suspects[3].initBodyParts();
					
					suspects[4].bodyType = BODY_TYPE.MED;
					suspects[4].shirtType = SHIRT_TYPE.THREE;
					suspects[4].shirtColor = SHIRT_COLOR.BLUE;
					suspects[4].faceType = FACE.ONE;
					suspects[4].hairType = HAIR.LONG_LONG;
					suspects[4].isMonster = false;
					suspects[4].suspectNumber = 5;
					suspects[4].initBodyParts();
					
					gameState = GAME_STATE.BEGIN_REGULAR;
				}
				else
					if(Gdx.input.isButtonPressed(0) && !prevClicked)
						gameState = GAME_STATE.BEGIN_REGULAR;
					else
					if(!Gdx.input.isButtonPressed(0) &&  prevClicked)
						prevClicked = false;
				break;
				
			}//end switch level
			
			break;
			
		case BEGIN_REGULAR:
			batch.draw(backgroundWindow, 46, 161);
			batch.draw(clock[0][clockState], 292, -53 - 56  + 480);
			for(Suspect s : suspects)
				s.Draw(batch);
			batch.draw(background, 0, 0);
			batch.draw(shuffle[0][0], 82, -413 - 55 + 480);
			batch.draw(confirm[0][0], 106, -335 - 66 + 480);
			batch.draw(folderClosed, -94, folderY);
			
			folderY -= 400 * Gdx.graphics.getDeltaTime();
			
			if(folderY < -463){
				folderY = 0;
				gameState = GAME_STATE.REGULAR;
			}
			
			break;
			
		case REGULAR:
			batch.draw(backgroundWindow, 46, 161);
			
			doPeople(batch);
			
			doClock(batch);
			
			batch.draw(background,0,0);
			
			if(Gdx.input.isButtonPressed(0) && mouseRec.overlaps(shuffleRec)){
				batch.draw(shuffle[0][1], 82, -413 -55 + 480);
				gameState = GAME_STATE.SHUFFLE_START;
				fade = 1;
			}
			else
				batch.draw(shuffle[0][0], 82, -413 - 55 + 480);
			
			if(selected[0] || selected[1] || selected[2] || selected[3] || selected[4]){
				if(Gdx.input.isButtonPressed(0) && mouseRec.overlaps(confirmRec)){
					batch.draw(confirm[0][2], 106, -335 - 66 + 480);
					gameState = GAME_STATE.AWAIT_RESULT;
				}
				else
					batch.draw(confirm[0][1], 106, -335 - 66 + 480);
			}
			else
				batch.draw(confirm[0][0], 106, -335 - 66 + 480);
			
			if(mouseRec.overlaps(deskFolderRec)){
				batch.setColor(Color.RED);
				batch.draw(deskFolder, 214, -373 - 110 + 480);
				batch.setColor(Color.WHITE);
				
				if(Gdx.input.isButtonPressed(0)){
					folderY = - 463;
					gameState = GAME_STATE.FOLDER_UP;
					elapsedTime = 0;
				}
			}else
				batch.draw(deskFolder, 214, -373 - 110 + 480);
			
			if(Gdx.input.isButtonPressed(0))
				batch.draw(cursor[0][1], mouseX, mouseY - cursor[0][1].getRegionHeight());
			else
				batch.draw(cursor[0][0], mouseX, mouseY - cursor[0][0].getRegionHeight());
		
			
			break;
			
		case FOLDER_UP:
			batch.draw(backgroundWindow, 46, 161);
			for(Suspect s : suspects)
				s.Draw(batch);
			batch.draw(background, 0, 0);
			batch.draw(shuffle[0][0], 82, -413 - 55 + 480);
			batch.draw(confirm[0][0], 106, -335 - 66 + 480);
			batch.draw(folderClosed, -94, folderY);
			
			folderY += 400 * Gdx.graphics.getDeltaTime();
			
			if(folderY >= 0){
				folderY = 0;
				started = true;
				gameState = GAME_STATE.BEGIN_LEVEL;
			}
			
			break;
		case SHUFFLE_START:
			fade -= Gdx.graphics.getDeltaTime();
			if(fade <= 0)
				fade = 0;
			
			batch.setColor(1,1,1,fade);
			batch.draw(backgroundWindow, 46, 161);
			for(Suspect s : suspects)
				s.Draw(batch);
			batch.setColor(Color.WHITE);;
			batch.draw(background, 0, 0);
			batch.draw(shuffle[0][0], 82, -413 - 55 + 480);
			batch.draw(confirm[0][0], 106, -335 - 66 + 480);
			
			if(fade == 0){
				gameState = GAME_STATE.SHUFFLE_END;
				shuffleState = !shuffleState;
				shuffle(level);
				System.out.println(level);
			}
			break;
			
		case SHUFFLE_END:
			fade += Gdx.graphics.getDeltaTime();
			if(fade >= 1)
				fade = 1;
			
			batch.setColor(1,1,1,fade);
			batch.draw(backgroundWindow, 46, 161);
			for(Suspect s : suspects)
				s.Draw(batch);
			batch.setColor(Color.WHITE);;
			batch.draw(background, 0, 0);
			batch.draw(shuffle[0][0], 82, -413 - 55 + 480);
			batch.draw(confirm[0][0], 106, -335 - 66 + 480);
			
			if(fade == 1){
				gameState = GAME_STATE.REGULAR;
			}
			
			break;
			
		case AWAIT_RESULT:
			elapsedTime += Gdx.graphics.getDeltaTime();
			
			batch.draw(backgroundWindow, 46, 161);
			for(Suspect s : suspects)
				s.Draw(batch);
			batch.draw(background, 0, 0);
			batch.draw(shuffle[0][0], 82, -413 - 55 + 480);
			batch.draw(confirm[0][0], 106, -335 - 66 + 480);
			
			if(elapsedTime >= 2){
				elapsedTime = 0;
				gameState = GAME_STATE.END_LEVEL;
				if(selected[0] || selected[3])
					yay.play(1.0f);
			}
			break;
		case END_LEVEL:
			int i = 0;
			if(selected[0])
				i = 0;
			if(selected[1])
				i = 1;
			if(selected[2])
				i = 2;
			if(selected[3])
				i = 3;
			if(selected[4])
				i = 4;
			
			if(suspects[i].isMonster){
				font.drawMessage("YOU GOT THE MONSTER", batch, 0, 300, 4, Color.WHITE);
				numCorrect++;
			}else
				font.drawMessage("Noooo~", batch, 0, 300, 4, Color.WHITE);
			
			elapsedTime += Gdx.graphics.getDeltaTime();
			if(elapsedTime >= 3){
				level++;
				started = false;
				shuffleState = false;
				selected[0] = selected[1] = selected [2] = selected[3] = selected[4] = false;
				elapsedTime = 0;
				if(level == 5)
					gameState = GAME_STATE.GAME_OVER;
				else
					gameState = GAME_STATE.BEGIN_LEVEL;
			}
			
			break;
			
		case GAME_OVER:
			
			
				
		}
		/*
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
			
		*/
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
		
		openingTex.dispose();
		
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
		
		backgroundMusic.dispose();
		
		yay.dispose();
		
		folderOpen.dispose();
		folderClosed.dispose();
		
		clockTex.dispose();
		for(int i = 0; i<clock[0].length; i++)
			clock[0][i].getTexture().dispose();
		
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
	
	private void shuffle(int level){
		switch(level){
		case 0:
			if(shuffleState){//switching from initial state
				suspects[4].bodyType = BODY_TYPE.SMALL;
				suspects[4].shirtType = SHIRT_TYPE.TWO;
				suspects[4].shirtColor = SHIRT_COLOR.PURPLE;
				suspects[4].faceType = FACE.FOUR;
				suspects[4].hairType = HAIR.LONG_LONG;
				suspects[4].isMonster = false;
				suspects[4].suspectNumber = 1;
				suspects[4].initBodyParts();
				
				suspects[3].bodyType = BODY_TYPE.MED;
				suspects[3].shirtType = SHIRT_TYPE.ONE;
				suspects[3].shirtColor = SHIRT_COLOR.GREEN;
				suspects[3].faceType = FACE.THREE;
				suspects[3].hairType = HAIR.BALD;
				suspects[3].isMonster = false;
				suspects[3].suspectNumber = 2;
				suspects[3].initBodyParts();
				
				suspects[2].bodyType = BODY_TYPE.MED;
				suspects[2].shirtType = SHIRT_TYPE.THREE;
				suspects[2].shirtColor = SHIRT_COLOR.BLUE;
				suspects[2].faceType = FACE.TWO;
				suspects[2].hairType = HAIR.SHORT;
				suspects[2].isMonster = false;
				suspects[2].suspectNumber = 3;
				suspects[2].initBodyParts();
				
				suspects[0].bodyType = BODY_TYPE.LARGE;
				suspects[0].shirtType = SHIRT_TYPE.THREE;
				suspects[0].shirtColor = SHIRT_COLOR.GREEN;
				suspects[0].faceType = FACE.ONE;
				suspects[0].hairType = HAIR.STYLE;
				suspects[0].isMonster = true;
				suspects[0].suspectNumber = 4;
				suspects[0].initBodyParts();
				
				suspects[1].bodyType = BODY_TYPE.SMALL;
				suspects[1].shirtType = SHIRT_TYPE.TWO;
				suspects[1].shirtColor = SHIRT_COLOR.BROWN;
				suspects[1].faceType = FACE.FOUR;
				suspects[1].hairType = HAIR.LONG;
				suspects[1].isMonster = false;
				suspects[1].suspectNumber = 5;
				suspects[1].initBodyParts();
			}else{//default state
				suspects[0].bodyType = BODY_TYPE.SMALL;
				suspects[0].shirtType = SHIRT_TYPE.TWO;
				suspects[0].shirtColor = SHIRT_COLOR.PURPLE;
				suspects[0].faceType = FACE.FOUR;
				suspects[0].hairType = HAIR.LONG_LONG;
				suspects[0].isMonster = false;
				suspects[0].suspectNumber = 1;
				suspects[0].initBodyParts();
				
				suspects[1].bodyType = BODY_TYPE.MED;
				suspects[1].shirtType = SHIRT_TYPE.ONE;
				suspects[1].shirtColor = SHIRT_COLOR.GREEN;
				suspects[1].faceType = FACE.THREE;
				suspects[1].hairType = HAIR.BALD;
				suspects[1].isMonster = false;
				suspects[1].suspectNumber = 2;
				suspects[1].initBodyParts();
				
				suspects[2].bodyType = BODY_TYPE.MED;
				suspects[2].shirtType = SHIRT_TYPE.THREE;
				suspects[2].shirtColor = SHIRT_COLOR.BLUE;
				suspects[2].faceType = FACE.TWO;
				suspects[2].hairType = HAIR.SHORT;
				suspects[2].isMonster = false;
				suspects[2].suspectNumber = 3;
				suspects[2].initBodyParts();
				
				suspects[3].bodyType = BODY_TYPE.LARGE;
				suspects[3].shirtType = SHIRT_TYPE.THREE;
				suspects[3].shirtColor = SHIRT_COLOR.GREEN;
				suspects[3].faceType = FACE.ONE;
				suspects[3].hairType = HAIR.STYLE;
				suspects[3].isMonster = true;
				suspects[3].suspectNumber = 4;
				suspects[3].initBodyParts();
				
				suspects[4].bodyType = BODY_TYPE.SMALL;
				suspects[4].shirtType = SHIRT_TYPE.TWO;
				suspects[4].shirtColor = SHIRT_COLOR.BROWN;
				suspects[4].faceType = FACE.FOUR;
				suspects[4].hairType = HAIR.LONG;
				suspects[4].isMonster = false;
				suspects[4].suspectNumber = 5;
				suspects[4].initBodyParts();
			}
			break;
		case 1:
			if(shuffleState){
				suspects[2].bodyType = BODY_TYPE.SMALL;
				suspects[2].shirtType = SHIRT_TYPE.THREE;
				suspects[2].shirtColor = SHIRT_COLOR.GREEN;
				suspects[2].faceType = FACE.ONE;
				suspects[2].hairType = HAIR.SHORT;
				suspects[2].isMonster = false;
				suspects[2].suspectNumber = 1;
				suspects[2].initBodyParts();
				
				suspects[0].bodyType = BODY_TYPE.MED;
				suspects[0].shirtType = SHIRT_TYPE.FOUR;
				suspects[0].shirtColor = SHIRT_COLOR.PURPLE;
				suspects[0].faceType = FACE.THREE;
				suspects[0].hairType = HAIR.LONG_LONG;
				suspects[0].isMonster = false;
				suspects[0].suspectNumber = 2;
				suspects[0].initBodyParts();
				
				suspects[4].bodyType = BODY_TYPE.LARGE;
				suspects[4].shirtType = SHIRT_TYPE.TWO;
				suspects[4].shirtColor = SHIRT_COLOR.PURPLE;
				suspects[4].faceType = FACE.THREE;
				suspects[4].hairType = HAIR.STYLE;
				suspects[4].isMonster = false;
				suspects[4].suspectNumber = 3;
				suspects[4].initBodyParts();
				
				suspects[3].bodyType = BODY_TYPE.SMALL;
				suspects[3].shirtType = SHIRT_TYPE.ONE;
				suspects[3].shirtColor = SHIRT_COLOR.RED;
				suspects[3].faceType = FACE.TWO;
				suspects[3].hairType = HAIR.BALD;
				suspects[3].isMonster = false;
				suspects[3].suspectNumber = 4;
				suspects[3].initBodyParts();
				
				suspects[1].bodyType = BODY_TYPE.SMALL;
				suspects[1].shirtType = SHIRT_TYPE.ONE;
				suspects[1].shirtColor = SHIRT_COLOR.PURPLE;
				suspects[1].faceType = FACE.FOUR;
				suspects[1].hairType = HAIR.SHORT;
				suspects[1].isMonster = true;
				suspects[1].suspectNumber = 5;
				suspects[1].initBodyParts();
			}else{
				suspects[0].bodyType = BODY_TYPE.SMALL;
				suspects[0].shirtType = SHIRT_TYPE.THREE;
				suspects[0].shirtColor = SHIRT_COLOR.GREEN;
				suspects[0].faceType = FACE.ONE;
				suspects[0].hairType = HAIR.SHORT;
				suspects[0].isMonster = false;
				suspects[0].suspectNumber = 1;
				suspects[0].initBodyParts();
				
				suspects[1].bodyType = BODY_TYPE.MED;
				suspects[1].shirtType = SHIRT_TYPE.FOUR;
				suspects[1].shirtColor = SHIRT_COLOR.PURPLE;
				suspects[1].faceType = FACE.THREE;
				suspects[1].hairType = HAIR.LONG_LONG;
				suspects[1].isMonster = true;
				suspects[1].suspectNumber = 2;
				suspects[1].initBodyParts();
				
				suspects[2].bodyType = BODY_TYPE.LARGE;
				suspects[2].shirtType = SHIRT_TYPE.TWO;
				suspects[2].shirtColor = SHIRT_COLOR.PURPLE;
				suspects[2].faceType = FACE.THREE;
				suspects[2].hairType = HAIR.STYLE;
				suspects[2].isMonster = false;
				suspects[2].suspectNumber = 3;
				suspects[2].initBodyParts();
				
				suspects[3].bodyType = BODY_TYPE.SMALL;
				suspects[3].shirtType = SHIRT_TYPE.ONE;
				suspects[3].shirtColor = SHIRT_COLOR.RED;
				suspects[3].faceType = FACE.TWO;
				suspects[3].hairType = HAIR.BALD;
				suspects[3].isMonster = false;
				suspects[3].suspectNumber = 4;
				suspects[3].initBodyParts();
				
				suspects[4].bodyType = BODY_TYPE.SMALL;
				suspects[4].shirtType = SHIRT_TYPE.ONE;
				suspects[4].shirtColor = SHIRT_COLOR.PURPLE;
				suspects[4].faceType = FACE.FOUR;
				suspects[4].hairType = HAIR.SHORT;
				suspects[4].isMonster = false;
				suspects[4].suspectNumber = 5;
				suspects[4].initBodyParts();
			}
			break;
		case 2:
			if(shuffleState){
				suspects[1].bodyType = BODY_TYPE.MED;
				suspects[1].shirtType = SHIRT_TYPE.ONE;
				suspects[1].shirtColor = SHIRT_COLOR.GREEN;
				suspects[1].faceType = FACE.FOUR;
				suspects[1].hairType = HAIR.LONG_LONG;
				suspects[1].isMonster = false;
				suspects[1].suspectNumber = 1;
				suspects[1].initBodyParts();
				
				suspects[0].bodyType = BODY_TYPE.LARGE;
				suspects[0].shirtType = SHIRT_TYPE.FOUR;
				suspects[0].shirtColor = SHIRT_COLOR.RED;
				suspects[0].faceType = FACE.TWO;
				suspects[0].hairType = HAIR.SHORT;
				suspects[0].isMonster = false;
				suspects[0].suspectNumber = 2;
				suspects[0].initBodyParts();
				
				suspects[2].bodyType = BODY_TYPE.MED;
				suspects[2].shirtType = SHIRT_TYPE.ONE;
				suspects[2].shirtColor = SHIRT_COLOR.RED;
				suspects[2].faceType = FACE.ONE;
				suspects[2].hairType = HAIR.SHORT;
				suspects[2].isMonster = false;
				suspects[2].suspectNumber = 3;
				suspects[2].initBodyParts();
				
				suspects[4].bodyType = BODY_TYPE.LARGE;
				suspects[4].shirtType = SHIRT_TYPE.THREE;
				suspects[4].shirtColor = SHIRT_COLOR.GREEN;
				suspects[4].faceType = FACE.ONE;
				suspects[4].hairType = HAIR.STYLE;
				suspects[4].isMonster = false;
				suspects[4].suspectNumber = 4;
				suspects[4].initBodyParts();
				
				suspects[3].bodyType = BODY_TYPE.SMALL;
				suspects[3].shirtType = SHIRT_TYPE.ONE;
				suspects[3].shirtColor = SHIRT_COLOR.GREEN;
				suspects[3].faceType = FACE.FOUR;
				suspects[3].hairType = HAIR.LONG;
				suspects[3].isMonster = true;
				suspects[3].suspectNumber = 5;
				suspects[3].initBodyParts();
			}else{
				suspects[0].bodyType = BODY_TYPE.MED;
				suspects[0].shirtType = SHIRT_TYPE.ONE;
				suspects[0].shirtColor = SHIRT_COLOR.GREEN;
				suspects[0].faceType = FACE.FOUR;
				suspects[0].hairType = HAIR.LONG_LONG;
				suspects[0].isMonster = false;
				suspects[0].suspectNumber = 1;
				suspects[0].initBodyParts();
				
				suspects[1].bodyType = BODY_TYPE.LARGE;
				suspects[1].shirtType = SHIRT_TYPE.FOUR;
				suspects[1].shirtColor = SHIRT_COLOR.RED;
				suspects[1].faceType = FACE.TWO;
				suspects[1].hairType = HAIR.SHORT;
				suspects[1].isMonster = false;
				suspects[1].suspectNumber = 2;
				suspects[1].initBodyParts();
				
				suspects[2].bodyType = BODY_TYPE.MED;
				suspects[2].shirtType = SHIRT_TYPE.ONE;
				suspects[2].shirtColor = SHIRT_COLOR.RED;
				suspects[2].faceType = FACE.ONE;
				suspects[2].hairType = HAIR.SHORT;
				suspects[2].isMonster = false;
				suspects[2].suspectNumber = 3;
				suspects[2].initBodyParts();
				
				suspects[3].bodyType = BODY_TYPE.LARGE;
				suspects[3].shirtType = SHIRT_TYPE.THREE;
				suspects[3].shirtColor = SHIRT_COLOR.GREEN;
				suspects[3].faceType = FACE.ONE;
				suspects[3].hairType = HAIR.STYLE;
				suspects[3].isMonster = true;
				suspects[3].suspectNumber = 4;
				suspects[3].initBodyParts();
				
				suspects[4].bodyType = BODY_TYPE.SMALL;
				suspects[4].shirtType = SHIRT_TYPE.ONE;
				suspects[4].shirtColor = SHIRT_COLOR.GREEN;
				suspects[4].faceType = FACE.FOUR;
				suspects[4].hairType = HAIR.LONG;
				suspects[4].isMonster = false;
				suspects[4].suspectNumber = 5;
				suspects[4].initBodyParts();
			}
			break;
		case 3:
			if(shuffleState){
				suspects[2].bodyType = BODY_TYPE.MED;
				suspects[2].shirtType = SHIRT_TYPE.TWO;
				suspects[2].shirtColor = SHIRT_COLOR.PURPLE;
				suspects[2].faceType = FACE.THREE;
				suspects[2].hairType = HAIR.LONG;
				suspects[2].isMonster = false;
				suspects[2].suspectNumber = 1;
				suspects[2].initBodyParts();
				
				suspects[0].bodyType = BODY_TYPE.LARGE;
				suspects[0].shirtType = SHIRT_TYPE.FOUR;
				suspects[0].shirtColor = SHIRT_COLOR.BLUE;
				suspects[0].faceType = FACE.FOUR;
				suspects[0].hairType = HAIR.STYLE;
				suspects[0].isMonster = false;
				suspects[0].suspectNumber = 2;
				suspects[0].initBodyParts();
				
				suspects[4].bodyType = BODY_TYPE.MED;
				suspects[4].shirtType = SHIRT_TYPE.ONE;
				suspects[4].shirtColor = SHIRT_COLOR.RED;
				suspects[4].faceType = FACE.ONE;
				suspects[4].hairType = HAIR.LONG_LONG;
				suspects[4].isMonster = true;
				suspects[4].suspectNumber = 3;
				suspects[4].initBodyParts();
				
				suspects[3].bodyType = BODY_TYPE.LARGE;
				suspects[3].shirtType = SHIRT_TYPE.THREE;
				suspects[3].shirtColor = SHIRT_COLOR.BROWN;
				suspects[3].faceType = FACE.THREE;
				suspects[3].hairType = HAIR.BALD;
				suspects[3].isMonster = false;
				suspects[3].suspectNumber = 4;
				suspects[3].initBodyParts();
				
				suspects[1].bodyType = BODY_TYPE.MED;
				suspects[1].shirtType = SHIRT_TYPE.FOUR;
				suspects[1].shirtColor = SHIRT_COLOR.PURPLE;
				suspects[1].faceType = FACE.TWO;
				suspects[1].hairType = HAIR.SHORT;
				suspects[1].isMonster = false;
				suspects[1].suspectNumber = 5;
				suspects[1].initBodyParts();
			}else{
				suspects[0].bodyType = BODY_TYPE.MED;
				suspects[0].shirtType = SHIRT_TYPE.TWO;
				suspects[0].shirtColor = SHIRT_COLOR.PURPLE;
				suspects[0].faceType = FACE.THREE;
				suspects[0].hairType = HAIR.LONG;
				suspects[0].isMonster = false;
				suspects[0].suspectNumber = 1;
				suspects[0].initBodyParts();
				
				suspects[1].bodyType = BODY_TYPE.LARGE;
				suspects[1].shirtType = SHIRT_TYPE.FOUR;
				suspects[1].shirtColor = SHIRT_COLOR.BLUE;
				suspects[1].faceType = FACE.FOUR;
				suspects[1].hairType = HAIR.STYLE;
				suspects[1].isMonster = false;
				suspects[1].suspectNumber = 2;
				suspects[1].initBodyParts();
				
				suspects[2].bodyType = BODY_TYPE.MED;
				suspects[2].shirtType = SHIRT_TYPE.ONE;
				suspects[2].shirtColor = SHIRT_COLOR.RED;
				suspects[2].faceType = FACE.ONE;
				suspects[2].hairType = HAIR.LONG_LONG;
				suspects[2].isMonster = false;
				suspects[2].suspectNumber = 3;
				suspects[2].initBodyParts();
				
				suspects[3].bodyType = BODY_TYPE.LARGE;
				suspects[3].shirtType = SHIRT_TYPE.THREE;
				suspects[3].shirtColor = SHIRT_COLOR.BROWN;
				suspects[3].faceType = FACE.THREE;
				suspects[3].hairType = HAIR.BALD;
				suspects[3].isMonster = false;
				suspects[3].suspectNumber = 4;
				suspects[3].initBodyParts();
				
				suspects[4].bodyType = BODY_TYPE.MED;
				suspects[4].shirtType = SHIRT_TYPE.FOUR;
				suspects[4].shirtColor = SHIRT_COLOR.PURPLE;
				suspects[4].faceType = FACE.TWO;
				suspects[4].hairType = HAIR.SHORT;
				suspects[4].isMonster = true;
				suspects[4].suspectNumber = 5;
				suspects[4].initBodyParts();
			}
			break;
		case 4:
			if(shuffleState){
				suspects[0].bodyType = BODY_TYPE.MED;
				suspects[0].shirtType = SHIRT_TYPE.TWO;
				suspects[0].shirtColor = SHIRT_COLOR.PURPLE;
				suspects[0].faceType = FACE.TWO;
				suspects[0].hairType = HAIR.STYLE;
				suspects[0].isMonster = false;
				suspects[0].suspectNumber = 1;
				suspects[0].initBodyParts();
				
				suspects[3].bodyType = BODY_TYPE.SMALL;
				suspects[3].shirtType = SHIRT_TYPE.FOUR;
				suspects[3].shirtColor = SHIRT_COLOR.RED;
				suspects[3].faceType = FACE.ONE;
				suspects[3].hairType = HAIR.LONG;
				suspects[3].isMonster = true;
				suspects[3].suspectNumber = 2;
				suspects[3].initBodyParts();
				
				suspects[1].bodyType = BODY_TYPE.LARGE;
				suspects[1].shirtType = SHIRT_TYPE.TWO;
				suspects[1].shirtColor = SHIRT_COLOR.BLUE;
				suspects[1].faceType = FACE.THREE;
				suspects[1].hairType = HAIR.SHORT;
				suspects[1].isMonster = false;
				suspects[1].suspectNumber = 3;
				suspects[1].initBodyParts();
				
				suspects[2].bodyType = BODY_TYPE.SMALL;
				suspects[2].shirtType = SHIRT_TYPE.ONE;
				suspects[2].shirtColor = SHIRT_COLOR.GREEN;
				suspects[2].faceType = FACE.FOUR;
				suspects[2].hairType = HAIR.STYLE;
				suspects[2].isMonster = false;
				suspects[2].suspectNumber = 4;
				suspects[2].initBodyParts();
				
				suspects[4].bodyType = BODY_TYPE.MED;
				suspects[4].shirtType = SHIRT_TYPE.THREE;
				suspects[4].shirtColor = SHIRT_COLOR.BLUE;
				suspects[4].faceType = FACE.ONE;
				suspects[4].hairType = HAIR.LONG_LONG;
				suspects[4].isMonster = false;
				suspects[4].suspectNumber = 5;
				suspects[4].initBodyParts();
			}else{
				suspects[0].bodyType = BODY_TYPE.MED;
				suspects[0].shirtType = SHIRT_TYPE.TWO;
				suspects[0].shirtColor = SHIRT_COLOR.PURPLE;
				suspects[0].faceType = FACE.TWO;
				suspects[0].hairType = HAIR.STYLE;
				suspects[0].isMonster = false;
				suspects[0].suspectNumber = 1;
				suspects[0].initBodyParts();
				
				suspects[1].bodyType = BODY_TYPE.SMALL;
				suspects[1].shirtType = SHIRT_TYPE.FOUR;
				suspects[1].shirtColor = SHIRT_COLOR.RED;
				suspects[1].faceType = FACE.ONE;
				suspects[1].hairType = HAIR.LONG;
				suspects[1].isMonster = false;
				suspects[1].suspectNumber = 2;
				suspects[1].initBodyParts();
				
				suspects[2].bodyType = BODY_TYPE.LARGE;
				suspects[2].shirtType = SHIRT_TYPE.TWO;
				suspects[2].shirtColor = SHIRT_COLOR.BLUE;
				suspects[2].faceType = FACE.THREE;
				suspects[2].hairType = HAIR.SHORT;
				suspects[2].isMonster = false;
				suspects[2].suspectNumber = 3;
				suspects[2].initBodyParts();
				
				suspects[3].bodyType = BODY_TYPE.SMALL;
				suspects[3].shirtType = SHIRT_TYPE.ONE;
				suspects[3].shirtColor = SHIRT_COLOR.GREEN;
				suspects[3].faceType = FACE.FOUR;
				suspects[3].hairType = HAIR.STYLE;
				suspects[3].isMonster = true;
				suspects[3].suspectNumber = 4;
				suspects[3].initBodyParts();
				
				suspects[4].bodyType = BODY_TYPE.MED;
				suspects[4].shirtType = SHIRT_TYPE.THREE;
				suspects[4].shirtColor = SHIRT_COLOR.BLUE;
				suspects[4].faceType = FACE.ONE;
				suspects[4].hairType = HAIR.LONG_LONG;
				suspects[4].isMonster = false;
				suspects[4].suspectNumber = 5;
				suspects[4].initBodyParts();
			}
			break;
		}
		
		for(Suspect s : suspects)
			s.initBodyParts();
	}
	
	private void doClock(SpriteBatch batch){
		elapsedTime += Gdx.graphics.getDeltaTime();
		
		if(elapsedTime >= 3.75){
			clockState++;
			elapsedTime = 0;
		}
		
		if(clockState == 8){
			clockState = 0;
			timeRunout = true;
			gameState = GAME_STATE.END_LEVEL;
		}
		
		batch.draw(clock[0][clockState], 292, -53 - 56 + 480);
	}
	
}
