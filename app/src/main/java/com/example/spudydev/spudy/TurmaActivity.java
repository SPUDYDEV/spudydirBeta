package com.example.spudydev.spudy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spudydev.spudy.entidades.Turma;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.infraestrutura.utils.Auxiliar;
import com.example.spudydev.spudy.infraestrutura.utils.MD5;
import com.example.spudydev.spudy.usuario.professor.gui.MainProfessorActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TurmaActivity extends AppCompatActivity {

    private EditText nomeTurma;
    private EditText cargaHorariaDiaria;

    private Turma turma = new Turma();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_criar_turma);

        nomeTurma = findViewById(R.id.edtNomeTurma);
        cargaHorariaDiaria = findViewById(R.id.edtCargaHorariaDiaria);

        Button btnCriarTurma = findViewById(R.id.btnCriarTurma);

        btnCriarTurma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigoTurma = criarTurma(nomeTurma.getText().toString(), cargaHorariaDiaria.getText().toString());
                Auxiliar.criarToast(TurmaActivity.this,"Codigo: " + codigoTurma);
                abrirTelaMainProfessor();
            }
        });



        //criando turma
    }


    private String criarTurma(String nomedaturma, String cargaHorariaDiaria){

        Turma turma = criarObjetoTurma(nomedaturma, cargaHorariaDiaria);

        String uid = AcessoFirebase.getFirebaseAutenticacao().getCurrentUser().getUid();
        String codigoTurma = MD5.hashCodigoTurma(uid, nomedaturma);

        //Salvando a turma na árvore turma
        AcessoFirebase.getFirebase().child("turma").child(codigoTurma).setValue(turma.toMapTurma());
        //Salvando a turma na árvore professor
        AcessoFirebase.getFirebase().child("professor").child(uid).child("turmasMinistradas").child(codigoTurma).setValue(codigoTurma);

        return codigoTurma;
    }

    @NonNull
    private static Turma criarObjetoTurma(String nomedaturma, String cargaHorariaDiaria) {
        Turma turma = new Turma();
        turma.setNome(nomedaturma);
        turma.setCargaHorariaDiaria(cargaHorariaDiaria);
        return turma;
    }
    public void abrirTelaMainProfessor(){
        Intent abrirTelaMainProfessor = new Intent(TurmaActivity.this, MainProfessorActivity.class);
        startActivity(abrirTelaMainProfessor);
        finish();

    }
}
