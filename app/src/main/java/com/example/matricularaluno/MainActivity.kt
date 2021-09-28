package com.example.matricularaluno

//oq melhorar:
//Layout - titulo
//criar o codigo para o campo pesquisar aluno

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    val turmaUm = Turma("android", "15:00", 5)

    //declaramos as var fora, pois precisam ser de escopo global.
    var recyclerViewListaAlunos : RecyclerView? = null
    var adapterListaAlunos : TurmaAdapter? = null
    var buttonMatricularAluno : Button? = null
    var inputNomeAluno : TextInputLayout? = null
    var inputDocumentoAluno : TextInputLayout? = null
    var imagemTopo : ImageView? = null
    var numeroAlunosMatriculados : TextView? = null
    var procurarAlunosMatriculados : TextInputLayout? = null

    //onCreate é executado quando a tela é criada (ela é chamada pelo Android)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("Tech school")

        bindViews()

        //configurar o comportamento de cada um desses componentes
        configurarBotaoMatricular()
        configurarLIstaAlunos()
        configurarImagemTopo()
    }

    fun bindViews() {
        buttonMatricularAluno = findViewById(R.id.button_matricularAluno)
        recyclerViewListaAlunos = findViewById(R.id.rv_listaAlunos)
        inputNomeAluno = findViewById(R.id.inputAlunoNome)
        inputDocumentoAluno = findViewById(R.id.inputAlunoDocumento)
        imagemTopo = findViewById(R.id.imagemTopo)
        numeroAlunosMatriculados = findViewById(R.id.numeroAlunosMatriculados)
        procurarAlunosMatriculados = findViewById(R.id.pesquisarAluno)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun configurarBotaoMatricular() {
        buttonMatricularAluno?.setOnClickListener {

            executarMatricula(pegarEntradaUsuario().first, pegarEntradaUsuario().second)
            limparCamposInput()
            closeKeyboard()
        }
    }

    private fun pegarEntradaUsuario() : Pair<String, String> {
            val nomeAluno = inputNomeAluno?.editText?.text.toString()
            val documentoAluno = inputDocumentoAluno?.editText?.text.toString()
            Log.d("documentoAluno", documentoAluno)
            Log.d("nomeAluno", nomeAluno)
            return Pair(nomeAluno, documentoAluno)
    }

    private fun executarMatricula (nomeAluno : String, documentoAluno : String) {
        val matriculadoComSucesso = turmaUm.matricularAluno(nomeAluno, documentoAluno)

        if(matriculadoComSucesso.first) {
            Toast.makeText(this, "Aluno matriculado com sucesso", Toast.LENGTH_SHORT).show()
        }else if(matriculadoComSucesso.second == "Turma já está completa") {
            Toast.makeText(this, "Turma já está completa", Toast.LENGTH_SHORT).show()
        }else {
            Toast.makeText(this, "Näo foi possível matricular o aluno, nome e documendo são obrigatórios.", Toast.LENGTH_SHORT).show()
        }
        atualizarListaAlunos()
        atualizarContadorAlunos()
    }

    private fun configurarLIstaAlunos() {
        //Instanciando TurmaAdapter usando "named arguments"
        adapterListaAlunos = TurmaAdapter(
            listaAlunos = emptyList(),
            onClickDesmatricular = {nome, documento, posicao ->
                executarDesmatricularAluno(nome, documento, posicao)
            }
        )
        //Definir que tipo de disposição de itens vai ser utilizado
        /*Todo recyclerView precisa de um adapter (informer qual adpter essa recycler view vai receber) e um layoutManager pra funcionar*/
        recyclerViewListaAlunos?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //Passar para o recyclerview qual o adaptador vai ser utilizado, ou seja, o criado anteriormente (instanciado)
        recyclerViewListaAlunos?.adapter = adapterListaAlunos
    }

    private fun executarDesmatricularAluno (nome: String, documento: String, posicao: Int) {
        val sucesso = turmaUm.desmatricularAluno(posicao)
        if(sucesso) {
            Toast.makeText(this, "Aluno desmatriculado com suceso: $nome $documento - ${posicao.toString()}", Toast.LENGTH_LONG).show()
        }else {
            Toast.makeText(this, "Desmatricular Aluno näo realizada com sucesso", Toast.LENGTH_SHORT).show()
        }
        atualizarListaAlunos()
        atualizarContadorAlunos()
    }

    private fun atualizarListaAlunos() {
        //busca a lista de alunos atualizada
        val listaAlunosTurma = turmaUm.getListaAlunosMatriculados()
        //Passamos a lista atualizada de alunos para o adaptador através da sua função
        adapterListaAlunos?.atualizaLista(listaAlunosTurma)
    }

    private fun configurarImagemTopo() {
        imagemTopo?.let {
            Glide.with(this).load("https://i0.wp.com/www.ambersistemas.com.br/wp-content/uploads/2017/09/dicas-para-captar-alunos-para-sua-escola-20170821133431.jpg.jpg?fit=1254%2C837&ssl=1").into(it)
        }
    }

    private fun atualizarContadorAlunos() {
        val quantidadeAlunosMatriculados = turmaUm.getListaAlunosMatriculados().size
        numeroAlunosMatriculados?.text = getString(R.string.numeroAlunosMatriculados, quantidadeAlunosMatriculados)
        if (quantidadeAlunosMatriculados > 0){
            procurarAlunosMatriculados?.visibility = View.VISIBLE
            //pesquisarAluno()
        }else {
            procurarAlunosMatriculados?.visibility = View.INVISIBLE
        }
    }

    private fun limparCamposInput() {
        inputNomeAluno?.editText?.text?.clear()
        inputDocumentoAluno?.editText?.text?.clear()
    }

    /*fun pesquisarAluno() {
        procurarAlunosMatriculados?.setOnClickListener {

            val alunoPesquisado = procurarAlunosMatriculados?.editText?.text
            Log.d("procurarAluno", alunoPesquisado.toString())
        }
    }*/

    @RequiresApi(Build.VERSION_CODES.M)
    fun closeKeyboard() {
        // this will give us the view which is currently focus in this layout
        val view = this.currentFocus

        // if nothing is currently focus then this will protect the app from crash
        if (view != null) {
            // now assign the system service to InputMethodManager
            val manager: InputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            manager
                .hideSoftInputFromWindow(
                    view.windowToken, 0
                )
        }
    }
}



