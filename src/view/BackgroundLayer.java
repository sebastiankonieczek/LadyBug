package view;

import graphics.api.GraphicsLayer;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

public class BackgroundLayer extends GraphicsLayer
{
   private static final int LOCATION_Y = 0;
   private static final int LOCATION_X = 0;
   private final Image content_;

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private BackgroundLayer( final Dimension size, final Image content )
   {
      super( size, new Point( 0, 0 ) );

      if( content == null ) {
         throw new IllegalArgumentException();
      }

      content_ = content;

      drawContent();
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public static final BackgroundLayer create( final Dimension size, final Image content )
   {
      return new BackgroundLayer( size, content );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public boolean needRepaint()
   {
      return false;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void updateContent()
   {
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void render( final Graphics2D g )
   {
      g.drawImage( content(), LOCATION_X, LOCATION_Y, size().width, size().height, null );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void drawContent()
   {
      final Graphics2D graphics = getGraphics( false );
      graphics.drawImage( content_, LOCATION_X, LOCATION_Y, null );

      graphics.dispose();
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public boolean isValid()
   {
      return true;
   }
}
