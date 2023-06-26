package com.example.inlupp1_amidala.Model

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.inlupp1_amidala.View.MainActivity
import com.example.inlupp1_amidala.ViewModel.MyViewModel

class PointSpinner(
    private val mainActivity: MainActivity,
    private val simpleSpinnerItem: Int,
    private val choices: List<String>,
    private val viewModel: MyViewModel

) : ArrayAdapter<String>(mainActivity, simpleSpinnerItem, choices) {

    override fun isEnabled(position: Int): Boolean {
        return !viewModel.disabledPositions.contains(position)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        view.isEnabled = isEnabled(position)

        return view
    }


}