package hu.bme.aut.android.graphqldemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo.ApolloClient
import hu.bme.aut.android.graphqldemo.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import spacex.api.RocketsQuery

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDemo.setOnClickListener {
            binding.tvData.text = ""
            graphQl()
        }
    }

    fun graphQl() {
        val apolloClient = ApolloClient.Builder()
            .serverUrl("https://spacex-production.up.railway.app/")
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            apolloClient.query(RocketsQuery()).toFlow().collect {
                val data = it.data?.rockets?.map { rocket ->
                    "${rocket?.id}, ${rocket?.name},\n${rocket?.description}\n\n"
                }?.joinToString(" ")

                runOnUiThread {
                    binding.tvData.text = data
                }
            }
        }

    }
}