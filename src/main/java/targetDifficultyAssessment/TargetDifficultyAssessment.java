package targetDifficultyAssessment;

import datastructures.block.Block;

import java.util.Date;
import java.util.Random;

public class TargetDifficultyAssessment {


    public static String randomStringOfLength32() {

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 32;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }

        return buffer.toString();
    }

    public static void run(int checkLimit, int accuracy) {

        for (int i=0 ; i<checkLimit ; i++){
            long sum=0;
            for (int x=0 ; x<accuracy; x++){
                Block block = new Block(randomStringOfLength32());
                block.mineBlock(i);
                long finish = new Date().getTime();
                long start = block.getTimeStamp();
                sum = sum + (finish - start);
            }
            float average = sum/accuracy;
            //average = average/10;
            System.out.println("Average mining time for difficulty "+i+":" );
            System.out.println(average);

        }
    }


}
