package me.gmcardoso.pedrapapeltesourafragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.gmcardoso.pedrapapeltesourafragments.databinding.FragmentSettingsBinding
import android.content.Context
import androidx.fragment.app.commit


class SettingsFragment : Fragment() {

    private lateinit var fragmentSettingsBinding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater,container, false)
        fragmentSettingsBinding.cancelarBt.setOnClickListener {
            cancelar()
        }

        fragmentSettingsBinding.salvarBt.setOnClickListener {
            salvar()
        }

        setInitialValues()

        return fragmentSettingsBinding.root
    }

    private fun setInitialValues() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val jogadores = sharedPref.getInt(getString(R.string.QTDE_JOGADORES), 2)
        val rodadas = sharedPref.getInt(getString(R.string.QTDE_RODADAS), 1)

        if (jogadores == 2) {
            fragmentSettingsBinding.doisJogadoresRb.isChecked = true
            fragmentSettingsBinding.tresJogadoresRb.isChecked = false
        } else {
            fragmentSettingsBinding.doisJogadoresRb.isChecked = false
            fragmentSettingsBinding.tresJogadoresRb.isChecked = true
        }
        when (rodadas) {
            1 -> {
                fragmentSettingsBinding.umaRodadaRb.isChecked = true
                fragmentSettingsBinding.tresRodadasRb.isChecked = false
                fragmentSettingsBinding.cincoRodadasRb.isChecked = false
            }
            3 -> {
                fragmentSettingsBinding.umaRodadaRb.isChecked = false
                fragmentSettingsBinding.tresRodadasRb.isChecked = true
                fragmentSettingsBinding.cincoRodadasRb.isChecked = false
            }
            else -> {
                fragmentSettingsBinding.umaRodadaRb.isChecked = false
                fragmentSettingsBinding.tresRodadasRb.isChecked = false
                fragmentSettingsBinding.cincoRodadasRb.isChecked = true
            }
        }
    }

    private fun cancelar() {
        activity?.supportFragmentManager?.commit {
            setReorderingAllowed(true)
            addToBackStack("principal")
            replace(R.id.principalFcv, MainFragment(), "MainFragment")
        }
    }

    private fun salvar() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val quantidadeJogadores =
                if (fragmentSettingsBinding.quantidadeJogadoresRg.checkedRadioButtonId == R.id.doisJogadoresRb) 2 else 3

            val quantidadeRodadas =
                if (fragmentSettingsBinding.quantidadeRodadasRg.checkedRadioButtonId == R.id.umaRodadaRb) 1 else if (fragmentSettingsBinding.quantidadeRodadasRg.checkedRadioButtonId == R.id.tresRodadasRb) 3 else 5
        with (sharedPref.edit()) {
            putInt(getString(R.string.QTDE_JOGADORES), quantidadeJogadores)
            putInt(getString(R.string.QTDE_RODADAS), quantidadeRodadas)
            apply()
        }

        cancelar()
    }
}