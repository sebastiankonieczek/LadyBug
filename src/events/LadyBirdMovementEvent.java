package events;

import java.util.List;

import main.Constants;
import state.api.State;
import state.api.StateRegistry;
import states.LadyBirdMovementState;
import states.LadyBirdMovementState.Direction;
import event.api.Event;

public class LadyBirdMovementEvent implements Event
{
   private final Direction direction_;

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private LadyBirdMovementEvent( final Direction direction )
   {
      direction_ = direction;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public static final LadyBirdMovementEvent create( final Direction direction )
   {
      return new LadyBirdMovementEvent( direction );
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

      state.setDirection( direction_ );
      state.setIsMoving( true );
   }
}
