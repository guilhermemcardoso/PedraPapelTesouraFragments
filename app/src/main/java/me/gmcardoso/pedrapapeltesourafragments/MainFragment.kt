package me.gmcardoso.pedrapapeltesourafragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.gmcardoso.pedrapapeltesourafragments.databinding.FragmentMainBinding
import android.content.Context
import android.util.Log
import java.lang.StringBuilder
import kotlin.random.Random


class MainFragment : Fragment(), View.OnClickListener {

    private val JOGADOR = "JOGADOR"
    private val COMPUTADOR1 = "COMPUTADOR1"
    private val COMPUTADOR2 = "COMPUTADOR2"

    private val PEDRA = "PEDRA"
    private val PAPEL = "PAPEL"
    private val TESOURA = "TESOURA"

    var vitoriasJogador = 0
    var vitoriasComputador1 = 0
    var vitoriasComputador2 = 0

    var rodadaAtual = 1

    private lateinit var fragmentMainBinding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false)
        Log.d("CREATE VIEW", "criando o main fragment")
        fragmentMainBinding.papelIb.setOnClickListener(this)
        fragmentMainBinding.pedraIb.setOnClickListener(this)
        fragmentMainBinding.tesouraIb.setOnClickListener(this)
        return fragmentMainBinding.root
    }

    override fun onClick(v: View) {

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val rodadas = sharedPref.getInt(getString(R.string.QTDE_RODADAS), 1)

        Log.d("RODADAS", rodadas.toString())
        val resultado = novaRodada(v)
        Log.d("TESTE", resultado.size.toString())
        val sb = StringBuilder()
        for (counter in 0 until resultado.size) {
            if (resultado[counter] == JOGADOR) {
                vitoriasJogador++
            }
            if (resultado[counter] == COMPUTADOR1) {
                vitoriasComputador1++
            }
            if (resultado[counter] == COMPUTADOR2) {
                vitoriasComputador2++
            }
        }

        if (rodadaAtual < rodadas) {
            for (counter in 0 until resultado.size) {
                sb.append(resultado[counter])
                if (counter < resultado.size - 1) {
                    sb.append(", ")
                }
            }
            if (resultado.size > 1) {
                sb.append(" ganharam esta rodada! Selecione sua próxima jogada para a rodada seguinte.")
            } else {
                sb.append(" ganhou esta rodada! Selecione sua próxima jogada para a rodada seguinte.")
            }
            rodadaAtual++
        } else {
            val campeoes: ArrayList<String> = ArrayList()
            if (vitoriasJogador >= vitoriasComputador1 && vitoriasJogador >= vitoriasComputador2) {
                campeoes.add(JOGADOR)
            }
            if (vitoriasComputador1 >= vitoriasJogador && vitoriasComputador1 >= vitoriasComputador2) {
                campeoes.add(COMPUTADOR1)
            }
            if (vitoriasComputador2 >= vitoriasComputador1 && vitoriasComputador2 >= vitoriasJogador) {
                campeoes.add(COMPUTADOR2)
            }
            for (counter in 0 until campeoes.size) {

                sb.append(campeoes[counter])
                if (counter < resultado.size - 1) {
                    sb.append(", ")
                }
            }
            if (campeoes.size > 1) {
                sb.append(" ganharam esta partida! Selecione sua próxima jogada para iniciar uma nova partida.")
            } else {
                sb.append(" ganhou esta partida! Selecione sua próxima jogada para iniciar uma nova partida.")
            }
            reiniciarPartida()
        }
        fragmentMainBinding.resultadoTv.text = sb.toString()
        fragmentMainBinding.resultadoLl.visibility = View.VISIBLE
    }

    private fun reiniciarPartida() {
        rodadaAtual = 1
        vitoriasJogador = 0
        vitoriasComputador1 = 0
        vitoriasComputador2 = 0
        fragmentMainBinding.resultadoLl.visibility = View.GONE
    }

    private fun novaRodada(v: View): ArrayList<String> {

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val jogadores = sharedPref?.getInt(getString(R.string.QTDE_JOGADORES), 2)

        val jogador: String
        var computador2 = ""
        when (v.id) {
            fragmentMainBinding.papelIb.id -> jogador = PAPEL
            fragmentMainBinding.tesouraIb.id -> jogador = TESOURA
            else -> jogador = PEDRA
        }
        Log.d("TESTE", jogador)
        val computador1: String = gerarJogada()
        if (jogadores == 3) {
            computador2 = gerarJogada()
        }
        val resultado: ArrayList<String> =
            gerarResultado(jogador, computador1, computador2, jogadores)


        when (jogador) {
            PEDRA -> fragmentMainBinding.jogadaTv.text = PEDRA
            PAPEL -> fragmentMainBinding.jogadaTv.text = PAPEL
            TESOURA -> fragmentMainBinding.jogadaTv.text = TESOURA
            else -> {
            }
        }
        when (computador1) {
            PEDRA -> fragmentMainBinding.jogadaComputador1Tv.text = PEDRA
            PAPEL -> fragmentMainBinding.jogadaComputador1Tv.text = PAPEL
            TESOURA -> fragmentMainBinding.jogadaComputador1Tv.text = TESOURA
            else -> {
            }
        }
        if (jogadores == 3) {
            fragmentMainBinding.jogadaComputador2Tv.visibility = View.VISIBLE
            fragmentMainBinding.computador2Tv.visibility = View.VISIBLE
            when (computador2) {
                PEDRA -> fragmentMainBinding.jogadaComputador2Tv.text = PEDRA
                PAPEL -> fragmentMainBinding.jogadaComputador2Tv.text = PAPEL
                TESOURA -> fragmentMainBinding.jogadaComputador2Tv.text = TESOURA
                else -> {
                }
            }
        } else {
            fragmentMainBinding.computador2Tv.visibility = View.GONE
            fragmentMainBinding.jogadaComputador2Tv.visibility = View.GONE
        }
        return resultado
    }

    private fun gerarResultado(
        jogador: String,
        computador1: String,
        computador2: String,
        quantidadeJogadores: Int?
    ): ArrayList<String> {
        val qtdeJogadores: Int = if(quantidadeJogadores != 3 && quantidadeJogadores != 2) 2 else quantidadeJogadores

        val vencedores = ArrayList<String>()
        if (jogador != computador1 && jogador != computador2 && computador1 != computador2 && qtdeJogadores == 3) {
            vencedores.add(JOGADOR)
            vencedores.add(COMPUTADOR1)
            vencedores.add(COMPUTADOR2)
            return vencedores
        }
        if (jogador == PEDRA) {
            if (computador1 == PAPEL || computador1 == PEDRA) {
                vencedores.add(COMPUTADOR1)
            }
            if (qtdeJogadores == 3) {
                if (computador2 == PAPEL || computador2 == PEDRA) {
                    vencedores.add(COMPUTADOR2)
                }
            }
            if (computador1 != PAPEL) {
                if (qtdeJogadores == 3 && computador2 != PAPEL || qtdeJogadores == 2) {
                    vencedores.add(JOGADOR)
                }
            }
            return vencedores
        }
        if (jogador == PAPEL) {
            if (computador1 == PAPEL || computador1 == TESOURA) {
                vencedores.add(COMPUTADOR1)
            }
            if (qtdeJogadores == 3) {
                if (computador2 == PAPEL || computador2 == TESOURA) {
                    vencedores.add(COMPUTADOR2)
                }
            }
            if (computador1 != TESOURA) {
                if (qtdeJogadores == 3 && computador2 != TESOURA || qtdeJogadores == 2) {
                    vencedores.add(JOGADOR)
                }
            }
            return vencedores
        }
        if (jogador == TESOURA) {
            if (computador1 == TESOURA || computador1 == PEDRA) {
                vencedores.add(COMPUTADOR1)
            }
            if (qtdeJogadores == 3) {
                if (computador2 == TESOURA || computador2 == PEDRA) {
                    vencedores.add(COMPUTADOR2)
                }
            }
            if (computador1 != PEDRA) {
                if (qtdeJogadores == 3 && computador2 != PEDRA || qtdeJogadores == 2) {
                    vencedores.add(JOGADOR)
                }
            }
            return vencedores
        }
        return vencedores
    }

    private fun gerarJogada(): String {
        val jogada: String = when (Random.nextInt(1, 4)) {
            1 -> PAPEL
            2 -> TESOURA
            else -> PEDRA
        }
        return jogada
    }
}