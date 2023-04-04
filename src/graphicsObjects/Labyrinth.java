package graphicsObjects;

import graphics.api.GraphicsObject;

import java.awt.Dimension;
import java.awt.Graphics2D;

import states.LabyrinthState;
import states.LabyrinthState.LabyrinthTileState;

public class Labyrinth extends GraphicsObject
{
   private final LabyrinthState labyrinthState_;
   private final Dimension tileSize_;

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private Labyrinth( final Dimension size, final Dimension tileSize, final LabyrinthState labyrinthState )
   {
      super( size );
      labyrinthState_ = labyrinthState;
      tileSize_ = tileSize;
      drawContent();
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public static final Labyrinth create( final Dimension size,
                                         final Dimension tileSize,
                                         final LabyrinthState labyrinthState )
   {
      return new Labyrinth( size, tileSize, labyrinthState );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void drawContent()
   {
      final Graphics2D graphics = getGraphics( true );
      for ( int i = 0; i < labyrinthState_.getWidth(); ++i ) {
         for ( int y = 0; y < labyrinthState_.getHeight(); ++y ) {
            final LabyrinthTileState tileState = labyrinthState_.getLabyrinth()[ i ][ y ];

            final int yStart = y * tileSize_.height;
            final int xStart = i * tileSize_.width;
            if ( tileState.hasWallTop() ) {
               graphics.drawLine( xStart, yStart, xStart + tileSize_.width, yStart );
            }
            if ( tileState.hasWallBottom() ) {
               graphics.drawLine( xStart,
                                  yStart + tileSize_.height,
                                  xStart + tileSize_.width,
                                  yStart + tileSize_.height );
            }
            if ( tileState.hasWallLeft() ) {
               graphics.drawLine( xStart, yStart, xStart, yStart + tileSize_.height );
            }
            if ( tileState.hasWallRight() ) {
               graphics.drawLine( xStart + tileSize_.width,
                                  yStart,
                                  xStart + tileSize_.width,
                                  yStart + tileSize_.height );
            }
         }
      }
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void updateContent()
   {
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public boolean isValid()
   {
      // always valid
      return true;
   }

}
