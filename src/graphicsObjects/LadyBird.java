package graphicsObjects;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

import graphics.api.GraphicsObject;
import states.ImmutableLadyBirdMovementState;
import states.LadyBirdMovementState;

public class LadyBird extends GraphicsObject
{
   private final Image ladyBirdMoveRight_;
   private final Image ladyBirdMoveLeft_;
   private final Image ladyBirdMoveUp_;
   private final Image ladyBirdMoveDown_;

   private Image imageToDraw_;

   private final LadyBirdMovementState state_;

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private LadyBird( final Dimension size,
                     final Image ladyBirdMoveRight,
                     final Image ladyBirdMoveLeft,
                     final Image ladyBirdMoveUp,
                     final Image ladyBirdMoveDown,
                     final LadyBirdMovementState state)
   {
      super( size );
      ladyBirdMoveDown_ = ladyBirdMoveDown;
      ladyBirdMoveLeft_ = ladyBirdMoveLeft;
      ladyBirdMoveRight_ = ladyBirdMoveRight;
      ladyBirdMoveUp_ = ladyBirdMoveUp;

      state_ = state;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public static final LadyBird create( final Dimension size,
                                        final Image ladyBirdMoveRight,
                                        final Image ladyBirdMoveLeft,
                                        final Image ladyBirdMoveUp,
                                        final Image ladyBirdMoveDown,
                                        final LadyBirdMovementState state )
   {
      return new LadyBird( size, ladyBirdMoveRight, ladyBirdMoveLeft, ladyBirdMoveUp, ladyBirdMoveDown, state );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////


   @Override
   public void drawContent()
   {
      final Graphics2D graphics = getGraphics( true );
      clear( graphics, new Point( 0, 0 ), size() );

      graphics.drawImage( imageToDraw_, 0, 0, null );

      graphics.dispose();
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public synchronized void updateContent()
   {
      final ImmutableLadyBirdMovementState currentState = state_.currentState();
      switch( currentState.getCurrentDirection() ) {
         case DOWN:
            imageToDraw_ = ladyBirdMoveDown_;
            break;
         case UP:
            imageToDraw_ = ladyBirdMoveUp_;
            break;
         case LEFT:
            imageToDraw_ = ladyBirdMoveLeft_;
            break;
         case RIGHT:
            imageToDraw_ = ladyBirdMoveRight_;
            break;
         default:
            throw new IllegalArgumentException( "invalid direction \"" + currentState.getCurrentDirection().name() + "\"" );
      }

      location( currentState.getCurrentLocation() );

      // after update draw content again
      drawContent();
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public boolean isValid()
   {
      return true;
   }
}
