package com.example.spudydev.spudy.entidades;

import com.example.spudydev.spudy.usuario.dominio.Usuario;
import com.example.spudydev.spudy.usuario.professor.dominio.Professor;

import java.util.ArrayList;
import java.util.HashMap;


public class Turma {

    private String nome;
    private String cargaHoraria;
    private String cargaHorariaDiaria;
    private Professor professor;

    public String getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(String cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public String getCargaHorariaDiaria() {
        return cargaHorariaDiaria;
    }

    public void setCargaHorariaDiaria(String cargaHorariaDiaria) {
        this.cargaHorariaDiaria = cargaHorariaDiaria;
    }

    private ArrayList<Usuario> alunos;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public ArrayList<Usuario> getAlunos() {
        return alunos;
    }

    public void setAlunos(ArrayList<Usuario> alunos) {
        this.alunos = alunos;
    }

    //Esta chamada vai ser feita pelo PROFESSOR

    public HashMap<String, Object> toMapTurma(){
        HashMap<String, Object> hashMapTurma = new HashMap<>();
        hashMapTurma.put("nomeTurma", getNome());
        hashMapTurma.put("cargaHorariaDiaria", getCargaHorariaDiaria());

        return hashMapTurma;
    }

    /*
    public void entrarTurma(String codigoTurma){
        String uid = AcessoFirebase.getFirebaseAutenticacao().getCurrentUser().getUid();
        //Se ligar se o cara colocar o mesmo nome.
        AcessoFirebase.getFirebase().child("aluno").child(uid).child("turmas").setValue(codigoTurma);
    }

    public void salvarTurmaMonitor(String codigoTurma){
        String uid = AcessoFirebase.getFirebaseAutenticacao().getCurrentUser().getUid();
        //Se ligar se o cara colocar o mesmo nome.
        AcessoFirebase.getFirebase().child("aluno").child(uid).child("turmasMonitor").setValue(codigoTurma);
    }
    */

}
