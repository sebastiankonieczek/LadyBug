package graphicsObjects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

import graphics.api.GraphicsObject;
import states.CloverLeafState;

public class CloverLeaf extends GraphicsObject
{
   public static final Dimension DIMENSION = new Dimension( 50, 50 );

   private final CloverLeafState state_;

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private final Image cloverLeaf_;

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private CloverLeaf( final Point location, final Image cloverLeaf, final CloverLeafState state )
   {
      super( DIMENSION , location );
      cloverLeaf_ = cloverLeaf;
      state_ = state;
      drawContent();
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public static final CloverLeaf create( final Point location, final Image cloverLeaf, final CloverLeafState state )
   {
      return new CloverLeaf( location, cloverLeaf, state );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void drawContent()
   {
      final Graphics2D graphics = getGraphics( true );

      clear( graphics, new Point( 0, 0 ), size() );

      graphics.setBackground( new Color( .0f, .0f, .0f, .0f ) );
      graphics.drawImage( cloverLeaf_, 0, 0, null );

      graphics.dispose();
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
      return !state_.currentState().isEaten();
   }
}
