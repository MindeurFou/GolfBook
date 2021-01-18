package com.example.golfbook.ui.compoundedComponents

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.golfbook.R
import com.example.golfbook.data.model.Hole
import com.example.golfbook.extensions.ViewExtensions.textChanges
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@FlowPreview
@ExperimentalCoroutinesApi
class HoleInputItem(
        context: Context,
        private val hole: Hole,
        lifecycleScope: CoroutineScope,
        private val onClick: (holeNumber: Int, par: Int) -> Unit
) : ConstraintLayout(context) {

    private val holeEditText: TextInputEditText
    private val holeInputLayout: TextInputLayout

    init {

        val view = inflate(context, R.layout.item_hole_input, this)

        holeEditText = view.findViewById(R.id.holeEditText)
        holeInputLayout = view.findViewById(R.id.holeInputLayout)


        holeInputLayout.hint = context.getString(R.string.holeNumber, hole.holeNumber)

        hole.par?.let {
            holeEditText.setText(it.toString())
        }

        holeEditText.textChanges()
                .debounce(300)
                .onEach {

                    if (!it.isNullOrBlank())
                        onClick.invoke(hole.holeNumber, it.toString().toInt())

                }.launchIn(lifecycleScope)

    }
}