package view;

import graphics.api.GraphicsLayer;
import graphics.api.GraphicsObject;
import graphics.api.GraphicsObjectRegistry;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import main.Constants;

public class ActionLayer extends GraphicsLayer
{
   private ActionLayer( final Dimension size )
   {
      super( size, new Point( 0, 0 ) );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public static final ActionLayer create( final Dimension size )
   {
      return new ActionLayer( size );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public boolean needRepaint()
   {
      return true;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void updateContent()
   {
      final List< GraphicsObject > graphicsObjects = GraphicsObjectRegistry.getInstance().getValuesForGroup( Constants.GRAPHICS_OBJECT_LIBRARY_ID_ACTION_LAYER );

      for( final GraphicsObject graphicsObject : graphicsObjects ) {
         if( !graphicsObject.isValid() ) {
            continue;
         }

         graphicsObject.updateContent();
      }

      drawContent();
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void render( final Graphics2D g )
   {
      g.drawImage( content(),
                   location().x,
                   location().y,
                   size().width,
                   size().height,
                   null );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void drawContent()
   {
      final List< GraphicsObject > graphicsObjects = GraphicsObjectRegistry.getInstance().getValuesForGroup( Constants.GRAPHICS_OBJECT_LIBRARY_ID_ACTION_LAYER );

      Graphics2D graphics = getGraphics( true );
      clear( graphics );
      graphics.dispose();

      graphics = getGraphics( true );
      for( final GraphicsObject graphicsObject : graphicsObjects ) {
         if( !graphicsObject.isValid() ) {
            continue;
         }

         // ensures correct transparency
         graphics.drawImage( graphicsObject.content(),
                             graphicsObject.location().x,
                             graphicsObject.location().y,
                             null );
      }
      graphics.dispose();

   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public boolean isValid()
   {
      return true;
   }
}
