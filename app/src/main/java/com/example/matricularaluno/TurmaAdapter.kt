package com.example.matricularaluno

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//pega a lista construida a partir da funcao matricularAluno. Com essa lista pronta o proximo passo é
//imprimir ela na tela. Para isso usamos um recyclerView. O mesmo recebe a lista e primeiro pega o tamanho
//dessa lista atraves da funcao getItemCount, tendo conhecimento desse valor, vai ser executado a funcao
//onCreatViewHolder. Se a funcao getItemCount retornou 5, logo serao criadas 5 veiwholders.
class TurmaAdapter (var listaAlunos : List<Pair<String, String>>, val onClickDesmatricular : (String, String, Int) -> Unit) : RecyclerView.Adapter<TurmaAdapter.AlunoViewHolder> () {
    //2
    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int) : AlunoViewHolder /*retorno: uma viewHolder para o aluno na posicao 0, e por ai seguinte.*/ {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_linha_aluno, parent, false)
        val viewHolderAluno = AlunoViewHolder(view, onClickDesmatricular)
        return viewHolderAluno //está retornando a viewHolder com dois textView dentro. Aqui preparamos a estrurua (a view com as "tags")
    } //criou as viewHolders (inflou). Chamou a classe AlunoViewHolder para informar qual o tipo de tag, de view q deve conter nessa viewHolder

    //3 //colocar os dados de texto nas views
    override fun onBindViewHolder(holder: AlunoViewHolder, position: Int) {
        val aluno = listaAlunos[position]
        holder.preencherDadosAlunos(aluno.first, aluno.second)
    }
    //1
    override fun getItemCount(): Int {
        return listaAlunos.size
    }

    fun atualizaLista(listaAlunosAtualizada: List<Pair<String, String>>) {
        listaAlunos = listaAlunosAtualizada
        //Sempre que mudamos a lista de dados de um adapter, temos que avisar o adaptador
        //Usamos a função notifyDataSetChanged() para isto
        //Por trás, isto iniciará a re-criação das linhas no recyclerview
        notifyDataSetChanged()
    }

    //viewholder precisa receber uma view
    //eu passei uma view como parametro. O itemView.findById eu estou dizendo da onde eu quero pegar o TextView. No caso, da view q eu acabei de passar.
    class AlunoViewHolder (itemView: View, val onClickDesmatricular: (String, String, Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val nomeAluno = itemView.findViewById<TextView>(R.id.nome_aluno)
        val documentoAluno = itemView.findViewById<TextView>(R.id.documento_aluno)
        val buttonDesmatricular = itemView.findViewById<Button>(R.id.buttonDesmatricular)

        fun preencherDadosAlunos (nome: String, documento: String) {
            nomeAluno.text = nome
            documentoAluno.text = documento

            buttonDesmatricular.setOnClickListener {onClickDesmatricular(nome, documento, adapterPosition)}
        }
    }

}