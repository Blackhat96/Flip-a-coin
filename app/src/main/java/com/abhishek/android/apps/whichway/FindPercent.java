package com.abhishek.android.apps.whichway;

import java.security.SecureRandom;

class FindPercent {
    int percent() {
        float per;
        try {
            int i;
            int DGWI = 0;
            for (i = 0; i < 1000; i++) {
                SecureRandom random = new SecureRandom();
                random.setSeed(System.currentTimeMillis() % random.nextInt());
                int rand = random.nextInt(1000);
                if (rand > 0 && rand < 500)
                    DGWI++;
            }
            per = (DGWI / (float) i) * 100;
            if (per > 50)
                return 1;
            return 0;
        } catch (Exception e) {
            //
        }
        return 0;
    }
}
