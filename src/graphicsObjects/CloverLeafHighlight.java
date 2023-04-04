package graphicsObjects;

import graphics.api.GraphicsObject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.util.HashMap;

import states.CloverLeafState;

public class CloverLeafHighlight extends GraphicsObject
{
   enum Direction
   {
      FADE_IN,
      FADE_OUT
   }

   public static final Dimension DIMENSION = new Dimension( 70, 70 );

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   static final HashMap< Integer, Integer > MILLIS_PER_PIXEL_MAP = new HashMap< Integer, Integer >();

   static {
      MILLIS_PER_PIXEL_MAP.put( 25, 80 );
      MILLIS_PER_PIXEL_MAP.put( 26, 80 );
      MILLIS_PER_PIXEL_MAP.put( 27, 80 );
      MILLIS_PER_PIXEL_MAP.put( 28, 80 );
      MILLIS_PER_PIXEL_MAP.put( 29, 40 );
      MILLIS_PER_PIXEL_MAP.put( 30, 40 );
      MILLIS_PER_PIXEL_MAP.put( 31, 40 );
      MILLIS_PER_PIXEL_MAP.put( 32, 80 );
      MILLIS_PER_PIXEL_MAP.put( 33, 80 );
      MILLIS_PER_PIXEL_MAP.put( 34, 80 );
      MILLIS_PER_PIXEL_MAP.put( 35, 80 );
   }

   private static final int MAXIMUM_BUBBLE_RADIUS = 35;
   private static final int MINIMUM_BUBBLE_RADIUS = 25;


   private Direction bubbleDirection_ = Direction.FADE_IN;
   private int bubbleRadius_ = MAXIMUM_BUBBLE_RADIUS;
   private long startTime_;
   private int millisPerPixel_ = MILLIS_PER_PIXEL_MAP.get( MAXIMUM_BUBBLE_RADIUS );

   private final CloverLeafState state_;

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private CloverLeafHighlight( final Point location, final CloverLeafState state )
   {
      super( DIMENSION , location );
      drawContent();
      startTime_ = System.currentTimeMillis();
      state_ = state;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public static final CloverLeafHighlight create( final Point location, final CloverLeafState state )
   {
      return new CloverLeafHighlight( location, state );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void drawContent()
   {
      final Graphics2D graphics = getGraphics( true );
      clear( graphics, new Point( 0, 0 ), size() );

      graphics.setBackground( new Color( .0f, .0f, .0f, .0f ) );

      graphics.setPaint( new RadialGradientPaint( new Point( DIMENSION.width / 2, DIMENSION.height / 2 ),
                                                  bubbleRadius_,
                                                  new float[]{.4f, .9f, 1.f},
                                                  new Color[]{ new Color( 0.f, 0.f, 0.f, 0f ),
                                                               new Color( 1.f, 1.f, 1.f, .5f ),
                                                               new Color( 0.f, 0.f, 0.f, 0f ) } ) );
      graphics.fillRect( 0, 0, DIMENSION.width, DIMENSION.height );

      graphics.dispose();
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void updateContent()
   {
      final long timePassed = ( System.currentTimeMillis() - startTime_ );
      final long pixelsToChange = timePassed / millisPerPixel_;
      if( pixelsToChange == 0 ) {
         return;
      }

      switch ( bubbleDirection_ ) {
         case FADE_IN:
         {
            bubbleRadius_ -= pixelsToChange;
            if( bubbleRadius_ < MINIMUM_BUBBLE_RADIUS ) {
               bubbleRadius_ = MINIMUM_BUBBLE_RADIUS/* + ( MINIMUM_BUBBLE_RADIUS - bubbleRadius_ )*/;
               bubbleDirection_ = Direction.FADE_OUT;
            }
            break;
         }
         case FADE_OUT:
         {
            bubbleRadius_ += pixelsToChange;
            if( bubbleRadius_ > MAXIMUM_BUBBLE_RADIUS ) {
               bubbleRadius_ = MAXIMUM_BUBBLE_RADIUS/* - ( bubbleRadius_ - MAXIMUM_BUBBLE_RADIUS )*/;
               bubbleDirection_ = Direction.FADE_IN;
               startTime_ = System.currentTimeMillis();
            }
            break;
         }
         default:
            new RuntimeException( "invalid direction" + bubbleDirection_.name() );
      }
      startTime_ = System.currentTimeMillis();
      millisPerPixel_ = MILLIS_PER_PIXEL_MAP.get( bubbleRadius_ );
      drawContent();
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public boolean isValid()
   {
      return !state_.currentState().isEaten();
   }

}
