package cosmitronique.ld33.Font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FontManager {
	
	private final String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private final String lowerCase = "abcdefghijklmnopqrstuvwxyz";
	private final String symbols = "!@#$%^&*()[]{}+-=:;?/\\_.,\'\"";
	private final String numbers = "0123456789><~` "; // > and < look like arrows, ~ is a blank box, ` is a checked box
	private TextureRegion[][] fontText;
	private Texture fontTex;
	private float xOffset, yOffset;
	private int texX, texY;
	
	public FontManager(){
		fontTex = new Texture(Gdx.files.internal("Font/thickFont.png"));
		fontText = TextureRegion.split(fontTex, 8, 8);
	}
	
	public void drawMultilinedMessage(String msg, SpriteBatch batch, float x, float y, float scale, Color color){
		String[] lines = msg.split("\n");
		for(String l : lines)
		{
			drawMessage(l,batch,x,y,scale,color);
			y -= 8 * scale;
		}
	}
	
	public void drawMessage(String msg, SpriteBatch batch, float x, float y, float scale, Color color){
		xOffset = yOffset = 0;
		texX = texY = 0;
		batch.setColor(color);
		
		for(int i = 0; i<msg.length(); i++){
			//find position in letters
			if(upperCase.indexOf(msg.charAt(i)) > -1){
				texX = upperCase.indexOf(msg.charAt(i));
				xOffset = i * 8 * scale;
				texY = 0;
			}
			else if(lowerCase.indexOf(msg.charAt(i)) > -1){
				texX = lowerCase.indexOf(msg.charAt(i));
				xOffset = i * 8 * scale;
				texY = 1;
			}
			else if(symbols.indexOf(msg.charAt(i)) > -1){
				texX =  symbols.indexOf(msg.charAt(i));
				xOffset = i * 8 * scale;
				texY = 2;
			}
			else if(numbers.indexOf(msg.charAt(i)) > -1){
				texX = numbers.indexOf(msg.charAt(i));
				xOffset = i * 8 * scale;
				texY = 3;
			}else{
				//assume it's a space
				continue;
			}
			
			//draw letter
			batch.draw(fontText[texY][texX], x + xOffset, y + yOffset, 8 * scale, 8 * scale);
		}
		
		batch.setColor(Color.WHITE);
	}
	
	public void dispose(){
		fontTex.dispose();
		for(int i = 0; i < fontText.length; i++)
			for(int j = 0; j < fontText[i].length; j++)
				fontText[i][j].getTexture().dispose();
	}

}
