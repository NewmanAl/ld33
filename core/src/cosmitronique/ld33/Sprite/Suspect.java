package cosmitronique.ld33.Sprite;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import cosmitronique.ld33.Font.FontManager;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Suspect {

	public enum BODY_TYPE{
		SMALL,
		MED,
		LARGE
	}
	
	public enum FACE{
		ONE,
		TWO,
		THREE,
		FOUR
	}
	
	public enum HAIR{
		SHORT,
		BALD,
		LONG,
		STYLE,
		LONG_LONG
	}
	
	public enum SHIRT_COLOR{
		PURPLE,
		BROWN,
		BLUE,
		RED,
		GREEN
	}
	
	public enum SHIRT_TYPE{
		ONE,
		TWO,
		THREE,
		FOUR
	}
	
	public enum PANTS{
		ONE,
		TWO,
		THREE
	}
	
	static private Texture bodiesTex;
	static private TextureRegion[][] bodies;
	static private Texture hairsTex;
	static private TextureRegion[][] hairs;
	static private Texture facesTex;
	static private TextureRegion[][] faces;
	static private Texture shirtsTexSmall;
	static private TextureRegion[][] shirtsSmall;
	static private Texture shirtsTexMed;
	static private TextureRegion[][] shirtsMed;
	static private Texture shirtsTexLarge;
	static private TextureRegion[][] shirtsLarge;
	static private Texture pantsTexSmall;
	static private TextureRegion[][] pantsSmall;
	static private Texture pantsTexMed;
	static private TextureRegion[][] pantsMed;
	static private Texture pantsTexLarge;
	static private TextureRegion[][] pantsLarge;
	static private Texture sign;
	static{
		bodiesTex = new Texture(Gdx.files.internal("Sprites/bodyTypes2.png"));
		bodies = TextureRegion.split(bodiesTex, 59, 173);
		
		hairsTex = new Texture(Gdx.files.internal("Sprites/hairTypes.png"));
		hairs = TextureRegion.split(hairsTex, 43, 46);
		
		facesTex = new Texture(Gdx.files.internal("Sprites/faceTypes.png"));
		faces = TextureRegion.split(facesTex, 31, 23);
		
		shirtsTexSmall = new Texture(Gdx.files.internal("Sprites/smallShirts.png"));
		shirtsSmall = TextureRegion.split(shirtsTexSmall, 55, 89);
		
		shirtsTexMed = new Texture(Gdx.files.internal("Sprites/medShirts.png"));
		shirtsMed = TextureRegion.split(shirtsTexMed, 55, 99);
		
		shirtsTexLarge = new Texture(Gdx.files.internal("Sprites/largeShirts.png"));
		shirtsLarge = TextureRegion.split(shirtsTexLarge, 55, 103);
		
		pantsTexSmall = new Texture(Gdx.files.internal("Sprites/smallPants.png"));
		pantsSmall = TextureRegion.split(pantsTexSmall, 37, 48);
		
		pantsTexMed = new Texture(Gdx.files.internal("Sprites/medPants.png"));
		pantsMed = TextureRegion.split(pantsTexMed, 37, 57);
		
		pantsTexLarge = new Texture(Gdx.files.internal("Sprites/largePants.png"));
		pantsLarge = TextureRegion.split(pantsTexLarge, 37, 66);
		
		sign = new Texture(Gdx.files.internal("Sprites/sign.png"));
	}
	public TextureRegion body;
	private TextureRegion face;
	private TextureRegion hair;
	private TextureRegion shirt;
	private TextureRegion pants;
	
	public BODY_TYPE bodyType;
	public FACE faceType;
	private float faceX, faceY;
	public HAIR hairType;
	private float hairX, hairY;
	public SHIRT_COLOR shirtColor;
	public SHIRT_TYPE shirtType;
	private float shirtX, shirtY;
	private float pantsX, pantsY;
	public boolean isMonster;
	private Random r;
	private float signX, signY;
	
	public float xPos, yPos;
	private float scale;
	private boolean isSelected;
	public int suspectNumber;
	FontManager font;
	
	public Suspect(BODY_TYPE body, FACE face, HAIR hair, SHIRT_COLOR shirtC, SHIRT_TYPE shirtT, boolean isMonster, float xPos, float yPos, FontManager f){
		bodyType = body;
		faceType = face;
		hairType = hair;
		shirtColor = shirtC;
		shirtType = shirtT;
		this.isMonster = isMonster;
		this.xPos = xPos;
		this.yPos = yPos;
		
		r = new Random();
		
		font = f;
		
		initBodyParts();
	}
	
	public void initBodyParts(){
		switch(bodyType){
		case SMALL:
			body = bodies[0][0];
			
			signX = 10;
			signY = -70 - 49 + 173;

			//FACE
			switch(faceType){
			case ONE:
				faceX = 12;
				faceY = -47 - 23 + 173;
				face = faces[0][0];
				break;
			case TWO:
				faceX = 12;
				faceY = -49 - 23 + 173;
				face = faces[0][1];
				break;
			case THREE:
				faceX = 12;
				faceY = -48 - 23 + 173;
				face = faces[0][2];
				break;
			case FOUR:
				faceX = 12;
				faceY = -45 - 23 + 173;
				face = faces[0][3];
				break;
			}
			
			//HAIR
			switch(hairType){
			case SHORT:
				hairX = 7;
				hairY = -16 - 46 + 173;
				hair = hairs[0][0 + r.nextInt(3)];
				break;
			case BALD:
				hairX = 7;
				hairY = -10 - 46 + 173;
				hair = hairs[0][3 + r.nextInt(3)];
				break;
			case LONG:
				hairX = 6;
				hairY = -26 - 46 + 173;
				hair = hairs[0][6 + r.nextInt(3)];
				break;
			case STYLE:
				hairX = 6;
				hairY = -19 - 46 + 173;
				hair = hairs[0][9 + r.nextInt(3)];
				break;
			case LONG_LONG:
				hairX = 6;
				hairY = -24 - 46 + 173;
				hair = hairs[0][12 + r.nextInt(3)];
				break;
			}
			
			//shirt
			switch(shirtType){
			case ONE:
			case TWO:
			case THREE:
			case FOUR:
				shirtX = 0;
				shirtY = -69 - 89 + 173;
				break;
			}
			
			//shirt color
			switch(shirtType){
			case ONE:	
				switch(shirtColor){
				case PURPLE:
					shirt = shirtsSmall[0][0];
					break;
				case RED:
					shirt = shirtsSmall[0][8];
					break;
				case GREEN:
					shirt = shirtsSmall[0][4];
					break;
				}
				break;
			case TWO:
				switch(shirtColor){
				case PURPLE:
					shirt = shirtsSmall[0][9];
					break;
				case BROWN:
					shirt = shirtsSmall[0][1];
					break;
				case BLUE:
					shirt = shirtsSmall[0][5];
					break;
				}
				break;
			case THREE:
				switch(shirtColor){
				case BROWN:
					shirt = shirtsSmall[0][6];
					break;
				case BLUE:
					shirt = shirtsSmall[0][2];
					break;
				case GREEN:
					shirt = shirtsSmall[0][10];
					break;
				}
				break;
			case FOUR:
				switch(shirtColor){
				case PURPLE:
					shirt = shirtsSmall[0][11];
					break;
				case BLUE:
					shirt = shirtsSmall[0][7];
					break;
				case RED:
					shirt = shirtsSmall[0][3];
					break;
				}
			}
			
			//pants
			pantsX = 9;
			pantsY = -125 - 48 + 173;
			pants = pantsSmall[0][1];
			
			break;
		case MED:
			body = bodies[0][1];
			
			signX = 10;
			signY = -53 - 49 + 173;
			
			//FACE
			switch(faceType){
			case ONE:
				faceX = 12;
				faceY = -30 - 23 + 173;
				face = faces[0][0];
				break;
			case TWO:
				faceX = 12;
				faceY = -32 - 23 + 173;
				face = faces[0][1];
				break;
			case THREE:
				faceX = 12;
				faceY = -31 - 23 + 173;
				face = faces[0][2];
				break;
			case FOUR:
				faceX = 12;
				faceY = -28 - 23 + 173;
				face = faces[0][3];
				break;
			}
			
			//hair
			switch(hairType){
			case SHORT:
				hairX = 7;
				hairY = 0 - 46 + 173;
				hair = hairs[0][0 + r.nextInt(3)];
				break;
			case BALD:
				hairX = 7;
				hairY = 6 - 46 + 173;
				hair = hairs[0][3 + r.nextInt(3)];
				break;
			case LONG:
				hairX = 6;
				hairY = -9 - 46 + 173;
				hair = hairs[0][6 + r.nextInt(3)];
				break;
			case STYLE:
				hairX = 6;
				hairY = -2 - 46 + 173;
				hair = hairs[0][9 + r.nextInt(3)];
				break;
			case LONG_LONG:
				hairX = 6;
				hairY = - 7 - 46 + 173;
				hair = hairs[0][12 + r.nextInt(3)];
				break;
			}
			
			//shirt
			switch(shirtType){
			case ONE:
			case TWO:
			case THREE:
				shirtX = 0;
				shirtY = -52 - 99 + 173;
				break;
			case FOUR:
				shirtX = -1;
				shirtY = -52 - 99 + 173;
			}
			
			//shirt color
			switch(shirtType){
			case ONE:	
				switch(shirtColor){
				case PURPLE:
					shirt = shirtsMed[0][0];
					break;
				case RED:
					shirt = shirtsMed[0][8];
					break;
				case GREEN:
					shirt = shirtsMed[0][4];
					break;
				}
				break;
			case TWO:
				switch(shirtColor){
				case PURPLE:
					shirt = shirtsMed[0][9];
					break;
				case BROWN:
					shirt = shirtsMed[0][1];
					break;
				case BLUE:
					shirt = shirtsMed[0][5];
					break;
				}
				break;
			case THREE:
				switch(shirtColor){
				case BROWN:
					shirt = shirtsMed[0][6];
					break;
				case BLUE:
					shirt = shirtsMed[0][2];
					break;
				case GREEN:
					shirt = shirtsMed[0][10];
					break;
				}
				break;
			case FOUR:
				switch(shirtColor){
				case PURPLE:
					shirt = shirtsMed[0][11];
					break;
				case BLUE:
					shirt = shirtsMed[0][7];
					break;
				case RED:
					shirt = shirtsMed[0][3];
					break;
				}
			}
			
			//pants
			pantsX = 9;
			pantsY = -116 - 57 + 173;
			pants = pantsMed[0][1];
				
			break;
			
		case LARGE:
			body = bodies[0][2];
			
			signX = 10;
			signY = - 40 - 49 + 173;
			
			//face
			switch(faceType){
			case ONE:
				faceX = 12;
				faceY = -7 - 23 + 173;
				face = faces[0][0];
				break;
			case TWO:
				faceX = 12;
				faceY = -20 - 23 + 173;
				face = faces[0][1];
				break;
			case THREE:
				faceX = 12;
				faceY = -18 - 23 + 173;
				face = faces[0][2];
				break;
			case FOUR:
				faceX = 12;
				faceY = -15 - 23 + 173;
				face = faces[0][3];
				break;
			}
			
			//Hair
			switch(hairType){
			case SHORT:
				hairX = 7;
				hairY = 12 - 46 + 173;
				hair = hairs[0][0 + r.nextInt(3)];
				break;
			case BALD:
				hairX = 7;
				hairY = 18 - 46 + 173;
				hair = hairs[0][3 + r.nextInt(3)];
				break;
			case LONG:
				hairX = 6;
				hairY = 4 - 46 + 173;
				hair = hairs[0][6 + r.nextInt(3)];
				break;
			case STYLE:
				hairX = 6;
				hairY = 11 - 46 + 173;
				hair = hairs[0][9 + r.nextInt(3)];
				break;
			case LONG_LONG:
				hairX = 6;
				hairY = 6 - 46 + 173;
				hair = hairs[0][12 + r.nextInt(3)];
				break;
			}
			
			//shirt
			switch(shirtType){
			case ONE:
				shirtX = 0;
				shirtY = -39 - 103 + 173;
				break;
			case TWO:
			case THREE:
				shirtX = 0;
				shirtY = -37 - 103 + 173;
				break;
			case FOUR:
				shirtX = -1;
				shirtY = -36.5f - 103 + 173;
				break;				
			}
			
			//shirt color
			switch(shirtType){
			case ONE:	
				switch(shirtColor){
				case PURPLE:
					shirt = shirtsLarge[0][0];
					break;
				case RED:
					shirt = shirtsLarge[0][8];
					break;
				case GREEN:
					shirt = shirtsLarge[0][4];
					break;
				}
				break;
			case TWO:
				switch(shirtColor){
				case PURPLE:
					shirt = shirtsLarge[0][9];
					break;
				case BROWN:
					shirt = shirtsLarge[0][1];
					break;
				case BLUE:
					shirt = shirtsLarge[0][5];
					break;
				}
				break;
			case THREE:
				switch(shirtColor){
				case BROWN:
					shirt = shirtsLarge[0][6];
					break;
				case BLUE:
					shirt = shirtsLarge[0][2];
					break;
				case GREEN:
					shirt = shirtsLarge[0][10];
					break;
				}
				break;
			case FOUR:
				switch(shirtColor){
				case PURPLE:
					shirt = shirtsLarge[0][11];
					break;
				case BLUE:
					shirt = shirtsLarge[0][7];
					break;
				case RED:
					shirt = shirtsLarge[0][3];
					break;
				}
			}
			
			//pants
			pantsX = 9;
			pantsY = -107 - 66 + 173;
			pants = pantsLarge[0][0];
			
			break;
		}
	}
	
	public void Draw(SpriteBatch batch){
		batch.draw(body, xPos, yPos);
		batch.draw(hair, xPos + hairX, yPos + hairY);
		batch.draw(face, xPos + faceX, yPos + faceY);
		batch.draw(shirt, xPos + shirtX, yPos + shirtY);
		batch.draw(pants, xPos + pantsX, yPos + pantsY);
		batch.draw(sign, xPos + signX, yPos + signY);
		font.drawMessage("0" + suspectNumber, batch, xPos + signX + 6, yPos + signY + 5, 1.5f, Color.BLACK);
	}
	
	public void dispose(){
		body = null;
		face = null;
		hair = null;
		shirt = null;
		pants = null;
		font = null;
	}
	
	public static void disposeAllTextures(){
		bodiesTex.dispose();
		for(int i = 0; i< bodies[0].length; i ++)
			bodies[0][i].getTexture().dispose();
		
		hairsTex.dispose();
		for(int i = 0; i< hairs[0].length; i++)
			hairs[0][i].getTexture().dispose();
		
		facesTex.dispose();
		for(int i = 0; i< faces[0].length; i++)
			faces[0][i].getTexture().dispose();
		
		shirtsTexSmall.dispose();
		for( int i = 0; i < shirtsSmall[0].length; i ++)
			shirtsSmall[0][i].getTexture().dispose();
		
		shirtsTexMed.dispose();
		for(int i = 0; i< shirtsMed[0].length; i++)
			shirtsMed[0][i].getTexture().dispose();
		
		shirtsTexLarge.dispose();
		for(int i = 0; i< shirtsLarge[0].length; i++)
			shirtsLarge[0][i].getTexture().dispose();
		
		pantsTexSmall.dispose();
		for(int i = 0; i< pantsSmall[0].length; i++)
			pantsSmall[0][i].getTexture().dispose();
		
		pantsTexMed.dispose();
		for(int i = 0; i< pantsMed[0].length; i++)
			pantsMed[0][i].getTexture().dispose();
		
		pantsTexLarge.dispose();
		for(int i = 0; i<pantsLarge[0].length; i++)
			pantsLarge[0][i].getTexture().dispose();
		
		sign.dispose();
	}
	
	
}
