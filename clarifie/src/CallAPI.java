import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import com.clarifai.api.ClarifaiClient;
import com.clarifai.api.ClarifaiRequest;
import com.clarifai.api.FeedbackRequest;
import com.clarifai.api.InfoResult;
import com.clarifai.api.RecognitionRequest;
import com.clarifai.api.RecognitionResult;
import com.clarifai.api.Tag;
import com.mortennobel.imagescaling.AdvancedResizeOp.*;
import com.mortennobel.imagescaling.ResampleOp;
import com.mortennobel.*;

import java.io.*;

import javax.imageio.ImageIO;

public class CallAPI {
	
	public static void main (String args[]) throws IOException
	{
		//System.out.println();
		//ClarifaiClient clarifai = new ClarifaiClient("v6nGGWcaTzQpO0IzdJlkZNS2JM383Y5C5515rTz6", "Dl33nlJeRTKqFkl3duFZ_9xWAyKbFdTX2g1vczMl");
		ClarifaiClient clarifai = new ClarifaiClient("46w8_lvHNrhohlSRDK1kDFc_BhhdZhZsPOV7bQth", "iupKzSfYwGAlXmNO8oITCOw2sY2_vFSJv2SzryCT");
		
		String filepath="./image/Smartplate Test/brussle sprouts.jpg";
		BufferedImage in = ImageIO.read(new File(filepath));
		ResampleOp  resampleOp = new ResampleOp (1024,1024);
		//resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Normal);
		
		BufferedImage rescaledImage = resampleOp.filter(in, null);
		
		new File("./image/test/resize").mkdir();
		
		File outputfile = new File("./image/test/resize/brussle sprouts.jpg");
		ImageIO.write(rescaledImage, "jpg", outputfile);
		
		File inputfile= new File ("./image/Smartplate Test/brussle sprouts.jpg");
		
		//File f = new File("./image/20150329_181618.jpg");
		
		System.out.println(outputfile.getAbsolutePath());
		if(outputfile.exists() && !outputfile.isDirectory())
		{
			List<RecognitionResult> results = clarifai.recognize(new RecognitionRequest(outputfile).setModel("37367_sorta2"));
			//List<RecognitionResult> results = clarifai.recognize(new RecognitionRequest(outputfile));
			
			RecognitionResult recogresult = results.get(0);
			System.out.println("The doc id for the document is :" +recogresult.getDocId());
			
			for (Tag tag : results.get(0).getTags()) {
			
				System.out.println(tag.getName() + ": " + tag.getProbability());
			}
		}
	}


	public static BufferedImage scale(BufferedImage sbi, int imageType, int dWidth, int dHeight, double fWidth, double fHeight) {
	    BufferedImage dbi = null;
	    if(sbi != null) {
	        dbi = new BufferedImage(dWidth, dHeight, imageType);
	        Graphics2D g = dbi.createGraphics();
	        AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
	        g.drawRenderedImage(sbi, at);
	    }
	    return dbi;
	}

}
