package com.example.weather.feature.main.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.weather.R
import com.example.weather.databinding.DialogRootedDeviceBinding
import com.example.weather.delegate.viewBinding
import com.example.weather.extension.setOnDebouncedClickListener

class RootedDeviceDialog : DialogFragment(R.layout.dialog_rooted_device) {

    private val binding: DialogRootedDeviceBinding by viewBinding()
    var onCloseButtonClick = {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        binding.closeBtn.setOnDebouncedClickListener {
            onCloseButtonClick.invoke()
        }
    }

    companion object {
        fun show(fm: FragmentManager, onCloseButtonClick: () -> Unit) {
            RootedDeviceDialog().apply {
                this.onCloseButtonClick = onCloseButtonClick
            }.showNow(fm, RootedDeviceDialog::javaClass.name)
        }
    }
}