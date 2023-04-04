package actions;

import input.api.Action;

import java.awt.event.KeyEvent;

import states.LadyBirdMovementState.Direction;
import event.api.Event;
import event.api.EventQueue;
import events.LadyBirdStopEvent;

public class LadyBirdStopAction implements Action< KeyEvent >
{
   private final Event event_;

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private LadyBirdStopAction( final Direction direction )
   {
      event_ = LadyBirdStopEvent.create( direction );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   @Override
   public void handle( final KeyEvent event )
   {
      EventQueue.getInstance().addEvent( event_ );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public static final LadyBirdStopAction create( final Direction direction )
   {
      return new LadyBirdStopAction( direction );
   }
}
