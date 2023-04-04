package actions;

import input.api.Action;

import java.awt.event.KeyEvent;

import states.LadyBirdMovementState.Direction;
import event.api.Event;
import event.api.EventQueue;
import events.LadyBirdMovementEvent;

public class LadyBirdMoveAction implements Action< KeyEvent >
{
   private final Direction direction_;
   private final Event event_;

   private LadyBirdMoveAction( final Direction direction )
   {
      direction_ = direction;
      event_ =  LadyBirdMovementEvent.create( direction_ );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public static final LadyBirdMoveAction create( final Direction direction )
   {
      return new LadyBirdMoveAction( direction );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void handle( final KeyEvent event )
   {
      EventQueue.getInstance().addEvent( event_ );
   }
}
