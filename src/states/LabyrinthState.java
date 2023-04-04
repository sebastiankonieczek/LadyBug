package states;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import state.api.ImmutableState;
import state.api.State;

public class LabyrinthState implements State, ImmutableState< LabyrinthState >
{
   public class LabyrinthTileState
   {
      private static final int BORDER_THICKNESS = 1;
      private boolean hasWallTop_;
      private boolean hasWallBottom_;
      private boolean hasWallLeft_;
      private boolean hasWallRight_;
      private final Rectangle bounds_;
      private final Rectangle borderTop_;
      private final Rectangle borderBottom_;
      private final Rectangle borderLeft_;
      private final Rectangle borderRight_;

      // /////////////////////////////////////////////////////////////////////////////////////////////////////////

      public LabyrinthTileState( final Rectangle bounds )
      {
         hasWallTop_ = true;
         hasWallBottom_ = true;
         hasWallLeft_ = true;
         hasWallRight_ = true;
         bounds_ = bounds;

         borderTop_ = new Rectangle( bounds_.x, bounds_.y, bounds_.width, BORDER_THICKNESS );
         borderBottom_ = new Rectangle( bounds_.x,
                                                       bounds_.y + bounds_.height - BORDER_THICKNESS,
                                                       bounds_.width,
                                                       BORDER_THICKNESS );
         borderLeft_ = new Rectangle( bounds_.x,
                                                     bounds_.y,
                                                     BORDER_THICKNESS,
                                                     bounds_.height );
         borderRight_ = new Rectangle( bounds_.x + bounds_.width - BORDER_THICKNESS,
                                                      bounds_.y,
                                                      BORDER_THICKNESS,
                                                      bounds_.height );
      }

      // /////////////////////////////////////////////////////////////////////////////////////////////////////////

      public void disableWallTop()
      {
         hasWallTop_ = false;
      }

      // /////////////////////////////////////////////////////////////////////////////////////////////////////////

      public void disableWallBottom()
      {
         hasWallBottom_ = false;
      }

      // /////////////////////////////////////////////////////////////////////////////////////////////////////////

      public void disableWallLeft()
      {
         hasWallLeft_ = false;
      }

      // /////////////////////////////////////////////////////////////////////////////////////////////////////////

      public void disableWallRight()
      {
         hasWallRight_ = false;
      }

      // /////////////////////////////////////////////////////////////////////////////////////////////////////////

      public void enableWallTop()
      {
         hasWallTop_ = true;
      }

      // /////////////////////////////////////////////////////////////////////////////////////////////////////////

      public void enableWallBottom()
      {
         hasWallBottom_ = true;
      }

      // /////////////////////////////////////////////////////////////////////////////////////////////////////////

      public void enableWallLeft()
      {
         hasWallLeft_ = true;
      }

      // /////////////////////////////////////////////////////////////////////////////////////////////////////////

      public void enableWallRight()
      {
         hasWallRight_ = true;
      }

      // /////////////////////////////////////////////////////////////////////////////////////////////////////////

      public boolean hasWallTop()
      {
         return hasWallTop_;
      }

      // /////////////////////////////////////////////////////////////////////////////////////////////////////////

      public boolean hasWallBottom()
      {
         return hasWallBottom_;
      }

      // /////////////////////////////////////////////////////////////////////////////////////////////////////////

      public boolean hasWallLeft()
      {
         return hasWallLeft_;
      }

      // /////////////////////////////////////////////////////////////////////////////////////////////////////////

      public boolean hasWallRight()
      {
         return hasWallRight_;
      }

      // /////////////////////////////////////////////////////////////////////////////////////////////////////////

      public Rectangle getBounds()
      {
         return bounds_;
      }

      // /////////////////////////////////////////////////////////////////////////////////////////////////////////

      public Rectangle getBorderTop()
      {
         return borderTop_;
      }

      // /////////////////////////////////////////////////////////////////////////////////////////////////////////

      public Rectangle getBorderBottom()
      {
         return borderBottom_;
      }

      // /////////////////////////////////////////////////////////////////////////////////////////////////////////

      public Rectangle getBorderLeft()
      {
         return borderLeft_;
      }

      ///////////////////////////////////////////////////////////////////////////////////////////////////////////

      public Rectangle getBorderRight()
      {
         return borderRight_;
      }

      ///////////////////////////////////////////////////////////////////////////////////////////////////////////

      public List< Rectangle > getBorders()
      {
         final ArrayList< Rectangle > enabledBorders = new ArrayList<>();
         if( hasWallBottom_ ) {
            enabledBorders.add( borderBottom_ );
         }
         if( hasWallTop_ ) {
            enabledBorders.add( borderTop_ );
         }
         if( hasWallLeft_ ) {
            enabledBorders.add( borderLeft_ );
         }
         if( hasWallRight_ ) {
            enabledBorders.add( borderRight_ );
         }
         return enabledBorders;
      }
   }

   ///////////////////////////////////////////////////////////////////////////////////////////////////////////

   private final LabyrinthTileState[][] labyrinth_;
   private final int width_;
   private final int height_;
   private final Dimension tileSize_;

   ///////////////////////////////////////////////////////////////////////////////////////////////////////////

   private LabyrinthState( final int width, final int height, final Dimension tileSize )
   {
      width_ = width;
      height_ = height;
      tileSize_ = tileSize;
      labyrinth_ = new LabyrinthTileState[ width ][ height ];

      for ( int i = 0; i < width; ++i ) {
         for ( int y = 0; y < height; ++y ) {
            labyrinth_[ i ][ y ] = new LabyrinthTileState( new Rectangle( new Point( i * tileSize.width,
                                                                                     y * tileSize.height ),
                                                                          tileSize ) );
         }
      }
   }

   ///////////////////////////////////////////////////////////////////////////////////////////////////////////

   public static final LabyrinthState create( final int width, final int height, final Dimension tileSize )
   {
      return new LabyrinthState( width, height, tileSize );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public LabyrinthTileState[][] getLabyrinth()
   {
      return labyrinth_;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public ImmutableState< ? extends State > currentState()
   {
      // do nothing here, labyrinth stays as it is
      return this;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void updateState( final long duration )
   {
      // do nothing here, labyrinth stays as it is
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public LabyrinthState clone()
   {
      final LabyrinthState state = new LabyrinthState( width_, height_, tileSize_ );

      for ( int i = 0; i < width_; ++i ) {
         for ( int y = 0; y < height_; ++y ) {
            final LabyrinthTileState labyrinthTileState = labyrinth_[ i ][ y ];
            state.labyrinth_[ i ][ y ] = new LabyrinthTileState( labyrinthTileState.getBounds() );
            state.labyrinth_[ i ][ y ].hasWallBottom_ = labyrinthTileState.hasWallBottom_;
            state.labyrinth_[ i ][ y ].hasWallTop_ = labyrinthTileState.hasWallTop_;
            state.labyrinth_[ i ][ y ].hasWallLeft_ = labyrinthTileState.hasWallLeft_;
            state.labyrinth_[ i ][ y ].hasWallRight_ = labyrinthTileState.hasWallRight_;
         }
      }
      return state;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public int getWidth()
   {
      return width_;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public int getHeight()
   {
      return height_;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public LabyrinthTileState getTileState( final int x, final int y )
   {
      return labyrinth_[ x ][ y ];

   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public List< LabyrinthTileState > getAffectedTiles( final LadyBirdMovementState ladyBirdState )
   {
      final List< LabyrinthTileState > tileStates = new ArrayList< LabyrinthTileState >();

      final Dimension size = ladyBirdState.getSize();
      final Point currentLocation = ladyBirdState.getCurrentLocation();

      final Point[] boundingPoints = new Point[4];
      final Point topLeft = currentLocation;
      boundingPoints[0] = topLeft;
      boundingPoints[1] = new Point( topLeft.x + size.width - 1, topLeft.y );
      boundingPoints[2] = new Point( topLeft.x + size.width - 1, topLeft.y + size.height - 1 );
      boundingPoints[3] = new Point( topLeft.x, topLeft.y + size.height - 1 );

      for ( final Point point : boundingPoints ) {

         final int arrayLocatorX = point.x / tileSize_.width;
         final int arrayLocatorY = point.y / tileSize_.height;

         final LabyrinthTileState labyrinthTileState = labyrinth_[ arrayLocatorX ][ arrayLocatorY ];
         if ( tileStates.contains( labyrinthTileState ) ) {
            continue;
         }
         tileStates.add( labyrinthTileState );
      }

      return tileStates;
   }
}
