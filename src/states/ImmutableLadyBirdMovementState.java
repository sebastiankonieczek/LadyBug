package states;

import java.awt.Point;

import state.api.ImmutableState;
import states.LadyBirdMovementState.Direction;

public class ImmutableLadyBirdMovementState implements ImmutableState< LadyBirdMovementState >
{
   private final LadyBirdMovementState state_;

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private ImmutableLadyBirdMovementState( final LadyBirdMovementState state )
   {
      state_ = state;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public static final ImmutableLadyBirdMovementState create( final LadyBirdMovementState state )
   {
      return new ImmutableLadyBirdMovementState( state );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public Direction getCurrentDirection()
   {
      return state_.getCurrentDirection();
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public Point getCurrentLocation()
   {
      return state_.getCurrentLocation();
   }
}
