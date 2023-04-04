package main;

import view.MainView;

public class Base
{
   public static void main( final String[] args )
   {
      final MainView mainView = MainView.create();
      mainView.show();
      mainView.initialize();

      LevelBuilder.buildRandomLevel();

      LadyBirdGame.create().start();
   }
}
