package de.tobiasschuerg.currencyedittext

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    val rand = Random()
    var moneyTextWatcher: TextWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        random.setOnClickListener {
            val locales = Locale.getAvailableLocales()
            val locale = locales[rand.nextInt(locales.size)]
            update(locale, rand.nextInt(1000000))
        }

        // 20€ default
        update(Locale.GERMANY, 20)
    }

    private fun update(locale: Locale, amount: Int) {
        if (moneyTextWatcher != null) {
            edittext.removeTextChangedListener(moneyTextWatcher)
        }
        moneyTextWatcher = MoneyTextWatcher(edittext, locale)
        edittext.addTextChangedListener(moneyTextWatcher)
        edittext.setText(amount.toString())
        edittext.setSelection(edittext.text.toString().length)
    }
}


