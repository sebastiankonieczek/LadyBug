package states;

import state.api.ImmutableState;

public class ImmutableCloverLeafState implements ImmutableState< CloverLeafState >
{
   private final boolean isEaten_;

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private ImmutableCloverLeafState( final boolean isEaten )
   {
      isEaten_ = isEaten;
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public static final ImmutableCloverLeafState create( final boolean isEaten )
   {
      return new ImmutableCloverLeafState( isEaten );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public boolean isEaten()
   {
      return isEaten_;
   }
}
