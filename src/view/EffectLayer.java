package view;

import graphics.api.GraphicsLayer;
import graphics.api.GraphicsObject;
import graphics.api.GraphicsObjectRegistry;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

import main.Constants;

public class EffectLayer extends GraphicsLayer
{
   private EffectLayer( final Dimension size )
   {
      super( size, new Point( 0, 0 ) );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public static final EffectLayer create( final Dimension size )
   {
      return new EffectLayer( size );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public boolean needRepaint()
   {
      return true;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void render( final Graphics2D graphics )
   {
      graphics.drawImage( content(),
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
      final GraphicsObjectRegistry graphicsObjectRegistry = GraphicsObjectRegistry.getInstance();
      final List< GraphicsObject > graphicsObjects = graphicsObjectRegistry.getValuesForGroup( Constants.GRAPHICS_OBJECT_LIBRARY_ID_EFFECT_LAYER );

      Graphics2D graphics = getGraphics( true );
      // probably clear only part to redraw;
      clear( graphics );
      graphics.dispose();

      graphics = getGraphics( true );
      for( final GraphicsObject graphicsObject : graphicsObjects ) {
         if( !graphicsObject.isValid() ) {
            continue;
         }

         graphics.drawImage( graphicsObject.content(),
                             graphicsObject.location().x,
                             graphicsObject.location().y,
                             null );
      }
      graphics.dispose();
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void updateContent()
   {
      final GraphicsObjectRegistry graphicsObjectRegistry = GraphicsObjectRegistry.getInstance();
      final List< GraphicsObject > graphicsObjects = graphicsObjectRegistry.getValuesForGroup( Constants.GRAPHICS_OBJECT_LIBRARY_ID_EFFECT_LAYER );

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
   public boolean isValid()
   {
      return true;
   }
}
