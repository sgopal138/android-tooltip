package com.example.tooltipapplication;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
        showTooltipButton.setOnClickListener(this::showTooltip);
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






}