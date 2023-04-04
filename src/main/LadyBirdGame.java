package main;

import java.util.List;

import collision.api.CollisionHandler;
import event.api.Event;
import event.api.EventQueue;
import loop.GameLoop;
import state.api.StateRegistry;
import states.CloverLeafState;
import states.LabyrinthState;
import states.LabyrinthState.LabyrinthTileState;
import states.LadyBirdMovementState;

public class LadyBirdGame extends GameLoop
{
   private static final long TICK_DURATION_IN_MILLIS = 40;

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private LadyBirdGame()
   {
      super( TICK_DURATION_IN_MILLIS );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   protected void updateStatesHook( final long duration )
   {
      // handle collision
      final StateRegistry stateRegistry = StateRegistry.getInstance();
      final List< state.api.State > ladyBirdStates = stateRegistry.getValuesForGroup( Constants.ID_LADY_BIRD_STATE );
      final List< state.api.State > cloverLeafStates = stateRegistry.getValuesForGroup( Constants.ID_CLOVER_LEAF_STATE );
      final List< state.api.State > labyrinthStates = stateRegistry.getValuesForGroup( Constants.ID_LABYRINTH_STATE );

      if ( ladyBirdStates.size() != 1 ) {
         throw new RuntimeException( "Invalid game state, number of LadyBirds must be 1!" );
      }

      final state.api.State state = ladyBirdStates.get( 0 );
      if ( !( state instanceof LadyBirdMovementState ) ) {
         throw new RuntimeException( "Invalid game state, LadyBird state object must be a \""
                                     + LadyBirdMovementState.class.getName() + "\" !" );
      }

      if ( labyrinthStates.size() != 1 ) {
         throw new RuntimeException( "Invalid game state, number of Labyrinths must be 1!" );
      }

      final state.api.State labyrinthState = labyrinthStates.get( 0 );
      if ( !( labyrinthState instanceof LabyrinthState ) ) {
         throw new RuntimeException( "Invalid game state, Labyrinth state object must be a \""
                                     + LabyrinthState.class.getName() + "\" !" );
      }

      final LabyrinthState labyrinthStateObject = (LabyrinthState) labyrinthState;
      final LadyBirdMovementState ladyBirdState = (LadyBirdMovementState) state;
      final CollisionHandler ladyBirdCollisionHandler = ladyBirdState.getCollisionHandler();

      final List< LabyrinthTileState > affectedTiles = labyrinthStateObject.getAffectedTiles( ladyBirdState );
      ladyBirdState.handleBorderCollision( affectedTiles );

      for ( final state.api.State cloverLeafState : cloverLeafStates ) {
         if ( !( cloverLeafState instanceof CloverLeafState ) ) {
            throw new RuntimeException( "Invalid game state, CloverLeaf state object must be a \""
                                        + CloverLeafState.class.getName() + "\" !" );
         }

         final CloverLeafState leafState = (CloverLeafState) cloverLeafState;
         final CollisionHandler cloverLeafCollisionHandler = leafState.getCollisionHandler();

         if ( ladyBirdCollisionHandler.collides( cloverLeafCollisionHandler ) ) {
            leafState.setIsEaten( true );
         }
      }

   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   protected void handleEventHook( final long duration )
   {
      final EventQueue eventQueue = EventQueue.getInstance();
      Event event = eventQueue.getEvent();

      while ( event != null ) {
         event.execute();

         event = eventQueue.getEvent();
      }
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public static final LadyBirdGame create()
   {
      return new LadyBirdGame();
   }
}
