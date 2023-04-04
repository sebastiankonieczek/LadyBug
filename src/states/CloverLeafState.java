package states;

import collision.api.CircleCollisionHandler;
import collision.api.CollisionHandler;
import state.api.State;

public class CloverLeafState implements State
{
   private boolean isEaten_ = false;
   private final CollisionHandler cloverLeafCollisionHandler_;

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private CloverLeafState( final CollisionHandler cloverLeafCollisionHandler )
   {
      cloverLeafCollisionHandler_ = cloverLeafCollisionHandler;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public synchronized ImmutableCloverLeafState currentState()
   {
      return ImmutableCloverLeafState.create( isEaten_ ? true : false );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void updateState( final long duration )
   {
      // nothing to do
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public CloverLeafState clone()
   {
      final CloverLeafState cloverLeafState = new CloverLeafState( cloverLeafCollisionHandler_ );
      cloverLeafState.isEaten_ = isEaten_;
      return cloverLeafState;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public void setIsEaten( final boolean isEaten )
   {
      isEaten_ = isEaten;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public CollisionHandler getCollisionHandler()
   {
      return cloverLeafCollisionHandler_;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public static CloverLeafState create( final CircleCollisionHandler cloverLeafCollisionHandler )
   {
      return new CloverLeafState( cloverLeafCollisionHandler );
   }
}
