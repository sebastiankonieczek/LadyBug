package states;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import collision.api.CircleCollisionHandler;
import collision.api.CollisionHandler;
import collision.api.RectangleCollisionHandler;
import state.api.State;
import states.LabyrinthState.LabyrinthTileState;

public class LadyBirdMovementState implements State
{
   private static final String BORDER_RIGHT = "RIGHT";
   private static final String BORDER_LEFT = "LEFT";
   private static final String BORDER_TOP = "TOP";
   private static final String BORDER_BOTTOM = "BOTTOM";

   private static final Map< Direction, List< String > > RELEVANT_BORDERS = new HashMap< Direction, List< String > >();
   private static final Map< Direction, String > BLOCKING_WALL = new HashMap< Direction, String >();

   static {
      final ArrayList< String > relevantDownBorders = new ArrayList< String >();
      relevantDownBorders.add( BORDER_TOP );
      relevantDownBorders.add( BORDER_LEFT );
      relevantDownBorders.add( BORDER_RIGHT );
      RELEVANT_BORDERS.put( Direction.DOWN, relevantDownBorders );
      BLOCKING_WALL.put( Direction.DOWN, BORDER_TOP );
      final ArrayList< String > relevantUpBorders = new ArrayList< String >();
      relevantUpBorders.add( BORDER_BOTTOM );
      relevantUpBorders.add( BORDER_LEFT );
      relevantUpBorders.add( BORDER_RIGHT );
      RELEVANT_BORDERS.put( Direction.UP, relevantUpBorders );
      BLOCKING_WALL.put( Direction.UP, BORDER_BOTTOM );
      final ArrayList< String > relevantLeftBorders = new ArrayList< String >();
      relevantLeftBorders.add( BORDER_BOTTOM );
      relevantLeftBorders.add( BORDER_TOP );
      relevantLeftBorders.add( BORDER_RIGHT );
      RELEVANT_BORDERS.put( Direction.LEFT, relevantLeftBorders );
      BLOCKING_WALL.put( Direction.LEFT, BORDER_RIGHT );
      final ArrayList< String > relevantRightBorders = new ArrayList< String >();
      relevantRightBorders.add( BORDER_BOTTOM );
      relevantRightBorders.add( BORDER_LEFT );
      relevantRightBorders.add( BORDER_TOP );
      RELEVANT_BORDERS.put( Direction.RIGHT, relevantRightBorders );
      BLOCKING_WALL.put( Direction.RIGHT, BORDER_LEFT );
   }

   private static final int BORDER_THICKNESS = 2;

   public enum Direction
   {
      UP,
      DOWN,
      LEFT,
      RIGHT
   }

   private static final int MILLIS_PER_PIXEL = 10;

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private Direction currentDirection_;
   private final Point currentLocation_;
   private final Dimension size_;
   private boolean isMoving_ = false;
   private final Rectangle bounds_;

   private final CircleCollisionHandler upAndDownCollisionHandler_;
   private final CircleCollisionHandler leftAndRightCollisionHandler_;
   private Point oldLocation_;

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private LadyBirdMovementState( final Direction initialDirection,
                                  final Point initialLocation,
                                  final Dimension size,
                                  final Rectangle bounds,
                                  final CircleCollisionHandler upAndDownCollisionHandler,
                                  final CircleCollisionHandler leftAndRightCollisionHandler )
   {
      currentDirection_ = initialDirection;
      currentLocation_ = initialLocation;
      bounds_ = bounds;
      upAndDownCollisionHandler_ = upAndDownCollisionHandler;
      leftAndRightCollisionHandler_ = leftAndRightCollisionHandler;
      size_ = size;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public static final LadyBirdMovementState
   create( final Direction initialDirection,
           final Point initialLocation,
           final Dimension size,
           final Rectangle bounds,
           final CircleCollisionHandler upAndDownCollisionHandler,
           final CircleCollisionHandler leftAndRightCollisionHandler )
   {
      return new LadyBirdMovementState( initialDirection,
                                        initialLocation,
                                        size,
                                        bounds,
                                        upAndDownCollisionHandler,
                                        leftAndRightCollisionHandler );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public synchronized ImmutableLadyBirdMovementState currentState()
   {
      try {
         return ImmutableLadyBirdMovementState.create( clone() );
      } catch ( final CloneNotSupportedException e ) {
         throw new RuntimeException( e );
      }
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void updateState( final long duration )
   {
      if ( isMoving_ ) {
         oldLocation_ = new Point( currentLocation_ );
         final int pixelsToMove = (int) ( duration / MILLIS_PER_PIXEL );

         int tmpX = currentLocation_.x;
         int tmpY = currentLocation_.y;

         switch ( currentDirection_ ) {
            case RIGHT:
               if( currentLocation_.x == bounds_.width ) {
                  return;
               }
               tmpX += pixelsToMove;
               break;
            case LEFT:
               if( currentLocation_.x == bounds_.x ) {
                  return;
               }
               tmpX -= pixelsToMove;
               break;
            case UP:
               if( currentLocation_.y == bounds_.y ) {
                  return;
               }
               tmpY -= pixelsToMove;
               break;
            case DOWN:
               if( currentLocation_.y == bounds_.height ) {
                  return;
               }
               tmpY += pixelsToMove;
               break;
            default:
               throw new IllegalStateException( "current direction has invalid state: \"" + currentDirection_
                                                + "\"" );
         }

         // moving out of area, set back so it seems there is a wall
         if ( tmpX > bounds_.width ) {
            tmpX = bounds_.width;
         } else if ( tmpX < bounds_.x ) {
            tmpX = bounds_.x;
         }

         if ( tmpY < bounds_.y ) {
            tmpY = bounds_.y;
         } else if ( tmpY > bounds_.height ) {
            tmpY = bounds_.height;
         }

         currentLocation_.move( tmpX, tmpY );

         upAndDownCollisionHandler_.setLocationOnMap( currentLocation_ );
         leftAndRightCollisionHandler_.setLocationOnMap( currentLocation_ );
      }
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public LadyBirdMovementState clone() throws CloneNotSupportedException
   {
      return new LadyBirdMovementState( currentDirection_,
                                        new Point( currentLocation_ ),
                                        new Dimension( size_ ),
                                        bounds_,
                                        upAndDownCollisionHandler_,
                                        leftAndRightCollisionHandler_ );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public Direction getCurrentDirection()
   {
      return currentDirection_;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public Point getCurrentLocation()
   {
      return currentLocation_;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public void setDirection( final Direction direction )
   {
      currentDirection_ = direction;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public void setIsMoving( final boolean isMoving )
   {
      isMoving_ = isMoving;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public CollisionHandler getCollisionHandler()
   {
      switch( currentDirection_ ) {
         case DOWN:
         case UP:
            return upAndDownCollisionHandler_;
         case LEFT:
         case RIGHT:
            return leftAndRightCollisionHandler_;
         default:
            throw new RuntimeException( "Invalid direction : " + currentDirection_.name() );
      }
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public Dimension getSize()
   {
      return size_;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public void handleBorderCollision( final List< LabyrinthTileState > tileStates )
   {
      if( oldLocation_ == null ) {
         return;
      }
      if( currentLocation_.equals( oldLocation_ ) ) {
         return;
      }
      final CollisionHandler collisionHandler = getCollisionHandler();
      tileStates.stream()
                .flatMap( tile -> tile.getBorders().stream() )
                .forEach( border -> {
                   final RectangleCollisionHandler handler = RectangleCollisionHandler.create( border.getSize() );
                   handler.setLocationOnMap( border.getLocation() );
                   if( collisionHandler.collides( handler ) ) {
                      moveToNewLocation( oldLocation_ );
                   }
                } );
   }

   ///////////////////////////////////////////////////////////////////////////////////////////////////////////

   private void moveToNewLocation( final Point oldLocation )
   {
      currentLocation_.move( oldLocation.x, oldLocation.y );
   }
}
