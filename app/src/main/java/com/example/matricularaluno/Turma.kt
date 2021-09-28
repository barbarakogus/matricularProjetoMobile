package com.example.matricularaluno

class Turma(val materia: String, val horario: String, val numeroMaximoAlunos: Int) {

    private val listaAlunosMatriculados = mutableListOf<Pair<String, String>>()
    //uma lista mutavél com dois dados do tipo String
    //essa val so pode ser manipulado pela funcao matricularAluno e getListaAlunosMatriculados

    fun matricularAluno (nome: String, documento: String) : Pair<Boolean, String> {
        if(nome.isNotEmpty() && documento.isNotEmpty()) {
            if(listaAlunosMatriculados.size < numeroMaximoAlunos) {
                listaAlunosMatriculados.add(Pair(nome, documento))
                return Pair(true, "Aluno matriculado com sucesso")
            }else {
                return Pair(false, "Turma já está completa")
            }
        }else {
            return Pair(false, "Nome e documento sao obrigatorios")
        }
    }
    fun desmatricularAluno (posicao: Int) : Boolean {
        listaAlunosMatriculados.removeAt(posicao)
        return true
    }
    fun getListaAlunosMatriculados () : List<Pair<String, String>> {
        return listaAlunosMatriculados
    }
}

