package ua.cn.stu.tic_tac_toe_activity_service.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.Random;

import ua.cn.stu.tic_tac_toe_activity_service.GameActivity;

public class ComputerStepService extends Service {

    private static final String TAG = ComputerStepService.class.getSimpleName();

    private final Random random = new Random();
    private final ComputerStepServiceBinder binder = new ComputerStepServiceBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void computerStep(List<GameActivity.Box> boxList) {
        for (int i = 0; i < 40; i++) {
            int index = random.nextInt(9);
            GameActivity.Box box = boxList.get(index);
            if (box.state == GameActivity.States.NONE) {
                box.state = GameActivity.States.CIRCLE;
                break;
            }
        }
    }

    public class ComputerStepServiceBinder extends Binder {

        public ComputerStepService getService() {
            return ComputerStepService.this;
        }
    }
}
