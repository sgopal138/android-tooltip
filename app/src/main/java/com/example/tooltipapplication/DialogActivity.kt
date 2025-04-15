package com.example.tooltipapplication


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class DialogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dialog)

        val showDialogButton: Button = findViewById(R.id.showDialogButton)

        /*showDialogButton.setOnClickListener {
            if (isBetween11PMAnd9AM()) {
//                showIkeaClosedDialog()
                showIkeaClosedCustomDialog()
            } else {
                showStoreOpenDialog()
            }
        }*/

        if (isStoreClosed()) {
            showDialogButton.isEnabled = false
            showIkeaClosedCustomDialog()
        } else {
            showDialogButton.isEnabled = true
            showDialogButton.setOnClickListener {
                // Proceed if store is open
                startActivity(Intent(this, WelcomeScanActivity::class.java))
            }
        }
    }

    private fun isStoreClosed(): Boolean {
        val now = Calendar.getInstance()
        val hour = now.get(Calendar.HOUR_OF_DAY)
//        return hour < 9 || hour == 0 // 0 = 12 AM, so 12 AM–8:59 AM = closed

        return  hour == 22
    }

    private fun showIkeaClosedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Good night, IKEA")
            .setMessage("Your IKEA is now closed and the app scanner is closing too. See you soon!")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .setCancelable(false)
            .show()
    }

    private fun showStoreOpenDialog() {
        AlertDialog.Builder(this)
            .setTitle("Welcome!")
            .setMessage("IKEA is open. You can start scanning now.")
            .setPositiveButton("OK") { _, _ ->
                // ✅ Launch new activity on OK
                val intent = Intent(this, WelcomeScanActivity::class.java)
                startActivity(intent)
            }
            .show()
    }


    private fun showIkeaClosedCustomDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_ikea_closed, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val okButton = dialogView.findViewById<TextView>(R.id.okButton)
        okButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }



}


