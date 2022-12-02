package ua.cn.stu.tic_tac_toe_thread;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ua.cn.stu.tic_tac_toe_thread.service.ComputerStepService;

public class GameActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "EXTRA";
    private static final String KET_STATE = "STATE";

    private GridLayout field;

    private List<Box> boxList;

    private ComputerStepService service;

    public enum States implements Serializable {
        NONE,
        CROSS,
        CIRCLE
    }

    public static class Box implements Serializable {
        public States state;
        public boolean disabled;

        public Box(States state, boolean disabled) {
            this.state = state;
            this.disabled = disabled;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        TextView nameTextView = findViewById(R.id.nameTextView);
        field = findViewById(R.id.tictactoeField);

        String login = getIntent().getSerializableExtra(EXTRA_NAME).toString();
        nameTextView.setText(login);

        if (savedInstanceState != null) {
            boxList = (List<Box>) savedInstanceState.getSerializable(KET_STATE);
        } else {
            boxList = generateBoxes();
        }

        findViewById(R.id.playAgain).setOnClickListener(v -> {
            finish();
            startActivity(getIntent());
        });

        findViewById(R.id.exitButton).setOnClickListener(v -> {
            onBackPressed();
        });

        initField();
        updateView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KET_STATE, (Serializable) boxList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, ComputerStepService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            service = ((ComputerStepService.ComputerStepServiceBinder) binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            service = null;
        }
    };

    public List<Box> generateBoxes() {
        List<Box> boxes = new ArrayList<>();
        for (int i = 0; i < 9; ++i) {
            Box box = new Box(States.NONE, false);
            boxes.add(box);
        }
        return boxes;
    }

    public void initField() {
        LayoutInflater inflater = LayoutInflater.from(this);
        field.setColumnCount(3);

        field.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int containerWH = field.getWidth();

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    containerWH = field.getHeight();
                }

                int boxWidth = containerWH / 3;
                int boxHeight = containerWH / 3;

                for (Box box : boxList) {
                    View boxRoot = inflater.inflate(R.layout.item_box, field, false);
                    boxRoot.setTag(box);
                    boxRoot.setOnClickListener(boxListener);

                    ViewGroup.LayoutParams params = boxRoot.getLayoutParams();
                    params.height = boxHeight;
                    params.width = boxWidth;

                    field.addView(boxRoot);
                }

                field.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                updateView();
            }
        });
    }

    public void updateView() {
        for (int i = 0; i < field.getChildCount(); ++i) {
            View boxRoot = field.getChildAt(i);
            Box box = (Box) boxRoot.getTag();

            if (box.state == States.CROSS) {
                boxRoot.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_outline_cross_24));
            } else if (box.state == States.CIRCLE) {
                boxRoot.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_outline_circle_24));
            }
        }
    }

    private final View.OnClickListener boxListener = v -> {
        Box box = (Box) v.getTag();
        if (box.state == States.NONE) {
            box.state = States.CROSS;
            computerStep();
            findWinner();
            updateView();
        }
    };

    private void showDialog(String title, String message) {
        Dialog currentDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, (d, w) -> {
                    onBackPressed();
                })
                .setNeutralButton(R.string.play_again_btn, (d, w) -> {
                    finish();
                    startActivity(getIntent());
                })
                .setCancelable(false)
                .create();
        currentDialog.show();
    }

    private void computerStep() { service.computerStep(boxList); }

    private void findWinner() {
        checkRows();
        checkColumns();
        ckeckDiagonal();
        checkReverseDiagonal();
    }

    private void checkReverseDiagonal() {
        if((boxList.get(2).state == States.CROSS && boxList.get(4).state == States.CROSS && boxList.get(6).state == States.CROSS)) {
            showDialog("Перемога", "Ви перемогли. Так тримати!");
        } else if((boxList.get(2).state == States.CIRCLE && boxList.get(4).state == States.CIRCLE && boxList.get(6).state == States.CIRCLE)) {
            showDialog("Поразка", "Ви програли. Пощастить наступного разу!");
        }
    }

    private void ckeckDiagonal() {
        if((boxList.get(0).state == States.CROSS && boxList.get(4).state == States.CROSS && boxList.get(8).state == States.CROSS)) {
            showDialog("Перемога", "Ви перемогли. Так тримати!");
        } else if((boxList.get(0).state == States.CIRCLE && boxList.get(4).state == States.CIRCLE && boxList.get(8).state == States.CIRCLE)) {
            showDialog("Поразка", "Ви програли. Пощастить наступного разу!");
        }
    }

    private void checkColumns() {
        for (int i = 0; i < 3; i++) {
            if((boxList.get(i).state == States.CROSS && boxList.get(i + 3).state == States.CROSS && boxList.get(i + 6).state == States.CROSS)) {
                showDialog("Перемога", "Ви перемогли. Так тримати!");
            } else if((boxList.get(i).state == States.CIRCLE && boxList.get(i + 3).state == States.CIRCLE && boxList.get(i + 6).state == States.CIRCLE)) {
                showDialog("Поразка", "Ви програли. Пощастить наступного разу!");
            }
        }
    }

    private void checkRows() {
        for (int i = 0; i < 9; i+=3) {
            if((boxList.get(i).state == States.CROSS && boxList.get(i + 1).state == States.CROSS && boxList.get(i + 2).state == States.CROSS)) {
                showDialog("Перемога", "Ви перемогли. Так тримати!");
            } else if((boxList.get(i).state == States.CIRCLE && boxList.get(i + 1).state == States.CIRCLE && boxList.get(i + 2).state == States.CIRCLE)) {
                showDialog("Поразка", "Ви програли. Пощастить наступного разу!");
            }
        }
    }
}