package thirty_throws.amho0063.inlupp1_amidala.model

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import thirty_throws.amho0063.inlupp1_amidala.view.MainActivity
import thirty_throws.amho0063.inlupp1_amidala.viewmodel.MyViewModel

/**
 * Author: Amidala Hoffmén
 *
 * Custom ArrayAdapter for the PointSpinner dropdown menu.
 */
class PointSpinnerAdapter(
    mainActivity: MainActivity,
    simpleSpinnerItem: Int,
    choices: List<String>,
    private val viewModel: MyViewModel

) : ArrayAdapter<String>(mainActivity, simpleSpinnerItem, choices) {

    /**
     * Overrides isEnabled() to determine if an item at the given position is enabled or not.
     * Returns true if the item is enabled, false otherwise.
     */
    override fun isEnabled(position: Int): Boolean {
        return !viewModel.disabledPositions.contains(position)
    }

    /**
     * Overrides getDropDownView() to customize the appearance of the dropdown items.
     * Sets the enabled state of the view based on its position.
     * Returns the modified view.
     */
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        view.isEnabled = isEnabled(position)

        return view
    }


}