package main;

import java.awt.Image;
import java.awt.MediaTracker;

import javax.swing.ImageIcon;

public class Utilities
{
   public static Image loadImage( final String imagesLadyBirdRight )
   {
      final ImageIcon imageIcon = new ImageIcon( imagesLadyBirdRight );

      while( imageIcon.getImageLoadStatus() == MediaTracker.LOADING ) {
         // whait for image load
      }

      if( imageIcon.getImageLoadStatus() == MediaTracker.ABORTED
               || imageIcon.getImageLoadStatus() == MediaTracker.ERRORED ) {
         throw new RuntimeException( "unable to load image \"" + imagesLadyBirdRight + "\"" );
      }

      return imageIcon.getImage();
   }
}
