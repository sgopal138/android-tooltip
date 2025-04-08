package com.example.tooltipapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView showTooltipButton = findViewById(R.id.textview);
//        showTooltipButton.setOnClickListener(v-> showTooltip(showTooltipButton, this));
        showTooltipButton.setOnClickListener(v-> {
            Intent intent = new Intent(MainActivity.this, WelcomeScanActivity.class);
            startActivity(intent);
        });
    }


    private void showTooltip(View anchorView) {
        Dialog tooltipDialog = new Dialog(this);
        tooltipDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        tooltipDialog.setContentView(R.layout.tooltip_layout);

        // Set transparent background
        if (tooltipDialog.getWindow() != null) {
            tooltipDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            tooltipDialog.getWindow().setDimAmount(0.5f); // Dim background
        }

        // Tooltip Text
        TextView tooltipText = tooltipDialog.findViewById(R.id.tooltip_text);
        tooltipText.setText("This is a tooltip message!");

        // Close button
        Button closeButton = tooltipDialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> tooltipDialog.dismiss());

        // Action button
        Button actionButton = tooltipDialog.findViewById(R.id.action_button);
        actionButton.setOnClickListener(v -> tooltipDialog.dismiss());

        // Ensure the TextView is fully drawn before positioning the tooltip
        anchorView.post(() -> {
            int[] location = new int[2];
            anchorView.getLocationOnScreen(location);

            int anchorX = location[0] + (anchorView.getWidth() / 2);
            int anchorY = location[1];

            tooltipDialog.show();

            // Ensure tooltip width is measured
            tooltipDialog.getWindow().getDecorView().post(() -> {
                Window window = tooltipDialog.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams params = window.getAttributes();
                    params.gravity = Gravity.TOP | Gravity.START; // Position relative to `TextView`
                    params.x = anchorX - (tooltipDialog.getWindow().getDecorView().getWidth() / 2); // Center tooltip horizontally
                    params.y = anchorY - anchorView.getHeight() - tooltipDialog.getWindow().getDecorView().getHeight() - 150; // Position above `TextView`
                    window.setAttributes(params);
                }
            });
        });
    }

    public void showTooltip(View anchorView, Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View tooltipView = inflater.inflate(R.layout.tooltip_layout, null);

        PopupWindow popupWindow = new PopupWindow(
                tooltipView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        // Measure the tooltip view
        tooltipView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        // Get the location of the anchor view on the screen
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);

        int xOffset = location[0] + anchorView.getWidth() / 2 - tooltipView.getMeasuredWidth() / 2;
        int yOffset = location[1] - tooltipView.getMeasuredHeight();

        // Show the popup window
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, xOffset, yOffset);

        // Dim the background
        View rootView = ((Activity) context).getWindow().getDecorView().getRootView();
        WindowManager.LayoutParams layoutParams = ((Activity) context).getWindow().getAttributes();
        layoutParams.alpha = 0.5f; // Reduce brightness
        ((Activity) context).getWindow().setAttributes(layoutParams);

        // Close button functionality
        Button closeButton = tooltipView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> {
            popupWindow.dismiss();
            // Restore background brightness when dismissed
            layoutParams.alpha = 1.0f;
            ((Activity) context).getWindow().setAttributes(layoutParams);
        });

        // Restore background brightness when popup is dismissed
        popupWindow.setOnDismissListener(() -> {
            layoutParams.alpha = 1.0f;
            ((Activity) context).getWindow().setAttributes(layoutParams);
        });
    }






}