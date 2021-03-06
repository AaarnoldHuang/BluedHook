package com.conch.bluedhook.ui

import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.conch.bluedhook.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_toolbar.*

class MainActivity : AppCompatActivity() {

    private var askSupport: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //check activated state
        if (isActivated(this)) {
            state.text = getString(R.string.success)
            state.setTextColor(ContextCompat.getColor(this, R.color.colorGreen))
        } else {
            state.text = getString(R.string.failed)
            state.setTextColor(ContextCompat.getColor(this, R.color.colorRed))
        }
        setSupportActionBar(toolbar)
        toolbar.title = title.toString()
    }

    /**
     * get isActivated state
     * We must hook this method in {@link SelfModule} and Return true
     */
    private fun isActivated(context: Context): Boolean {
        var isExp = false
        try {
            val contentResolver: ContentResolver = context.contentResolver;
            val uri = Uri.parse("content://me.weishu.exposed.CP/")
            var result: Bundle? = null
            try {
                result = contentResolver.call(uri, "active", null, null)
            } catch (e: RuntimeException) {
                // TaiChi is killed, try invoke
                try {
                    var intent = Intent("me.weishu.exp.ACTION_ACTIVE");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent)
                } catch (e1: Throwable) {
                    return false
                }
            }
            if (result == null) {
                result = contentResolver.call(uri, "active", null, null)
            }

            if (result == null) {
                return false
            }
            isExp = result.getBoolean("active", false)
        } catch (ignored: Throwable) {
        }
        return isExp
    }

    /**
     * create OptionsMenu
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mian_menu, menu)
        return true
    }

    /**
     * when menu selected
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.link_me -> {
                linkMe()
            }
            R.id.download -> {
                download()
            }
            R.id.telegram -> {
                telegram()
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        if (askSupport) {
            askSupport = false
            Snackbar.make(root, R.string.openApplication, Snackbar.LENGTH_LONG).show()
        }
    }

    /**
     * open download link
     */
    private fun download() {
        AlertDialog.Builder(this)
                .setTitle(R.string.dialogTitle)
                .setMessage(R.string.downloadTips)
                .setPositiveButton(R.string.sure) { _, _ ->
                    val uri = Uri.parse("https://pan.baidu.com/s/1nvZLaml")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
                .create()
                .show()

    }

    /**
     * open telegram and join this group
     */
    private fun telegram() {
        AlertDialog.Builder(this)
                .setTitle(R.string.dialogTitle)
                .setMessage(R.string.telegramTips)
                .setPositiveButton(R.string.sure) { _, _ ->
                    val uri = Uri.parse("https://t.me/Chat4Gay")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
                .create()
                .show()

    }

    /**
     * open blued and go to user's profile
     */
    private fun linkMe() {
        AlertDialog.Builder(this)
                .setTitle(R.string.dialogTitle)
                .setMessage(R.string.supportTips)
                .setPositiveButton(R.string.sure) { _, _ ->
                    val intent = Intent(Intent.ACTION_MAIN)
                    intent.addCategory(Intent.CATEGORY_LAUNCHER)
                    intent.action = "android.intent.action.VIEW"
                    val cn = ComponentName("com.soft.blued", "com.soft.blued.ui.welcome.FirstActivity")
                    intent.component = cn
                    intent.data = Uri.parse("blued://native.blued.cn?action=profile&enc=1&uid=aOvL2v")
                    startActivity(intent)
                    askSupport = true
                }
                .create()
                .show()

    }
}
