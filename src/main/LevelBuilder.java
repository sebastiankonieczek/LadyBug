package main;

import graphics.api.GraphicsObjectRegistry;
import graphicsObjects.CloverLeaf;
import graphicsObjects.CloverLeafHighlight;
import graphicsObjects.Labyrinth;
import graphicsObjects.LadyBird;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import state.api.StateRegistry;
import states.CloverLeafState;
import states.LabyrinthState;
import states.LabyrinthState.LabyrinthTileState;
import states.LadyBirdMovementState;
import states.LadyBirdMovementState.Direction;
import collision.api.CircleCollisionHandler;
import collision.api.CollisionHandlerFactory;

public class LevelBuilder
{
   private static final int LABYRINTH_WIDTH = 550;
   private static final int LABYRINTH_HEIGHT = 550;

   private enum PathDirection
   {
      GO_LEFT,
      GO_RIGHT,
      GO_UP,
      GO_DOWN,
      STAY
   }

   private static final int LABYRINTH_MATRIX_HEIGHT = 10;
   private static final int LABYRINTH_MATRIX_WIDTH = 10;

   private static final String IMAGES_CLOVER_LEAF_THREE_LEAFES_1_PNG = "images/cloverLeaf/drei_bleatter_1.png";
   private static final String IMAGES_CLOVER_LEAF_THREE_LEAFES_2_PNG = "images/cloverLeaf/drei_bleatter_2.png";
   private static final String IMAGES_CLOVER_LEAF_THREE_LEAFES_3_PNG = "images/cloverLeaf/drei_bleatter_3.png";
   private static final String IMAGES_CLOVER_LEAF_THREE_LEAFES_4_PNG = "images/cloverLeaf/drei_bleatter_4.png";

   private static final int LADY_BIRD_MOVEMENT_BOUNDS_HEIGHT = 500;
   private static final int LADY_BIRD_MOVEMENT_BOUNDS_WIDTH = 500;

   private static final String IMAGES_LADY_BIRD_RIGHT = "images/ladyBird/marienkäfer_rechts.png";
   private static final String IMAGES_LADY_BIRD_LEFT = "images/ladyBird/marienkäfer_links.png";
   private static final String IMAGES_LADY_BIRD_UP = "images/ladyBird/marienkäfer_oben.png";
   private static final String IMAGES_LADY_BIRD_DOWN = "images/ladyBird/marienkäfer_unten.png";

   private static final String LADY_BIRD_COLLISION_LEFT_RIGHT_XML = "collisionFiles/ladyBird/lady_bird_collision_left_right.xml";
   private static final String LADY_BIRD_COLLISION_UP_DOWN_XML = "collisionFiles/ladyBird/lady_bird_collision_up_down.xml";

   private static final String CLOVER_LEAF_DREI_BLAETTER_1_COLLISION_XML = "collisionFiles/cloverLeaf/drei_blaetter_1_collision.xml";
   private static final String CLOVER_LEAF_DREI_BLAETTER_2_COLLISION_XML = "collisionFiles/cloverLeaf/drei_blaetter_2_collision.xml";
   private static final String CLOVER_LEAF_DREI_BLAETTER_3_COLLISION_XML = "collisionFiles/cloverLeaf/drei_blaetter_3_collision.xml";
   private static final String CLOVER_LEAF_DREI_BLAETTER_4_COLLISION_XML = "collisionFiles/cloverLeaf/drei_blaetter_4_collision.xml";

   private static final int TILE_HEIGHT = 55;
   private static final int TILE_WIGHT = 55;

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public static void buildRandomLevel()
   {
      final ArrayList< Image > cloverLeafes = new ArrayList< Image >();
      cloverLeafes.add( Utilities.loadImage( IMAGES_CLOVER_LEAF_THREE_LEAFES_1_PNG ) );
      cloverLeafes.add( Utilities.loadImage( IMAGES_CLOVER_LEAF_THREE_LEAFES_2_PNG ) );
      cloverLeafes.add( Utilities.loadImage( IMAGES_CLOVER_LEAF_THREE_LEAFES_3_PNG ) );
      cloverLeafes.add( Utilities.loadImage( IMAGES_CLOVER_LEAF_THREE_LEAFES_4_PNG ) );

      final ArrayList< String > cloverLeafeCollisionHandlers = new ArrayList< String >();
      cloverLeafeCollisionHandlers.add( CLOVER_LEAF_DREI_BLAETTER_1_COLLISION_XML );
      cloverLeafeCollisionHandlers.add( CLOVER_LEAF_DREI_BLAETTER_2_COLLISION_XML );
      cloverLeafeCollisionHandlers.add( CLOVER_LEAF_DREI_BLAETTER_3_COLLISION_XML );
      cloverLeafeCollisionHandlers.add( CLOVER_LEAF_DREI_BLAETTER_4_COLLISION_XML );

      final RandomInt locationX = RandomInt.create( 10 );
      final RandomInt locationY = RandomInt.create( 10 );
      final Random leafNumber = new Random();

      final boolean[][] matrix = new boolean[ LABYRINTH_MATRIX_WIDTH ][ LABYRINTH_MATRIX_HEIGHT ];
      final List< Point > cloverLeafLocations = new ArrayList< Point >();
      final LabyrinthState labyrinthState = LabyrinthState.create( LABYRINTH_MATRIX_WIDTH,
                                                                   LABYRINTH_MATRIX_HEIGHT,
                                                                   new Dimension( TILE_WIGHT, TILE_HEIGHT ) );

      final GraphicsObjectRegistry graphicsObjectRegistry = GraphicsObjectRegistry.getInstance();
      int i = 0;
      final int minimumDistance = 2;
      int missesInARow = 0;
      final Point startLocation = new Point( LABYRINTH_MATRIX_WIDTH / 2, LABYRINTH_MATRIX_HEIGHT / 2 );

      final StateRegistry stateRegistry = StateRegistry.getInstance();
      while ( i < 15 ) {
         int posX = locationX.getNext();
         int posY = locationY.getNext();
         final int leafNr = leafNumber.nextInt( 4 );

         final Image leaf = cloverLeafes.get( leafNr );

         if ( missesInARow > 1000 ) {
            int tmpDistance = minimumDistance;
            Point nextMatching = null;
            while ( nextMatching == null && tmpDistance >= 0 ) {
               nextMatching = findNextMatching( matrix, tmpDistance, startLocation );
               --tmpDistance;
            }
            posX = nextMatching.x;
            posY = nextMatching.y;
         } else {

            if ( posX == startLocation.x && posY == startLocation.y ) {
               ++missesInARow;
               continue;
            }

            if ( matrix[ posX ][ posY ] ) {
               ++missesInARow;
               continue;
            }

            if ( !checkDistance( matrix, new Point( posX, posY ), minimumDistance ) ) {
               ++missesInARow;
               continue;
            }
         }

         missesInARow = 0;

         matrix[ posX ][ posY ] = true;
         cloverLeafLocations.add( new Point( posX, posY ) );

         final CircleCollisionHandler cloverLeafCollisionHandler = CollisionHandlerFactory.createCircleCollisionHandler( cloverLeafeCollisionHandlers.get( leafNr ) );
         final int cloverLeafPosX = posX * TILE_WIGHT;
         final int cloverLeafPosY = posY * TILE_HEIGHT;
         cloverLeafCollisionHandler.setLocationOnMap( new Point( cloverLeafPosX, cloverLeafPosY ) );
         final CloverLeafState cloverLeafState = CloverLeafState.create( cloverLeafCollisionHandler );

         final CloverLeafHighlight cloverLeafHighlight = CloverLeafHighlight.create( new Point( cloverLeafPosX - ( ( CloverLeafHighlight.DIMENSION.width - CloverLeaf.DIMENSION.width ) / 2 ),
                                                                                                cloverLeafPosY - ( ( CloverLeafHighlight.DIMENSION.height - CloverLeaf.DIMENSION.height ) / 2 ) ),
                                                                                     cloverLeafState );

         final CloverLeaf cloverLeaf = CloverLeaf.create( new Point( cloverLeafPosX, cloverLeafPosY ),
                                                          leaf,
                                                          cloverLeafState );

         graphicsObjectRegistry.registerValueForGroup( Constants.GRAPHICS_OBJECT_LIBRARY_ID_ACTION_LAYER,
                                                       cloverLeaf );
         graphicsObjectRegistry.registerValueForGroup( Constants.GRAPHICS_OBJECT_LIBRARY_ID_EFFECT_LAYER,
                                                       cloverLeafHighlight );

         stateRegistry.registerValueForGroup( Constants.ID_CLOVER_LEAF_STATE, cloverLeafState );

         ++i;
      }

      constructPathThroughLabyrinth( cloverLeafLocations, labyrinthState );

      final Labyrinth labyrinth = Labyrinth.create( new Dimension( LABYRINTH_WIDTH, LABYRINTH_HEIGHT ),
                                                    new Dimension( TILE_WIGHT, TILE_HEIGHT ),
                                                    labyrinthState );

      graphicsObjectRegistry.registerValueForGroup( Constants.GRAPHICS_OBJECT_LIBRARY_ID_EFFECT_LAYER,
                                                    labyrinth );

      stateRegistry.registerValueForGroup( Constants.ID_LABYRINTH_STATE, labyrinthState );

      createAndRegisterLadyBird();
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private static Point findNextMatching( final boolean[][] matrix,
                                          final int minimumDistance,
                                          final Point startLocation )
   {
      for ( int i = 0; i < matrix.length; ++i ) {
         for ( int y = 0; y < matrix[ i ].length; ++y ) {
            final Point currentLocation = new Point( i, y );
            if ( matrix[ i ][ y ] ) {
               continue;
            }

            if ( !checkDistance( matrix, currentLocation, minimumDistance ) ) {
               continue;
            }

            if ( currentLocation.equals( startLocation ) ) {
               continue;
            }

            return currentLocation;

         }
      }
      return null;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private static boolean checkDistance( final boolean[][] matrix,
                                         final Point currentLocation,
                                         final int minimumDistance )
   {
      boolean isDistanceOkay = true;

      for ( int i = 1; i <= minimumDistance; ++i ) {
         // check right
         isDistanceOkay &= ( !matrix[ Math.min( currentLocation.x + i, matrix.length - 1 ) ][ currentLocation.y ] );
         // check left
         isDistanceOkay &= ( !matrix[ Math.max( currentLocation.x - i, 0 ) ][ currentLocation.y ] );

         // check bottom
         isDistanceOkay &= ( !matrix[ currentLocation.x ][ Math.min( currentLocation.y + i,
                                                                     matrix[ currentLocation.x ].length - 1 ) ] );
         // check top
         isDistanceOkay &= ( !matrix[ currentLocation.x ][ Math.max( currentLocation.y - i, 0 ) ] );

         // check top left
         isDistanceOkay &= ( !matrix[ Math.max( currentLocation.x - i, 0 ) ][ Math.max( currentLocation.y - i,
                                                                                        0 ) ] );
         // check bottom left
         isDistanceOkay &= ( !matrix[ Math.max( currentLocation.x - i, 0 ) ][ Math.min( currentLocation.y + i,
                                                                                        matrix[ currentLocation.x ].length - 1 ) ] );

         // check top right
         isDistanceOkay &= ( !matrix[ Math.min( currentLocation.x + i, matrix.length - 1 ) ][ Math.max( currentLocation.y - i,
                                                                                                        0 ) ] );
         // check bottom right
         isDistanceOkay &= ( !matrix[ Math.min( currentLocation.x + i, matrix.length - 1 ) ][ Math.min( currentLocation.y + i,
                                                                                                        matrix[ currentLocation.x ].length - 1 ) ] );
      }
      return isDistanceOkay;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private static void constructPathThroughLabyrinth( final List< Point > cloverLeafLocations,
                                                      final LabyrinthState labyrinthState )
   {
      final List< Point > tempList = new ArrayList< Point >();
      tempList.addAll( cloverLeafLocations );
      final RandomInt startLeaf = RandomInt.create( tempList.size() );
      final RandomInt numberOfLeafs = RandomInt.create( 7 );
      Point startPoint = new Point( LABYRINTH_MATRIX_WIDTH / 2, LABYRINTH_MATRIX_HEIGHT / 2 );
      int indexInArray = startLeaf.getNext();

      final List< Point > pointsWithPaths = new ArrayList< Point >();
      pointsWithPaths.add( new Point( LABYRINTH_MATRIX_WIDTH / 2, LABYRINTH_MATRIX_HEIGHT / 2 ) );
      final Random startPointRandom = new Random();

      int numberOfLeafesInPath = Math.min( numberOfLeafs.getNext(), tempList.size() );
      while ( !tempList.isEmpty() ) {
         numberOfLeafesInPath = Math.min( numberOfLeafs.getNext(), tempList.size() );
         startPoint = pointsWithPaths.get( startPointRandom.nextInt( pointsWithPaths.size() ) );
         indexInArray = findNearest( startPoint, tempList );

         for ( int i = 0; i < numberOfLeafesInPath; ++i ) {
            final Point endPoint = tempList.remove( indexInArray );
            buildPath( startPoint, endPoint, labyrinthState );
            startPoint = endPoint;
            pointsWithPaths.add( endPoint );
            indexInArray = findNearest( startPoint, tempList );
         }

      }
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private static int findNearest( final Point startPoint, final List< Point > tempList )
   {
      final Dimension nearestDistance = new Dimension( LABYRINTH_WIDTH, LABYRINTH_HEIGHT );
      int nearestIndex = -1;
      int i = 0;
      for ( final Point currentPoint : tempList ) {
         final int xDist = Math.abs( currentPoint.x - startPoint.x );
         final int yDist = Math.abs( currentPoint.y - startPoint.y );

         if ( xDist <= nearestDistance.width && yDist <= nearestDistance.height ) {
            nearestDistance.width = xDist;
            nearestDistance.height = yDist;
            nearestIndex = i;
         }
         ++i;
      }

      return nearestIndex;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private static void buildPath( final Point start, final Point end, final LabyrinthState labyrinthState )
   {
      PathDirection xAxisDirection;
      if ( start.x == end.x ) {
         xAxisDirection = PathDirection.STAY;
      } else if ( start.x > end.x ) {
         xAxisDirection = PathDirection.GO_LEFT;
      } else {
         xAxisDirection = PathDirection.GO_RIGHT;
      }

      PathDirection yAxisDirection;
      if ( start.y == end.y ) {
         yAxisDirection = PathDirection.STAY;
      } else if ( start.y > end.y ) {
         yAxisDirection = PathDirection.GO_UP;
      } else {
         yAxisDirection = PathDirection.GO_DOWN;
      }

      final Point current = new Point( start.x, start.y );

      boolean doXaxis = true;

      while ( !current.equals( end ) ) {
         final LabyrinthTileState tile = labyrinthState.getTileState( current.x, current.y );

         if ( doXaxis ) {
            if ( current.x == end.x ) {
               xAxisDirection = PathDirection.STAY;
            }
            if ( xAxisDirection == PathDirection.GO_LEFT ) {
               tile.disableWallLeft();
               current.x--;
               labyrinthState.getTileState( current.x, current.y ).disableWallRight();
            } else if ( xAxisDirection == PathDirection.GO_RIGHT ) {
               tile.disableWallRight();
               current.x++;
               labyrinthState.getTileState( current.x, current.y ).disableWallLeft();
            }
         } else {

            if ( current.y == end.y ) {
               yAxisDirection = PathDirection.STAY;
            }
            if ( yAxisDirection == PathDirection.GO_UP ) {
               tile.disableWallTop();
               current.y--;
               labyrinthState.getTileState( current.x, current.y ).disableWallBottom();
            } else if ( yAxisDirection == PathDirection.GO_DOWN ) {
               tile.disableWallBottom();
               current.y++;
               labyrinthState.getTileState( current.x, current.y ).disableWallTop();
            }
         }

         doXaxis = !doXaxis;
      }
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private static void createAndRegisterLadyBird()
   {
      final Image ladyBirdMoveRight = Utilities.loadImage( IMAGES_LADY_BIRD_RIGHT );
      final Image ladyBirdMoveLeft = Utilities.loadImage( IMAGES_LADY_BIRD_LEFT );
      final Image ladyBirdMoveUp = Utilities.loadImage( IMAGES_LADY_BIRD_UP );
      final Image ladyBirdMoveDown = Utilities.loadImage( IMAGES_LADY_BIRD_DOWN );

      final int width = ladyBirdMoveRight.getWidth( null );
      final int height = ladyBirdMoveRight.getHeight( null );

      final Point ladyBirdStartLocation = new Point( LABYRINTH_MATRIX_WIDTH / 2 * TILE_WIGHT, LABYRINTH_MATRIX_HEIGHT / 2
                                                                                  * TILE_HEIGHT );

      final CircleCollisionHandler upAndDownCollisionHandler = CollisionHandlerFactory.createCircleCollisionHandler( LADY_BIRD_COLLISION_UP_DOWN_XML );
      final CircleCollisionHandler leftAndRightCollisionHandler = CollisionHandlerFactory.createCircleCollisionHandler( LADY_BIRD_COLLISION_LEFT_RIGHT_XML );

      upAndDownCollisionHandler.setLocationOnMap( ladyBirdStartLocation );
      leftAndRightCollisionHandler.setLocationOnMap( ladyBirdStartLocation );

      final Dimension ladyBirdSize = new Dimension( width, height );
      final LadyBirdMovementState ladyBirdMovementState = LadyBirdMovementState.create( Direction.RIGHT,
                                                                                        ladyBirdStartLocation,
                                                                                        ladyBirdSize,
                                                                                        new Rectangle( new Point( 0,
                                                                                                                  0 ),
                                                                                                       new Dimension( LADY_BIRD_MOVEMENT_BOUNDS_WIDTH,
                                                                                                                      LADY_BIRD_MOVEMENT_BOUNDS_HEIGHT ) ),
                                                                                        upAndDownCollisionHandler,
                                                                                        leftAndRightCollisionHandler );

      final LadyBird ladyBird = LadyBird.create( ladyBirdSize,
                                                 ladyBirdMoveRight,
                                                 ladyBirdMoveLeft,
                                                 ladyBirdMoveUp,
                                                 ladyBirdMoveDown,
                                                 ladyBirdMovementState );

      GraphicsObjectRegistry.getInstance()
                            .registerValueForGroup( Constants.GRAPHICS_OBJECT_LIBRARY_ID_ACTION_LAYER,
                                                    ladyBird );

      StateRegistry.getInstance().registerValueForGroup( Constants.ID_LADY_BIRD_STATE, ladyBirdMovementState );
   }
}
