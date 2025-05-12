
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.tooltipapplication.R

class XmlComposeFragment : Fragment() {

    private lateinit var rootContainer: ViewGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_xml_compose, container, false)
        rootContainer = requireActivity().findViewById(android.R.id.content)

        val composeView = view.findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {
            MaterialTheme {
               /* Button(onClick = { showComposeDialog() }) {
                    Text("Show Compose Dialog")
                }*/
            }
        }
        return view
    }

    /*private fun showComposeDialog() {
        val dialog = ComposeDialog(requireContext())
        dialog.setContent {
            FullScreenComposeDialog(onDismiss = { dialog.dismiss() })
        }
        dialog.show()
    }*/
}
