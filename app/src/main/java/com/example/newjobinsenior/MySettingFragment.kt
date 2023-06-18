package com.example.newjobinsenior

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class MySettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val idPreference: EditTextPreference? = findPreference("id")
//        idPreference?.summaryProvider = EditTextPreference.SimpleSummaryProvider.getInstance()
        idPreference?.summaryProvider = Preference.SummaryProvider<EditTextPreference> {
                preference ->
            val text:String? = preference.text
            if(TextUtils.isEmpty(text)){
                "ID 설정이 되지 않았습니다."
            }
            else{
                "설정된 ID는 $text 입니다."
            }

        }

        val bgColorPreference: ListPreference? = findPreference("bg_color")
        bgColorPreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()

        val txColorPreference: ListPreference? = findPreference("tx_color")
        txColorPreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
    }
}