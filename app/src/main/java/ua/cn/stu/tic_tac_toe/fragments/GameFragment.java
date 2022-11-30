package ua.cn.stu.tic_tac_toe.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ua.cn.stu.tic_tac_toe.R;

public class GameFragment extends BaseFragment {

    private static final String ARG_NAME = "NAME";
    private static final String KET_STATE = "STATE";

    private TextView nameTextView;
    private GridLayout field;

    private final Random random = new Random();
    private List<Box> boxList;

    private enum States implements Serializable {
        NONE,
        CROSS,
        CIRCLE
    }

    private static class Box implements Serializable {
        States state;
        boolean disabled;

        Box(States state, boolean disabled) {
            this.state = state;
            this.disabled = disabled;
        }
    }

    public static GameFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        GameFragment fragment = new GameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameTextView = view.findViewById(R.id.nameTextView);
        field = view.findViewById(R.id.tictactoeField);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String value = bundle.getString(ARG_NAME);
            nameTextView.setText(value);
        }

        if (savedInstanceState != null) {
            boxList = (List<Box>) savedInstanceState.getSerializable(KET_STATE);
        } else {
            boxList = generateBoxes();
        }

        view.findViewById(R.id.playAgain).setOnClickListener(v -> {
            restartFragment();
        });

        view.findViewById(R.id.exitButton).setOnClickListener(v -> {
            getNavigator().back();
        });

        initField();
        updateView();
    }

    private void restartFragment() {
        GameFragment gameFragment = GameFragment.newInstance(nameTextView.getText().toString());
        getParentFragmentManager()
                .popBackStack();
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, gameFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KET_STATE, (Serializable) boxList);
    }

    public List<Box> generateBoxes() {
        List<Box> boxes = new ArrayList<>();
        for (int i = 0; i < 9; ++i) {
            Box box = new Box(States.NONE, false);
            boxes.add(box);
        }
        return boxes;
    }

    public void initField() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        field.setColumnCount(3);

        field.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int containerWidth = field.getWidth();
                int containerHeight = field.getHeight();
                int boxWidth = containerWidth / 3;
                int boxHeight = containerHeight / 3;

                for (Box box : boxList) {
                    View boxRoot = inflater.inflate(R.layout.item_box, field, false);
                    boxRoot.setTag(box);
                    boxRoot.setOnClickListener(boxListener);

                    ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) boxRoot.getLayoutParams();
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
                boxRoot.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_outline_cross_24));
            } else if (box.state == States.CIRCLE) {
                boxRoot.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.ic_outline_circle_24));
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
        Dialog currentDialog = new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, (d, w) -> {
                    getNavigator().back();
                })
                .setNeutralButton(R.string.play_again_btn, (d, w) -> {
                    restartFragment();
                })
                .setCancelable(false)
                .create();
        currentDialog.show();
    }

    private void computerStep() {
        for (int i = 0; i < 40; i++) {
            int index = random.nextInt(9);
            Box box = boxList.get(index);
            if (box.state == States.NONE) {
                box.state = States.CIRCLE;
                break;
            }
        }
    }

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
