package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomInt
{
   private final int seed_;
   private final Random random_ = new Random();
   private final List< Integer > available_ = new ArrayList< Integer >();

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   private RandomInt( final int seed )
   {
      seed_ = seed;

      for( int i = 0; i < seed; ++i )
      {
         available_.add( i );
      }
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public static final RandomInt create( final int seed )
   {
      return new RandomInt( seed );
   }

   // /////////////////////////////////////////////////////////////////////////////////////////////////////////

   public int getNext()
   {
      if( available_.isEmpty() )
      {
         for( int i = 0; i < seed_; ++i )
         {
            available_.add( i );
         }
      }

      final int selected = random_.nextInt( available_.size() );

      return available_.remove( selected );
   }
}
