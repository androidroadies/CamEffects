package com.multidots.cameffects.effects;

import android.graphics.Bitmap;
import android.graphics.Point;

/**
 * æ»¤é•œæ•ˆæžœçš„ç±»ï¼Œå®šä¹‰äº†åŸºæœ¬çš„æ¸²æŸ“æ–¹æ³•
 * @author ragnarok
 *
 */
public class BitmapFilter {
	/**
	 * æ‰€æœ‰æ»¤é•œæ•ˆæžœçš„id
	 */
	public static final int GRAY_STYLE = 1; // é»‘ç™½æ•ˆæžœ
	public static final int RELIEF_STYLE = 2; // æµ®é›•æ•ˆæžœ
	public static final int VAGUE_STYLE = 3; // æ¨¡ç³Šæ•ˆæžœ
	public static final int OIL_STYLE = 4; // æ²¹ç”»æ•ˆæžœ
	public static final int NEON_STYLE = 5; // éœ“è™¹ç�¯æ•ˆæžœ
	public static final int PIXELATE_STYLE = 6; // åƒ�ç´ åŒ–æ•ˆæžœ
	public static final int TV_STYLE = 7; // TVæ•ˆæžœ
	public static final int INVERT_STYLE = 8; // å��è‰²æ•ˆæžœ
	public static final int BLOCK_STYLE = 9; // ç‰ˆç”»
	public static final int OLD_STYLE = 10; // æ€€æ—§æ•ˆæžœ
	public static final int SHARPEN_STYLE = 11; // é”�åŒ–æ•ˆæžœ
	public static final int LIGHT_STYLE = 12; // å…‰ç…§æ•ˆæžœ
	public static final int LOMO_STYLE = 13;
	
	public static final int HIGHLIGHT = 14;
	
	public static final int FLY = 15;
	public static final int PHOTOGRAPHY = 16;
	public static final int CONTRAST = 17;
	public static final int DECRISING = 18;
	public static final int ROTATE = 19;
	public static final int BRIGHT = 20;
	public static final int MATRIX = 21;
	private static final Matrix Matrix = null;
	public static final int BLUR = 22;
	public static final int BOOST = 24;
	public static final int ROUND = 23;
	public static final int WATER = 25;
	private static Matrix matrix;
	
	
	
	
	
	
	
	
	/**
	 * è®¾ç½®æ»¤é•œæ•ˆæžœï¼Œ
	 * @param bitmap
	 * @param styeNo, æ•ˆæžœid
	 */
	@SuppressWarnings({ "unused", "static-access" })
	public static Bitmap changeStyle(Bitmap bitmap, int styleNo) {
		if (styleNo == GRAY_STYLE) {
			//return changeToGray(bitmap);
			return GrayFilter.changeToGray(bitmap);
		}
		else if (styleNo == RELIEF_STYLE) {
			return ReliefFilter.changeToRelief(bitmap);
		}
		else if (styleNo == VAGUE_STYLE) {
			return VagueFilter.changeToVague(bitmap);
		}
		else if (styleNo == OIL_STYLE) {
			return OilFilter.changeToOil(bitmap);
		}
		else if (styleNo == NEON_STYLE) {
			return NeonFilter.changeToNeon(bitmap);
		}
		else if (styleNo == PIXELATE_STYLE) {
			return PixelateFilter.changeToPixelate(bitmap);
		}
			
		else if (styleNo == TV_STYLE) {
			return TvFilter.changeToTV(bitmap);
		}
		else if (styleNo == INVERT_STYLE) {
			return InvertFilter.chageToInvert(bitmap);
		}
		else if (styleNo == BLOCK_STYLE) {
			return BlockFilter.changeToBrick(bitmap);
		}
		else if (styleNo == OLD_STYLE) {
			return OldFilter.changeToOld(bitmap);
		}
		else if (styleNo == SHARPEN_STYLE) {
			return SharpenFilter.changeToSharpen(bitmap);
		}
		else if (styleNo == LIGHT_STYLE) {
			return LightFilter.changeToLight(bitmap);
		}
		else if (styleNo == LOMO_STYLE) {
			return LomoFilter.changeToLomo(bitmap);
		}
		
		else if (styleNo == HIGHLIGHT) {
			double blue = 10;
			double red = 10;
			double green = 10;
			return HighLight.doColorFilter(bitmap, red, green, blue);
		}
		
		else if (styleNo == FLY) {
			double blue = 10;
			double red = 10;
			double green = 10;
			return Fly.doGamma(bitmap, red, green, blue);
		}
		else if (styleNo == CONTRAST) {
			double blue = 10;
			double red = 10;
			double green = 10;
			return Contrast.createContrast(bitmap, green);
		}
		
		else if (styleNo == PHOTOGRAPHY) {
			double blue = 10;
			double red = 10;
			double green = 10;
			int depth = 0;
			return PhotoGraphy.createSepiaToningEffect(bitmap, depth, red, green, blue);
		}
		
		else if (styleNo == DECRISING) {
			double blue = 10;
			double red = 10;
			double green = 10;
			int depth = 0;
			int bitOffset = 128;
			return Decrising.decreaseColorDepth(bitmap, bitOffset);
		}
		
		else if (styleNo == ROTATE) {
			double blue = 10;
			double red = 10;
			double green = 10;
			int depth = 0;
			int bitOffset = 128;
			float degree = 270;
			return Rotate.rotate(bitmap, degree);
		}
		
		else if (styleNo == BRIGHT) {
			return Brightness.doBrightness(bitmap, 20);
		}
		
		else if (styleNo == MATRIX) {
			return Matrix.computeConvolution3x3(bitmap, matrix);
		}
		
		/*else if (styleNo == BLUR) {
			return Blur.applyGaussianBlur(bitmap);
		}
		
		else if (styleNo == SHARP) {
			return Sharp.sharpen(bitmap, 6);
		}*/
		
		else if (styleNo == BOOST) {
			float percent = 67;
			int type = 0;
			return Boost.boost(bitmap, type, percent);
		}
		
		else if (styleNo == ROUND) {
			return RoundCorner.roundCorner(bitmap, 45);
		}
		
		else if (styleNo == WATER) {
			int color = 0;
			int size = 0 ;
			Point location = null;
			int alpha = 0;
			return WaterMaking.mark(bitmap, null, null, alpha, color, size, false);
					}
		
			return bitmap;
	}
	
	
	
}

















