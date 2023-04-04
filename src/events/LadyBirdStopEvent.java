package events;

import java.util.List;

import main.Constants;
import state.api.State;
import state.api.StateRegistry;
import states.ImmutableLadyBirdMovementState;
import states.LadyBirdMovementState;
import states.LadyBirdMovementState.Direction;
import event.api.Event;

public class LadyBirdStopEvent implements Event
{
   private final Direction direction_;

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private LadyBirdStopEvent( final Direction direction )
   {
      direction_ = direction;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public static final LadyBirdStopEvent create( final Direction direction )
   {
      return new LadyBirdStopEvent( direction );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void execute()
   {
      final List< State > ladyBirdStates = StateRegistry.getInstance().getValuesForGroup( Constants.ID_LADY_BIRD_STATE );

      if( ladyBirdStates.isEmpty() ) {
         return;
      }

      final LadyBirdMovementState state = (LadyBirdMovementState)ladyBirdStates.get( 0 );
      final ImmutableLadyBirdMovementState currentState = state.currentState();

      // our direction is obviously not the moving direction any more, so we do not need to stop it.
      if( direction_ != currentState.getCurrentDirection() ) {
         return;
      }

      state.setIsMoving( false );
   }

}
