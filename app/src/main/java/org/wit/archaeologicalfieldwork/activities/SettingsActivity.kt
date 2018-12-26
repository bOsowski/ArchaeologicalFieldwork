package org.wit.archaeologicalfieldwork.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.intentFor
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.main.MainApp

class SettingsActivity : AppCompatActivity() {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        app = application as MainApp
        setSupportActionBar(toolbar)

        var addedHillforts = 0
        var visitedHillforts = 0
        var totalHillforts = 0

        async(UI) {
            app.hillforts.findAll().forEach {
                totalHillforts++
                if (it.addedBy == app.user?.email){
                    addedHillforts++
                }
                visitedHillforts += app.visits.findAll().filter { it.addedBy == app.user?.email }.size
            }

            email.setText(app.user?.email)
            email.invalidate()
            password.setText("")
            password.invalidate()

            hillforts_visited_settings.text = resources.getString(R.string.hillforts_added_settings, addedHillforts.toString())
            hillforts_added_settings.text = resources.getString(R.string.hillforts_visited_settings, visitedHillforts.toString())
            hillforts_total.text = resources.getString(R.string.hillforts_total, totalHillforts.toString())
        }



        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(intentFor<LoginActivity>())
        }

        save_user_settings.setOnClickListener {
            //todo: remove this functinality
//            app.user?.email.set = email.text.toString()
//            user.password = password.text.toString()
//            async(UI) {
//                app.users.update(user)
//            }
        }
    }

}
