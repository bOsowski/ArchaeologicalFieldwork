package org.wit.archaeologicalfieldwork.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.intentFor
import org.wit.archaeologicalfieldwork.R
import org.wit.archaeologicalfieldwork.main.MainApp
import org.wit.archaeologicalfieldwork.models.User

class SettingsActivity : AppCompatActivity() {

    lateinit var app: MainApp
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        app = application as MainApp
        user = app.currentUser
        setSupportActionBar(toolbar)

        var addedHillforts = 0
        var visitedHillforts = 0

        app.forts.findAll().forEach {
            if (it.addedBy == user.id){
                addedHillforts++
            }
            it.visits.forEach {
                if(it.userId == user.id){
                    visitedHillforts++
                }
            }
        }

        email.setText(user.email)
        email.invalidate()
        password.setText(user.password)
        password.invalidate()

        hillforts_visited_settings.text = resources.getString(R.string.hillforts_added_settings, addedHillforts.toString())
        hillforts_added_settings.text = resources.getString(R.string.hillforts_visited_settings, visitedHillforts.toString())

        logout.setOnClickListener {
            startActivity(intentFor<LoginActivity>())
        }

        save_user_settings.setOnClickListener {
            user.email = email.text.toString()
            user.password = password.text.toString()
            app.users.update(user)
        }
    }

}
