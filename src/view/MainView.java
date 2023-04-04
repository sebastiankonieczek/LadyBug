package view;

import graphics.api.BaseLayer;
import input.api.InputManager;
import input.api.KeyInputManager;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import loop.DrawLoop;
import main.Utilities;
import states.LadyBirdMovementState.Direction;
import actions.LadyBirdMoveAction;
import actions.LadyBirdStopAction;

public class MainView
{
   private static final int BASIC_LAYER_POSITION_Y = 0;
   private static final int BASIC_LAYER_POSITION_X = 0;

   private static final String IMAGE_BACKGROUND = "images/hintergrund.png";

   private static int SCREEN_WIDTH = 550;
   private static int SCREEN_HEIGHT = 550;

   private final JFrame mainFrame_;

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private MainView()
   {
      mainFrame_ = new JFrame();
      mainFrame_.setSize( SCREEN_WIDTH, SCREEN_HEIGHT );
      mainFrame_.setLocation( 400, 400 );
      mainFrame_.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      mainFrame_.setResizable( false );
      mainFrame_.setIgnoreRepaint( true );
      //      activate when in-game menu is ready
      mainFrame_.setUndecorated( true );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public static final MainView create()
   {
      return new MainView();
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public void show()
   {
      try {
         SwingUtilities.invokeAndWait( new Runnable()
         {
            @Override
            public void run()
            {
               mainFrame_.setVisible( true );
            }
         } );
      } catch ( final InterruptedException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch ( final InvocationTargetException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public void hide()
   {
      try {
         SwingUtilities.invokeAndWait( new Runnable()
         {
            @Override
            public void run()
            {
               mainFrame_.setVisible( false );
            }
         } );
      } catch ( final InterruptedException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch ( final InvocationTargetException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public void initialize()
   {
      try {
         SwingUtilities.invokeAndWait( new Runnable()
         {
            @Override
            public void run()
            {
               final Dimension basicLayerDimension = new Dimension( SCREEN_WIDTH, SCREEN_HEIGHT );

               final BaseLayer baseLayer = BaseLayer.create();
               baseLayer.setSize( SCREEN_WIDTH, SCREEN_HEIGHT );
               baseLayer.setLocation( BASIC_LAYER_POSITION_X, BASIC_LAYER_POSITION_Y );

               final BackgroundLayer backgroundLayer = BackgroundLayer.create( basicLayerDimension, loadBackgroundImage() );
               baseLayer.appendLayer( backgroundLayer );
               baseLayer.requestFocus();

               final ActionLayer actionLayer = ActionLayer.create( basicLayerDimension );
               baseLayer.appendLayer( actionLayer );

               final EffectLayer effectLayer = EffectLayer.create( basicLayerDimension );
               baseLayer.appendLayer( effectLayer );

               final InputManager inputManager = InputManager.create( false, true );

               inputManager.addInputListeners( baseLayer );

               final KeyInputManager keyInputManager = inputManager.keyInputManager();
               keyInputManager.registerKeyPressedAction( KeyEvent.VK_RIGHT, LadyBirdMoveAction.create( Direction.RIGHT ) );
               keyInputManager.registerKeyPressedAction( KeyEvent.VK_LEFT, LadyBirdMoveAction.create( Direction.LEFT ) );
               keyInputManager.registerKeyPressedAction( KeyEvent.VK_UP, LadyBirdMoveAction.create( Direction.UP ) );
               keyInputManager.registerKeyPressedAction( KeyEvent.VK_DOWN, LadyBirdMoveAction.create( Direction.DOWN ) );

               keyInputManager.registerKeyReleasedAction( KeyEvent.VK_RIGHT, LadyBirdStopAction.create( Direction.RIGHT ) );
               keyInputManager.registerKeyReleasedAction( KeyEvent.VK_LEFT, LadyBirdStopAction.create( Direction.LEFT ) );
               keyInputManager.registerKeyReleasedAction( KeyEvent.VK_UP, LadyBirdStopAction.create( Direction.UP ) );
               keyInputManager.registerKeyReleasedAction( KeyEvent.VK_DOWN, LadyBirdStopAction.create( Direction.DOWN ) );

               mainFrame_.getContentPane().add( baseLayer );
               baseLayer.requestFocus();


               final List< BaseLayer > layers = new ArrayList< BaseLayer >();
               layers.add( baseLayer );

               // start draw loop
               DrawLoop.create( layers ).start();
            }

         } );
      } catch ( final InterruptedException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch ( final InvocationTargetException e ) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private Image loadBackgroundImage()
   {
      return Utilities.loadImage( IMAGE_BACKGROUND );
   }
}
