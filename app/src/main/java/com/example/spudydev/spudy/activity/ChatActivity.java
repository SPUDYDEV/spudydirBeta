package com.example.spudydev.spudy.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spudydev.spudy.R;
import com.example.spudydev.spudy.entidades.Mensagem;
import com.example.spudydev.spudy.infraestrutura.persistencia.AcessoFirebase;
import com.example.spudydev.spudy.perfil.negocio.DadosMenuLateral;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView nomeTurma;
    private EditText edtChatBarraMensagem;
    private String codigoTurma;
    private ArrayList<String> listaMensagens;
    private ListView lvMensagens;
    private DadosMenuLateral dadosMenuDAO = new DadosMenuLateral();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        dadosMenuDAO.resgatarUsuario(navigationView, FirebaseAuth.getInstance().getCurrentUser());

        //Botões
        nomeTurma = findViewById(R.id.tvChatNomeTurma);
        edtChatBarraMensagem = findViewById(R.id.edtChatBarraMensagem);
        Button btnChatUsuario = findViewById(R.id.btnChatUsuario);
        Button btnChatFalta = findViewById(R.id.btnChatFaltas);
        Button btnChatNotas = findViewById(R.id.btnChatNotas);
        Button btnChatMateriais = findViewById(R.id.btnChatMateriais);
        Button btnChatEnviarMensagem = findViewById(R.id.btnChatEnviarMensagem);
        codigoTurma = getIntent().getStringExtra("tipoConta");
        lvMensagens = findViewById(R.id.lvMensagens);

        //Métodos
        carregaNomeTurma();
        carregarMensagens();

        btnChatEnviarMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarMensagem();
            }
        });






    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void carregaNomeTurma(){
        AcessoFirebase.getFirebase().child("turma").child(codigoTurma).child("nomeTurma").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nomeTurma.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void carregarMensagens(){
        AcessoFirebase.getFirebase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> mensagens = (HashMap<String,Object>) dataSnapshot.child("chat").child(codigoTurma).getValue();
                listaMensagens = new ArrayList<>();
                for (Object i: mensagens.values()){
                    if (!(i.equals("SENTINELA"))){
                        String texto = dataSnapshot.child("chat").child(codigoTurma).child(i.toString()).child("texto").getValue(String.class);
                        String autor = dataSnapshot.child("pessoa").child(AcessoFirebase.getUidUsuario()).child("nome").getValue(String.class);
                        listaMensagens.add(texto+"\n~ "+autor);
                    }
                }
                setListViewMensagens(listaMensagens);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void enviarMensagem(){
        if (!edtChatBarraMensagem.getText().toString().isEmpty()) {
            AcessoFirebase.getFirebase().child("chat").child(codigoTurma).child("SENTINELA").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Pega o SENTINELA para usar como parâmetro
                    int idMensagem = dataSnapshot.getValue(int.class);
                    //Cria a mensagem
                    Mensagem mensagem = new Mensagem(edtChatBarraMensagem.getText().toString(), AcessoFirebase.getUidUsuario(),idMensagem);
                    //Salva a mensagem no banco
                    AcessoFirebase.getFirebase().child("chat").child(codigoTurma).child(String.valueOf(idMensagem)).setValue(mensagem);
                    //Incrementar sentinela
                    AcessoFirebase.getFirebase().child("chat").child(codigoTurma).child("SENTINELA").setValue(++idMensagem);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            carregarMensagens();
        }else{
            Toast.makeText(this, "Campo de mensagem vazio.", Toast.LENGTH_SHORT).show();
        }
    }

    public void setListViewMensagens(ArrayList<String> listaTurmaAluno){
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaTurmaAluno);
        lvMensagens.setAdapter(itemsAdapter);
    }

}
